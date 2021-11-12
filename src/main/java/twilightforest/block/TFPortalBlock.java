package twilightforest.block;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fmllegacy.network.PacketDistributor;
import org.apache.commons.lang3.mutable.MutableInt;
import twilightforest.TFConfig;
import twilightforest.TFSounds;
import twilightforest.TwilightForestMod;
import twilightforest.data.BlockTagGenerator;
import twilightforest.network.MissingAdvancementToastPacket;
import twilightforest.network.TFPacketHandler;
import twilightforest.util.PlayerHelper;
import twilightforest.world.NoReturnTeleporter;
import twilightforest.world.registration.TFGenerationSettings;
import twilightforest.world.TFTeleporter;

import javax.annotation.Nullable;
import java.util.*;

// KelpBlock seems to use ILiquidContainer as it's a block that permanently has water, so I suppose in best practices we also use this interface as well?

public class TFPortalBlock extends HalfTransparentBlock implements LiquidBlockContainer {

	public static final BooleanProperty DISALLOW_RETURN = BooleanProperty.create("is_one_way");

	private static final VoxelShape AABB = Shapes.create(new AABB(0.0F, 0.0F, 0.0F, 1.0F, 0.8125F, 1.0F));

	private static final int MIN_PORTAL_SIZE =  4;
	private static final int MAX_PORTAL_SIZE = 64;

	private static final HashSet<ServerPlayer> playersNotified = new HashSet<>();

	public TFPortalBlock(BlockBehaviour.Properties props) {
		super(props);
		this.registerDefaultState(this.stateDefinition.any().setValue(DISALLOW_RETURN, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(DISALLOW_RETURN);
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return AABB;
	}

	@Override
	@Deprecated
	public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return state.getValue(DISALLOW_RETURN) ? AABB : Shapes.empty();
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		// The portal itself is kind of technically water, and this checks the checkbox in Sugar Cane logic to not destroy itself when portal is made.
		return Fluids.WATER.getFlowing(1, false); // 1 is minimum value. Minecraft wiki at time of this writing has the values backwards.
	}

	public boolean tryToCreatePortal(Level world, BlockPos pos, ItemEntity catalyst, @Nullable Player player) {

		BlockState state = world.getBlockState(pos);

		if (canFormPortal(state) && world.getBlockState(pos.below()).canOcclude()) {
			Map<BlockPos, Boolean> blocksChecked = new HashMap<>();
			blocksChecked.put(pos, true);

			MutableInt size = new MutableInt(0);

			if (recursivelyValidatePortal(world, pos, blocksChecked, size, state) && size.intValue() >= MIN_PORTAL_SIZE) {

				if (TFConfig.COMMON_CONFIG.checkPortalDestination.get()) {
					boolean checkProgression = TFGenerationSettings.isProgressionEnforced(catalyst.level);
					if (!TFTeleporter.isSafeAround(world, pos, catalyst, checkProgression)) {
						// TODO: "failure" effect - particles?
						if (player != null) {
							player.displayClientMessage(new TranslatableComponent(TwilightForestMod.ID + ".twilight_portal.unsafe"), true);
						}
						return false;
					}
				}

				catalyst.getItem().shrink(1);
				causeLightning(world, pos, TFConfig.COMMON_CONFIG.portalLightning.get());

				for (Map.Entry<BlockPos, Boolean> checkedPos : blocksChecked.entrySet()) {
					if (checkedPos.getValue()) {
						world.setBlock(checkedPos.getKey(), TFBlocks.TWILIGHT_PORTAL.get().defaultBlockState(), 2);
					}
				}

				return true;
			}
		}

		return false;
	}

	public boolean canFormPortal(BlockState state) {
		return state.is(BlockTagGenerator.PORTAL_POOL) || state.getBlock() == this && state.getValue(DISALLOW_RETURN);
	}

	private static void causeLightning(Level world, BlockPos pos, boolean fake) {
		LightningBolt bolt = new LightningBolt(EntityType.LIGHTNING_BOLT, world);
		bolt.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
		bolt.setVisualOnly(fake);
		world.addFreshEntity(bolt);

		if (fake && world instanceof ServerLevel) {
			double range = 3.0D;
			List<Entity> list = world.getEntitiesOfClass(Entity.class, new AABB(pos).inflate(range));

			for (Entity victim : list) {
				if (!ForgeEventFactory.onEntityStruckByLightning(victim, bolt)) {
					victim.thunderHit((ServerLevel) world, bolt);
				}
			}
		}
	}

	private static boolean recursivelyValidatePortal(Level world, BlockPos pos, Map<BlockPos, Boolean> blocksChecked, MutableInt portalSize, BlockState poolBlock) {
		if (portalSize.incrementAndGet() > MAX_PORTAL_SIZE) return false;

		boolean isPoolProbablyEnclosed = true;

		for (int i = 0; i < 4 /* FIXME Screw it, 4. Thanks Mojang /* Direction.Plane.HORIZONTAL .length*/ && portalSize.intValue() <= MAX_PORTAL_SIZE; i++) {
			BlockPos positionCheck = pos.relative(Direction.from2DDataValue(i));

			if (!blocksChecked.containsKey(positionCheck)) {
				BlockState state = world.getBlockState(positionCheck);

				if (state == poolBlock && world.getBlockState(positionCheck.below()).canOcclude()) {
					blocksChecked.put(positionCheck, true);
					if (isPoolProbablyEnclosed) {
						isPoolProbablyEnclosed = recursivelyValidatePortal(world, positionCheck, blocksChecked, portalSize, poolBlock);
					}

				} else if (isGrassOrDirt(state) && isNatureBlock(world.getBlockState(positionCheck.above()))) {
					blocksChecked.put(positionCheck, false);

				} else return false;
			}
		}

		return isPoolProbablyEnclosed;
	}

	private static boolean isNatureBlock(BlockState state) {
		return BlockTagGenerator.PORTAL_DECO.contains(state.getBlock());
	}

	private static boolean isGrassOrDirt(BlockState state) {
		return BlockTagGenerator.PORTAL_EDGE.contains(state.getBlock());
	}

	@Override
	@Deprecated
	public void neighborChanged(BlockState state, Level world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
		boolean good = world.getBlockState(pos.below()).canOcclude();

		for (Direction facing : Direction.Plane.HORIZONTAL) {
			if (!good) break;

			BlockState neighboringState = world.getBlockState(pos.relative(facing));

			good = isGrassOrDirt(neighboringState) || neighboringState == state;
		}

		if (!good) {
			world.levelEvent(2001, pos, Block.getId(state));
			world.setBlock(pos, Blocks.WATER.defaultBlockState(), 0b11);
		}
	}

	private static final TranslatableComponent PORTAL_UNWORTHY = new TranslatableComponent(TwilightForestMod.ID + ".ui.portal.unworthy");
	@Override
	public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entity) {
		if (state == this.defaultBlockState()) {
			if (entity instanceof ServerPlayer player && !player.isCreative() && !player.isSpectator()) {
				Advancement requirement = PlayerHelper.getAdvancement(player, TFConfig.getPortalLockingAdvancement());

				if (requirement != null && !PlayerHelper.doesPlayerHaveRequiredAdvancement(player, requirement)) {
					player.displayClientMessage(PORTAL_UNWORTHY, true);

					if (!TFPortalBlock.isPlayerNotifiedOfRequirement(player)) {
						// .doesPlayerHaveRequiredAdvancement null-checks already, so we can skip null-checking the `requirement`
						DisplayInfo info = requirement.getDisplay();
						TFPacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), info == null ? new MissingAdvancementToastPacket(new TranslatableComponent(".ui.advancement.no_title"), new ItemStack(TFBlocks.TWILIGHT_PORTAL_MINIATURE_STRUCTURE.get())) : new MissingAdvancementToastPacket(info.getTitle(), info.getIcon()));

						TFPortalBlock.playerNotifiedOfRequirement(player);
					}

					return;
				}
			}

			attemptSendEntity(entity, false, true);
		}
	}

	public static boolean isPlayerNotifiedOfRequirement(ServerPlayer player) {
		return playersNotified.contains(player);
	}

	public static void playerNotifiedOfRequirement(ServerPlayer player) {
		playersNotified.add(player);
	}

	private static ResourceKey<Level> getDestination(Entity entity) {
		ResourceKey<Level> twilightForest = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(TFConfig.COMMON_CONFIG.DIMENSION.portalDestinationID.get()));

		return !entity.getCommandSenderWorld().dimension().location().equals(twilightForest.location())
				? twilightForest : ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(TFConfig.COMMON_CONFIG.originDimension.get())); // FIXME: cache this for gods sake
	}

	public static void attemptSendEntity(Entity entity, boolean forcedEntry, boolean makeReturnPortal) {
		if (!entity.isAlive() || entity.level.isClientSide) {
			return;
		}

		if (entity.isPassenger() || entity.isVehicle() || !entity.canChangeDimensions()) {
			return;
		}

		if (!forcedEntry && entity.portalTime > 0) {
			return;
		}

		// set a cooldown before this can run again
		entity.portalTime = 10;

		ResourceKey<Level> destination = getDestination(entity);
		ServerLevel serverWorld = entity.getCommandSenderWorld().getServer().getLevel(destination);

		if(serverWorld == null)
			return;

		entity.changeDimension(serverWorld, makeReturnPortal ? new TFTeleporter(forcedEntry) : new NoReturnTeleporter());

		if (destination ==  ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(TFConfig.COMMON_CONFIG.DIMENSION.portalDestinationID.get())) && entity instanceof ServerPlayer && forcedEntry) {
			ServerPlayer playerMP = (ServerPlayer) entity;
			// set respawn point for TF dimension to near the arrival portal, only if we spawn here on world creation
			playerMP.setRespawnPosition(destination, playerMP.blockPosition(), playerMP.getYRot(), true, false);
		}

	}

	// Full [VanillaCopy] of BlockPortal.randomDisplayTick
	// TODO Eeeh... Let's look at changing this too alongside a new model.
	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, Random rand) {
		int random = rand.nextInt(100);
		if (stateIn.getValue(DISALLOW_RETURN) && random < 80) return;

		if (random == 0) {
			worldIn.playLocalSound(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, TFSounds.PORTAL_WOOSH, SoundSource.BLOCKS, 0.5F, rand.nextFloat() * 0.4F + 0.8F, false);
		}

		for (int i = 0; i < 4; ++i) {
			double xPos = pos.getX() + rand.nextFloat();
			double yPos = pos.getY()+1D;
			double zPos = pos.getZ() + rand.nextFloat();
			double xSpeed = (rand.nextFloat() - 0.5D) * 0.5D;
			double ySpeed = rand.nextFloat();
			double zSpeed = (rand.nextFloat() - 0.5D) * 0.5D;

			worldIn.addParticle(ParticleTypes.PORTAL, xPos, yPos, zPos, xSpeed, ySpeed, zSpeed);
		}
	}

	@Override
	public boolean canPlaceLiquid(BlockGetter iBlockReader, BlockPos blockPos, BlockState blockState, Fluid fluid) {
		return false;
	}

	@Override
	public boolean placeLiquid(LevelAccessor iWorld, BlockPos blockPos, BlockState blockState, FluidState fluidState) {
		return false;
	}
}

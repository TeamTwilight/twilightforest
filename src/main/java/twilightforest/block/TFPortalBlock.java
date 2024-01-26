package twilightforest.block;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
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
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.network.PacketDistributor;
import org.apache.commons.lang3.mutable.MutableInt;
import org.jetbrains.annotations.Nullable;
import twilightforest.TFConfig;
import twilightforest.client.MissingAdvancementToast;
import twilightforest.data.tags.BlockTagGenerator;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFSounds;
import twilightforest.network.MissingAdvancementToastPacket;
import twilightforest.util.LandmarkUtil;
import twilightforest.util.PlayerHelper;
import twilightforest.world.NoReturnTeleporter;
import twilightforest.world.TFTeleporter;
import twilightforest.world.registration.TFGenerationSettings;

import java.util.*;

// KelpBlock seems to use ILiquidContainer as it's a block that permanently has water, so I suppose in best practices we also use this interface as well?
public class TFPortalBlock extends HalfTransparentBlock implements LiquidBlockContainer {

	public static final BooleanProperty DISALLOW_RETURN = BooleanProperty.create("is_one_way");

	private static final VoxelShape AABB = Shapes.create(new AABB(0.0F, 0.0F, 0.0F, 1.0F, 0.8125F, 1.0F));
	@Nullable
	private static ResourceKey<Level> cachedOriginDimension;

	public static final Component PORTAL_UNWORTHY = Component.translatable("misc.twilightforest.portal_unworthy");
	private static final int MIN_PORTAL_SIZE = 4;
	private static final HashSet<ServerPlayer> playersNotified = new HashSet<>();

	public TFPortalBlock(BlockBehaviour.Properties properties) {
		super(properties);
		this.registerDefaultState(this.getStateDefinition().any().setValue(DISALLOW_RETURN, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(DISALLOW_RETURN);
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		return AABB;
	}

	@Override
	@Deprecated
	public VoxelShape getCollisionShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		return state.getValue(DISALLOW_RETURN) ? AABB : Shapes.empty();
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		// The portal itself is kind of technically water, and this checks the checkbox in Sugar Cane logic to not destroy itself when portal is made.
		return Fluids.WATER.getFlowing(1, false); // 1 is minimum value. Minecraft wiki at time of this writing has the values backwards.
	}

	public boolean tryToCreatePortal(Level level, BlockPos pos, ItemEntity catalyst, @Nullable Player player) {

		BlockState state = level.getBlockState(pos);

		if (this.canFormPortal(state) && level.getBlockState(pos.below()).isFaceSturdy(level, pos, Direction.UP)) {
			Map<BlockPos, Boolean> blocksChecked = new HashMap<>();
			blocksChecked.put(pos, true);

			MutableInt size = new MutableInt(0);

			if (recursivelyValidatePortal(level, pos, blocksChecked, size, state) && size.intValue() >= MIN_PORTAL_SIZE) {

				if (TFConfig.COMMON_CONFIG.checkPortalDestination.get()) {
					boolean checkProgression = LandmarkUtil.isProgressionEnforced(catalyst.level());
					if (!TFTeleporter.isSafeAround(level, pos, catalyst, checkProgression)) {
						// TODO: "failure" effect - particles?
						if (player != null) {
							player.displayClientMessage(Component.translatable("misc.twilightforest.portal_unsafe"), true);
						}
						return false;
					}
				}

				catalyst.getItem().shrink(1);
				causeLightning(level, pos, TFConfig.COMMON_CONFIG.portalLightning.get());

				for (Map.Entry<BlockPos, Boolean> checkedPos : blocksChecked.entrySet()) {
					if (checkedPos.getValue()) {
						level.setBlock(checkedPos.getKey(), TFBlocks.TWILIGHT_PORTAL.get().defaultBlockState(), 2);
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

	private static void causeLightning(Level level, BlockPos pos, boolean fake) {
		LightningBolt bolt = new LightningBolt(EntityType.LIGHTNING_BOLT, level);
		bolt.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
		bolt.setVisualOnly(fake);
		level.addFreshEntity(bolt);

		if (fake && level instanceof ServerLevel) {
			double range = 3.0D;
			List<Entity> list = level.getEntitiesOfClass(Entity.class, new AABB(pos).inflate(range));

			for (Entity victim : list) {
				if (!EventHooks.onEntityStruckByLightning(victim, bolt)) {
					victim.thunderHit((ServerLevel) level, bolt);
				}
			}
		}
	}

	public static boolean recursivelyValidatePortal(Level level, BlockPos pos, Map<BlockPos, Boolean> blocksChecked, MutableInt portalSize, BlockState poolBlock) {
		if (portalSize.incrementAndGet() > TFConfig.COMMON_CONFIG.maxPortalSize.get()) return false;

		boolean isPoolProbablyEnclosed = true;

		for (int i = 0; i < 4 && portalSize.intValue() <= TFConfig.COMMON_CONFIG.maxPortalSize.get(); i++) {
			BlockPos positionCheck = pos.relative(Direction.from2DDataValue(i));

			if (!blocksChecked.containsKey(positionCheck)) {
				BlockState state = level.getBlockState(positionCheck);

				if (state == poolBlock && level.getBlockState(positionCheck.below()).isFaceSturdy(level, pos, Direction.UP)) {
					blocksChecked.put(positionCheck, true);
					if (isPoolProbablyEnclosed) {
						isPoolProbablyEnclosed = recursivelyValidatePortal(level, positionCheck, blocksChecked, portalSize, poolBlock);
					}

				} else if (isGrassOrDirt(state) && isNatureBlock(level.getBlockState(positionCheck.above()))) {
					blocksChecked.put(positionCheck, false);

				} else return false;
			}
		}

		return isPoolProbablyEnclosed;
	}

	private static boolean isNatureBlock(BlockState state) {
		return state.is(BlockTagGenerator.PORTAL_DECO);
	}

	private static boolean isGrassOrDirt(BlockState state) {
		return state.is(BlockTagGenerator.PORTAL_EDGE);
	}

	@Override
	@Deprecated
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
		boolean good = level.getBlockState(pos.below()).isFaceSturdy(level, pos, Direction.UP);

		for (Direction facing : Direction.Plane.HORIZONTAL) {
			if (!good) break;

			BlockState neighboringState = level.getBlockState(pos.relative(facing));

			good = isGrassOrDirt(neighboringState) || neighboringState == state;
		}

		if (!good) {
			level.levelEvent(2001, pos, Block.getId(state));
			level.setBlock(pos, Blocks.WATER.defaultBlockState(), 0b11);
		}
	}

	@Override
	public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
		if (state == this.defaultBlockState()) {
			if (entity instanceof ServerPlayer player && !player.isCreative() && !player.isSpectator() && TFConfig.getPortalLockingAdvancement(player) != null) {
				AdvancementHolder requirement = PlayerHelper.getAdvancement(player, Objects.requireNonNull(TFConfig.getPortalLockingAdvancement(player)));

				if (requirement != null && !PlayerHelper.doesPlayerHaveRequiredAdvancement(player, requirement)) {
					player.displayClientMessage(PORTAL_UNWORTHY, true);

					if (!TFPortalBlock.isPlayerNotifiedOfRequirement(player)) {
						// .doesPlayerHaveRequiredAdvancement null-checks already, so we can skip null-checking the `requirement`
						DisplayInfo info = requirement.value().display().orElse(null);
						PacketDistributor.PLAYER.with(player).send(info == null ? new MissingAdvancementToastPacket(Component.translatable("twilightforest.ui.advancement.no_title"), new ItemStack(TFBlocks.TWILIGHT_PORTAL_MINIATURE_STRUCTURE.get())) : new MissingAdvancementToastPacket(info.getTitle(), info.getIcon()));
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
		if (cachedOriginDimension == null) cachedOriginDimension = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(TFConfig.COMMON_CONFIG.originDimension.get()));
		return !entity.getCommandSenderWorld().dimension().location().equals(TFGenerationSettings.DIMENSION)
				? TFGenerationSettings.DIMENSION_KEY : cachedOriginDimension;
	}

	public static void attemptSendEntity(Entity entity, boolean forcedEntry, boolean makeReturnPortal) {
		if (!entity.isAlive() || entity.level().isClientSide()) {
			return;
		}

		if (entity.isPassenger() || entity.isVehicle() || !entity.canChangeDimensions()) {
			return;
		}

		ResourceKey<Level> destination = getDestination(entity);
		ServerLevel serverWorld = entity.getCommandSenderWorld().getServer().getLevel(destination);

		if (serverWorld == null)
			return;

		entity.changeDimension(serverWorld, makeReturnPortal ? new TFTeleporter(forcedEntry) : new NoReturnTeleporter());

		if (destination == TFGenerationSettings.DIMENSION_KEY && entity instanceof ServerPlayer playerMP && forcedEntry) {
			// set respawn point for TF dimension to near the arrival portal, only if we spawn here on world creation
			playerMP.setRespawnPosition(destination, playerMP.blockPosition(), playerMP.getYRot(), true, false);
		}

	}

	// Full [VanillaCopy] of BlockPortal.randomDisplayTick
	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource rand) {
		int random = rand.nextInt(100);
		if (state.getValue(DISALLOW_RETURN) && random < 80) return;

		if (random == 0) {
			level.playLocalSound(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, TFSounds.PORTAL_WHOOSH.get(), SoundSource.BLOCKS, 0.5F, rand.nextFloat() * 0.4F + 0.8F, false);
		}

		for (int i = 0; i < 4; ++i) {
			double xPos = pos.getX() + rand.nextFloat();
			double yPos = pos.getY() + 1D;
			double zPos = pos.getZ() + rand.nextFloat();
			double xSpeed = (rand.nextFloat() - 0.5D) * 0.5D;
			double ySpeed = rand.nextFloat();
			double zSpeed = (rand.nextFloat() - 0.5D) * 0.5D;

			level.addParticle(ParticleTypes.PORTAL, xPos, yPos, zPos, xSpeed, ySpeed, zSpeed);
		}
	}

	@Override
	public boolean canPlaceLiquid(@Nullable Player player, BlockGetter getter, BlockPos pos, BlockState state, Fluid fluid) {
		return false;
	}

	@Override
	public boolean placeLiquid(LevelAccessor accessor, BlockPos pos, BlockState state, FluidState fluidState) {
		return false;
	}
}

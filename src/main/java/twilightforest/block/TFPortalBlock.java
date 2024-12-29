package twilightforest.block;

import net.minecraft.advancements.AdvancementHolder;
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
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.network.PacketDistributor;
import org.apache.commons.lang3.mutable.MutableInt;
import org.jetbrains.annotations.Nullable;
import twilightforest.config.TFConfig;
import twilightforest.data.tags.BlockTagGenerator;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFDataAttachments;
import twilightforest.init.TFDimension;
import twilightforest.init.TFSounds;
import twilightforest.network.MissingAdvancementToastPacket;
import twilightforest.util.landmarks.LandmarkUtil;
import twilightforest.util.PlayerHelper;
import twilightforest.world.TFTeleporter;

import java.util.*;

// KelpBlock seems to use ILiquidContainer as it's a block that permanently has water, so I suppose in best practices we also use this interface as well?
public class TFPortalBlock extends HalfTransparentBlock implements LiquidBlockContainer, Portal {

	public static final BooleanProperty DISALLOW_RETURN = BooleanProperty.create("is_one_way");
	public static final Component PORTAL_UNWORTHY = Component.translatable("misc.twilightforest.portal_unworthy");
	private static final VoxelShape AABB = Shapes.create(new AABB(0.0F, 0.0F, 0.0F, 1.0F, 0.8125F, 1.0F));
	private static final int MIN_PORTAL_SIZE = 4;
	private static final HashSet<ServerPlayer> playersNotified = new HashSet<>();
	@Nullable
	private static ResourceKey<Level> cachedOriginDimension;

	public TFPortalBlock(BlockBehaviour.Properties properties) {
		super(properties);
		this.registerDefaultState(this.getStateDefinition().any().setValue(DISALLOW_RETURN, false));
	}

	private static void causeLightning(Level level, BlockPos pos, boolean destructive) {
		LightningBolt bolt = new LightningBolt(EntityType.LIGHTNING_BOLT, level);
		bolt.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
		bolt.setVisualOnly(destructive);
		level.addFreshEntity(bolt);

		if (destructive && level instanceof ServerLevel) {
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
		if (portalSize.incrementAndGet() > TFConfig.maxPortalSize) return false;

		boolean isPoolProbablyEnclosed = true;

		for (int i = 0; i < 4 && portalSize.intValue() <= TFConfig.maxPortalSize; i++) {
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

	public static boolean isPlayerNotifiedOfRequirement(ServerPlayer player) {
		return playersNotified.contains(player);
	}

	public static void playerNotifiedOfRequirement(ServerPlayer player) {
		playersNotified.add(player);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(DISALLOW_RETURN);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		return AABB;
	}

	@Override
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

				if (!TFConfig.checkPortalPlacement) {
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
				causeLightning(level, pos, TFConfig.destructivePortalLightning);

				for (Map.Entry<BlockPos, Boolean> checkedPos : blocksChecked.entrySet()) {
					if (checkedPos.getValue()) {
						level.setBlock(checkedPos.getKey(), TFBlocks.TWILIGHT_PORTAL.get().defaultBlockState(), Block.UPDATE_CLIENTS);
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

	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
		boolean good = level.getBlockState(pos.below()).isFaceSturdy(level, pos, Direction.UP);

		for (Direction facing : Direction.Plane.HORIZONTAL) {
			if (!good) break;

			BlockState neighboringState = level.getBlockState(pos.relative(facing));

			good = isGrassOrDirt(neighboringState) || neighboringState == state;
		}

		if (!good) {
			level.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, pos, Block.getId(state));
			level.setBlock(pos, Blocks.WATER.defaultBlockState(), Block.UPDATE_ALL);
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
						PacketDistributor.sendToPlayer(player, info == null ? new MissingAdvancementToastPacket(Component.translatable("twilightforest.ui.advancement.no_title"), new ItemStack(TFBlocks.TWILIGHT_PORTAL_MINIATURE_STRUCTURE.get())) : new MissingAdvancementToastPacket(info.getTitle(), info.getIcon()));
						TFPortalBlock.playerNotifiedOfRequirement(player);
					}

					return;
				}
			}

			if (entity.canUsePortal(false)) {
				entity.setAsInsidePortal(this, entity.blockPosition());
				entity.getData(TFDataAttachments.TF_PORTAL_COOLDOWN).setInPortal(true);
			}
		}
	}

	// Full [VanillaCopy] of NetherPortalBlock.animateTick
	@Override
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

	@Override
	public int getPortalTransitionTime(ServerLevel level, Entity entity) {
		return 60;
	}

	@Nullable
	@Override
	public DimensionTransition getPortalDestination(ServerLevel level, Entity entity, BlockPos pos) {
		if (cachedOriginDimension == null) cachedOriginDimension = ResourceKey.create(Registries.DIMENSION, ResourceLocation.parse(TFConfig.originDimension));
		ResourceKey<Level> newDimension = !level.dimension().location().equals(TFDimension.DIMENSION) ? TFDimension.DIMENSION_KEY : cachedOriginDimension;
		ServerLevel serverlevel = level.getServer().getLevel(newDimension);
		if (serverlevel == null) {
			return null;
		} else {
			WorldBorder worldborder = serverlevel.getWorldBorder();
			double d0 = DimensionType.getTeleportationScale(level.dimensionType(), serverlevel.dimensionType());
			BlockPos newPos = worldborder.clampToBounds(pos.getX() * d0, pos.getY(), pos.getZ() * d0);
			return TFTeleporter.createTransition(entity, serverlevel, newPos, false);
		}
	}
}

package twilightforest.block;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.*;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.lang3.mutable.MutableInt;
import twilightforest.TFConfig;
import twilightforest.TwilightForestMod;
import twilightforest.world.TFDimensions;
import twilightforest.world.TFGenerationSettings;
import twilightforest.world.TFTeleporter;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

// KelpBlock seems to use ILiquidContainer as it's a block that permanently has water, so I suppose in best practices we also use this interface as well?
public class BlockTFPortal extends BreakableBlock implements ILiquidContainer {

	public static final BooleanProperty DISALLOW_RETURN = BooleanProperty.create("is_one_way");

	private static final VoxelShape AABB = VoxelShapes.create(new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 0.8125F, 1.0F));
	private static final AxisAlignedBB AABB_ITEM = new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 0.4F, 1.0F);

	private static final int MIN_PORTAL_SIZE =  4;
	private static final int MAX_PORTAL_SIZE = 64;

	public BlockTFPortal(Block.Properties props) {
		super(props);
		this.setDefaultState(this.stateContainer.getBaseState().with(DISALLOW_RETURN, false));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(DISALLOW_RETURN);
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return AABB;
	}

	@Override
	@Deprecated
	public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return state.get(DISALLOW_RETURN) ? AABB : VoxelShapes.empty();
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		// The portal itself is kind of technically water, and this checks the checkbox in Sugar Cane logic to not destroy itself when portal is made.
		return Fluids.WATER.getFlowingFluidState(1, false); // 1 is minimum value. Minecraft wiki at time of this writing has the values backwards.
	}

	//	@Override
//	@Deprecated
//	public void addCollisionBoxToList(BlockState state, World world, BlockPos pos, AxisAlignedBB entityBB, List<AxisAlignedBB> blockBBs, @Nullable Entity entity, boolean isActualState) {
//		addCollisionBoxToList(pos, entityBB, blockBBs, entity instanceof EntityItem ? AABB_ITEM : state.getCollisionBoundingBox(world, pos));
//	}

	public boolean tryToCreatePortal(World world, BlockPos pos, ItemEntity catalyst, @Nullable PlayerEntity player) {

		BlockState state = world.getBlockState(pos);

		if (canFormPortal(state) && world.getBlockState(pos.down()).isSolid()) {
			Map<BlockPos, Boolean> blocksChecked = new HashMap<>();
			blocksChecked.put(pos, true);

			MutableInt size = new MutableInt(0);

			if (recursivelyValidatePortal(world, pos, blocksChecked, size, state) && size.intValue() >= MIN_PORTAL_SIZE) {

				if (TFConfig.COMMON_CONFIG.checkPortalDestination.get()) {
					boolean checkProgression = TFGenerationSettings.isProgressionEnforced(catalyst.world);
					if (!TFTeleporter.isSafeAround(world, pos, catalyst, checkProgression)) {
						// TODO: "failure" effect - particles?
						if (player != null) {
							player.sendStatusMessage(new TranslationTextComponent(TwilightForestMod.ID + ".twilight_portal.unsafe"), true);
						}
						return false;
					}
				}

				catalyst.getItem().shrink(1);
				causeLightning(world, pos, TFConfig.COMMON_CONFIG.portalLightning.get());

				for (Map.Entry<BlockPos, Boolean> checkedPos : blocksChecked.entrySet()) {
					if (checkedPos.getValue()) {
						world.setBlockState(checkedPos.getKey(), TFBlocks.twilight_portal.get().getDefaultState(), 2);
					}
				}

				return true;
			}
		}

		return false;
	}

	public boolean canFormPortal(BlockState state) {
		return state == Blocks.WATER.getDefaultState() || state.getBlock() == this && state.get(DISALLOW_RETURN);
	}

	private static void causeLightning(World world, BlockPos pos, boolean fake) {
		LightningBoltEntity bolt = new LightningBoltEntity(EntityType.LIGHTNING_BOLT, world);
		bolt.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
		bolt.setEffectOnly(fake);
		world.addEntity(bolt);

		if (fake && world instanceof ServerWorld) {
			double range = 3.0D;
			List<Entity> list = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos).grow(range));

			for (Entity victim : list) {
				if (!ForgeEventFactory.onEntityStruckByLightning(victim, bolt)) {
					victim.func_241841_a((ServerWorld) world, bolt);
				}
			}
		}
	}

	private static boolean recursivelyValidatePortal(World world, BlockPos pos, Map<BlockPos, Boolean> blocksChecked, MutableInt portalSize, BlockState requiredState) {

		if (portalSize.incrementAndGet() > MAX_PORTAL_SIZE) return false;

		boolean isPoolProbablyEnclosed = true;

		for (int i = 0; i < 4 /* FIXME Screw it, 4. Thanks Mojang /* Direction.Plane.HORIZONTAL .length*/ && portalSize.intValue() <= MAX_PORTAL_SIZE; i++) {
			BlockPos positionCheck = pos.offset(Direction.byHorizontalIndex(i));

			if (!blocksChecked.containsKey(positionCheck)) {
				BlockState state = world.getBlockState(positionCheck);

				if (state == requiredState && world.getBlockState(positionCheck.down()).isSolid()) {
					blocksChecked.put(positionCheck, true);
					if (isPoolProbablyEnclosed) {
						isPoolProbablyEnclosed = recursivelyValidatePortal(world, positionCheck, blocksChecked, portalSize, requiredState);
					}

				} else if (isGrassOrDirt(state) && isNatureBlock(world.getBlockState(positionCheck.up())) || state.getBlock() == TFBlocks.uberous_soil.get()) {
					blocksChecked.put(positionCheck, false);

				} else return false;
			}
		}

		return isPoolProbablyEnclosed;
	}

	private static boolean isNatureBlock(BlockState state) {
		Material mat = state.getMaterial();
		return mat == Material.PLANTS || mat == Material.TALL_PLANTS || mat == Material.LEAVES;
	}

	private static boolean isGrassOrDirt(BlockState state) {
		Material mat = state.getMaterial();
		return state.isSolid() && (mat == Material.ORGANIC || mat == Material.EARTH);
	}

	@Override
	@Deprecated
	public void neighborChanged(BlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
		boolean good = world.getBlockState(pos.down()).isSolid();

		for (Direction facing : Direction.Plane.HORIZONTAL) {
			if (!good) break;

			BlockState neighboringState = world.getBlockState(pos.offset(facing));

			good = isGrassOrDirt(neighboringState) || neighboringState == state;
		}

		if (!good) {
			world.playEvent(2001, pos, Block.getStateId(state));
			world.setBlockState(pos, Blocks.WATER.getDefaultState(), 0b11);
		}
	}

	//TODO: Move to client
//	@Override
//	@OnlyIn(Dist.CLIENT)
//	public BlockRenderLayer getRenderLayer() {
//		return BlockRenderLayer.TRANSLUCENT;
//	}

	@Override
	public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entity) {
		if (state == this.getDefaultState()) {
			attemptSendPlayer(entity, false);
		}
	}

	private static RegistryKey<World> getDestination(Entity entity) {
		return !entity.getEntityWorld().getDimensionKey().getLocation().equals(TFDimensions.twilightForest.getLocation())
				? TFDimensions.twilightForest : World.OVERWORLD /*DimensionType.byName(new ResourceLocation(TFConfig.COMMON_CONFIG.originDimension.get()))*/;
	}

	public static void attemptSendPlayer(Entity entity, boolean forcedEntry) {

		if (!entity.isAlive() || entity.world.isRemote) {
			return;
		}

		if (entity.isPassenger() || entity.isBeingRidden() || !entity.isNonBoss()) {
			return;
		}

		if (!forcedEntry && entity.portalCounter > 0) {
			return;
		}

		// set a cooldown before this can run again
		entity.portalCounter = 10;

		RegistryKey<World> destination = getDestination(entity);
		ServerWorld serverWorld = entity.getEntityWorld().getServer().getWorld(destination);

		if(serverWorld == null)
			return;

		entity.changeDimension(serverWorld, new TFTeleporter());

		if (destination == TFDimensions.twilightForest && entity instanceof ServerPlayerEntity) {
			ServerPlayerEntity playerMP = (ServerPlayerEntity) entity;
			// set respawn point for TF dimension to near the arrival portal
			playerMP.func_242111_a(destination, playerMP.getPosition(), playerMP.rotationYaw, true, false);
		}
	}

	// Full [VanillaCopy] of BlockPortal.randomDisplayTick
	// TODO Eeeh... Let's look at changing this too alongside a new model.
	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		int random = rand.nextInt(100);
		if (stateIn.get(DISALLOW_RETURN) && random < 80) return;

		if (random == 0) {
			worldIn.playSound((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, SoundEvents.BLOCK_PORTAL_AMBIENT, SoundCategory.BLOCKS, 0.5F, rand.nextFloat() * 0.4F + 0.8F, false);
		}

		for (int i = 0; i < 4; ++i) {
			double xPos = (double) ((float) pos.getX() + rand.nextFloat());
			double yPos = pos.getY()+1D;
			double zPos = (double) ((float) pos.getZ() + rand.nextFloat());
			double xSpeed = ((double) rand.nextFloat() - 0.5D) * 0.5D;
			double ySpeed = rand.nextFloat();
			double zSpeed = ((double) rand.nextFloat() - 0.5D) * 0.5D;
			//int j = rand.nextInt(2) * 2 - 1;

			//if (worldIn.getBlockState(pos.west()).getBlock() != this && worldIn.getBlockState(pos.east()).getBlock() != this) {
			//	xPos = (double) pos.getX() + 0.5D + 0.25D * (double) j;
			//	xSpeed = (double) (rand.nextFloat() * 2.0F * (float) j);
			//} else {
			//	zPos = (double) pos.getZ() + 0.5D + 0.25D * (double) j;
			//	zSpeed = (double) (rand.nextFloat() * 2.0F * (float) j);
			//}

			worldIn.addParticle(ParticleTypes.PORTAL, xPos, yPos, zPos, xSpeed, ySpeed, zSpeed);
		}
	}

	@Override
	public boolean canContainFluid(IBlockReader iBlockReader, BlockPos blockPos, BlockState blockState, Fluid fluid) {
		return false;
	}

	@Override
	public boolean receiveFluid(IWorld iWorld, BlockPos blockPos, BlockState blockState, FluidState fluidState) {
		return false;
	}
}

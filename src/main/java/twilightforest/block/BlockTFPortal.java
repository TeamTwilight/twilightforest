package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBreakable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.mutable.MutableInt;
import twilightforest.TFConfig;
import twilightforest.TFTeleporter;
import twilightforest.TwilightForestMod;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class BlockTFPortal extends BlockBreakable {

	public static final IProperty<Boolean> DISALLOW_RETURN = PropertyBool.create("is_one_way");

	private static final AxisAlignedBB AABB = new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 0.8125F, 1.0F);
	private static final AxisAlignedBB AABB_ITEM = new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 0.4F, 1.0F);
	private static final int PORTAL_SIZE_LIMIT = 64;

	public BlockTFPortal() {
		super(Material.PORTAL, false);
		this.setHardness(-1F);
		this.setSoundType(SoundType.GLASS);
		this.setLightLevel(0.75F);
		this.setDefaultState(this.blockState.getBaseState().withProperty(DISALLOW_RETURN, false));
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, DISALLOW_RETURN);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(DISALLOW_RETURN) ? 1 : 0;
	}

	@Override
	@Deprecated
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(DISALLOW_RETURN, meta == 1);
	}

	@Override
	@Deprecated
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		return AABB;
	}

	@Override
	@Deprecated
	public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		return state.getValue(DISALLOW_RETURN) ? AABB : NULL_AABB;
	}

	@Override
	@Deprecated
	public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos, AxisAlignedBB entityBB, List<AxisAlignedBB> blockBBs, @Nullable Entity entity, boolean isActualState) {
		addCollisionBoxToList(pos, entityBB, blockBBs, entity != null && entity instanceof EntityItem ? AABB_ITEM : state.getCollisionBoundingBox(world, pos));
	}

	@Override
	@Deprecated
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	@Deprecated
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}

	public boolean tryToCreatePortal(World world, BlockPos pos, EntityItem catalyst, @Nullable EntityPlayer player) {

		IBlockState state = world.getBlockState(pos);

		if (state == Blocks.WATER.getDefaultState() || (state.getBlock() == this && state.getValue(DISALLOW_RETURN))) {
			HashMap<BlockPos, Boolean> blocksChecked = new HashMap<>();
			blocksChecked.put(pos, true);

			MutableInt size = new MutableInt(0);

			if (recursivelyValidatePortal(world, pos, blocksChecked, size, state) && size.intValue() > 3) {

				if (TFConfig.checkPortalDestination) {
					TFTeleporter teleporter = TFTeleporter.getTeleporterForDim(catalyst.getServer(), getDestination(catalyst));
					boolean checkProgression = catalyst.world.getGameRules().getBoolean(TwilightForestMod.ENFORCED_PROGRESSION_RULE);
					if (!teleporter.isSafeAround(pos, catalyst, checkProgression)) {
						// TODO: "failure" effect - particles?
						if (player != null) {
							player.sendStatusMessage(new TextComponentTranslation(TwilightForestMod.ID + ".twilight_portal.unsafe"), true);
						}
						return false;
					}
				}

				catalyst.getItem().shrink(1);
				causeLightning(world, pos, TFConfig.portalLightning);

				for (Map.Entry<BlockPos, Boolean> checkedPos : blocksChecked.entrySet()) {
					if (checkedPos.getValue()) {
						world.setBlockState(checkedPos.getKey(), TFBlocks.twilight_portal.getDefaultState(), 2);
					}
				}

				return true;
			}
		}

		return false;
	}

	private static void causeLightning(World world, BlockPos pos, boolean fake) {
		EntityLightningBolt bolt = new EntityLightningBolt(world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, fake);
		world.addWeatherEffect(bolt);

		if (fake) {
			double range = 3.0D;
			List<Entity> list = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos).grow(range));

			for (Entity victim : list) {
				if (!ForgeEventFactory.onEntityStruckByLightning(victim, bolt)) {
					victim.onStruckByLightning(bolt);
				}
			}
		}
	}

	private static boolean recursivelyValidatePortal(World world, BlockPos pos, HashMap<BlockPos, Boolean> blocksChecked, MutableInt waterLimit, IBlockState requiredBlockFor) {
		boolean isPoolProbablyEnclosed = true;

		waterLimit.increment();
		if (waterLimit.intValue() > PORTAL_SIZE_LIMIT) return false;

		for (int i = 0; i < EnumFacing.HORIZONTALS.length && waterLimit.intValue() <= PORTAL_SIZE_LIMIT; i++) {
			BlockPos positionCheck = pos.offset(EnumFacing.HORIZONTALS[i]);

			if (!blocksChecked.containsKey(positionCheck)) {
				IBlockState state = world.getBlockState(positionCheck);

				if (state == requiredBlockFor && world.getBlockState(positionCheck.down()).isFullCube()) {
					blocksChecked.put(positionCheck, true);

					isPoolProbablyEnclosed = isPoolProbablyEnclosed && recursivelyValidatePortal(world, positionCheck, blocksChecked, waterLimit, requiredBlockFor);
				} else if ((isGrassOrDirt(state) && isNatureBlock(world.getBlockState(positionCheck.up()))) || state.getBlock() == TFBlocks.uberous_soil) {
					blocksChecked.put(positionCheck, false);
				} else return false;
			}
		}

		return isPoolProbablyEnclosed;
	}

	private static boolean isNatureBlock(IBlockState state) {
		Material mat = state.getMaterial();
		return (mat == Material.PLANTS || mat == Material.VINE || mat == Material.LEAVES);
	}

	private static boolean isGrassOrDirt(IBlockState state) {
		Material mat = state.getMaterial();
		return state.isFullCube() && (mat == Material.GRASS || mat == Material.GROUND);
	}

	@Override
	@Deprecated
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block notUsed, BlockPos fromPos) {
		boolean good = world.getBlockState(pos.down()).isFullCube();

		for (EnumFacing facing : EnumFacing.HORIZONTALS) {
			if (!good) break;

			IBlockState neighboringState = world.getBlockState(pos.offset(facing));

			good = isGrassOrDirt(neighboringState) || neighboringState == state;
		}

		if (!good) {
			world.playEvent(2001, pos, Block.getStateId(state));
			world.setBlockState(pos, Blocks.WATER.getDefaultState(), 0b11);
		}
	}

	@Override
	public int quantityDropped(Random random) {
		return 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.TRANSLUCENT;
	}

	@Override
	public void onEntityCollision(World world, BlockPos pos, IBlockState state, Entity entity) {
		if (state == this.getDefaultState()) {
			attemptSendPlayer(entity, false);
		}
	}

	private static int getDestination(Entity entity) {
		return entity.dimension != TFConfig.dimension.dimensionID
				? TFConfig.dimension.dimensionID : TFConfig.originDimension;
	}

	public static void attemptSendPlayer(Entity entity, boolean forcedEntry) {

		if (entity.isDead || entity.world.isRemote) {
			return;
		}

		if (entity.isRiding() || entity.isBeingRidden() || !entity.isNonBoss()) {
			return;
		}

		if (!forcedEntry && entity.timeUntilPortal > 0) {
			return;
		}

		// set a cooldown before this can run again
		entity.timeUntilPortal = 10;

		int destination = getDestination(entity);

		entity.changeDimension(destination, TFTeleporter.getTeleporterForDim(entity.getServer(), destination));

		if (destination == TFConfig.dimension.dimensionID && entity instanceof EntityPlayerMP) {
			EntityPlayerMP playerMP = (EntityPlayerMP) entity;
			// set respawn point for TF dimension to near the arrival portal
			playerMP.setSpawnChunk(new BlockPos(playerMP), true, TFConfig.dimension.dimensionID);
		}
	}

	// Full [VanillaCopy] of BlockPortal.randomDisplayTick
	// TODO Eeeh... Let's look at changing this too alongside a new model.
	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		int random = rand.nextInt(100);
		if (stateIn.getValue(DISALLOW_RETURN) && random < 80) return;

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

			worldIn.spawnParticle(EnumParticleTypes.PORTAL, xPos, yPos, zPos, xSpeed, ySpeed, zSpeed);
		}
	}
}

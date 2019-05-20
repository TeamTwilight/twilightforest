package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.network.TFPacketHandler;
import twilightforest.client.ModelRegisterCallback;
import twilightforest.client.ModelUtils;
import twilightforest.item.TFItems;
import twilightforest.network.PacketAnnihilateBlock;
import twilightforest.world.ChunkGeneratorTFBase;
import twilightforest.world.TFWorld;

import java.util.Random;

public class BlockTFCastleDoor extends Block implements ModelRegisterCallback {

	public static final IProperty<Boolean> ACTIVE = PropertyBool.create("active");
	public static final IProperty<Integer> LOCK_INDEX = PropertyInteger.create("lock_index", 0, 3);

	private final boolean isVanished;

	private static final AxisAlignedBB REAPPEARING_BB = new AxisAlignedBB(0.375F, 0.375F, 0.375F, 0.625F, 0.625F, 0.625F);

	public BlockTFCastleDoor(boolean isVanished) {
		super(isVanished ? Material.GLASS : Material.ROCK, isVanished ? MapColor.AIR : MapColor.CYAN);

		//this.setBlockUnbreakable();
		//this.setResistance(Float.MAX_VALUE);

		this.setHardness(100F);
		this.setResistance(35F);

		this.isVanished = isVanished;
		this.lightOpacity = isVanished ? 0 : 255;

		this.setCreativeTab(TFItems.creativeTab);
		this.setDefaultState(blockState.getBaseState().withProperty(ACTIVE, false));
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, ACTIVE, LOCK_INDEX);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int meta = state.getValue(LOCK_INDEX);
		meta |= state.getValue(ACTIVE) ? 8 : 0;
		return meta;
	}

	@Override
	@Deprecated
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState()
				.withProperty(ACTIVE, (meta & 8) != 0)
				.withProperty(LOCK_INDEX, meta & 3);
	}

	@Override
	@Deprecated
	public boolean isOpaqueCube(IBlockState state) {
		return !this.isVanished;
	}

	@Override
	@Deprecated
	public boolean isFullCube(IBlockState state) {
		return !this.isVanished;
	}

	@Override
	@Deprecated
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return isVanished ? BlockFaceShape.UNDEFINED : super.getBlockFaceShape(worldIn, state, pos, face);
	}

	@Override
	@Deprecated
	public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		return isVanished ? NULL_AABB : super.getCollisionBoundingBox(state, world, pos);
	}

	@Override
	@Deprecated
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		return isVanished ? REAPPEARING_BB : super.getBoundingBox(state, world, pos);
	}

	@Override
	public boolean isPassable(IBlockAccess blockAccess, BlockPos pos) {
		return this.isVanished;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		return onActivation(world, pos, state);
	}

	@Override
	@Deprecated
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
		if (!(block instanceof BlockTFCastleDoor) && world.isBlockPowered(pos)) {
			onActivation(world, pos, state);
		}
	}

	private boolean onActivation(World world, BlockPos pos, IBlockState state) {

		if (isVanished || state.getValue(ACTIVE)) return false;

		if (isBlockLocked(world, pos)) {
			world.playSound(null, pos, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 1.0F, 0.3F);
		} else {
			changeToActiveBlock(world, pos, state);
		}
		return true;
	}

	private static void changeToActiveBlock(World world, BlockPos pos, IBlockState originState) {
		changeActiveState(world, pos, true, originState);
		playVanishSound(world, pos);

		world.scheduleUpdate(pos, originState.getBlock(), 2 + world.rand.nextInt(5));
	}

	private static void changeActiveState(World world, BlockPos pos, boolean active, IBlockState originState) {
		if (originState.getBlock() instanceof BlockTFCastleDoor) {
			world.setBlockState(pos, originState.withProperty(ACTIVE, active), 3);
			world.markBlockRangeForRenderUpdate(pos, pos);
			world.notifyNeighborsRespectDebug(pos, originState.getBlock(), false);
		}
	}

	private static boolean isBlockLocked(World world, BlockPos pos) {
		// check if we are in a structure, and if that structure says that we are locked
		if (!world.isRemote && TFWorld.getChunkGenerator(world) instanceof ChunkGeneratorTFBase) {
			ChunkGeneratorTFBase generator = (ChunkGeneratorTFBase) TFWorld.getChunkGenerator(world);
			return generator.isStructureLocked(pos, world.getBlockState(pos).getValue(LOCK_INDEX));
		}
		return false;
	}

	@Override
	public int tickRate(World world) {
		return 5;
	}

	@Override // todo 1.10 recheck all of this
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {

		if (world.isRemote) return;

		if (this.isVanished) {
			if (state.getValue(ACTIVE)) {
				world.setBlockState(pos, TFBlocks.castle_door.getDefaultState().withProperty(LOCK_INDEX, state.getValue(LOCK_INDEX)));
				playVanishSound(world, pos);
			} else {
				changeToActiveBlock(world, pos, state);
			}
		} else {
			// if we have an active castle door, turn it into a vanished door block
			if (state.getValue(ACTIVE)) {
				world.setBlockState(pos, getOtherBlock(this).getDefaultState().withProperty(LOCK_INDEX, state.getValue(LOCK_INDEX)));
				world.scheduleUpdate(pos, getOtherBlock(this), 80);

				playReappearSound(world, pos);

				this.sendAnnihilateBlockPacket(world, pos);

				// activate all adjacent inactive doors
				for (EnumFacing e : EnumFacing.VALUES) {
					checkAndActivateCastleDoor(world, pos.offset(e));
				}
			}
			// inactive solid door blocks we don't care about updates
		}
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return TFItems.castle_door;
	}

	@Override
	public int damageDropped(IBlockState state) {
		return state.getValue(LOCK_INDEX);
	}

	private void sendAnnihilateBlockPacket(World world, BlockPos pos) {
		IMessage message = new PacketAnnihilateBlock(pos);
		NetworkRegistry.TargetPoint targetPoint = new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 64);
		TFPacketHandler.CHANNEL.sendToAllAround(message, targetPoint);
	}

	private static void playVanishSound(World world, BlockPos pos) {
		world.playSound(null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.125f, world.rand.nextFloat() * 0.25F + 1.75F);
	}

	private static void playReappearSound(World world, BlockPos pos) {
		world.playSound(null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.125f, world.rand.nextFloat() * 0.25F + 1.25F);
	}

	private static Block getOtherBlock(Block block) {
		return block == TFBlocks.castle_door ? TFBlocks.castle_door_vanished : TFBlocks.castle_door;
	}

	/**
	 * If the targeted block is a vanishing block, activate it
	 */
	public static void checkAndActivateCastleDoor(World world, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);

		if (state.getBlock() == TFBlocks.castle_door && !state.getValue(ACTIVE) && !isBlockLocked(world, pos)) {
			changeToActiveBlock(world, pos, state);
		}
//		if (state.getBlock() == TFBlocks.castle_door_vanished && !state.getValue(ACTIVE) && !isBlockLocked(world, pos)) {
//    		changeToActiveBlock(world, x, y, z, meta);
//    	}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random random) {
		if (state.getValue(ACTIVE)) ;
		{
			for (int i = 0; i < 1; ++i) {
				//this.sparkle(world, x, y, z, random);
			}
		}
	}

	// [VanillaCopy] BlockRedStoneOre.spawnParticles with own rand
	@SuppressWarnings("unused")
	private void sparkle(World worldIn, BlockPos pos, Random rand) {
		Random random = rand;
		double d0 = 0.0625D;

		for (int i = 0; i < 6; ++i) {
			double d1 = (double) ((float) pos.getX() + random.nextFloat());
			double d2 = (double) ((float) pos.getY() + random.nextFloat());
			double d3 = (double) ((float) pos.getZ() + random.nextFloat());

			if (i == 0 && !worldIn.getBlockState(pos.up()).isOpaqueCube()) {
				d2 = (double) pos.getY() + 0.0625D + 1.0D;
			}

			if (i == 1 && !worldIn.getBlockState(pos.down()).isOpaqueCube()) {
				d2 = (double) pos.getY() - 0.0625D;
			}

			if (i == 2 && !worldIn.getBlockState(pos.south()).isOpaqueCube()) {
				d3 = (double) pos.getZ() + 0.0625D + 1.0D;
			}

			if (i == 3 && !worldIn.getBlockState(pos.north()).isOpaqueCube()) {
				d3 = (double) pos.getZ() - 0.0625D;
			}

			if (i == 4 && !worldIn.getBlockState(pos.east()).isOpaqueCube()) {
				d1 = (double) pos.getX() + 0.0625D + 1.0D;
			}

			if (i == 5 && !worldIn.getBlockState(pos.west()).isOpaqueCube()) {
				d1 = (double) pos.getX() - 0.0625D;
			}

			if (d1 < (double) pos.getX() || d1 > (double) (pos.getX() + 1) || d2 < 0.0D || d2 > (double) (pos.getY() + 1) || d3 < (double) pos.getZ() || d3 > (double) (pos.getZ() + 1)) {
				worldIn.spawnParticle(EnumParticleTypes.REDSTONE, d1, d2, d3, 0.0D, 0.0D, 0.0D, new int[0]);
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public BlockRenderLayer getRenderLayer() {
		return isVanished ? BlockRenderLayer.TRANSLUCENT : BlockRenderLayer.CUTOUT;
	}

	@SideOnly(Side.CLIENT)
	@Override
	@Deprecated
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		return !(blockAccess.getBlockState(pos.offset(side)).getBlock() instanceof BlockTFCastleDoor) && super.shouldSideBeRendered(blockState, blockAccess, pos, side);
	}

	@Override
	public void getSubBlocks(CreativeTabs creativeTab, NonNullList<ItemStack> list) {
		if (this == TFBlocks.castle_door) {
			for (int i = 0; i < LOCK_INDEX.getAllowedValues().size(); i++) {
				list.add(new ItemStack(this, 1, i));
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModel() {
		if (!isVanished) {
			ModelUtils.registerToStateSingleVariant(this, LOCK_INDEX);
		}
	}

	@Override
	public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
		return new ItemStack(TFItems.castle_door, 1, damageDropped(state));
	}

	@Override
	protected ItemStack getSilkTouchDrop(IBlockState state) {
		return new ItemStack(TFItems.castle_door, 1, damageDropped(state));
	}
}

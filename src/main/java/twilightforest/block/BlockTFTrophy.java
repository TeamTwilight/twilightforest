package twilightforest.block;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.SkullBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import twilightforest.TFSounds;
import twilightforest.enums.BossVariant;
import twilightforest.tileentity.TileEntityTFTrophy;

import javax.annotation.Nullable;

//@Optional.Interface(modid = "thaumcraft", iface = "thaumcraft.api.crafting.IInfusionStabiliser")
public class BlockTFTrophy extends SkullBlock /*implements IInfusionStabiliser*/ {

	private static final AxisAlignedBB HYDRA_Y_BB = new AxisAlignedBB(0.25F, 0.0F, 0.25F, 0.75F, 0.5F, 0.75F);
	private static final AxisAlignedBB HYDRA_EAST_BB = new AxisAlignedBB(0.0F, 0.25F, 0.25F, 0.5F, 0.75F, 0.75F);
	private static final AxisAlignedBB HYDRA_WEST_BB = new AxisAlignedBB(0.5F, 0.25F, 0.25F, 1.0F, 0.75F, 0.75F);
	private static final AxisAlignedBB HYDRA_SOUTH_BB = new AxisAlignedBB(0.25F, 0.25F, 0.0F, 0.75F, 0.75F, 0.5F);
	private static final AxisAlignedBB HYDRA_NORTH_BB = new AxisAlignedBB(0.25F, 0.25F, 0.5F, 0.75F, 0.75F, 1.0F);
	private static final VoxelShape URGHAST_BB = VoxelShapes.create(new AxisAlignedBB(0.25F, 0.5F, 0.25F, 0.75F, 1F, 0.75F));

	private final BossVariant variant;

	public BlockTFTrophy(BossVariant variant) {
		super(Types.PLAYER, Properties.create(Material.MISCELLANEOUS).hardnessAndResistance(1.0F)); //TODO: Placeholder variable
		this.variant = variant;
		setDefaultState(stateContainer.getBaseState().with(SkullBlock.ROTATION, 0));
	}

	@Override
	@Deprecated
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader access, BlockPos pos, ISelectionContext context) {
		TileEntity te = access.getTileEntity(pos);
		if (te instanceof TileEntityTFTrophy) {
			//TODO 1.14: Flatten
			switch (variant) {
				//TODO: Rotations have changed. Verify
//				case HYDRA:
//					// hydra bounds TODO: actually use non-default bounds here
//					switch (state.get(SkullBlock.ROTATION)) {
//						case UP:
//						default:
//							return HYDRA_Y_BB;
//						case NORTH:
//							return HYDRA_NORTH_BB;
//						case SOUTH:
//							return HYDRA_SOUTH_BB;
//						case WEST:
//							return HYDRA_WEST_BB;
//						case EAST:
//							return HYDRA_EAST_BB;
//					}
				case UR_GHAST:
					return URGHAST_BB;
				// TODO: also add case for Questing Ram?
			}
		}
		return super.getShape(state, access, pos, context);
	}

	@Override
	@Deprecated
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand handIn, BlockRayTraceResult hit) {
		TileEntity te = worldIn.getTileEntity(pos);
		if (te instanceof TileEntityTFTrophy) {
			SoundEvent sound = null;
			float volume = 1.0F;
			switch (variant) {
				case NAGA:
					sound = TFSounds.NAGA_RATTLE;
					volume = 1.25F;
					break;
				case LICH:
					sound = SoundEvents.ENTITY_BLAZE_AMBIENT;
					volume = 0.35F;
					break;
				case HYDRA:
					sound = TFSounds.HYDRA_GROWL;
					break;
				case UR_GHAST:
					sound = SoundEvents.ENTITY_GHAST_AMBIENT;
					break;
				case SNOW_QUEEN:
					sound = TFSounds.SNOW_QUEEN_AMBIENT;
					break;
				case KNIGHT_PHANTOM:
					sound = TFSounds.WRAITH;
					break;
				case MINOSHROOM:
					sound = SoundEvents.ENTITY_COW_AMBIENT;
					volume = 0.5F;
					break;
				case QUEST_RAM:
					sound = SoundEvents.ENTITY_SHEEP_AMBIENT;
					break;
			}
			if (sound != null)
				worldIn.playSound(playerIn, pos, sound, SoundCategory.BLOCKS, volume, 16.0F);
		}
		return ActionResultType.SUCCESS;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileEntityTFTrophy();
	}

	public BossVariant getVariant() {
		return variant;
	}

	//	@Override
//	public ItemStack getItem(World world, BlockPos pos, BlockState state) {
//		TileEntity te = world.getTileEntity(pos);
//		if (te instanceof TileEntityTFTrophy) {
//			return new ItemStack(TFItems.trophy, 1, ((TileEntityTFTrophy) te).getSkullType());
//		}
//		return ItemStack.EMPTY;
//	}
//
//	@Override
//	public Item getItemDropped(BlockState state, Random rand, int fortune) {
//		return TFItems.trophy;
//	}
//
//	// [VanillaCopy] of superclass, relevant edits indicated
//	@Override
//	public List<ItemStack> getDrops(IBlockAccess worldIn, BlockPos pos, BlockState state, int fortune) {
//		java.util.List<ItemStack> ret = new java.util.ArrayList<ItemStack>();
//		{
//			if (!((Boolean) state.getValue(NODROP)).booleanValue()) {
//				TileEntity tileentity = worldIn.getTileEntity(pos);
//
//				if (tileentity instanceof TileEntitySkull) {
//					TileEntitySkull tileentityskull = (TileEntitySkull) tileentity;
//					ItemStack itemstack = new ItemStack(TFItems.trophy, 1, tileentityskull.getSkullType()); // TF - use our item
//
//                    /*if (tileentityskull.getSkullType() == 3 && tileentityskull.getPlayerProfile() != null)
//					{
//                        itemstack.setTag(new CompoundNBT());
//                        CompoundNBT CompoundNBT = new CompoundNBT();
//                        NBTUtil.writeGameProfile(CompoundNBT, tileentityskull.getPlayerProfile());
//                        itemstack.getTag().setTag("SkullOwner", CompoundNBT);
//                    }*/// TF - don't set player skins
//
//					ret.add(itemstack);
//				}
//			}
//		}
//		return ret;
//	}
//
//	@Override
//	public boolean canStabaliseInfusion(World world, BlockPos blockPos) {
//		return true;
//	}
}

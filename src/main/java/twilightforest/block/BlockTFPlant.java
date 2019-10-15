package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.enums.PlantVariant;
import twilightforest.client.ModelRegisterCallback;
import twilightforest.item.TFItems;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockTFPlant extends BlockBush implements IShearable, ModelRegisterCallback
{
	public static final IProperty<PlantVariant> VARIANT = PropertyEnum.create("variant", PlantVariant.class);

	protected BlockTFPlant() {
		super(Material.PLANTS);
		this.setTickRandomly(true);
		this.setHardness(0.0F);
		this.setSoundType(SoundType.PLANT);
		this.setCreativeTab(TFItems.creativeTab);
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, VARIANT);
	}

	@Override
	@Deprecated
	public IBlockState getStateFromMeta(int meta) {
		PlantVariant variant = PlantVariant.values()[MathHelper.clamp(meta, 0, PlantVariant.values().length)];
		return getDefaultState().withProperty(VARIANT, variant);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(VARIANT).ordinal();
	}

	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
		world.scheduleUpdate(pos, this, world.rand.nextInt(50) + 20);
	}

	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
		return (state.getBlock().isAir(state, world, pos) || state.getMaterial().isReplaceable()) && canBlockStay(world, pos, state);
	}

	@Override
	public boolean canBlockStay(World world, BlockPos pos, IBlockState state) {
		IBlockState soil = world.getBlockState(pos.down());

		/*
			Comment from superclass:
			Forge: This function is called during world gen and placement, before this block is set, so if we are not 'here' then assume it's the pre-check.
			Therefore, we just take the OR of all the conditions below as the most general "can block stay" check
		*/
		if (state.getBlock() != this) {
			return BlockTFPlant.canPlaceRootAt(world, pos)
					|| soil.getBlock().canSustainPlant(soil, world, pos.down(), EnumFacing.UP, this)
					|| soil.isSideSolid(world, pos, EnumFacing.UP)
					|| ((world.getLight(pos) >= 3 || world.canSeeSky(pos)) && soil.getBlock().canSustainPlant(soil, world, pos.down(), EnumFacing.UP, this));
		} else {
			switch (state.getValue(VARIANT)) {
				case TORCHBERRY:
				case ROOT_STRAND:
					return BlockTFPlant.canPlaceRootAt(world, pos);
				case FORESTGRASS:
				case DEADBUSH:
					return soil.getBlock().canSustainPlant(soil, world, pos.down(), EnumFacing.UP, this);
				case MUSHGLOOM:
				case MOSSPATCH:
					return soil.isSideSolid(world, pos, EnumFacing.UP);
				default:
					return (world.getLight(pos) >= 3 || world.canSeeSky(pos)) && soil.getBlock().canSustainPlant(soil, world, pos.down(), EnumFacing.UP, this);
			}
		}
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess access, BlockPos pos) {
		PlantVariant variant = state.getValue(VARIANT);

		long seed = pos.getX() * 3129871 ^ pos.getY() * 116129781L ^ pos.getZ();
		seed = seed * seed * 42317861L + seed * 11L;

		if (variant == PlantVariant.MOSSPATCH) {
			int xOff0 = (int) (seed >> 12 & 3L);
			int xOff1 = (int) (seed >> 15 & 3L);
			int zOff0 = (int) (seed >> 18 & 3L);
			int zOff1 = (int) (seed >> 21 & 3L);

			boolean xConnect0 = access.getBlockState(pos.east()).getBlock() == this && access.getBlockState(pos.east()).getValue(VARIANT) == PlantVariant.MOSSPATCH;
			boolean xConnect1 = access.getBlockState(pos.west()).getBlock() == this && access.getBlockState(pos.west()).getValue(VARIANT) == PlantVariant.MOSSPATCH;
			boolean zConnect0 = access.getBlockState(pos.south()).getBlock() == this && access.getBlockState(pos.south()).getValue(VARIANT) == PlantVariant.MOSSPATCH;
			boolean zConnect1 = access.getBlockState(pos.north()).getBlock() == this && access.getBlockState(pos.north()).getValue(VARIANT) == PlantVariant.MOSSPATCH;

			return new AxisAlignedBB(xConnect1 ? 0F : (1F + xOff1) / 16F, 0.0F, zConnect1 ? 0F : (1F + zOff1) / 16F,
					xConnect0 ? 1F : (15F - xOff0) / 16F, 1F / 16F, zConnect0 ? 1F : (15F - zOff0) / 16F);

		} else if (variant == PlantVariant.CLOVERPATCH) {
			int xOff0 = (int) (seed >> 12 & 3L);
			int xOff1 = (int) (seed >> 15 & 3L);
			int zOff0 = (int) (seed >> 18 & 3L);
			int zOff1 = (int) (seed >> 21 & 3L);

			int yOff0 = (int) (seed >> 24 & 1L);
			int yOff1 = (int) (seed >> 27 & 1L);

			boolean xConnect0 = access.getBlockState(pos.east()).getBlock() == this && access.getBlockState(pos.east()).getValue(VARIANT) == PlantVariant.CLOVERPATCH;
			boolean xConnect1 = access.getBlockState(pos.west()).getBlock() == this && access.getBlockState(pos.west()).getValue(VARIANT) == PlantVariant.CLOVERPATCH;
			boolean zConnect0 = access.getBlockState(pos.south()).getBlock() == this && access.getBlockState(pos.south()).getValue(VARIANT) == PlantVariant.CLOVERPATCH;
			boolean zConnect1 = access.getBlockState(pos.north()).getBlock() == this && access.getBlockState(pos.north()).getValue(VARIANT) == PlantVariant.CLOVERPATCH;

			return new AxisAlignedBB(xConnect1 ? 0F : (1F + xOff1) / 16F, 0.0F, zConnect1 ? 0F : (1F + zOff1) / 16F,
					xConnect0 ? 1F : (15F - xOff0) / 16F, (1F + yOff0 + yOff1) / 16F, zConnect0 ? 1F : (15F - zOff0) / 16F);
		} else if (variant == PlantVariant.MAYAPPLE) {
			return new AxisAlignedBB(4F / 16F, 0, 4F / 16F, 13F / 16F, 6F / 16F, 13F / 16F);
		} else {
			return FULL_BLOCK_AABB;
		}
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		return NULL_AABB;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public int getLightValue(IBlockState state) {
		switch (state.getValue(VARIANT)) {
			case MUSHGLOOM:
				return 3;
			case TORCHBERRY:
				return 8;
			default:
				return 0;
		}
	}

	public static boolean canPlaceRootAt(World world, BlockPos pos) {
		IBlockState state = world.getBlockState(pos.up());
		if (state.getMaterial() == Material.GROUND || state.getMaterial() == Material.GRASS) {
			// can always hang below dirt blocks
			return true;
		} else {
			return (state.getBlock() == TFBlocks.twilight_plant && state.getValue(BlockTFPlant.VARIANT) == PlantVariant.ROOT_STRAND)
					|| state == TFBlocks.root.getDefaultState();
		}
	}

	@Override
	public Block.EnumOffsetType getOffsetType() {
		return Block.EnumOffsetType.NONE;
	}


//    /** todo thaumcraft
//     * Drops the block items with a specified chance of dropping the specified items
//     */
//    public void dropBlockAsItemWithChance(World var1, int var2, int var3, int var4, int var5, float var6, int var7)
//    {
//        super.dropBlockAsItemWithChance(var1, var2, var3, var4, var5, var6, var7);
//
//        if (!var1.isRemote && var5 == 1)
//        {
//            this.dropBlockAsItem_do(var1, var2, var3, var4, new ItemStack(mod_ThaumCraft.itemComponents, 1, 2));
//        }
//
//        if (!var1.isRemote && var5 == 3)
//        {
//            this.dropBlockAsItem_do(var1, var2, var3, var4, new ItemStack(mod_ThaumCraft.itemPlants, 1, 3));
//        }
//
//        if (!var1.isRemote && (var5 == 2 || var5 == 4) && var1.rand.nextInt(10) == 0)
//        {
//            this.dropBlockAsItem_do(var1, var2, var3, var4, new ItemStack(mod_ThaumCraft.itemArtifactTainted, 1, 0));
//        }
//    }
//    

	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		List<ItemStack> ret = NonNullList.create();

		switch (state.getValue(VARIANT)) {
			case TORCHBERRY:
				ret.add(new ItemStack(TFItems.torchberries));
				break;
			case MOSSPATCH:
			case MAYAPPLE:
			case CLOVERPATCH:
			case FIDDLEHEAD:
			case FORESTGRASS:
			case DEADBUSH:
			case ROOT_STRAND:
				break;
			default:
				ret.add(new ItemStack(this, 1, damageDropped(state)));
				break;
		}

		return ret;
	}

	@Override
	public int damageDropped(IBlockState state) {
		return getMetaFromState(state);
	}

	@Override
	public boolean isShearable(ItemStack item, IBlockAccess world, BlockPos pos) {
		return true;
	}

	@Override
	public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune) {
		List<ItemStack> ret = NonNullList.create();
		ret.add(new ItemStack(this, 1, damageDropped(world.getBlockState(pos))));
		return ret;
	}

	@Override
	public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack) {
		// do not call normal harvest if the player is shearing
		if (world.isRemote || stack.getItem() != Items.SHEARS) {
			super.harvestBlock(world, player, pos, state, te, stack);
		}
	}

	@Override
	public void getSubBlocks(CreativeTabs creativeTab, NonNullList<ItemStack> list) {
		int n = PlantVariant.values().length;
		for (int i = 0; i < n; i++) {
			list.add(new ItemStack(this, 1, i));
		}
	}

	@Override
	public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
		IBlockState blockState = world.getBlockState(pos);
		if (blockState.getBlock() == this) {
			switch (blockState.getValue(VARIANT)) {
				case MOSSPATCH:
				case MUSHGLOOM:
					return EnumPlantType.Cave;
				default:
					return EnumPlantType.Plains;
			}
		}
		return EnumPlantType.Plains;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random random) {
		super.randomDisplayTick(state, world, pos, random);

		if (state.getValue(VARIANT) == PlantVariant.MOSSPATCH && random.nextInt(10) == 0) {
			world.spawnParticle(EnumParticleTypes.TOWN_AURA, pos.getX() + random.nextFloat(), pos.getY() + 0.1F, pos.getZ() + random.nextFloat(), 0.0D, 0.0D, 0.0D);
		}

	}

	@SideOnly(Side.CLIENT)
	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModel() {
		for (int i = 0; i < PlantVariant.values().length; i++) {
			String variant = "inventory_" + PlantVariant.values()[i].getName();
			ModelResourceLocation mrl = new ModelResourceLocation(getRegistryName(), variant);
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), i, mrl);
		}
	}

	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
		return super.getExtendedState(state, world, pos);
	}
}

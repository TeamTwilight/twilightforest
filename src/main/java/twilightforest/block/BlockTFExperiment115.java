package twilightforest.block;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.TwilightForestMod;
import twilightforest.client.ModelRegisterCallback;
import twilightforest.item.TFItems;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BlockTFExperiment115 extends Block implements ModelRegisterCallback {
    public static final PropertyInteger NOMS = PropertyInteger.create("omnomnom", 0, 7);
    private static final PropertyBool REGENERATE = PropertyBool.create("regenerate");

    private static final AxisAlignedBB[] AABB = new AxisAlignedBB[] {
            new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.5D, 0.9375D),
            new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.5D, 0.5D, 0.9375D),
            new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.5D, 0.5D, 0.5D)
    };

    public BlockTFExperiment115() {
        super(Material.CAKE, MapColor.IRON);
        this.setTickRandomly(true);
        this.setDefaultState(this.blockState.getBaseState().withProperty(NOMS, 7).withProperty(REGENERATE, false));
        this.setSoundType(SoundType.CLOTH);
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(TFItems.experiment_115);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        switch (state.getValue(NOMS)) {
            default:
                return AABB[0];
            case 4:
            case 5:
                return AABB[1];
            case 6:
            case 7:
                return AABB[2];
        }
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        int bitesTaken = state.getValue(NOMS);
        ItemStack stack = player.getHeldItem(hand);

        if (!player.isSneaking()) {
            if (bitesTaken > 0 && stack.getItem() == TFItems.experiment_115) {
                worldIn.setBlockState(pos, state.withProperty(NOMS, bitesTaken - 1));
                if (!player.isCreative()) stack.shrink(1);
                if (player instanceof EntityPlayerMP) CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
                return true;
            } else if (((!state.getValue(REGENERATE)) && stack.getItem() == Items.REDSTONE) && (player.isCreative() || bitesTaken == 0)) {
                worldIn.setBlockState(pos, state.withProperty(REGENERATE,true));
                if (!player.isCreative()) stack.shrink(1);
                if (player instanceof EntityPlayerMP) CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
                return true;
            }
        }

        return this.eatCake(worldIn, pos, state, player) || stack.isEmpty();
    }

    private boolean eatCake(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        if (!player.canEat(false)) return false;
        else {
            player.addStat(StatList.CAKE_SLICES_EATEN);
            player.getFoodStats().addStats(4, 0.3F);
            int i = state.getValue(NOMS);

            if (i < 7) world.setBlockState(pos, state.withProperty(NOMS, i + 1), 3);
            else       world.setBlockToAir(pos);

            if (player instanceof EntityPlayerMP)
                CriteriaTriggers.CONSUME_ITEM.trigger((EntityPlayerMP) player, new ItemStack(TFItems.experiment_115, 8 - i));

            return true;
        }
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (state.getValue(REGENERATE) && state.getValue(NOMS) != 0) {
            worldIn.setBlockState(pos, state.withProperty(NOMS, state.getValue(NOMS) - 1));
        }
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (!this.canBlockStay(worldIn, pos)) {
            worldIn.setBlockToAir(pos);
        }
    }

    private boolean canBlockStay(World world, BlockPos pos) {
        return world.getBlockState(pos.down()).getMaterial().isSolid();
    }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos pos) {
        return super.canPlaceBlockAt(world, pos) && canBlockStay(world, pos);
    }

    @Override
    public int quantityDropped(Random random)
    {
        return 0;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(NOMS, meta & 7).withProperty(REGENERATE, (meta & 8) == 8);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(NOMS) | (state.getValue(REGENERATE) ? 8 : 0);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, NOMS, REGENERATE);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos) {
        return 15-(state.getValue(NOMS)*2);
    }

    @Override
    public boolean hasComparatorInputOverride(IBlockState state)
    {
        return true;
    }

    @Override
    public boolean canProvidePower(IBlockState state) {
        return state.getValue(REGENERATE);
    }

    @Override
    public int getWeakPower(IBlockState state, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return state.getValue(REGENERATE) ? 15-(state.getValue(NOMS)*2) : 0;
    }

    @SideOnly(Side.CLIENT)
    public void registerModel() {
        ModelLoader.setCustomModelResourceLocation(TFItems.experiment_115, 0, new ModelResourceLocation(TwilightForestMod.ID + ":experiment_115", "inventory"));
        ModelLoader.setCustomModelResourceLocation(TFItems.experiment_115, 1, new ModelResourceLocation(TwilightForestMod.ID + ":experiment_115", "inventory_full"));
        ModelLoader.setCustomModelResourceLocation(TFItems.experiment_115, 2, new ModelResourceLocation(TwilightForestMod.ID + ":experiment_115", "inventory_think"));
    }
}

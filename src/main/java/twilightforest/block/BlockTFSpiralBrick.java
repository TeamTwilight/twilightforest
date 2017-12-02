package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.client.ModelRegisterCallback;
import twilightforest.client.ModelUtils;

import java.util.Locale;

public class BlockTFSpiralBrick extends Block implements ModelRegisterCallback {
    public static final PropertyEnum<Diagonals> DIAGONAL = PropertyEnum.create("diagonal", Diagonals.class);
    public static final PropertyEnum<EnumFacing.Axis> AXIS_FACING = PropertyEnum.create("axis", EnumFacing.Axis.class);

    public BlockTFSpiralBrick() {
        super(Material.ROCK, MapColor.STONE);
        this.setHardness(1.5F);
        this.setResistance(10.0F);
        this.setSoundType(SoundType.STONE);
        this.setDefaultState(this.blockState.getBaseState().withProperty(DIAGONAL, Diagonals.TOP_RIGHT).withProperty(AXIS_FACING, EnumFacing.Axis.X));
    }

    @Override
    public BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, AXIS_FACING, DIAGONAL);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return (state.getValue(AXIS_FACING).ordinal() << 2) | (state.getValue(DIAGONAL).ordinal());
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(DIAGONAL, Diagonals.values()[meta & 0b0011]).withProperty(AXIS_FACING, EnumFacing.Axis.values()[(meta & 0b1100) >> 2]);
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        EnumFacing playerFacing = /*facing.getAxis() != EnumFacing.Axis.Y ? facing.getAxis() :*/ placer.getHorizontalFacing();

        // The Spiral should face the player when they place. It is two-sided.
        // This should divide the placement zone into a 2x2 zones and
        // should determine which quarter of the spiral the player shall place.
        return super.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer)
                .withProperty(AXIS_FACING, playerFacing.getAxis())
                .withProperty(DIAGONAL, getDiagonalFromPlacement(facing, playerFacing, playerFacing.getAxis() == EnumFacing.Axis.X ? hitZ : hitX, hitY));
    }

    private static Diagonals getDiagonalFromPlacement(EnumFacing blockFace, EnumFacing playerHorizontalFace, float hitXorZ, float hitY) {
        float correctedHitXorZ = (playerHorizontalFace.getAxis() == EnumFacing.Axis.X) /* ^ (playerHorizontalFace.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE)*/
                ? hitXorZ : (hitXorZ - 1F) * (-1F);

        if (blockFace.getAxis() == EnumFacing.Axis.Y)
            return getDiagonalFromDirectionals(correctedHitXorZ < 0.5F, blockFace == EnumFacing.UP);
        else
            return getDiagonalFromDirectionals(correctedHitXorZ < 0.5F, hitY < 0.5F);
    }

    private static Diagonals getDiagonalFromDirectionals(boolean isRight, boolean isBottom) {
        if (isRight) return isBottom ? Diagonals.BOTTOM_RIGHT : Diagonals.TOP_RIGHT;
        else return isBottom ? Diagonals.BOTTOM_LEFT : Diagonals.TOP_LEFT;
    }

    private enum Diagonals implements IStringSerializable {
        TOP_RIGHT,
        BOTTOM_RIGHT,
        BOTTOM_LEFT,
        TOP_LEFT;

        @Override
        public String getName() {
            return name().toLowerCase(Locale.ROOT);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModel() {
        ModelLoader.setCustomStateMapper(this, new StateMap.Builder().withName(AXIS_FACING).withSuffix("_spiral_bricks").build());
        ModelUtils.registerToState(this, 0, this.getDefaultState().withProperty(DIAGONAL, Diagonals.BOTTOM_LEFT));
    }
}

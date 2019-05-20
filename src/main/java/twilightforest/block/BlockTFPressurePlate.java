package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPressurePlate;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import twilightforest.client.ModelRegisterCallback;

public class BlockTFPressurePlate extends BlockPressurePlate implements ModelRegisterCallback {

    private final MapColor mapColor;

    public BlockTFPressurePlate(Material material, MapColor mapColor, BlockPressurePlate.Sensitivity sensitivity) {
        super(material, sensitivity);
        this.mapColor = mapColor;
    }

    @Override
    public Block setSoundType(SoundType sound) {
        return super.setSoundType(sound);
    }

    @Override
    public MapColor getMapColor(IBlockState state, IBlockAccess world, BlockPos pos) {
        return mapColor;
    }
}

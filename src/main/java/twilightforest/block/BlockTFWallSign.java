package twilightforest.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.WallSignBlock;
import net.minecraft.block.WoodType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import twilightforest.tileentity.TileEntityTFSign;

public class BlockTFWallSign extends WallSignBlock {
    public BlockTFWallSign(Properties properties, WoodType type) {
        super(properties, type);
    }

    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new TileEntityTFSign();
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }
}

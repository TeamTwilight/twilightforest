package twilightforest.tileentity;

import net.minecraft.tileentity.SignTileEntity;
import net.minecraft.tileentity.TileEntityType;

public class TileEntityTFSign extends SignTileEntity {

    @Override
    public TileEntityType<?> getType() {
        return TFTileEntities.TF_SIGN.get();
    }
}

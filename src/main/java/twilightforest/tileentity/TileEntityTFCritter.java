package twilightforest.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraftforge.fml.common.FMLCommonHandler;

public abstract class TileEntityTFCritter extends TileEntity implements ITickable {
	protected static final boolean isClient = FMLCommonHandler.instance().getSide().isClient();
}

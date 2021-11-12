package twilightforest.world;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class NoReturnTeleporter extends TFTeleporter {
    public NoReturnTeleporter() {
        super(false);
    }

    @Override
    protected BlockPos makePortalAt(Level world, BlockPos pos) {
        return pos;
    }
}

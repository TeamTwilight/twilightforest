package twilightforest;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class NoPortalTFTeleporter extends TFTeleporter {
	
	public static NoPortalTFTeleporter getTeleporterForDim(MinecraftServer server, int dim) {
		WorldServer ws = server.getWorld(dim);

		for (Teleporter t : ws.customTeleporters) {
			if (t instanceof NoPortalTFTeleporter) {
				return (NoPortalTFTeleporter) t;
			}
		}

		NoPortalTFTeleporter tp = new NoPortalTFTeleporter(ws);
		ws.customTeleporters.add(tp);
		return tp;
	}

	private NoPortalTFTeleporter(WorldServer dest) {
		super(dest);
	}
	
	@Override
	protected BlockPos makePortalAt(World world, BlockPos pos) {
		return pos;
	}
}

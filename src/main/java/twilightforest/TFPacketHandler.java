package twilightforest;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import twilightforest.network.PacketAnnihilateBlock;
import twilightforest.network.PacketAreaProtection;
import twilightforest.network.PacketChangeBiome;
import twilightforest.network.PacketEnforceProgressionStatus;
import twilightforest.network.PacketMagicMap;
import twilightforest.network.PacketMazeMap;
import twilightforest.network.PacketStructureProtection;
import twilightforest.network.PacketStructureProtectionClear;
import twilightforest.network.PacketThrowPlayer;

public class TFPacketHandler {
	public static final SimpleNetworkWrapper CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(TwilightForestMod.ID);

	public static void init() {
		int id = 0;
		CHANNEL.registerMessage(PacketAnnihilateBlock.Handler.class, PacketAnnihilateBlock.class, id++, Side.CLIENT);
		CHANNEL.registerMessage(PacketAreaProtection.Handler.class, PacketAreaProtection.class, id++, Side.CLIENT);
		CHANNEL.registerMessage(PacketChangeBiome.Handler.class, PacketChangeBiome.class, id++, Side.CLIENT);
		CHANNEL.registerMessage(PacketEnforceProgressionStatus.Handler.class, PacketEnforceProgressionStatus.class, id++, Side.CLIENT);
		CHANNEL.registerMessage(PacketStructureProtection.Handler.class, PacketStructureProtection.class, id++, Side.CLIENT);
		CHANNEL.registerMessage(PacketStructureProtectionClear.Handler.class, PacketStructureProtectionClear.class, id++, Side.CLIENT);
		CHANNEL.registerMessage(PacketThrowPlayer.Handler.class, PacketThrowPlayer.class, id++, Side.CLIENT);
		CHANNEL.registerMessage(PacketMagicMap.Handler.class, PacketMagicMap.class, id++, Side.CLIENT);
		CHANNEL.registerMessage(PacketMazeMap.Handler.class, PacketMazeMap.class, id++, Side.CLIENT);
	}

}

package twilightforest.network;

import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import twilightforest.TwilightForestMod;

import java.util.function.Supplier;

public class TFPacketHandler {
	private static final String PROTOCOL_VERSION = "1";
	public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
			TwilightForestMod.prefix("channel"),
			() -> PROTOCOL_VERSION,
			PROTOCOL_VERSION::equals,
			PROTOCOL_VERSION::equals
	);

	@SuppressWarnings("UnusedAssignment")
	public static void init() {
		int id = 0;
		CHANNEL.messageBuilder(PacketAnnihilateBlock.class, id++).encoder(PacketAnnihilateBlock::encode).decoder(PacketAnnihilateBlock::new).consumer(PacketAnnihilateBlock.Handler::onMessage).add();
		CHANNEL.messageBuilder(PacketAreaProtection.class, id++).encoder(PacketAreaProtection::encode).decoder(PacketAreaProtection::new).consumer(new SimpleChannel.MessageBuilder.ToBooleanBiFunction<PacketAreaProtection, Supplier<NetworkEvent.Context>>() {
			@Override
			public boolean applyAsBool(PacketAreaProtection message, Supplier<NetworkEvent.Context> ctx) {
				return PacketAreaProtection.Handler.onMessage(message, ctx);
			}
		}).add();
		CHANNEL.messageBuilder(PacketChangeBiome.class, id++).encoder(PacketChangeBiome::encode).decoder(PacketChangeBiome::new).consumer(PacketChangeBiome.Handler::onMessage).add();
		CHANNEL.messageBuilder(PacketEnforceProgressionStatus.class, id++).encoder(PacketEnforceProgressionStatus::encode).decoder(PacketEnforceProgressionStatus::new).consumer(PacketEnforceProgressionStatus.Handler::onMessage).add();
		CHANNEL.messageBuilder(PacketStructureProtection.class, id++).encoder(PacketStructureProtection::encode).decoder(PacketStructureProtection::new).consumer(PacketStructureProtection.Handler::onMessage).add();
		CHANNEL.messageBuilder(PacketStructureProtectionClear.class, id++).encoder(PacketStructureProtectionClear::encode).decoder(PacketStructureProtectionClear::new).consumer(PacketStructureProtectionClear.Handler::onMessage).add();
		CHANNEL.messageBuilder(PacketThrowPlayer.class, id++).encoder(PacketThrowPlayer::encode).decoder(PacketThrowPlayer::new).consumer(PacketThrowPlayer.Handler::onMessage).add();
		CHANNEL.messageBuilder(PacketMagicMap.class, id++).encoder(PacketMagicMap::encode).decoder(PacketMagicMap::new).consumer(PacketMagicMap.Handler::onMessage).add();
		CHANNEL.messageBuilder(PacketMazeMap.class, id++).encoder(PacketMazeMap::encode).decoder(PacketMazeMap::new).consumer(PacketMazeMap.Handler::onMessage).add();
		CHANNEL.messageBuilder(PacketUpdateShield.class, id++).encoder(PacketUpdateShield::encode).decoder(PacketUpdateShield::new).consumer(PacketUpdateShield.Handler::onMessage).add();
		CHANNEL.messageBuilder(PacketSetSkylightEnabled.class, id++).encoder(PacketSetSkylightEnabled::encode).decoder(PacketSetSkylightEnabled::new).consumer(PacketSetSkylightEnabled.Handler::onMessage).add();
		CHANNEL.messageBuilder(PacketUncraftingGui.class, id++).encoder(PacketUncraftingGui::encode).decoder(PacketUncraftingGui::new).consumer(PacketUncraftingGui.Handler::onMessage).add();
		CHANNEL.messageBuilder(PacketUpdateTFMultipart.class, id++).encoder(PacketUpdateTFMultipart::encode).decoder(PacketUpdateTFMultipart::new).consumer(PacketUpdateTFMultipart.Handler::onMessage).add();
		CHANNEL.messageBuilder(PacketSpawnFallenLeafFrom.class, id++).encoder(PacketSpawnFallenLeafFrom::encode).decoder(PacketSpawnFallenLeafFrom::new).consumer(PacketSpawnFallenLeafFrom.Handler::onMessage).add();
	}
}

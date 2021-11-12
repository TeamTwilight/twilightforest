package twilightforest.network;

import net.minecraftforge.fmllegacy.network.NetworkEvent;
import net.minecraftforge.fmllegacy.network.NetworkRegistry;
import net.minecraftforge.fmllegacy.network.simple.SimpleChannel;
import twilightforest.TwilightForestMod;

import java.util.function.Supplier;

public class TFPacketHandler {
	// Bump this number every time theres a breaking change, to ensure people dont mess things up when joining on the wrong version
	private static final String PROTOCOL_VERSION = "2";
	public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
			TwilightForestMod.prefix("channel"),
			() -> PROTOCOL_VERSION,
			PROTOCOL_VERSION::equals,
			PROTOCOL_VERSION::equals
	);

	@SuppressWarnings("UnusedAssignment")
	public static void init() {
		int id = 0;
		CHANNEL.messageBuilder(AreaProtectionPacket.class, id++).encoder(AreaProtectionPacket::encode).decoder(AreaProtectionPacket::new).consumer(new SimpleChannel.MessageBuilder.ToBooleanBiFunction<AreaProtectionPacket, Supplier<NetworkEvent.Context>>() {
			@Override
			public boolean applyAsBool(AreaProtectionPacket message, Supplier<NetworkEvent.Context> ctx) {
				return AreaProtectionPacket.Handler.onMessage(message, ctx);
			}
		}).add();
		CHANNEL.messageBuilder(ChangeBiomePacket.class, id++).encoder(ChangeBiomePacket::encode).decoder(ChangeBiomePacket::new).consumer(ChangeBiomePacket.Handler::onMessage).add();
		CHANNEL.messageBuilder(EnforceProgressionStatusPacket.class, id++).encoder(EnforceProgressionStatusPacket::encode).decoder(EnforceProgressionStatusPacket::new).consumer(EnforceProgressionStatusPacket.Handler::onMessage).add();
		CHANNEL.messageBuilder(StructureProtectionPacket.class, id++).encoder(StructureProtectionPacket::encode).decoder(StructureProtectionPacket::new).consumer(StructureProtectionPacket.Handler::onMessage).add();
		CHANNEL.messageBuilder(StructureProtectionClearPacket.class, id++).encoder(StructureProtectionClearPacket::encode).decoder(StructureProtectionClearPacket::new).consumer(StructureProtectionClearPacket.Handler::onMessage).add();
		CHANNEL.messageBuilder(ThrowPlayerPacket.class, id++).encoder(ThrowPlayerPacket::encode).decoder(ThrowPlayerPacket::new).consumer(ThrowPlayerPacket.Handler::onMessage).add();
		CHANNEL.messageBuilder(MagicMapPacket.class, id++).encoder(MagicMapPacket::encode).decoder(MagicMapPacket::new).consumer(MagicMapPacket.Handler::onMessage).add();
		CHANNEL.messageBuilder(MazeMapPacket.class, id++).encoder(MazeMapPacket::encode).decoder(MazeMapPacket::new).consumer(MazeMapPacket.Handler::onMessage).add();
		CHANNEL.messageBuilder(UpdateShieldPacket.class, id++).encoder(UpdateShieldPacket::encode).decoder(UpdateShieldPacket::new).consumer(UpdateShieldPacket.Handler::onMessage).add();
		CHANNEL.messageBuilder(UncraftingGuiPacket.class, id++).encoder(UncraftingGuiPacket::encode).decoder(UncraftingGuiPacket::new).consumer(UncraftingGuiPacket.Handler::onMessage).add();
		CHANNEL.messageBuilder(UpdateTFMultipartPacket.class, id++).encoder(UpdateTFMultipartPacket::encode).decoder(UpdateTFMultipartPacket::new).consumer(UpdateTFMultipartPacket.Handler::onMessage).add();
		CHANNEL.messageBuilder(SpawnFallenLeafFromPacket.class, id++).encoder(SpawnFallenLeafFromPacket::encode).decoder(SpawnFallenLeafFromPacket::new).consumer(SpawnFallenLeafFromPacket.Handler::onMessage).add();
		CHANNEL.messageBuilder(MissingAdvancementToastPacket.class, id++).encoder(MissingAdvancementToastPacket::encode).decoder(MissingAdvancementToastPacket::new).consumer(MissingAdvancementToastPacket::handle).add();
	}
}

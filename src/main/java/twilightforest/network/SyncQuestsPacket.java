package twilightforest.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;
import twilightforest.TwilightForestMod;
import tamaized.beanification.Autowired;
import twilightforest.entity.passive.quest.ram.QuestingRamContext;
import twilightforest.entity.passive.quest.ram.QuestingRamCurrentContext;

public record SyncQuestsPacket(QuestingRamContext ram) implements CustomPacketPayload {

	@Autowired
	private static QuestingRamCurrentContext questingRamCurrentContext;

	public static final Type<@NotNull SyncQuestsPacket> TYPE = new Type<>(TwilightForestMod.prefix("sync_quests"));
	public static final StreamCodec<@NotNull RegistryFriendlyByteBuf, @NotNull SyncQuestsPacket> STREAM_CODEC = StreamCodec.composite(
		QuestingRamContext.STREAM_CODEC, SyncQuestsPacket::ram,
		SyncQuestsPacket::new
	);

	@Override
	public Type<? extends @NotNull CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handle(SyncQuestsPacket packet, IPayloadContext context) {
		context.enqueueWork(() -> questingRamCurrentContext.setContext(packet.ram()));
	}
}

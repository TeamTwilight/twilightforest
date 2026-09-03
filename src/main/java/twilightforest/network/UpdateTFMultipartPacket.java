package twilightforest.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import twilightforest.TwilightForestMod;
import twilightforest.entity.TFPart;

import javax.annotation.Nullable;
import java.util.*;

public record UpdateTFMultipartPacket(int entityId, @Nullable Entity entity, @Nullable Map<Integer, PartDataHolder> data) implements CustomPacketPayload {

	public static final Type<UpdateTFMultipartPacket> TYPE = new Type<>(TwilightForestMod.prefix("update_multipart_entity"));
	public static final StreamCodec<RegistryFriendlyByteBuf, UpdateTFMultipartPacket> STREAM_CODEC = CustomPacketPayload.codec(UpdateTFMultipartPacket::write, UpdateTFMultipartPacket::new);

	public UpdateTFMultipartPacket(RegistryFriendlyByteBuf buf) {
		this(buf.readInt(), null, new HashMap<>());
		int index;
		while ((index = buf.readInt()) != -1) {
			this.data.put(index, PartDataHolder.decode(buf));
		}
	}

	public UpdateTFMultipartPacket(Entity entity) {
		this(-1, entity, collectPartData(entity));
	}

	private static Map<Integer, PartDataHolder> collectPartData(Entity entity) {
		Map<Integer, PartDataHolder> data = new HashMap<>();
		TFPart.forEachPart(entity, (part, index) -> data.put(index, part.writeData()));
		return data;
	}

	public void write(RegistryFriendlyByteBuf buf) {
		if (this.entity == null)
			throw new IllegalStateException("Null Entity while encoding UpdateTFMultipartPacket");
		if (this.data == null)
			throw new IllegalStateException("Null Data while encoding UpdateTFMultipartPacket");
		buf.writeInt(this.entity.getId());
		this.data.forEach((index, data) -> {
			buf.writeInt(index);
			data.encode(buf);
		});
		buf.writeInt(-1);
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handle(UpdateTFMultipartPacket message, IPayloadContext ctx) {
		ctx.enqueueWork(() -> {
			int eId = message.entity != null && message.entityId <= 0 ? message.entity.getId() : message.entityId; // Account for Singleplayer
			Entity ent = ctx.player().level().getEntity(eId);
			if (ent != null && message.data != null) {
				TFPart.forEachPart(ent, (part, index) -> {
					PartDataHolder data = message.data.get(index);
					if (data != null)
						part.readData(data);
				});
			}
		});
	}

	public record PartDataHolder(double x, double y, double z,
								 float yRot, float xRot,
								 float width, float height,
								 boolean fixed,
								 @Nullable List<SynchedEntityData.DataValue<?>> data) {


		public void encode(RegistryFriendlyByteBuf buffer) {
			buffer.writeDouble(this.x());
			buffer.writeDouble(this.y());
			buffer.writeDouble(this.z());
			buffer.writeFloat(this.yRot());
			buffer.writeFloat(this.xRot());
			buffer.writeFloat(this.width());
			buffer.writeFloat(this.height());
			buffer.writeBoolean(this.fixed());
			if (this.data() != null) {
				for (SynchedEntityData.DataValue<?> datavalue : this.data()) {
					datavalue.write(buffer);
				}
			}
			buffer.writeByte(255);
		}

		static PartDataHolder decode(RegistryFriendlyByteBuf buffer) {
			return new PartDataHolder(buffer.readDouble(), buffer.readDouble(), buffer.readDouble(),
				buffer.readFloat(), buffer.readFloat(),
				buffer.readFloat(), buffer.readFloat(),
				buffer.readBoolean(),
				unpack(buffer)
			);
		}

		private static List<SynchedEntityData.DataValue<?>> unpack(RegistryFriendlyByteBuf buf) {
			List<SynchedEntityData.DataValue<?>> list = new ArrayList<>();

			int i;
			while ((i = buf.readUnsignedByte()) != 255) {
				list.add(SynchedEntityData.DataValue.read(buf, i));
			}

			return list;
		}

	}
}

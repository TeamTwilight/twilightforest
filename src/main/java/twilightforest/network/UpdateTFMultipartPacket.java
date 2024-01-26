package twilightforest.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.entity.PartEntity;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import twilightforest.TwilightForestMod;
import twilightforest.entity.TFPart;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public record UpdateTFMultipartPacket(int entityId, @Nullable Entity entity, @Nullable List<PartDataHolder> data) implements CustomPacketPayload {

	public static final ResourceLocation ID = TwilightForestMod.prefix("update_multipart_entity");

	public UpdateTFMultipartPacket(FriendlyByteBuf buf) {
		this(buf.readInt(), null, new ArrayList<>());
		int len = buf.readInt();
		for (int i = 0; i < len; i++) {
			if (buf.readBoolean()) {
				this.data.add(PartDataHolder.decode(buf));
			}
		}
	}

	public UpdateTFMultipartPacket(Entity entity) {
		this(-1, entity, null);
	}

	@Override
	public void write(FriendlyByteBuf buf) {
		if (this.entity == null)
			throw new IllegalStateException("Null Entity while encoding UpdateTFMultipartPacket");
		buf.writeInt(this.entity.getId());
		PartEntity<?>[] parts = this.entity.getParts();
		// We assume the client and server part arrays are identical, else everything will crash and burn. Don't even bother handling it.
		if (parts != null) {
			buf.writeInt(parts.length);
			for (PartEntity<?> part : parts) {
				if (part instanceof TFPart<?> tfPart) {
					buf.writeBoolean(true);
					tfPart.writeData().encode(buf);
				} else {
					buf.writeBoolean(false);
				}
			}
		} else {
			buf.writeInt(0);
		}
	}

	@Override
	public ResourceLocation id() {
		return ID;
	}

	public static void handle(UpdateTFMultipartPacket message, PlayPayloadContext ctx) {
		ctx.workHandler().execute(() -> {
			int eId = message.entity != null && message.entityId <= 0 ? message.entity.getId() : message.entityId; // Account for Singleplayer
			Entity ent = ctx.level().orElseThrow().getEntity(eId);
			if (ent != null && ent.isMultipartEntity()) {
				PartEntity<?>[] parts = ent.getParts();
				if (parts == null)
					return;
				int index = 0;
				for (PartEntity<?> part : parts) {
					if (part instanceof TFPart<?> tfPart) {
						if (message.data == null && message.entity != null && message.entity.getParts()[index] instanceof TFPart<?> otherPart)
							tfPart.readData(otherPart.writeData());  // Account for Singleplayer
						else if (message.data != null)
							tfPart.readData(message.data.get(index));
						index++;
					}
				}
			}
		});
	}

	public record PartDataHolder(double x, double y, double z,
								 float yRot, float xRot,
								 float width, float height,
								 boolean fixed, boolean dirty,
								 @Nullable List<SynchedEntityData.DataValue<?>> data) {


		public void encode(FriendlyByteBuf buffer) {
			buffer.writeDouble(this.x());
			buffer.writeDouble(this.y());
			buffer.writeDouble(this.z());
			buffer.writeFloat(this.yRot());
			buffer.writeFloat(this.xRot());
			buffer.writeFloat(this.width());
			buffer.writeFloat(this.height());
			buffer.writeBoolean(this.fixed());
			buffer.writeBoolean(this.dirty());
			if (this.dirty() && this.data() != null) {
				for (SynchedEntityData.DataValue<?> datavalue : this.data()) {
					datavalue.write(buffer);
				}

				buffer.writeByte(255);
			}
		}

		static PartDataHolder decode(FriendlyByteBuf buffer) {
			boolean dirty;
			return new PartDataHolder(buffer.readDouble(), buffer.readDouble(), buffer.readDouble(),
					buffer.readFloat(), buffer.readFloat(),
					buffer.readFloat(), buffer.readFloat(),
					buffer.readBoolean(), dirty = buffer.readBoolean(),
					dirty ? unpack(buffer) : null
			);
		}

		private static List<SynchedEntityData.DataValue<?>> unpack(FriendlyByteBuf buf) {
			List<SynchedEntityData.DataValue<?>> list = new ArrayList<>();

			int i;
			while ((i = buf.readUnsignedByte()) != 255) {
				list.add(SynchedEntityData.DataValue.read(buf, i));
			}

			return list;
		}

	}
}

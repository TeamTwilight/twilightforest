package twilightforest.network;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import net.minecraftforge.entity.PartEntity;
import net.minecraftforge.fml.network.NetworkEvent;
import twilightforest.entity.TFPartEntity;

import java.io.IOException;
import java.util.List;
import java.util.function.Supplier;

public class PacketUpdateTFMultipart {

	private int id;
	private PacketBuffer buffer;
	private Entity entity;

	public PacketUpdateTFMultipart(PacketBuffer buf) {
		id = buf.readInt();
		buffer = buf;
	}

	public PacketUpdateTFMultipart(Entity entity) {
		this.entity = entity;
	}

	public void encode(PacketBuffer buf) {
		try {
			buf.writeInt(entity.getEntityId());
			PartEntity<?>[] parts = entity.getParts();
			// We assume the client and server part arrays are identical, else everything will crash and burn. Don't even bother handling it.
			if (parts != null) {
				for (PartEntity<?> part : parts) {
					if (part instanceof TFPartEntity) {
						TFPartEntity<?> tfPart = (TFPartEntity<?>) part;
						tfPart.writeData(buf);
						boolean dirty = tfPart.getDataManager().isDirty();
						buf.writeBoolean(dirty);
						if (dirty)
							EntityDataManager.writeEntries(tfPart.getDataManager().getDirty(), buf);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static class Handler {
		public static boolean onMessage(PacketUpdateTFMultipart message, Supplier<NetworkEvent.Context> ctx) {
			ctx.get().enqueueWork(new Runnable() {
				@Override
				public void run() {
					try {
						World world = Minecraft.getInstance().world;
						if (world == null)
							return;
						Entity ent = world.getEntityByID(message.id);
						if (ent != null && ent.isMultipartEntity()) {
							PartEntity<?>[] parts = ent.getParts();
							if (parts == null)
								return;
							for (PartEntity<?> part : parts) {
								if (part instanceof TFPartEntity) {
									TFPartEntity<?> tfPart = (TFPartEntity<?>) part;
									tfPart.readData(message.buffer);
									if (message.buffer.readBoolean()) {
										List<EntityDataManager.DataEntry<?>> data = EntityDataManager.readEntries(message.buffer);
										if (data != null)
											tfPart.getDataManager().setEntryValues(data);
									}
								}
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			return true;
		}
	}
}

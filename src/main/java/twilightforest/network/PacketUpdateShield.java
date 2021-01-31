package twilightforest.network;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import twilightforest.capabilities.CapabilityList;
import twilightforest.capabilities.shield.IShieldCapability;

import java.util.function.Supplier;

public class PacketUpdateShield {

	private final int entityID;
	private final int temporaryShields;
	private final int permanentShields;

	public PacketUpdateShield(int id, IShieldCapability cap) {
		entityID = id;
		temporaryShields = cap.temporaryShieldsLeft();
		permanentShields = cap.permanentShieldsLeft();
	}

	public PacketUpdateShield(Entity entity, IShieldCapability cap) {
		this(entity.getEntityId(), cap);
	}

	public PacketUpdateShield(PacketBuffer buf) {
		entityID = buf.readInt();
		temporaryShields = buf.readInt();
		permanentShields = buf.readInt();
	}

	public void encode(PacketBuffer buf) {
		buf.writeInt(entityID);
		buf.writeInt(temporaryShields);
		buf.writeInt(permanentShields);
	}

	public static class Handler {

		public static boolean onMessage(PacketUpdateShield message, Supplier<NetworkEvent.Context> ctx) {
			ctx.get().enqueueWork(new Runnable() {
				@Override
				public void run() {
					Entity entity = Minecraft.getInstance().world.getEntityByID(message.entityID);
					if (entity instanceof LivingEntity) {
						entity.getCapability(CapabilityList.SHIELDS).ifPresent(cap -> {
							cap.setShields(message.temporaryShields, true);
							cap.setShields(message.permanentShields, false);
						});
					}
				}
			});

			return true;
		}
	}
}

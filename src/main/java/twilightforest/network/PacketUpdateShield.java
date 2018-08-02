package twilightforest.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import twilightforest.capabilities.CapabilityList;
import twilightforest.capabilities.shield.IShieldCapability;

public class PacketUpdateShield implements IMessage {
	private int entityID;
	private int shields;

	public PacketUpdateShield() {
	}

	public PacketUpdateShield(int id, IShieldCapability cap) {
		entityID = id;
		shields = cap.shieldsLeft();
	}

	public PacketUpdateShield(Entity entity, IShieldCapability cap) {
		this(entity.getEntityId(), cap);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		entityID = buf.readInt();
		shields = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(entityID);
		buf.writeInt(shields);
	}

	public static class Handler implements IMessageHandler<PacketUpdateShield, IMessage> {

		@Override
		public IMessage onMessage(PacketUpdateShield message, MessageContext ctx) {
			Minecraft.getMinecraft().addScheduledTask(new Runnable() {
				@Override
				public void run() {
					Entity entity = Minecraft.getMinecraft().world.getEntityByID(message.entityID);
					if (entity != null && entity.hasCapability(CapabilityList.SHIELDS, null)) {
						IShieldCapability cap = entity.getCapability(CapabilityList.SHIELDS, null);
						if (cap != null)
							cap.setShields(message.shields);
					}
				}
			});

			return null;
		}
	}
}

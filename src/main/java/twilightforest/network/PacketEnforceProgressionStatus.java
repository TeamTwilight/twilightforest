package twilightforest.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import twilightforest.TwilightForestMod;

import java.util.function.Supplier;

public class PacketEnforceProgressionStatus {

	private final boolean enforce;

	public PacketEnforceProgressionStatus(PacketBuffer buf) {
		this.enforce = buf.readBoolean();
	}

	public PacketEnforceProgressionStatus(boolean enforce) {
		this.enforce = enforce;
	}

	public void encode(PacketBuffer buf) {
		buf.writeBoolean(enforce);
	}

	public static class Handler {

		public static boolean onMessage(PacketEnforceProgressionStatus message, Supplier<NetworkEvent.Context> ctx) {
			ctx.get().enqueueWork(new Runnable() {
				@Override
				public void run() {
					Minecraft.getInstance().world.getGameRules().get(TwilightForestMod.ENFORCED_PROGRESSION_RULE).set(message.enforce, null);
				}
			});
			return true;
		}
	}
}

package twilightforest.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import twilightforest.world.WorldProviderTwilightForest;

public class PacketSetSkylightEnabled implements IMessage {

    private boolean enabled;

    public PacketSetSkylightEnabled() {}

    public PacketSetSkylightEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        enabled = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(enabled);
    }

    public static class Handler implements IMessageHandler<PacketSetSkylightEnabled, IMessage> {

        @Override
        public IMessage onMessage(PacketSetSkylightEnabled message, MessageContext ctx) {
            WorldProviderTwilightForest.setSkylightEnabled(message.enabled);
            return null;
        }
    }
}

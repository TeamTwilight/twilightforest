package twilightforest.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import twilightforest.inventory.ContainerTFUncrafting;

public class PacketUncraftingGui implements IMessage {
    private int type;

    public PacketUncraftingGui(int type) {
        this.type = type;
    }

    public PacketUncraftingGui() {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        type = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(type);
    }

    public static class Handler implements IMessageHandler<PacketUncraftingGui, IMessage> {
        public Handler () {

        }

        @SuppressWarnings("Convert2Lambda")
        @Override
        public IMessage onMessage(PacketUncraftingGui message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;

            player.getServerWorld().addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    Container container = player.openContainer;

                    if (container instanceof ContainerTFUncrafting) {
                        ContainerTFUncrafting uncrafting = (ContainerTFUncrafting) container;

                        switch (message.type) {
                            case 0:
                                uncrafting.unrecipeInCycle++;
                                break;
                            case 1:
                                uncrafting.unrecipeInCycle--;
                                break;
                            case 2:
                                uncrafting.ingredientsInCycle++;
                                break;
                            case 3:
                                uncrafting.ingredientsInCycle--;
                                break;
                            case 4:
                                uncrafting.recipeInCycle++;
                                break;
                            case 5:
                                uncrafting.recipeInCycle--;
                                break;
                        }

                        if (message.type < 4)
                            uncrafting.onCraftMatrixChanged(uncrafting.tinkerInput);

                        if (message.type >= 4)
                            uncrafting.onCraftMatrixChanged(uncrafting.assemblyMatrix);
                    }
                }
            });

            return null;
        }
    }
}

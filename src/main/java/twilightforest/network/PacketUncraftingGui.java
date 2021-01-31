package twilightforest.network;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import twilightforest.inventory.ContainerTFUncrafting;

import java.util.function.Supplier;

public class PacketUncraftingGui {
    private int type;

    public PacketUncraftingGui(int type) {
        this.type = type;
    }

    public PacketUncraftingGui(PacketBuffer buf) {
        type = buf.readInt();
    }

    public void encode(PacketBuffer buf) {
        buf.writeInt(type);
    }

    public static class Handler {

        @SuppressWarnings("Convert2Lambda")
        public static boolean onMessage(PacketUncraftingGui message, Supplier<NetworkEvent.Context> ctx) {
            ServerPlayerEntity player = ctx.get().getSender();

            ctx.get().enqueueWork(new Runnable() {
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

            return true;
        }
    }
}

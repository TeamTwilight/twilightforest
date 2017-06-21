package twilightforest.item;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.client.ModelRegisterCallback;

@Mod.EventBusSubscriber(Side.CLIENT)
@SideOnly(Side.CLIENT)
public class TFRegisterItemRenderingEvent {
	@SubscribeEvent
	public static void onModelRegistryReady(ModelRegistryEvent event) {
		for (Block b : Block.REGISTRY) {
			if (b instanceof ModelRegisterCallback) {
				((ModelRegisterCallback) b).registerModel();
			}
		}

		for (Item i : Item.REGISTRY) {
			if (i instanceof ModelRegisterCallback) {
				((ModelRegisterCallback) i).registerModel();
			}
		}

		// Straggler
	}
}

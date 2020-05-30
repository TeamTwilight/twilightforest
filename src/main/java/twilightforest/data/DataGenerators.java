package twilightforest.data;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import twilightforest.TwilightForestMod;

@Mod.EventBusSubscriber(modid = TwilightForestMod.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent evt) {
		evt.getGenerator().addProvider(new BlockstateGenerator(evt.getGenerator(), evt.getExistingFileHelper()));
		evt.getGenerator().addProvider(new ItemModelGenerator(evt.getGenerator(), evt.getExistingFileHelper()));
		evt.getGenerator().addProvider(new BlockTagGenerator(evt.getGenerator()));
		evt.getGenerator().addProvider(new ItemTagGenerator(evt.getGenerator()));
		evt.getGenerator().addProvider(new LootGenerator(evt.getGenerator()));
		evt.getGenerator().addProvider(new StonecuttingGenerator(evt.getGenerator()));
	}
}

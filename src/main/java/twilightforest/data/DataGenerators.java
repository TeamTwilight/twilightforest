package twilightforest.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import twilightforest.TwilightForestMod;

@Mod.EventBusSubscriber(modid = TwilightForestMod.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent evt) {
		DataGenerator generator = evt.getGenerator();
		ExistingFileHelper helper = evt.getExistingFileHelper();

		generator.addProvider(new AdvancementProvider(generator));
		evt.getGenerator().addProvider(new BlockstateGenerator(generator, helper));
		evt.getGenerator().addProvider(new ItemModelGenerator(generator, helper));
		BlockTagsProvider blocktags = new BlockTagGenerator(generator, helper);
		generator.addProvider(blocktags);
		generator.addProvider(new FluidTagGenerator(generator, helper));
		generator.addProvider(new ItemTagGenerator(generator, blocktags, helper));
		generator.addProvider(new EntityTagGenerator(generator, helper));
		generator.addProvider(new CustomTagGenerator.EnchantmentTagGenerator(generator, helper));
		generator.addProvider(new LootGenerator(generator));
		generator.addProvider(new StonecuttingGenerator(generator));
		generator.addProvider(new CraftingGenerator(generator));
		generator.addProvider(new TwilightWorldDataCompiler(generator));
	}
}

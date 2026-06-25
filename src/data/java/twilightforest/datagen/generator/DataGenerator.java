package twilightforest.datagen.generator;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.server.packs.PackType;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import tamaized.beanification.Component;
import twilightforest.datagen.data.*;
import twilightforest.datagen.data.custom.QuestGenerator;
import twilightforest.datagen.data.custom.StructureTemplateDefinitionGenerator;
import twilightforest.datagen.data.custom.stalactites.StalactiteGenerator;
import twilightforest.datagen.data.loot.LootGenerator;
import twilightforest.datagen.data.recipes.CraftingGeneratorRunner;
import twilightforest.datagen.data.recipes.RecipePriorityGenerator;
import twilightforest.datagen.data.tags.*;

import java.util.concurrent.CompletableFuture;

@Component
public class DataGenerator {

	public void generate(GatherDataEvent.Client event) {
		net.minecraft.data.DataGenerator generator = event.getGenerator();
		PackOutput output = event.getGenerator().getPackOutput();

		//datapack registry things
		DatapackBuiltinEntriesProvider datapackProvider = new RegistryDataGenerator(output, event.getLookupProvider());
		CompletableFuture<HolderLookup.Provider> lookupProvider = datapackProvider.getRegistryProvider();
		generator.addProvider(true, datapackProvider);

		//tags
		generator.addProvider(true, new BannerPatternTagGenerator(output, lookupProvider));
		generator.addProvider(true, new BiomeTagGenerator(output, lookupProvider));
		generator.addProvider(true, new BlockEntityTypeTagGenerator(output, lookupProvider));
		BlockTagGenerator blocktags = new BlockTagGenerator(output, lookupProvider);
		generator.addProvider(true, blocktags);
		generator.addProvider(true, new DamageTypeTagGenerator(output, lookupProvider));
		generator.addProvider(true, new DimensionTypeTagGenerator(output, lookupProvider));
		generator.addProvider(true, new EntityTypeTagGenerator(output, lookupProvider));
		generator.addProvider(true, new ItemTagGenerator(output, lookupProvider));
		generator.addProvider(true, new PaintingVariantTagGenerator(output, lookupProvider));
		generator.addProvider(true, new StructureTagGenerator(output, lookupProvider));
		generator.addProvider(true, new WoodPaletteTagGenerator(output, lookupProvider));

		//the other stuff
		generator.addProvider(true, new CraftingGeneratorRunner(output, lookupProvider));
		generator.addProvider(true, new DataMapGenerator(output, lookupProvider));
		generator.addProvider(true, new LootGenerator(output, lookupProvider));
		generator.addProvider(true, new LootModifierGenerator(output, lookupProvider));
		generator.addProvider(true, new QuestGenerator(output));
		generator.addProvider(true, new RecipePriorityGenerator(output, lookupProvider));
		generator.addProvider(true, new StalactiteGenerator(output));
		generator.addProvider(true, new StructureTemplateDefinitionGenerator(output, lookupProvider));
		generator.addProvider(true, new TFAdvancementProvider(output, lookupProvider));
		generator.addProvider(true, new TFStructureUpdater("structures", output, event.getResourceManager(PackType.SERVER_DATA)));
	}
}

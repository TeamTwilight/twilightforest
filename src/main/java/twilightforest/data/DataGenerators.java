package twilightforest.data;

import net.minecraft.DetectedVersion;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.metadata.PackMetadataGenerator;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.util.InclusiveRange;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import twilightforest.TwilightForestMod;
import twilightforest.data.custom.QuestGenerator;
import twilightforest.data.custom.StructureTemplateDefinitionGenerator;
import twilightforest.data.custom.stalactites.StalactiteGenerator;
import twilightforest.data.models.BlockModelGenerator;
import twilightforest.data.models.ModelGenerator;
import twilightforest.data.recipes.CraftingGeneratorRunner;
import twilightforest.data.tags.*;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = TwilightForestMod.ID, bus = EventBusSubscriber.Bus.MOD)
public class DataGenerators {

	@SubscribeEvent
	public static void gatherData(GatherDataEvent.Client event) {
		DataGenerator generator = event.getGenerator();
		PackOutput output = event.getGenerator().getPackOutput();

		//client generators
		generator.addProvider(true, new ModelGenerator(output));
		generator.addProvider(true, new ParticleGenerator(output));
		generator.addProvider(true, new SoundGenerator(output));

		//registry-based stuff
		DatapackBuiltinEntriesProvider datapackProvider = new RegistryDataGenerator(output, event.getLookupProvider());
		CompletableFuture<HolderLookup.Provider> lookupProvider = datapackProvider.getRegistryProvider();
		generator.addProvider(true, datapackProvider);
		generator.addProvider(true, new BiomeTagGenerator(output, lookupProvider));
		generator.addProvider(true, new CustomTagGenerator.BannerPatternTagGenerator(output, lookupProvider));
		generator.addProvider(true, new CustomTagGenerator.DimensionTypeTagGenerator(output, lookupProvider));
		generator.addProvider(true, new CustomTagGenerator.WoodPaletteTagGenerator(output, lookupProvider));
		generator.addProvider(true, new CustomTagGenerator.PaintingVariantTagGenerator(output, lookupProvider));
		generator.addProvider(true, new DamageTypeTagGenerator(output, lookupProvider));
		generator.addProvider(true, new StructureTagGenerator(output, lookupProvider));
		generator.addProvider(true, new TFAdvancementProvider(output, lookupProvider));
		generator.addProvider(true, new LootGenerator(output, lookupProvider));

		//server generators
		generator.addProvider(true, new DataMapGenerator(output, lookupProvider));
		generator.addProvider(true, new StalactiteGenerator(output));
		generator.addProvider(true, new TFStructureUpdater("structures", output, event.getResourceManager(PackType.SERVER_DATA)));

		//normal tags
		BlockTagGenerator blocktags = new BlockTagGenerator(output, lookupProvider);
		generator.addProvider(true, blocktags);
		generator.addProvider(true, new CustomTagGenerator.BlockEntityTagGenerator(output, lookupProvider));
		generator.addProvider(true, new FluidTagGenerator(output, lookupProvider));
		generator.addProvider(true, new ItemTagGenerator(output, lookupProvider, blocktags.contentsGetter()));
		generator.addProvider(true, new EntityTagGenerator(output, lookupProvider));
		generator.addProvider(true, new CraftingGeneratorRunner(output, lookupProvider));
		generator.addProvider(true, new LootModifierGenerator(output, lookupProvider));

		generator.addProvider(true, new StructureTemplateDefinitionGenerator(output, lookupProvider));

		//these have to go last due to magic paintings
		//when magic paintings are registered their atlas and lang content is too
		generator.addProvider(true, new AtlasGenerator(output, lookupProvider));
		generator.addProvider(true, new LangGenerator(output));

		generator.addProvider(true, new QuestGenerator(output));

		//pack.mcmeta
		generator.addProvider(true, new PackMetadataGenerator(output).add(PackMetadataSection.TYPE, new PackMetadataSection(
			Component.literal("Resources for Twilight Forest"),
			DetectedVersion.BUILT_IN.getPackVersion(PackType.SERVER_DATA),
			Optional.of(new InclusiveRange<>(0, Integer.MAX_VALUE)))));
	}
}

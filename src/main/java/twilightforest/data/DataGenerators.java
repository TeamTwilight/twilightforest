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
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import twilightforest.TwilightForestMod;
import twilightforest.data.custom.QuestGenerator;
import twilightforest.data.custom.stalactites.StalactiteGenerator;
import twilightforest.data.tags.*;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = TwilightForestMod.ID, bus = EventBusSubscriber.Bus.MOD)
public class DataGenerators {

	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		PackOutput output = event.getGenerator().getPackOutput();
		ExistingFileHelper helper = event.getExistingFileHelper();

		//client generators
		generator.addProvider(event.includeClient(), new BlockstateGenerator(output, helper));
		generator.addProvider(event.includeClient(), new ItemModelGenerator(output, helper));
		generator.addProvider(event.includeClient(), new ParticleGenerator(output, helper));
		generator.addProvider(event.includeClient(), new SoundGenerator(output, helper));

		//registry-based stuff
		DatapackBuiltinEntriesProvider datapackProvider = new RegistryDataGenerator(output, event.getLookupProvider());
		CompletableFuture<HolderLookup.Provider> lookupProvider = datapackProvider.getRegistryProvider();
		generator.addProvider(event.includeServer(), datapackProvider);
		generator.addProvider(event.includeServer(), new BiomeTagGenerator(output, lookupProvider, helper));
		generator.addProvider(event.includeServer(), new CustomTagGenerator.BannerPatternTagGenerator(output, lookupProvider, helper));
		generator.addProvider(event.includeServer(), new CustomTagGenerator.DimensionTypeTagGenerator(output, lookupProvider, helper));
		generator.addProvider(event.includeServer(), new CustomTagGenerator.WoodPaletteTagGenerator(output, lookupProvider, helper));
		generator.addProvider(event.includeServer(), new CustomTagGenerator.PaintingVariantTagGenerator(output, lookupProvider, helper));
		generator.addProvider(event.includeServer(), new DamageTypeTagGenerator(output, lookupProvider, helper));
		generator.addProvider(event.includeServer(), new StructureTagGenerator(output, lookupProvider, helper));
		generator.addProvider(event.includeServer(), new TFAdvancementProvider(output, lookupProvider, helper));
		generator.addProvider(event.includeServer(), new LootGenerator(output, lookupProvider));

		//server generators
		generator.addProvider(event.includeServer(), new DataMapGenerator(output, lookupProvider));
		generator.addProvider(event.includeServer(), new StalactiteGenerator(output));
		generator.addProvider(event.includeServer(), new TFStructureUpdater("structures", output, helper));

		//normal tags
		BlockTagGenerator blocktags = new BlockTagGenerator(output, lookupProvider, helper);
		generator.addProvider(event.includeServer(), blocktags);
		generator.addProvider(event.includeServer(), new CustomTagGenerator.BlockEntityTagGenerator(output, lookupProvider, helper));
		generator.addProvider(event.includeServer(), new FluidTagGenerator(output, lookupProvider, helper));
		generator.addProvider(event.includeServer(), new ItemTagGenerator(output, lookupProvider, blocktags.contentsGetter(), helper));
		generator.addProvider(event.includeServer(), new EntityTagGenerator(output, lookupProvider, helper));
		generator.addProvider(event.includeServer(), new CraftingGenerator(output, lookupProvider));
		generator.addProvider(event.includeServer(), new LootModifierGenerator(output, lookupProvider));

		//these have to go last due to magic paintings
		//when magic paintings are registered their atlas and lang content is too
		generator.addProvider(event.includeClient(), new AtlasGenerator(output, lookupProvider, helper));
		generator.addProvider(event.includeClient(), new LangGenerator(output));

		generator.addProvider(event.includeServer(), new QuestGenerator(output));

		//pack.mcmeta
		generator.addProvider(true, new PackMetadataGenerator(output).add(PackMetadataSection.TYPE, new PackMetadataSection(
			Component.literal("Resources for Twilight Forest"),
			DetectedVersion.BUILT_IN.getPackVersion(PackType.SERVER_DATA),
			Optional.of(new InclusiveRange<>(0, Integer.MAX_VALUE)))));
	}
}

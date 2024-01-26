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
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import twilightforest.TwilightForestMod;
import twilightforest.data.custom.CrumbleHornGenerator;
import twilightforest.data.custom.TransformationPowderGenerator;
import twilightforest.data.custom.stalactites.StalactiteGenerator;
import twilightforest.data.tags.*;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = TwilightForestMod.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		PackOutput output = event.getGenerator().getPackOutput();
		CompletableFuture<HolderLookup.Provider> provider = event.getLookupProvider();
		ExistingFileHelper helper = event.getExistingFileHelper();

		generator.addProvider(event.includeServer(), new TFAdvancementProvider(output, provider, helper));
		generator.addProvider(event.includeClient(), new BlockstateGenerator(output, helper));
		generator.addProvider(event.includeClient(), new ItemModelGenerator(output, helper));
		generator.addProvider(event.includeClient(), new SoundGenerator(output, helper));
		generator.addProvider(event.includeServer(), new CustomTagGenerator.BannerPatternTagGenerator(output, provider, helper));
		BlockTagGenerator blocktags = new BlockTagGenerator(output, provider, helper);
		generator.addProvider(event.includeServer(), blocktags);
		generator.addProvider(event.includeServer(), new CustomTagGenerator.BlockEntityTagGenerator(output, provider, helper));
		generator.addProvider(event.includeServer(), new FluidTagGenerator(output, provider, helper));
		generator.addProvider(event.includeServer(), new ItemTagGenerator(output, provider, blocktags.contentsGetter(), helper));
		generator.addProvider(event.includeServer(), new EntityTagGenerator(output, provider, helper));
		generator.addProvider(event.includeServer(), new CustomTagGenerator.EnchantmentTagGenerator(output, provider, helper));
		generator.addProvider(event.includeServer(), new LootGenerator(output));
		generator.addProvider(event.includeServer(), new CraftingGenerator(output, provider));
		generator.addProvider(event.includeServer(), new LootModifierGenerator(output));

		DatapackBuiltinEntriesProvider datapackProvider = new RegistryDataGenerator(output, provider);
		CompletableFuture<HolderLookup.Provider> lookupProvider = datapackProvider.getRegistryProvider();
		generator.addProvider(event.includeServer(), datapackProvider);
		generator.addProvider(event.includeServer(), new CustomTagGenerator.WoodPaletteTagGenerator(output, lookupProvider, helper));
		generator.addProvider(event.includeServer(), new BiomeTagGenerator(output, lookupProvider, helper));
		generator.addProvider(event.includeServer(), new DamageTypeTagGenerator(output, lookupProvider, helper));
		generator.addProvider(event.includeServer(), new StructureTagGenerator(output, lookupProvider, helper));

		generator.addProvider(event.includeServer(), new CrumbleHornGenerator(output, helper));
		generator.addProvider(event.includeServer(), new TransformationPowderGenerator(output, helper));
		generator.addProvider(event.includeServer(), new StalactiteGenerator(output));
		generator.addProvider(true, new PackMetadataGenerator(output).add(PackMetadataSection.TYPE, new PackMetadataSection(
						Component.literal("Resources for Twilight Forest"),
						DetectedVersion.BUILT_IN.getPackVersion(PackType.SERVER_DATA),
						Optional.of(new InclusiveRange<>(0, Integer.MAX_VALUE)))));
		generator.addProvider(event.includeClient(), new AtlasGenerator(output, provider, helper));
		generator.addProvider(event.includeClient(), new LangGenerator(output));
		generator.addProvider(event.includeClient(), new ParticleGenerator(output, helper));

		generator.addProvider(event.includeServer(), new CustomTagGenerator.DimensionTypeTagGenerator(output, lookupProvider, helper));
	}
}

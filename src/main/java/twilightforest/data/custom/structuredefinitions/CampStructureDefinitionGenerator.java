package twilightforest.data.custom.structuredefinitions;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import tamaized.beanification.Autowired;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFBlocks;
import twilightforest.init.custom.TemplateMarkerHandlers;
import twilightforest.world.components.processors.StateTransfiguringProcessor;
import twilightforest.world.components.structures.camp.CampPieces;
import twilightforest.world.components.structures.util.TemplateMarkerHandlerList;
import twilightforest.world.components.structures.util.TemplatePoolInstance;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class CampStructureDefinitionGenerator extends StructureTemplateDefinitionProvider {
	@Autowired
	private static CampPieces campPieces;

	public CampStructureDefinitionGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
		super(output, lookupProvider, TwilightForestMod.ID, existingFileHelper, "Camp");
	}

	@Override
	protected void generatePools() {
		this.lookupProvider.thenAccept(this::generatePoolsWithProvider);
	}

	private void generatePoolsWithProvider(HolderLookup.Provider provider) {
		Holder.Reference<TemplateMarkerHandlerList> campMarkers = this.getMarkers(provider, TemplateMarkerHandlers.CAMP_MARKER_HANDLERS);

		this.add("camp/campfire_east", campPieces.start, this.weightedRigidTemplate(100, 1, null, campMarkers, null, null));
		this.add("camp/campfire_south", campPieces.start, this.weightedRigidTemplate(100, 1, null, campMarkers, null, null));
		this.add("camp/campfire_west", campPieces.start, this.weightedRigidTemplate(100, 1, null, campMarkers, null, null));

		this.configureTents(provider);

		this.configureRackPaths();

		this.configureBigPaths();

		this.configureSmallPaths();

		this.configureDeco(campMarkers);
	}

	private void configureTents(HolderLookup.Provider provider) {
		Holder.Reference<TemplateMarkerHandlerList> campMarkers = this.getMarkers(provider, TemplateMarkerHandlers.CAMP_MARKER_HANDLERS);

		int defaultWeight = 10;
		TemplatePoolInstance.ChooseRandomProcessors randomizedProcessors = new TemplatePoolInstance.ChooseRandomProcessors(List.of(
			SimpleWeightedRandomList.<StructureProcessor>builder()
				.add(new StateTransfiguringProcessor(List.of(new ProcessorRule(new BlockMatchTest(Blocks.LIGHT_GRAY_WOOL), AlwaysTrueTest.INSTANCE, Blocks.WHITE_WOOL.defaultBlockState()))), defaultWeight)
				.add(new StateTransfiguringProcessor(List.of(new ProcessorRule(new BlockMatchTest(Blocks.LIGHT_GRAY_WOOL), AlwaysTrueTest.INSTANCE, Blocks.LIGHT_GRAY_WOOL.defaultBlockState()))), defaultWeight)
				.add(new StateTransfiguringProcessor(List.of(new ProcessorRule(new BlockMatchTest(Blocks.LIGHT_GRAY_WOOL), AlwaysTrueTest.INSTANCE, Blocks.GRAY_WOOL.defaultBlockState()))), defaultWeight)
				.add(new StateTransfiguringProcessor(List.of(new ProcessorRule(new BlockMatchTest(Blocks.LIGHT_GRAY_WOOL), AlwaysTrueTest.INSTANCE, Blocks.BROWN_WOOL.defaultBlockState()))), defaultWeight)
				.add(new StateTransfiguringProcessor(List.of(new ProcessorRule(new BlockMatchTest(Blocks.LIGHT_GRAY_WOOL), AlwaysTrueTest.INSTANCE, Blocks.GREEN_WOOL.defaultBlockState()))), defaultWeight)
				.add(new StateTransfiguringProcessor(List.of(new ProcessorRule(new BlockMatchTest(Blocks.LIGHT_GRAY_WOOL), AlwaysTrueTest.INSTANCE, Blocks.BLUE_WOOL.defaultBlockState()))), defaultWeight)
				.add(new StateTransfiguringProcessor(List.of(new ProcessorRule(new BlockMatchTest(Blocks.LIGHT_GRAY_WOOL), AlwaysTrueTest.INSTANCE, Blocks.RED_WOOL.defaultBlockState()))), defaultWeight)
				.add(new StateTransfiguringProcessor(List.of(new ProcessorRule(new BlockMatchTest(Blocks.LIGHT_GRAY_WOOL), AlwaysTrueTest.INSTANCE, Blocks.LIME_WOOL.defaultBlockState()))), 1)
				.build(),
			SimpleWeightedRandomList.<StructureProcessor>builder()
				.add(new StateTransfiguringProcessor(List.of(new ProcessorRule(new BlockMatchTest(Blocks.RED_BED), AlwaysTrueTest.INSTANCE, Blocks.RED_BED.defaultBlockState()))), defaultWeight)
				.add(new StateTransfiguringProcessor(List.of(new ProcessorRule(new BlockMatchTest(Blocks.RED_BED), AlwaysTrueTest.INSTANCE, Blocks.LIGHT_BLUE_BED.defaultBlockState()))), defaultWeight)
				.add(new StateTransfiguringProcessor(List.of(new ProcessorRule(new BlockMatchTest(Blocks.RED_BED), AlwaysTrueTest.INSTANCE, Blocks.LIME_BED.defaultBlockState()))), defaultWeight)
				.add(new StateTransfiguringProcessor(List.of(new ProcessorRule(new BlockMatchTest(Blocks.RED_BED), AlwaysTrueTest.INSTANCE, Blocks.ORANGE_BED.defaultBlockState()))), defaultWeight)
				.add(new StateTransfiguringProcessor(List.of(new ProcessorRule(new BlockMatchTest(Blocks.RED_BED), AlwaysTrueTest.INSTANCE, Blocks.LIGHT_GRAY_BED.defaultBlockState()))), defaultWeight)
				.build()
		));

		this.add("camp/tent/solo_tent", campPieces.tent, this.weightedRigidTemplate(100, 1, 0, campMarkers, randomizedProcessors, null));
		this.add("camp/tent/duo_tent", campPieces.tent, this.weightedRigidTemplate(75, 1, 0, campMarkers, randomizedProcessors, null));
		this.add("camp/tent/open_tent", campPieces.tent, this.weightedRigidTemplate(75, 1, 0, campMarkers, randomizedProcessors, null));
		this.add("camp/tent/luxury_tent", campPieces.tent, this.weightedRigidTemplate(50, 1, 0, campMarkers, randomizedProcessors, null));
	}

	private void configureRackPaths() {
		TemplatePoolInstance pathTemplateData = this.weightedPathTemplate(100, null, Map.of(campPieces.deco.toString(), campPieces.rack.toString()));

		this.add("camp/path/path_2x4", campPieces.rackPath, pathTemplateData);
		this.add("camp/path/path_3x4", campPieces.rackPath, pathTemplateData);
	}

	private void configureBigPaths() {
		TemplatePoolInstance pathTemplateData = this.weightedPathTemplate(25, null, null);

		this.addEmpty(campPieces.mainPath, 300);

		this.add("camp/path/intersection_left", campPieces.mainPath, pathTemplateData);
		this.add("camp/path/intersection_right", campPieces.mainPath, pathTemplateData);
		this.add("camp/path/intersection_short", campPieces.mainPath, pathTemplateData);
		this.add("camp/path/j_path", campPieces.mainPath, pathTemplateData);
		this.add("camp/path/l_path", campPieces.mainPath, pathTemplateData);
	}

	private void configureSmallPaths() {
		TemplatePoolInstance pathTemplateData = this.weightedPathTemplate(50, null, null);

		this.addEmpty(campPieces.path, 300);

		this.addToAllPools("camp/path/path_2x4", pathTemplateData, campPieces.mainPath, campPieces.path);
		this.addToAllPools("camp/path/path_2x6", pathTemplateData, campPieces.mainPath, campPieces.path);
		this.addToAllPools("camp/path/path_2x7", pathTemplateData, campPieces.mainPath, campPieces.path);
		this.addToAllPools("camp/path/path_3x4", pathTemplateData, campPieces.mainPath, campPieces.path);
	}

	private void configureDeco(Holder.Reference<TemplateMarkerHandlerList> campMarkers) {
		TemplatePoolInstance twoLayerTemplateData = this.weightedRigidTemplate(100, 2, 0, campMarkers, null, null);
		TemplatePoolInstance berryTemplateData = this.weightedRigidTemplate(75, 2, 0, campMarkers, null, null);

		this.addToAllPools("camp/deco/double_drying_rack", twoLayerTemplateData, campPieces.deco, campPieces.rack);
		this.addToAllPools("camp/deco/long_drying_rack", twoLayerTemplateData, campPieces.deco, campPieces.rack);

		this.add("camp/deco/garden_1x3", campPieces.deco, twoLayerTemplateData);
		this.add("camp/deco/garden_2x4", campPieces.deco, twoLayerTemplateData);
		this.add("camp/deco/garden_straight", campPieces.deco, twoLayerTemplateData);
		this.add("camp/deco/garden_u", campPieces.deco, twoLayerTemplateData);

		this.add("camp/deco/berries_staked", campPieces.deco, berryTemplateData);
		this.add("camp/deco/berries_trellis", campPieces.deco, berryTemplateData);
		this.add("camp/deco/blackberry_wall", campPieces.deco, berryTemplateData);
		this.add("camp/deco/blueberry_wall", campPieces.deco, berryTemplateData);
		this.add("camp/deco/raspberry_wall", campPieces.deco, berryTemplateData);
		this.add("camp/deco/berries_support", campPieces.deco, berryTemplateData);

		this.add("camp/deco/compost_large", campPieces.deco, twoLayerTemplateData);
		this.add("camp/deco/compost_small", campPieces.deco, twoLayerTemplateData);

		this.add("camp/deco/repair_station", campPieces.deco, twoLayerTemplateData);

		this.add("camp/deco/pen", campPieces.deco, twoLayerTemplateData);

		TemplatePoolInstance.ChooseRandomProcessors randomizedWoods = new TemplatePoolInstance.ChooseRandomProcessors(List.of(
			SimpleWeightedRandomList.<StructureProcessor>builder()
				.add(new StateTransfiguringProcessor(List.of(new ProcessorRule(new BlockMatchTest(TFBlocks.TWILIGHT_OAK_LOG.value()), AlwaysTrueTest.INSTANCE, TFBlocks.TWILIGHT_OAK_LOG.value().defaultBlockState()))), 100)
				.add(new StateTransfiguringProcessor(List.of(new ProcessorRule(new BlockMatchTest(TFBlocks.TWILIGHT_OAK_LOG.value()), AlwaysTrueTest.INSTANCE, TFBlocks.CANOPY_LOG.value().defaultBlockState()))), 90)
				.add(new StateTransfiguringProcessor(List.of(new ProcessorRule(new BlockMatchTest(TFBlocks.TWILIGHT_OAK_LOG.value()), AlwaysTrueTest.INSTANCE, Blocks.OAK_LOG.defaultBlockState()))), 80)
				.add(new StateTransfiguringProcessor(List.of(new ProcessorRule(new BlockMatchTest(TFBlocks.TWILIGHT_OAK_LOG.value()), AlwaysTrueTest.INSTANCE, Blocks.BIRCH_LOG.defaultBlockState()))), 70)
				.build()
		));
		TemplatePoolInstance lumberTemplateData = this.weightedRigidTemplate(100, 1, 0, campMarkers, randomizedWoods, null);

		this.add("camp/deco/lumber1", campPieces.deco, lumberTemplateData);
		this.add("camp/deco/lumber2", campPieces.deco, lumberTemplateData);
		this.add("camp/deco/lumber3", campPieces.deco, lumberTemplateData);

		this.addEmpty(campPieces.deco, 1000);
	}
}

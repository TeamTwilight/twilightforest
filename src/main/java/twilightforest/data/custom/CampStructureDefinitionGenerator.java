package twilightforest.data.custom;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import twilightforest.TwilightForestMod;
import twilightforest.beans.Autowired;
import twilightforest.world.components.structures.camp.CampPieces;
import twilightforest.world.components.structures.util.TemplatePoolInstance;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class CampStructureDefinitionGenerator extends StructureTemplateDefinitionProvider {
	@Autowired
	private static CampPieces campPieces;

	public CampStructureDefinitionGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
		super(output, lookupProvider, TwilightForestMod.ID, existingFileHelper, "Camp");
	}

	@Override
	protected void generatePools() {
		this.add("camp/campfire", campPieces.start, this.weightedRigidTemplate(100, 1, Optional.empty()));

		this.configureTents();

		this.configureBigPaths();

		this.configureSmallPaths();

		this.configureDeco();
	}

	private void configureTents() {
		this.add("camp/tent/solo_tent", campPieces.tent, this.weightedRigidTemplate(100, 1, Optional.of(0)));
		this.add("camp/tent/duo_tent", campPieces.tent, this.weightedRigidTemplate(75, 1, Optional.of(0)));
		this.add("camp/tent/luxury_tent", campPieces.tent, this.weightedRigidTemplate(50, 1, Optional.of(0)));
	}

	private void configureBigPaths() {
		TemplatePoolInstance rigidTemplateData = this.weightedPathTemplate(25);

		this.add("camp/path/intersection_left", campPieces.mainPath, rigidTemplateData);
		this.add("camp/path/intersection_right", campPieces.mainPath, rigidTemplateData);
		this.add("camp/path/intersection_short", campPieces.mainPath, rigidTemplateData);
		this.add("camp/path/j_path", campPieces.mainPath, rigidTemplateData);
		this.add("camp/path/l_path", campPieces.mainPath, rigidTemplateData);
		this.addEmpty(campPieces.mainPath, 200);
	}

	private void configureSmallPaths() {
		TemplatePoolInstance pathTemplateData = this.weightedPathTemplate(50);

		this.addToAllPools("camp/path/path_2x4", pathTemplateData, campPieces.mainPath, campPieces.path);
		this.addToAllPools("camp/path/path_2x6", pathTemplateData, campPieces.mainPath, campPieces.path);
		this.addToAllPools("camp/path/path_2x7", pathTemplateData, campPieces.mainPath, campPieces.path);
		this.addToAllPools("camp/path/path_3x4", pathTemplateData, campPieces.mainPath, campPieces.path);
		this.addEmpty(campPieces.path, 100);
	}

	private void configureDeco() {
		TemplatePoolInstance rigidTemplateDataAnyDiff = this.weightedRigidTemplate(100, 1, Optional.of(1));
		TemplatePoolInstance rigidTemplateDataOneDiff = this.weightedRigidTemplate(100, 1, Optional.of(1));
		TemplatePoolInstance rigidTemplateDataNoDiff = this.weightedRigidTemplate(100, 1, Optional.of(0));

		this.add("camp/deco/double_drying_rack", campPieces.deco, rigidTemplateDataAnyDiff);
		this.add("camp/deco/long_drying_rack", campPieces.deco, rigidTemplateDataAnyDiff);

		this.add("camp/deco/garden_1x2", campPieces.deco, rigidTemplateDataOneDiff);
		this.add("camp/deco/garden_2x4", campPieces.deco, rigidTemplateDataOneDiff);

		this.add("camp/deco/lumber", campPieces.deco, rigidTemplateDataOneDiff);

		this.add("camp/deco/water_basin", campPieces.deco, rigidTemplateDataNoDiff);
		this.add("camp/deco/wooden_basin", campPieces.deco, rigidTemplateDataNoDiff);
		this.addEmpty(campPieces.deco, 1000);
	}
}

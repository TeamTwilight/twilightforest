package twilightforest.data.custom;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import twilightforest.TwilightForestMod;
import twilightforest.beans.Autowired;
import twilightforest.world.components.structures.camp.CampPieces;

import java.util.concurrent.CompletableFuture;

public class CampStructureDefinitionGenerator extends StructureTemplateDefinitionProvider {
	@Autowired
	private static CampPieces campPieces;

	public CampStructureDefinitionGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
		super(output, lookupProvider, TwilightForestMod.ID, existingFileHelper, "Camp");
	}

	@Override
	protected void generatePools() {
		this.add("camp/campfire", campPieces.start, 100);

		this.add("camp/tent/solo_tent", campPieces.tent, 100);
		this.add("camp/tent/duo_tent", campPieces.tent, 75);
		this.add("camp/tent/luxury_tent", campPieces.tent, 50);

		this.addAllTemplatesToPool(campPieces.mainPath, 100,
			"camp/path/intersection_left",
			"camp/path/intersection_right",
			"camp/path/intersection_short",
			"camp/path/j_path",
			"camp/path/l_path"
		);

		this.addToAllPools("camp/path/path_2x4", 100, campPieces.mainPath, campPieces.path);
		this.addToAllPools("camp/path/path_2x6", 100, campPieces.mainPath, campPieces.path);
		this.addToAllPools("camp/path/path_2x7", 100, campPieces.mainPath, campPieces.path);
		this.addToAllPools("camp/path/path_3x4", 100, campPieces.mainPath, campPieces.path);

		this.addAllTemplatesToPool(campPieces.deco, 100,
			"camp/deco/double_drying_rack",
			"camp/deco/garden_1x2",
			"camp/deco/garden_2x4",
			"camp/deco/long_drying_rack",
			"camp/deco/lumber",
			"camp/deco/water_basin",
			"camp/deco/wooden_basin"
		);
	}
}

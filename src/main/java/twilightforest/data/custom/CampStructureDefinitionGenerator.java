package twilightforest.data.custom;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.util.random.Weight;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
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
		this.add("camp/campfire", campPieces.start, new TemplatePoolInstance(
			Weight.of(100),
			Optional.empty(),
			StructureTemplatePool.Projection.RIGID,
			TerrainAdjustment.BEARD_BOX,
			Optional.of(new TemplatePoolInstance.HeightAdjustment(Heightmap.Types.WORLD_SURFACE_WG, 1))
		));

		this.configureTents();

		this.configureBigPaths();

		this.configureSmallPaths();

		this.configureDeco();
	}

	private void configureTents() {
		this.add("camp/tent/solo_tent", campPieces.tent, new TemplatePoolInstance(
			Weight.of(100),
			Optional.empty(),
			StructureTemplatePool.Projection.RIGID,
			TerrainAdjustment.BEARD_BOX,
			Optional.of(new TemplatePoolInstance.HeightAdjustment(Heightmap.Types.WORLD_SURFACE_WG, 1))
		));
		this.add("camp/tent/duo_tent", campPieces.tent, new TemplatePoolInstance(
			Weight.of(75),
			Optional.empty(),
			StructureTemplatePool.Projection.RIGID,
			TerrainAdjustment.BEARD_BOX,
			Optional.of(new TemplatePoolInstance.HeightAdjustment(Heightmap.Types.WORLD_SURFACE_WG, 1))
		));
		this.add("camp/tent/luxury_tent", campPieces.tent, new TemplatePoolInstance(
			Weight.of(50),
			Optional.empty(),
			StructureTemplatePool.Projection.RIGID,
			TerrainAdjustment.BEARD_BOX,
			Optional.of(new TemplatePoolInstance.HeightAdjustment(Heightmap.Types.WORLD_SURFACE_WG, 1))
		));
	}

	private void configureBigPaths() {
		this.add("camp/path/intersection_left", campPieces.mainPath, 100);
		this.add("camp/path/intersection_right", campPieces.mainPath, 100);
		this.add("camp/path/intersection_short", campPieces.mainPath, 100);
		this.add("camp/path/j_path", campPieces.mainPath, 100);
		this.add("camp/path/l_path", campPieces.mainPath, 100);
	}

	private void configureSmallPaths() {
		TemplatePoolInstance data = new TemplatePoolInstance(
			Weight.of(100),
			Optional.empty(),
			StructureTemplatePool.Projection.TERRAIN_MATCHING,
			TerrainAdjustment.NONE,
			Optional.empty()
		);

		this.addToAllPools("camp/path/path_2x4", data, campPieces.mainPath, campPieces.path);
		this.addToAllPools("camp/path/path_2x6", data, campPieces.mainPath, campPieces.path);
		this.addToAllPools("camp/path/path_2x7", data, campPieces.mainPath, campPieces.path);
		this.addToAllPools("camp/path/path_3x4", data, campPieces.mainPath, campPieces.path);
	}

	private void configureDeco() {
		TemplatePoolInstance offset0 = new TemplatePoolInstance(
			Weight.of(100),
			Optional.empty(),
			StructureTemplatePool.Projection.RIGID,
			TerrainAdjustment.BEARD_BOX,
			Optional.of(new TemplatePoolInstance.HeightAdjustment(Heightmap.Types.WORLD_SURFACE_WG, 0))
		);
		TemplatePoolInstance offset1 = new TemplatePoolInstance(
			Weight.of(100),
			Optional.empty(),
			StructureTemplatePool.Projection.RIGID,
			TerrainAdjustment.BEARD_BOX,
			Optional.of(new TemplatePoolInstance.HeightAdjustment(Heightmap.Types.WORLD_SURFACE_WG, 1))
		);

		this.add("camp/deco/double_drying_rack", campPieces.deco, offset0);
		this.add("camp/deco/garden_1x2", campPieces.deco, offset1);
		this.add("camp/deco/garden_2x4", campPieces.deco, offset1);
		this.add("camp/deco/long_drying_rack", campPieces.deco, offset0);
		this.add("camp/deco/lumber", campPieces.deco, offset0);
		this.add("camp/deco/water_basin", campPieces.deco, offset1);
		this.add("camp/deco/wooden_basin", campPieces.deco, offset1);
	}
}

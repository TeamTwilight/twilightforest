package twilightforest.world.registration;

import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.event.RegistryEvent;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.structures.courtyard.NagaCourtyardPieces;
import twilightforest.world.components.structures.darktower.DarkTowerPieces;
import twilightforest.world.components.structures.finalcastle.FinalCastlePieces;
import twilightforest.world.components.structures.icetower.IceTowerPieces;
import twilightforest.world.components.structures.lichtower.LichTowerPieces;
import twilightforest.world.components.structures.lichtowerrevamp.LichTowerRevampPieces;
import twilightforest.world.components.structures.minotaurmaze.MinotaurMazePieces;
import twilightforest.world.components.structures.mushroomtower.MushroomTowerPieces;
import twilightforest.world.components.structures.start.LegacyStructureFeature;
import twilightforest.world.components.structures.stronghold.StrongholdPieces;
import twilightforest.world.components.structures.trollcave.TrollCavePieces;

@SuppressWarnings("deprecation")
public class TFStructures {

	public static final StructureFeature<NoneFeatureConfiguration> HEDGE_MAZE = new LegacyStructureFeature(TFFeature.HEDGE_MAZE);
	public static final StructureFeature<NoneFeatureConfiguration> QUEST_GROVE = new LegacyStructureFeature(TFFeature.QUEST_GROVE);
	public static final StructureFeature<NoneFeatureConfiguration> MUSHROOM_TOWER = new LegacyStructureFeature(TFFeature.MUSHROOM_TOWER);
	public static final StructureFeature<NoneFeatureConfiguration> HOLLOW_HILL_SMALL = new LegacyStructureFeature(TFFeature.SMALL_HILL);
	public static final StructureFeature<NoneFeatureConfiguration> HOLLOW_HILL_MEDIUM = new LegacyStructureFeature(TFFeature.MEDIUM_HILL);
	public static final StructureFeature<NoneFeatureConfiguration> HOLLOW_HILL_LARGE = new LegacyStructureFeature(TFFeature.LARGE_HILL);
	public static final StructureFeature<NoneFeatureConfiguration> NAGA_COURTYARD = new LegacyStructureFeature(TFFeature.NAGA_COURTYARD);
	public static final StructureFeature<NoneFeatureConfiguration> LICH_TOWER = new LegacyStructureFeature(TFFeature.LICH_TOWER);
	public static final StructureFeature<NoneFeatureConfiguration> LABYRINTH = new LegacyStructureFeature(TFFeature.LABYRINTH);
	public static final StructureFeature<NoneFeatureConfiguration> HYDRA_LAIR = new LegacyStructureFeature(TFFeature.HYDRA_LAIR);
	public static final StructureFeature<NoneFeatureConfiguration> KNIGHT_STRONGHOLD = new LegacyStructureFeature(TFFeature.KNIGHT_STRONGHOLD);
	public static final StructureFeature<NoneFeatureConfiguration> DARK_TOWER = new LegacyStructureFeature(TFFeature.DARK_TOWER);
	public static final StructureFeature<NoneFeatureConfiguration> YETI_CAVE = new LegacyStructureFeature(TFFeature.YETI_CAVE);
	public static final StructureFeature<NoneFeatureConfiguration> AURORA_PALACE = new LegacyStructureFeature(TFFeature.ICE_TOWER);
	public static final StructureFeature<NoneFeatureConfiguration> TROLL_CAVE = new LegacyStructureFeature(TFFeature.TROLL_CAVE);
	public static final StructureFeature<NoneFeatureConfiguration> FINAL_CASTLE = new LegacyStructureFeature(TFFeature.FINAL_CASTLE);

	public static void register(RegistryEvent.Register<StructureFeature<?>> event) {
		TFFeature.init();
		new MushroomTowerPieces();
		new NagaCourtyardPieces();
		new LichTowerPieces();
		new LichTowerRevampPieces();
		new MinotaurMazePieces();
		new StrongholdPieces();
		new DarkTowerPieces();
		new IceTowerPieces();
		new TrollCavePieces();
		new FinalCastlePieces();

		register(event, HEDGE_MAZE, "hedge_maze");
		register(event, QUEST_GROVE, "quest_grove");
		register(event, MUSHROOM_TOWER, "mushroom_tower");
		register(event, HOLLOW_HILL_SMALL, "small_hollow_hill");
		register(event, HOLLOW_HILL_MEDIUM, "medium_hollow_hill");
		register(event, HOLLOW_HILL_LARGE, "large_hollow_hill");
		register(event, NAGA_COURTYARD, "naga_courtyard");
		register(event, LICH_TOWER, "lich_tower");
		register(event, LABYRINTH, "labyrinth");
		register(event, HYDRA_LAIR, "hydra_lair");
		register(event, KNIGHT_STRONGHOLD, "knight_stronghold");
		register(event, DARK_TOWER, "dark_tower");
		register(event, YETI_CAVE, "yeti_cave");
		register(event, AURORA_PALACE, "aurora_palace");
		register(event, TROLL_CAVE, "troll_cave");
		register(event, FINAL_CASTLE, "final_castle");

	}

	private static void register(RegistryEvent.Register<StructureFeature<?>> event, StructureFeature<?> structure, String name) {
		event.getRegistry().register(structure.setRegistryName(TwilightForestMod.prefix(name)));
	}
}

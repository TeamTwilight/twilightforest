package twilightforest.data.custom;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.structures.lichtowerrevamp.LichTowerPieces;

import java.util.concurrent.CompletableFuture;

public class LichTowerStructureDefinitionGenerator extends StructureTemplateDefinitionProvider {
	public LichTowerStructureDefinitionGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
		super(output, lookupProvider, TwilightForestMod.ID, existingFileHelper, "Lich Tower");
	}

	@Override
	protected void generatePools() {
		for(String roomId36 : new String[]{"lich_tower/bridge_spawner", "lich_tower/bridge_spawner_bend", "lich_tower/bridge_spawner_ropes", "lich_tower/bridge_spawner_wide", "lich_tower/bridge_spawner_zag", "lich_tower/bridge_spawner_zig"}) {
			this.add(roomId36, LichTowerPieces.MOB_BRIDGE, 100);
		}

		this.addToAllPools("lich_tower/wall_bars", 100,
			LichTowerPieces.DOOR_STOPPER
		);
		this.addToAllPools("lich_tower/wall_cobble", 100,
			LichTowerPieces.DOOR_STOPPER,
			LichTowerPieces.DOOR_STOPPER_FALLBACK
		);

		for(String roomId35 : new String[]{"lich_tower/3x3/tree", "lich_tower/3x3/water_fountain", "lich_tower/3x3/water_well", "lich_tower/3x3/wither_rose"}) {
			this.add(roomId35, LichTowerPieces.CENTER_DECOR, 100);
		}
		for(String roomId34 : new String[]{"lich_tower/3x3/lava_well", "lich_tower/3x3/sapling", "lich_tower/3x3/water_fountain", "lich_tower/3x3/water_well"}) {
			this.add(roomId34, LichTowerPieces.ROOM_DECOR, 100);
		}

		for(String roomId33 : new String[]{"lich_tower/3x3/short_lookout", "lich_tower/3x3/lookout", "lich_tower/3x3/double", "lich_tower/3x3/taller_double"}) {
			this.add(roomId33, LichTowerPieces.ROOM_3, 100);
		}
		for(String roomId32 : new String[]{"lich_tower/3x3/pyramid_roof", "lich_tower/3x3/slabs_roof", "lich_tower/3x3/fence_roof", "lich_tower/3x3/curved_roof", "lich_tower/3x3/pointed_roof"}) {
			this.add(roomId32, LichTowerPieces.ROOM_3_ROOF, 100);
		}
		for(String roomId31 : new String[]{"lich_tower/3x3/pyramid_roof", "lich_tower/3x3/slabs_roof", "lich_tower/3x3/fence_roof", "lich_tower/3x3/curved_roof", "lich_tower/3x3/pointed_roof"}) {
			this.add(roomId31, LichTowerPieces.ROOM_3_SIDE_ROOF, 100);
		}

		for(String roomId30 : new String[]{"lich_tower/5x5/elbow_junction", "lich_tower/5x5/full_junction", "lich_tower/5x5/straight_junction", "lich_tower/5x5/t_junction", "lich_tower/5x5/altar", "lich_tower/5x5/desk", "lich_tower/5x5/zombie_trap", "lich_tower/5x5/full_junction_2", "lich_tower/5x5/full_junction_3", "lich_tower/5x5/ladder", "lich_tower/5x5/library", "lich_tower/5x5/lone_chest", "lich_tower/5x5/potion", "lich_tower/5x5/spawner_1", "lich_tower/5x5/spawner_2", "lich_tower/5x5/spawner_3", "lich_tower/5x5/spawner_4", "lich_tower/5x5/spawner_5", "lich_tower/5x5/trinity", "lich_tower/5x5/webbed_spawner"}) {
			this.add(roomId30, LichTowerPieces.ROOM_5, 100);
		}
		for(String roomId29 : new String[]{"lich_tower/5x5/pyramid_roof", "lich_tower/5x5/slabs_roof", "lich_tower/5x5/fence_roof", "lich_tower/5x5/curved_roof", "lich_tower/5x5/pointed_roof"}) {
			this.add(roomId29, LichTowerPieces.ROOM_5_ROOF, 100);
		}
		for(String roomId28 : new String[]{"lich_tower/5x5/pyramid_roof", "lich_tower/5x5/slabs_roof", "lich_tower/5x5/fence_roof", "lich_tower/5x5/curved_roof", "lich_tower/5x5/pointed_roof"}) {
			this.add(roomId28, LichTowerPieces.ROOM_5_SIDE_ROOF, 100);
		}

		for(String roomId27 : new String[]{"lich_tower/7x7/elbow_junction", "lich_tower/7x7/full_junction", "lich_tower/7x7/straight_junction", "lich_tower/7x7/t_junction", "lich_tower/7x7/altars", "lich_tower/7x7/book_staircases", "lich_tower/7x7/cactus", "lich_tower/7x7/desk", "lich_tower/7x7/full_junction_1", "lich_tower/7x7/full_junction_2", "lich_tower/7x7/grave", "lich_tower/7x7/garden_lab", "lich_tower/7x7/library_hall", "lich_tower/7x7/nursery", "lich_tower/7x7/potion", "lich_tower/7x7/ritual", "lich_tower/7x7/tiered_library", "lich_tower/7x7/tiered_study", "lich_tower/7x7/pedestal_junction", "lich_tower/7x7/shelved_jars", "lich_tower/7x7/walled_library", "lich_tower/7x7/pedestal_library", "lich_tower/7x7/barbed_webs", "lich_tower/7x7/corner_caging"}) {
			this.add(roomId27, LichTowerPieces.ROOM_7, 100);
		}
		for(String roomId26 : new String[]{"lich_tower/7x7/pyramid_roof", "lich_tower/7x7/slabs_roof", "lich_tower/7x7/fence_roof", "lich_tower/7x7/curved_roof", "lich_tower/7x7/pointed_roof"}) {
			this.add(roomId26, LichTowerPieces.ROOM_7_ROOF, 100);
		}
		for(String roomId25 : new String[]{"lich_tower/7x7/pyramid_roof", "lich_tower/7x7/slabs_roof", "lich_tower/7x7/fence_roof", "lich_tower/7x7/curved_roof", "lich_tower/7x7/pointed_roof"}) {
			this.add(roomId25, LichTowerPieces.ROOM_7_SIDE_ROOF, 100);
		}

		for(String roomId24 : new String[]{"lich_tower/9x9/elbow_junction", "lich_tower/9x9/full_junction", "lich_tower/9x9/straight_junction", "lich_tower/9x9/t_junction", "lich_tower/9x9/archives", "lich_tower/9x9/enchanting_prison", "lich_tower/9x9/tiered_library", "lich_tower/9x9/mossy_junction", "lich_tower/9x9/altar", "lich_tower/9x9/lectern_hall", "lich_tower/9x9/tiered_study", "lich_tower/9x9/center_decor", "lich_tower/9x9/library_junction", "lich_tower/9x9/winding_ways", "lich_tower/9x9/tiered_spawner", "lich_tower/9x9/cauldron_keep", "lich_tower/9x9/large_study", "lich_tower/9x9/lockup", "lich_tower/9x9/holding"}) {
			this.add(roomId24, LichTowerPieces.ROOM_9, 100);
		}
		for(String roomId23 : new String[]{"lich_tower/9x9/pyramid_roof", "lich_tower/9x9/slabs_roof", "lich_tower/9x9/fence_roof", "lich_tower/9x9/curved_roof", "lich_tower/9x9/pointed_roof"}) {
			this.add(roomId23, LichTowerPieces.ROOM_9_ROOF, 100);
		}
		for(String roomId22 : new String[]{"lich_tower/9x9/pyramid_roof", "lich_tower/9x9/slabs_roof", "lich_tower/9x9/fence_roof", "lich_tower/9x9/curved_roof", "lich_tower/9x9/pointed_roof"}) {
			this.add(roomId22, LichTowerPieces.ROOM_9_SIDE_ROOF, 100);
		}

		for(String roomId21 : new String[]{"lich_tower/gallery/castaway_paradise", "lich_tower/gallery/darkness", "lich_tower/gallery/lucid_lands", "lich_tower/gallery/music_in_the_mire", "lich_tower/gallery/the_hostile_paradise"}) {
			this.add(roomId21, LichTowerPieces.GALLERY, 100);
		}
		for(String roomId20 : new String[]{"lich_tower/gallery/fence_roof_even", "lich_tower/gallery/slabs_roof_even", "lich_tower/gallery/stairs_roof_even"}) {
			this.add(roomId20, LichTowerPieces.GALLERY_ROOF_EVEN, 100);
		}
		for(String roomId19 : new String[]{"lich_tower/gallery/fence_roof_odd", "lich_tower/gallery/slabs_roof_odd", "lich_tower/gallery/stairs_roof_odd"}) {
			this.add(roomId19, LichTowerPieces.GALLERY_ROOF_ODD, 100);
		}

		for(String roomId18 : new String[]{"lich_tower/5x5/beard_checkered", "lich_tower/5x5/beard_chiseled", "lich_tower/5x5/beard_chunks", "lich_tower/5x5/beard_staggered"}) {
			this.add(roomId18, LichTowerPieces.ROOM_5_BEARD, 100);
		}
		this.add("lich_tower/5x5/beard_trim", LichTowerPieces.ROOM_5_TRIM, 100);

		for(String roomId17 : new String[]{"lich_tower/7x7/beard_checkered", "lich_tower/7x7/beard_chiseled", "lich_tower/7x7/beard_chunks", "lich_tower/7x7/beard_staggered"}) {
			this.add(roomId17, LichTowerPieces.ROOM_7_BEARD, 100);
		}
		this.add("lich_tower/7x7/beard_trim", LichTowerPieces.ROOM_7_TRIM, 100);

		for(String roomId16 : new String[]{"lich_tower/9x9/beard_checkered", "lich_tower/9x9/beard_chiseled", "lich_tower/9x9/beard_chunks", "lich_tower/9x9/beard_staggered"}) {
			this.add(roomId16, LichTowerPieces.ROOM_9_BEARD, 100);
		}
		this.add("lich_tower/9x9/beard_trim", LichTowerPieces.ROOM_9_TRIM, 100);

		this.add("lich_tower/3x3/flat_side_roof", LichTowerPieces.ROOM_3_SIDE_ROOF_FALLBACK, 100);
		this.add("lich_tower/5x5/flat_side_roof", LichTowerPieces.ROOM_5_SIDE_ROOF_FALLBACK, 100);
		this.add("lich_tower/7x7/flat_side_roof", LichTowerPieces.ROOM_7_SIDE_ROOF_FALLBACK, 100);
		this.add("lich_tower/9x9/flat_side_roof", LichTowerPieces.ROOM_9_SIDE_ROOF_FALLBACK, 100);

		this.add("lich_tower/3x3/flat_roof", LichTowerPieces.ROOM_3_ROOF_FALLBACK, 100);
		this.add("lich_tower/5x5/flat_roof", LichTowerPieces.ROOM_5_ROOF_FALLBACK, 100);
		this.add("lich_tower/7x7/flat_roof", LichTowerPieces.ROOM_7_ROOF_FALLBACK, 100);
		this.add("lich_tower/9x9/flat_roof", LichTowerPieces.ROOM_9_ROOF_FALLBACK, 100);

		this.add("lich_tower/5x5/beard_flat", LichTowerPieces.ROOM_5_BEARD_FALLBACK, 100);
		this.add("lich_tower/7x7/beard_flat", LichTowerPieces.ROOM_7_BEARD_FALLBACK, 100);
		this.add("lich_tower/9x9/beard_flat", LichTowerPieces.ROOM_9_BEARD_FALLBACK, 100);

		for(String roomId15 : new String[]{"lich_tower/5x5/straight_junction", "lich_tower/5x5/elbow_junction", "lich_tower/5x5/t_junction", "lich_tower/5x5/desk", "lich_tower/5x5/lectern", "lich_tower/5x5/lone_chest", "lich_tower/5x5/altar", "lich_tower/5x5/potion", "lich_tower/5x5/trinity"}) {
			this.add(roomId15, LichTowerPieces.ROOM_5_LADDER_0, 100);
		}
		for(String roomId14 : new String[]{"lich_tower/5x5/straight_junction", "lich_tower/5x5/elbow_junction", "lich_tower/5x5/t_junction", "lich_tower/5x5/desk", "lich_tower/5x5/lectern", "lich_tower/5x5/lone_chest", "lich_tower/5x5/altar", "lich_tower/5x5/potion", "lich_tower/5x5/trinity"}) {
			this.add(roomId14, LichTowerPieces.ROOM_5_LADDER_2, 100);
		}

		for(String roomId13 : new String[]{"lich_tower/7x7/full_junction_1", "lich_tower/7x7/desk", "lich_tower/7x7/altars", "lich_tower/7x7/altar"}) {
			this.add(roomId13, LichTowerPieces.ROOM_7_LADDER_0, 100);
		}
		for(String roomId12 : new String[]{"lich_tower/7x7/potion", "lich_tower/7x7/library_hall", "lich_tower/7x7/jar_study", "lich_tower/7x7/elbow_chest", "lich_tower/7x7/guarded_chest", "lich_tower/7x7/potion_lab"}) {
			this.add(roomId12, LichTowerPieces.ROOM_7_LADDER_1, 100);
		}
		for(String roomId11 : new String[]{"lich_tower/7x7/library_hall", "lich_tower/7x7/jar_study", "lich_tower/7x7/elbow_chest", "lich_tower/7x7/guarded_chest", "lich_tower/7x7/garden_lab", "lich_tower/7x7/potion_lab"}) {
			this.add(roomId11, LichTowerPieces.ROOM_7_LADDER_3, 100);
		}
		for(String roomId10 : new String[]{"lich_tower/7x7/full_junction_1", "lich_tower/7x7/desk", "lich_tower/7x7/altars", "lich_tower/7x7/jar_study", "lich_tower/7x7/altar"}) {
			this.add(roomId10, LichTowerPieces.ROOM_7_LADDER_4, 100);
		}

		for(String roomId9 : new String[]{"lich_tower/9x9/archives", "lich_tower/9x9/enchanting_prison", "lich_tower/9x9/mossy_junction", "lich_tower/9x9/study", "lich_tower/9x9/centerpiece", "lich_tower/9x9/altar", "lich_tower/9x9/lectern_hall", "lich_tower/9x9/cauldron_keep", "lich_tower/9x9/large_study"}) {
			this.add(roomId9, LichTowerPieces.ROOM_9_LADDER_1, 100);
		}
		for(String roomId8 : new String[]{"lich_tower/9x9/study", "lich_tower/9x9/library_junction", "lich_tower/9x9/cauldron_keep"}) {
			this.add(roomId8, LichTowerPieces.ROOM_9_LADDER_2, 100);
		}

		for(String roomId7 : new String[]{"lich_tower/9x9/study", "lich_tower/9x9/library_junction"}) {
			this.add(roomId7, LichTowerPieces.ROOM_9_LADDER_4, 100);
		}
		for(String roomId6 : new String[]{"lich_tower/9x9/archives", "lich_tower/9x9/enchanting_prison", "lich_tower/9x9/mossy_junction", "lich_tower/9x9/study", "lich_tower/9x9/centerpiece", "lich_tower/9x9/altar", "lich_tower/9x9/lectern_hall", "lich_tower/9x9/large_study"}) {
			this.add(roomId6, LichTowerPieces.ROOM_9_LADDER_5, 100);
		}

		{
			// A little rarer than the rest :)
			this.add("lich_tower/9x9/classic_library", LichTowerPieces.ROOM_9_LADDER_2, 20);
			this.add("lich_tower/9x9/seven", LichTowerPieces.ROOM_9_LADDER_2, 1);
			this.add("lich_tower/9x9/seven", LichTowerPieces.ROOM_9_LADDER_4, 1);
		}

		for(String roomId5 : new String[]{"lich_tower/central_bridge", "lich_tower/central_bridge_open"}) {
			this.add(roomId5, LichTowerPieces.BRIDGE_FROM_CENTRAL, 100);
		}
		for(String roomId4 : new String[]{"lich_tower/central_bridge"}) {
			this.add(roomId4, LichTowerPieces.BRIDGE_FROM_CENTRAL_FALLBACK, 100);
		}

		for(String roomId3 : new String[]{"lich_tower/room_bridge_1", "lich_tower/room_bridge_2", "lich_tower/room_bridge_3", "lich_tower/room_bridge_4", "lich_tower/room_bridge_5"}) {
			this.add(roomId3, LichTowerPieces.ROOM_BRIDGE, 100);
		}
		for(String roomId2 : new String[]{"lich_tower/no_bridge"}) {
			this.add(roomId2, LichTowerPieces.ROOM_BRIDGE_FALLBACK, 100);
		}

		for(String roomId1 : new String[]{"lich_tower/end_bridge_1", "lich_tower/end_bridge_2", "lich_tower/end_bridge_3", "lich_tower/end_bridge_4", "lich_tower/end_bridge_5"}) {
			this.add(roomId1, LichTowerPieces.END_BRIDGE, 100);
		}

		this.add("lich_tower/grave", LichTowerPieces.YARD_GRAVE, 100);
		this.add("lich_tower/grave_open", LichTowerPieces.YARD_GRAVE, 50); // A little rarer
		this.add("lich_tower/grave_ajar", LichTowerPieces.YARD_GRAVE, 25); // Rarest

		for(String roomId : new String[]{"lich_tower/9x9/keepsake_casket"}) {
			this.add(roomId, LichTowerPieces.ROOM_9_SPECIAL, 100);
		}
	}
}

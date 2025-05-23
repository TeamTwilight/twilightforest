package twilightforest.datagen.data.custom;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.structures.finalcastle.FinalCastleBellTower21Component;
import twilightforest.world.components.structures.finalcastle.FinalCastleBossGazeboComponent;
import twilightforest.world.components.structures.finalcastle.FinalCastleLargeTowerComponent;
import twilightforest.world.components.structures.lichtowerrevamp.LichTowerPieces;

import java.util.concurrent.CompletableFuture;

public class StructureTemplateDefinitionGenerator extends StructureTemplateDefinitionProvider {
	public StructureTemplateDefinitionGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(output, lookupProvider, TwilightForestMod.ID);
	}

	@Override
	protected void generatePools() {
		this.addAllTemplatesToPool(LichTowerPieces.MOB_BRIDGE, 100,
			"lich_tower/bridge_spawner",
			"lich_tower/bridge_spawner_bend",
			"lich_tower/bridge_spawner_ropes",
			"lich_tower/bridge_spawner_wide",
			"lich_tower/bridge_spawner_zag",
			"lich_tower/bridge_spawner_zig"
		);

		this.addToAllPools("lich_tower/wall_bars", 100,
			LichTowerPieces.DOOR_STOPPER
		);
		this.addToAllPools("lich_tower/wall_cobble", 100,
			LichTowerPieces.DOOR_STOPPER,
			LichTowerPieces.DOOR_STOPPER_FALLBACK
		);

		this.addAllTemplatesToPool(LichTowerPieces.CENTER_DECOR, 100,
			"lich_tower/3x3/tree",
			"lich_tower/3x3/water_fountain",
			"lich_tower/3x3/water_well",
			"lich_tower/3x3/wither_rose"
		);
		this.addAllTemplatesToPool(LichTowerPieces.ROOM_DECOR, 100,
			"lich_tower/3x3/lava_well",
			"lich_tower/3x3/sapling",
			"lich_tower/3x3/water_fountain",
			"lich_tower/3x3/water_well"
		);

		this.addAllTemplatesToPool(LichTowerPieces.ROOM_3, 100,
			"lich_tower/3x3/short_lookout",
			"lich_tower/3x3/lookout",
			"lich_tower/3x3/double",
			"lich_tower/3x3/taller_double"
		);
		this.addAllTemplatesToPool(LichTowerPieces.ROOM_3_ROOF, 100,
			"lich_tower/3x3/pyramid_roof",
			"lich_tower/3x3/slabs_roof",
			"lich_tower/3x3/fence_roof",
			"lich_tower/3x3/curved_roof",
			"lich_tower/3x3/pointed_roof"
		);
		this.addAllTemplatesToPool(LichTowerPieces.ROOM_3_SIDE_ROOF, 100,
			"lich_tower/3x3/pyramid_roof",
			"lich_tower/3x3/slabs_roof",
			"lich_tower/3x3/fence_roof",
			"lich_tower/3x3/curved_roof",
			"lich_tower/3x3/pointed_roof"
		);

		this.addAllTemplatesToPool(LichTowerPieces.ROOM_5, 100,
			"lich_tower/5x5/elbow_junction",
			"lich_tower/5x5/full_junction",
			"lich_tower/5x5/straight_junction",
			"lich_tower/5x5/t_junction",
			"lich_tower/5x5/altar",
			"lich_tower/5x5/desk",
			"lich_tower/5x5/zombie_trap",
			"lich_tower/5x5/full_junction_2",
			"lich_tower/5x5/full_junction_3",
			"lich_tower/5x5/ladder",
			"lich_tower/5x5/library",
			"lich_tower/5x5/lone_chest",
			"lich_tower/5x5/potion",
			"lich_tower/5x5/spawner_1",
			"lich_tower/5x5/spawner_2",
			"lich_tower/5x5/spawner_3",
			"lich_tower/5x5/spawner_4",
			"lich_tower/5x5/spawner_5",
			"lich_tower/5x5/trinity",
			"lich_tower/5x5/webbed_spawner"
		);
		this.addAllTemplatesToPool(LichTowerPieces.ROOM_5_ROOF, 100,
			"lich_tower/5x5/pyramid_roof",
			"lich_tower/5x5/slabs_roof",
			"lich_tower/5x5/fence_roof",
			"lich_tower/5x5/curved_roof",
			"lich_tower/5x5/pointed_roof"
		);
		this.addAllTemplatesToPool(LichTowerPieces.ROOM_5_SIDE_ROOF, 100,
			"lich_tower/5x5/pyramid_roof",
			"lich_tower/5x5/slabs_roof",
			"lich_tower/5x5/fence_roof",
			"lich_tower/5x5/curved_roof",
			"lich_tower/5x5/pointed_roof"
		);

		this.addAllTemplatesToPool(LichTowerPieces.ROOM_7, 100,
			"lich_tower/7x7/elbow_junction",
			"lich_tower/7x7/full_junction",
			"lich_tower/7x7/straight_junction",
			"lich_tower/7x7/t_junction",
			"lich_tower/7x7/altars",
			"lich_tower/7x7/book_staircases",
			"lich_tower/7x7/cactus",
			"lich_tower/7x7/desk",
			"lich_tower/7x7/full_junction_1",
			"lich_tower/7x7/full_junction_2",
			"lich_tower/7x7/grave",
			"lich_tower/7x7/garden_lab",
			"lich_tower/7x7/library_hall",
			"lich_tower/7x7/nursery",
			"lich_tower/7x7/potion",
			"lich_tower/7x7/ritual",
			"lich_tower/7x7/tiered_library",
			"lich_tower/7x7/tiered_study",
			"lich_tower/7x7/pedestal_junction",
			"lich_tower/7x7/shelved_jars",
			"lich_tower/7x7/walled_library",
			"lich_tower/7x7/pedestal_library",
			"lich_tower/7x7/barbed_webs",
			"lich_tower/7x7/corner_caging"
		);
		this.addAllTemplatesToPool(LichTowerPieces.ROOM_7_ROOF, 100,
			"lich_tower/7x7/pyramid_roof",
			"lich_tower/7x7/slabs_roof",
			"lich_tower/7x7/fence_roof",
			"lich_tower/7x7/curved_roof",
			"lich_tower/7x7/pointed_roof"
		);
		this.addAllTemplatesToPool(LichTowerPieces.ROOM_7_SIDE_ROOF, 100,
			"lich_tower/7x7/pyramid_roof",
			"lich_tower/7x7/slabs_roof",
			"lich_tower/7x7/fence_roof",
			"lich_tower/7x7/curved_roof",
			"lich_tower/7x7/pointed_roof"
		);

		this.addAllTemplatesToPool(LichTowerPieces.ROOM_9, 100,
			"lich_tower/9x9/elbow_junction",
			"lich_tower/9x9/full_junction",
			"lich_tower/9x9/straight_junction",
			"lich_tower/9x9/t_junction",
			"lich_tower/9x9/archives",
			"lich_tower/9x9/enchanting_prison",
			"lich_tower/9x9/tiered_library",
			"lich_tower/9x9/mossy_junction",
			"lich_tower/9x9/altar",
			"lich_tower/9x9/lectern_hall",
			"lich_tower/9x9/tiered_study",
			"lich_tower/9x9/center_decor",
			"lich_tower/9x9/library_junction",
			"lich_tower/9x9/winding_ways",
			"lich_tower/9x9/tiered_spawner",
			"lich_tower/9x9/cauldron_keep",
			"lich_tower/9x9/large_study",
			"lich_tower/9x9/lockup",
			"lich_tower/9x9/holding"
		);
		this.addAllTemplatesToPool(LichTowerPieces.ROOM_9_ROOF, 100,
			"lich_tower/9x9/pyramid_roof",
			"lich_tower/9x9/slabs_roof",
			"lich_tower/9x9/fence_roof",
			"lich_tower/9x9/curved_roof",
			"lich_tower/9x9/pointed_roof"
		);
		this.addAllTemplatesToPool(LichTowerPieces.ROOM_9_SIDE_ROOF, 100,
			"lich_tower/9x9/pyramid_roof",
			"lich_tower/9x9/slabs_roof",
			"lich_tower/9x9/fence_roof",
			"lich_tower/9x9/curved_roof",
			"lich_tower/9x9/pointed_roof"
		);

		this.addAllTemplatesToPool(LichTowerPieces.GALLERY, 100,
			"lich_tower/gallery/castaway_paradise",
			"lich_tower/gallery/darkness",
			"lich_tower/gallery/lucid_lands",
			"lich_tower/gallery/music_in_the_mire",
			"lich_tower/gallery/the_hostile_paradise"
		);
		this.addAllTemplatesToPool(LichTowerPieces.GALLERY_ROOF_EVEN, 100,
			"lich_tower/gallery/fence_roof_even",
			"lich_tower/gallery/slabs_roof_even",
			"lich_tower/gallery/stairs_roof_even"
		);
		this.addAllTemplatesToPool(LichTowerPieces.GALLERY_ROOF_ODD, 100,
			"lich_tower/gallery/fence_roof_odd",
			"lich_tower/gallery/slabs_roof_odd",
			"lich_tower/gallery/stairs_roof_odd"
		);

		this.addAllTemplatesToPool(LichTowerPieces.ROOM_5_BEARD, 100,
			"lich_tower/5x5/beard_checkered",
			"lich_tower/5x5/beard_chiseled",
			"lich_tower/5x5/beard_chunks",
			"lich_tower/5x5/beard_staggered"
		);
		this.add("lich_tower/5x5/beard_trim", LichTowerPieces.ROOM_5_TRIM, 100);

		this.addAllTemplatesToPool(LichTowerPieces.ROOM_7_BEARD, 100,
			"lich_tower/7x7/beard_checkered",
			"lich_tower/7x7/beard_chiseled",
			"lich_tower/7x7/beard_chunks",
			"lich_tower/7x7/beard_staggered"
		);
		this.add("lich_tower/7x7/beard_trim", LichTowerPieces.ROOM_7_TRIM, 100);

		this.addAllTemplatesToPool(LichTowerPieces.ROOM_9_BEARD, 100,
			"lich_tower/9x9/beard_checkered",
			"lich_tower/9x9/beard_chiseled",
			"lich_tower/9x9/beard_chunks",
			"lich_tower/9x9/beard_staggered"
		);
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

		this.addAllTemplatesToPool(LichTowerPieces.ROOM_5_LADDER_0, 100,
			"lich_tower/5x5/straight_junction",
			"lich_tower/5x5/elbow_junction",
			"lich_tower/5x5/t_junction",
			"lich_tower/5x5/desk",
			"lich_tower/5x5/lectern",
			"lich_tower/5x5/lone_chest",
			"lich_tower/5x5/altar",
			"lich_tower/5x5/potion",
			"lich_tower/5x5/trinity"
		);
		this.addAllTemplatesToPool(LichTowerPieces.ROOM_5_LADDER_2, 100,
			"lich_tower/5x5/straight_junction",
			"lich_tower/5x5/elbow_junction",
			"lich_tower/5x5/t_junction",
			"lich_tower/5x5/desk",
			"lich_tower/5x5/lectern",
			"lich_tower/5x5/lone_chest",
			"lich_tower/5x5/altar",
			"lich_tower/5x5/potion",
			"lich_tower/5x5/trinity"
		);

		this.addAllTemplatesToPool(LichTowerPieces.ROOM_7_LADDER_0, 100,
			"lich_tower/7x7/full_junction_1",
			"lich_tower/7x7/desk",
			"lich_tower/7x7/altars",
			"lich_tower/7x7/altar"
		);
		this.addAllTemplatesToPool(LichTowerPieces.ROOM_7_LADDER_1, 100,
			"lich_tower/7x7/potion",
			"lich_tower/7x7/library_hall",
			"lich_tower/7x7/jar_study",
			"lich_tower/7x7/elbow_chest",
			"lich_tower/7x7/guarded_chest",
			"lich_tower/7x7/potion_lab"
		);
		this.addAllTemplatesToPool(LichTowerPieces.ROOM_7_LADDER_3, 100,
			"lich_tower/7x7/library_hall",
			"lich_tower/7x7/jar_study",
			"lich_tower/7x7/elbow_chest",
			"lich_tower/7x7/guarded_chest",
			"lich_tower/7x7/garden_lab",
			"lich_tower/7x7/potion_lab"
		);
		this.addAllTemplatesToPool(LichTowerPieces.ROOM_7_LADDER_4, 100,
			"lich_tower/7x7/full_junction_1",
			"lich_tower/7x7/desk",
			"lich_tower/7x7/altars",
			"lich_tower/7x7/jar_study",
			"lich_tower/7x7/altar"
		);

		this.addAllTemplatesToPool(LichTowerPieces.ROOM_9_LADDER_1, 100,
			"lich_tower/9x9/archives",
			"lich_tower/9x9/enchanting_prison",
			"lich_tower/9x9/mossy_junction",
			"lich_tower/9x9/study",
			"lich_tower/9x9/centerpiece",
			"lich_tower/9x9/altar",
			"lich_tower/9x9/lectern_hall",
			"lich_tower/9x9/cauldron_keep",
			"lich_tower/9x9/large_study"
		);
		this.addAllTemplatesToPool(LichTowerPieces.ROOM_9_LADDER_2, 100,
			"lich_tower/9x9/study",
			"lich_tower/9x9/library_junction",
			"lich_tower/9x9/cauldron_keep"
		);

		this.addAllTemplatesToPool(LichTowerPieces.ROOM_9_LADDER_4, 100,
			"lich_tower/9x9/study",
			"lich_tower/9x9/library_junction"
		);
		this.addAllTemplatesToPool(LichTowerPieces.ROOM_9_LADDER_5, 100,
			"lich_tower/9x9/archives",
			"lich_tower/9x9/enchanting_prison",
			"lich_tower/9x9/mossy_junction",
			"lich_tower/9x9/study",
			"lich_tower/9x9/centerpiece",
			"lich_tower/9x9/altar",
			"lich_tower/9x9/lectern_hall",
			"lich_tower/9x9/large_study"
		);

		{
			// A little rarer than the rest :)
			this.add("lich_tower/9x9/classic_library", LichTowerPieces.ROOM_9_LADDER_2, 20);
			this.add("lich_tower/9x9/seven", LichTowerPieces.ROOM_9_LADDER_2, 1);
			this.add("lich_tower/9x9/seven", LichTowerPieces.ROOM_9_LADDER_4, 1);
		}

		this.addAllTemplatesToPool(LichTowerPieces.BRIDGE_FROM_CENTRAL, 100,
			"lich_tower/central_bridge",
			"lich_tower/central_bridge_open"
		);
		this.addAllTemplatesToPool(LichTowerPieces.BRIDGE_FROM_CENTRAL_FALLBACK, 100,
			"lich_tower/central_bridge"
		);

		this.addAllTemplatesToPool(LichTowerPieces.ROOM_BRIDGE, 100,
			"lich_tower/room_bridge_1",
			"lich_tower/room_bridge_2",
			"lich_tower/room_bridge_3",
			"lich_tower/room_bridge_4",
			"lich_tower/room_bridge_5"
		);
		this.addAllTemplatesToPool(LichTowerPieces.ROOM_BRIDGE_FALLBACK, 100,
			"lich_tower/no_bridge"
		);

		this.addAllTemplatesToPool(LichTowerPieces.END_BRIDGE, 100,
			"lich_tower/end_bridge_1",
			"lich_tower/end_bridge_2",
			"lich_tower/end_bridge_3",
			"lich_tower/end_bridge_4",
			"lich_tower/end_bridge_5"
		);

		this.add("lich_tower/grave", LichTowerPieces.YARD_GRAVE, 100);
		this.add("lich_tower/grave_open", LichTowerPieces.YARD_GRAVE, 50); // A little rarer
		this.add("lich_tower/grave_ajar", LichTowerPieces.YARD_GRAVE, 25); // Rarest

		this.addAllTemplatesToPool(LichTowerPieces.ROOM_9_SPECIAL, 100,
			"lich_tower/9x9/keepsake_casket"
		);

		this.add("final_castle/temp/large_tower", FinalCastleLargeTowerComponent.LARGE_TOWER_TEMP_POOL, 100);
		this.add("final_castle/temp/bell_tower", FinalCastleBellTower21Component.BELL_TOWER_TEMP_POOL, 100);
		this.add("final_castle/temp/gazebo", FinalCastleBossGazeboComponent.GAZEBO_TEMP_POOL, 100);
	}
}

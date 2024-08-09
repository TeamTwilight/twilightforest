package twilightforest.world.components.structures.lichtowerrevamp;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.resources.ResourceLocation;
import twilightforest.TwilightForestMod;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

// TODO: Move to DataMaps if possible to obtain RegistryAccess
public final class LichTowerPieces {
	public static final ResourceLocation ENCLOSED_BRIDGE_CENTRAL = TwilightForestMod.prefix("lich_tower/central_bridge");
	static final ResourceLocation[] CENTER_BRIDGES = new ResourceLocation[] {
		ENCLOSED_BRIDGE_CENTRAL,
		TwilightForestMod.prefix("lich_tower/central_bridge_open")
	};
	public static final ResourceLocation DIRECT_ATTACHMENT = TwilightForestMod.prefix("lich_tower/no_bridge");
	static final ResourceLocation[] ROOM_BRIDGES = new ResourceLocation[] {
		TwilightForestMod.prefix("lich_tower/room_bridge_5"),
		TwilightForestMod.prefix("lich_tower/room_bridge_3")
	};
	static final ResourceLocation COBBLESTONE_WALL = TwilightForestMod.prefix("lich_tower/wall_cobble");
	static final ResourceLocation[] BRIDGE_COVERS = new ResourceLocation[] {
		TwilightForestMod.prefix("lich_tower/wall_bars"),
		COBBLESTONE_WALL
	};
	static final ResourceLocation[] MOB_BRIDGES = new ResourceLocation[] {
		TwilightForestMod.prefix("lich_tower/bridge_spawner"),
		TwilightForestMod.prefix("lich_tower/bridge_spawner_bend"),
		TwilightForestMod.prefix("lich_tower/bridge_spawner_ring"),
		TwilightForestMod.prefix("lich_tower/bridge_spawner_ropes"),
		TwilightForestMod.prefix("lich_tower/bridge_spawner_wide"),
		TwilightForestMod.prefix("lich_tower/bridge_spawner_zag")
	};
	static final ResourceLocation[] CENTER_DECORS = new ResourceLocation[] {
		TwilightForestMod.prefix("lich_tower/3x3/tree"),
		TwilightForestMod.prefix("lich_tower/3x3/water_fountain"),
		TwilightForestMod.prefix("lich_tower/3x3/water_well"),
		TwilightForestMod.prefix("lich_tower/3x3/wither_rose")
	};
	static final ResourceLocation[] ROOM_DECORS = new ResourceLocation[] {
		TwilightForestMod.prefix("lich_tower/3x3/chest"),
		TwilightForestMod.prefix("lich_tower/3x3/lava_well"),
		TwilightForestMod.prefix("lich_tower/3x3/sapling"),
		TwilightForestMod.prefix("lich_tower/3x3/water_fountain"),
		TwilightForestMod.prefix("lich_tower/3x3/water_well")
	};

	static final ResourceLocation[][] ROOMS = new ResourceLocation[][] {
		new ResourceLocation[] {
			TwilightForestMod.prefix("lich_tower/3x3/short_lookout"),
			TwilightForestMod.prefix("lich_tower/3x3/lookout"),
			TwilightForestMod.prefix("lich_tower/3x3/double"),
			TwilightForestMod.prefix("lich_tower/3x3/taller_double")
		},
		new ResourceLocation[] {
			TwilightForestMod.prefix("lich_tower/5x5/elbow_junction"),
			TwilightForestMod.prefix("lich_tower/5x5/full_junction"),
			TwilightForestMod.prefix("lich_tower/5x5/straight_junction"),
			TwilightForestMod.prefix("lich_tower/5x5/t_junction"),
			TwilightForestMod.prefix("lich_tower/5x5/altar"),
			TwilightForestMod.prefix("lich_tower/5x5/desk"),
			TwilightForestMod.prefix("lich_tower/5x5/full_junction_1"),
			TwilightForestMod.prefix("lich_tower/5x5/full_junction_2"),
			TwilightForestMod.prefix("lich_tower/5x5/full_junction_3"),
			TwilightForestMod.prefix("lich_tower/5x5/ladder"),
			TwilightForestMod.prefix("lich_tower/5x5/library"),
			TwilightForestMod.prefix("lich_tower/5x5/lone_chest"),
			TwilightForestMod.prefix("lich_tower/5x5/spawner_1"),
			TwilightForestMod.prefix("lich_tower/5x5/spawner_2"),
			TwilightForestMod.prefix("lich_tower/5x5/spawner_3"),
			TwilightForestMod.prefix("lich_tower/5x5/spawner_4"),
			TwilightForestMod.prefix("lich_tower/5x5/spawner_5"),
			TwilightForestMod.prefix("lich_tower/5x5/webbed_spawner")
		},
		new ResourceLocation[] {
			TwilightForestMod.prefix("lich_tower/7x7/elbow_junction"),
			TwilightForestMod.prefix("lich_tower/7x7/full_junction"),
			TwilightForestMod.prefix("lich_tower/7x7/straight_junction"),
			TwilightForestMod.prefix("lich_tower/7x7/t_junction"),
			TwilightForestMod.prefix("lich_tower/7x7/altars"),
			TwilightForestMod.prefix("lich_tower/7x7/book_staircases"),
			TwilightForestMod.prefix("lich_tower/7x7/cactus"),
			TwilightForestMod.prefix("lich_tower/7x7/desk"),
			TwilightForestMod.prefix("lich_tower/7x7/full_junction_1"),
			TwilightForestMod.prefix("lich_tower/7x7/full_junction_2"),
			TwilightForestMod.prefix("lich_tower/7x7/grave"),
			TwilightForestMod.prefix("lich_tower/7x7/library_hall"),
			TwilightForestMod.prefix("lich_tower/7x7/nursery"),
			TwilightForestMod.prefix("lich_tower/7x7/potion"),
			TwilightForestMod.prefix("lich_tower/7x7/ritual"),
			TwilightForestMod.prefix("lich_tower/7x7/tiered_library"),
			TwilightForestMod.prefix("lich_tower/7x7/tiered_study"),
			TwilightForestMod.prefix("lich_tower/7x7/tiered_spawner"),
			TwilightForestMod.prefix("lich_tower/7x7/center_decor"),
			TwilightForestMod.prefix("lich_tower/7x7/pedestal_junction"),
			TwilightForestMod.prefix("lich_tower/7x7/shelved_jars")
		},
		new ResourceLocation[] {
			TwilightForestMod.prefix("lich_tower/9x9/elbow_junction"),
			TwilightForestMod.prefix("lich_tower/9x9/full_junction"),
			TwilightForestMod.prefix("lich_tower/9x9/straight_junction"),
			TwilightForestMod.prefix("lich_tower/9x9/t_junction"),
			TwilightForestMod.prefix("lich_tower/9x9/archives"),
			TwilightForestMod.prefix("lich_tower/9x9/enchanting_prison"),
			TwilightForestMod.prefix("lich_tower/9x9/tiered_library"),
			TwilightForestMod.prefix("lich_tower/9x9/mossy_junction"),
			TwilightForestMod.prefix("lich_tower/9x9/altar"),
			TwilightForestMod.prefix("lich_tower/9x9/lectern_hall"),
			TwilightForestMod.prefix("lich_tower/9x9/tiered_study"),
			TwilightForestMod.prefix("lich_tower/9x9/center_decor"),
			TwilightForestMod.prefix("lich_tower/9x9/library_junction")
		}
	};
	static final ResourceLocation[] GALLERY_ROOMS = new ResourceLocation[] {
		TwilightForestMod.prefix("lich_tower/gallery/castaway_paradise"),
		TwilightForestMod.prefix("lich_tower/gallery/darkness"),
		TwilightForestMod.prefix("lich_tower/gallery/lucid_lands"),
		TwilightForestMod.prefix("lich_tower/gallery/music_in_the_mire"),
		TwilightForestMod.prefix("lich_tower/gallery/the_hostile_paradise")
	};
	static final ResourceLocation[] GALLERY_ROOFS_EVEN = new ResourceLocation[] {
		TwilightForestMod.prefix("lich_tower/gallery/fence_roof_even"),
		TwilightForestMod.prefix("lich_tower/gallery/slabs_roof_even"),
		TwilightForestMod.prefix("lich_tower/gallery/stairs_roof_even")
	};
	static final ResourceLocation[] GALLERY_ROOFS_ODD = new ResourceLocation[] {
		TwilightForestMod.prefix("lich_tower/gallery/fence_roof_odd"),
		TwilightForestMod.prefix("lich_tower/gallery/slabs_roof_odd"),
		TwilightForestMod.prefix("lich_tower/gallery/stairs_roof_odd")
	};

	static final ResourceLocation[][] ROOFS = new ResourceLocation[][] {
		new ResourceLocation[] {
			TwilightForestMod.prefix("lich_tower/3x3/pyramid_roof"),
			TwilightForestMod.prefix("lich_tower/3x3/slabs_roof"),
			TwilightForestMod.prefix("lich_tower/3x3/fence_roof"),
			TwilightForestMod.prefix("lich_tower/3x3/curved_roof"),
			TwilightForestMod.prefix("lich_tower/3x3/pointed_roof")
		},
		new ResourceLocation[] {
			TwilightForestMod.prefix("lich_tower/5x5/pyramid_roof"),
			TwilightForestMod.prefix("lich_tower/5x5/slabs_roof"),
			TwilightForestMod.prefix("lich_tower/5x5/fence_roof"),
			TwilightForestMod.prefix("lich_tower/5x5/curved_roof"),
			TwilightForestMod.prefix("lich_tower/5x5/pointed_roof")
		},
		new ResourceLocation[] {
			TwilightForestMod.prefix("lich_tower/7x7/pyramid_roof"),
			TwilightForestMod.prefix("lich_tower/7x7/slabs_roof"),
			TwilightForestMod.prefix("lich_tower/7x7/fence_roof"),
			TwilightForestMod.prefix("lich_tower/7x7/curved_roof"),
			TwilightForestMod.prefix("lich_tower/7x7/pointed_roof")
		},
		new ResourceLocation[] {
			TwilightForestMod.prefix("lich_tower/9x9/pyramid_roof"),
			TwilightForestMod.prefix("lich_tower/9x9/slabs_roof"),
			TwilightForestMod.prefix("lich_tower/9x9/fence_roof"),
			TwilightForestMod.prefix("lich_tower/9x9/curved_roof"),
			TwilightForestMod.prefix("lich_tower/9x9/pointed_roof")
		}
	};

	static final ResourceLocation[][] SIDE_ROOFS = new ResourceLocation[][] {
		new ResourceLocation[] {
			TwilightForestMod.prefix("lich_tower/3x3/pyramid_roof"),
			TwilightForestMod.prefix("lich_tower/3x3/slabs_side_roof"),
			TwilightForestMod.prefix("lich_tower/3x3/fence_roof"),
			TwilightForestMod.prefix("lich_tower/3x3/curved_roof"),
			TwilightForestMod.prefix("lich_tower/3x3/pointed_roof")
		},
		new ResourceLocation[] {
			TwilightForestMod.prefix("lich_tower/5x5/pyramid_roof"),
			TwilightForestMod.prefix("lich_tower/5x5/slabs_side_roof"),
			TwilightForestMod.prefix("lich_tower/5x5/fence_roof"),
			TwilightForestMod.prefix("lich_tower/5x5/curved_roof"),
			TwilightForestMod.prefix("lich_tower/5x5/pointed_roof")
		},
		new ResourceLocation[] {
			TwilightForestMod.prefix("lich_tower/7x7/pyramid_roof"),
			TwilightForestMod.prefix("lich_tower/7x7/slabs_side_roof"),
			TwilightForestMod.prefix("lich_tower/7x7/fence_roof"),
			TwilightForestMod.prefix("lich_tower/7x7/curved_roof"),
			TwilightForestMod.prefix("lich_tower/7x7/pointed_roof")
		},
		new ResourceLocation[] {
			TwilightForestMod.prefix("lich_tower/9x9/pyramid_roof"),
			TwilightForestMod.prefix("lich_tower/9x9/slabs_side_roof"),
			TwilightForestMod.prefix("lich_tower/9x9/fence_roof"),
			TwilightForestMod.prefix("lich_tower/9x9/curved_roof"),
			TwilightForestMod.prefix("lich_tower/9x9/pointed_roof")
		}
	};

	static final ResourceLocation[][] BEARDS = new ResourceLocation[][] {
		new ResourceLocation[] {
			TwilightForestMod.prefix("lich_tower/5x5/beard_fancy"),
			TwilightForestMod.prefix("lich_tower/5x5/beard_fangs"),
			TwilightForestMod.prefix("lich_tower/5x5/beard_plain")
		},
		new ResourceLocation[] {
			TwilightForestMod.prefix("lich_tower/7x7/beard_fancy"),
			TwilightForestMod.prefix("lich_tower/7x7/beard_fangs"),
			TwilightForestMod.prefix("lich_tower/7x7/beard_plain")
		},
		new ResourceLocation[] {
			TwilightForestMod.prefix("lich_tower/9x9/beard_fancy"),
			TwilightForestMod.prefix("lich_tower/9x9/beard_fangs"),
			TwilightForestMod.prefix("lich_tower/9x9/beard_plain")
		}
	};

	static final ResourceLocation[] FLAT_SIDE_ROOFS = new ResourceLocation[] {
		TwilightForestMod.prefix("lich_tower/3x3/flat_side_roof"),
		TwilightForestMod.prefix("lich_tower/5x5/flat_side_roof"),
		TwilightForestMod.prefix("lich_tower/7x7/flat_side_roof"),
		TwilightForestMod.prefix("lich_tower/9x9/flat_side_roof")
	};

	static final ResourceLocation[] FLAT_ROOFS = new ResourceLocation[] {
		TwilightForestMod.prefix("lich_tower/3x3/flat_roof"),
		TwilightForestMod.prefix("lich_tower/5x5/flat_roof"),
		TwilightForestMod.prefix("lich_tower/7x7/flat_roof"),
		TwilightForestMod.prefix("lich_tower/9x9/flat_roof")
	};

	static final ResourceLocation[] FLAT_BEARDS = new ResourceLocation[] {
		TwilightForestMod.prefix("lich_tower/5x5/beard_flat"),
		TwilightForestMod.prefix("lich_tower/7x7/beard_flat"),
		TwilightForestMod.prefix("lich_tower/9x9/beard_flat")
	};

	static final Set<String> LADDER_PLACEMENTS_1 = new HashSet<>(List.of(
		"twilightforest:ladder_below/0",
		"twilightforest:ladder_below/2"
	));
	static final Set<String> LADDER_PLACEMENTS_2 = new HashSet<>(List.of(
		"twilightforest:ladder_below/0",
		"twilightforest:ladder_below/1",
		"twilightforest:ladder_below/3",
		"twilightforest:ladder_below/4"
	));
	static final Set<String> LADDER_PLACEMENTS_3 = new HashSet<>(List.of(
		"twilightforest:ladder_below/1",
		"twilightforest:ladder_below/2",
		"twilightforest:ladder_below/4",
		"twilightforest:ladder_below/5"
	));

	static final List<Int2ObjectMap<List<ResourceLocation>>> LADDER_ROOMS = List.of(
		new Int2ObjectArrayMap<>(Map.of(
			0, List.of(
				TwilightForestMod.prefix("lich_tower/5x5/straight_junction"),
				TwilightForestMod.prefix("lich_tower/5x5/elbow_junction"),
				TwilightForestMod.prefix("lich_tower/5x5/t_junction"),
				TwilightForestMod.prefix("lich_tower/5x5/desk"),
				TwilightForestMod.prefix("lich_tower/5x5/lectern"),
				TwilightForestMod.prefix("lich_tower/5x5/lone_chest"),
				TwilightForestMod.prefix("lich_tower/5x5/altar")
			),
			2, List.of(
				TwilightForestMod.prefix("lich_tower/5x5/straight_junction"),
				TwilightForestMod.prefix("lich_tower/5x5/elbow_junction"),
				TwilightForestMod.prefix("lich_tower/5x5/t_junction"),
				TwilightForestMod.prefix("lich_tower/5x5/desk"),
				TwilightForestMod.prefix("lich_tower/5x5/lectern"),
				TwilightForestMod.prefix("lich_tower/5x5/lone_chest"),
				TwilightForestMod.prefix("lich_tower/5x5/altar")
			)
		)),
		new Int2ObjectArrayMap<>(Map.of(
			0, List.of(
				TwilightForestMod.prefix("lich_tower/7x7/full_junction_1"),
				TwilightForestMod.prefix("lich_tower/7x7/desk"),
				TwilightForestMod.prefix("lich_tower/7x7/altars"),
				TwilightForestMod.prefix("lich_tower/7x7/altar")
			),
			1, List.of(
				TwilightForestMod.prefix("lich_tower/7x7/elbow_junction"),
				TwilightForestMod.prefix("lich_tower/7x7/t_junction"),
				TwilightForestMod.prefix("lich_tower/7x7/potion"),
				TwilightForestMod.prefix("lich_tower/7x7/library_hall"),
				TwilightForestMod.prefix("lich_tower/7x7/jar_study"),
				TwilightForestMod.prefix("lich_tower/7x7/elbow_chest"),
				TwilightForestMod.prefix("lich_tower/7x7/guarded_chest"),
				TwilightForestMod.prefix("lich_tower/7x7/potion_lab")
			),
			3, List.of(
				TwilightForestMod.prefix("lich_tower/7x7/elbow_junction"),
				TwilightForestMod.prefix("lich_tower/7x7/t_junction"),
				TwilightForestMod.prefix("lich_tower/7x7/library_hall"),
				TwilightForestMod.prefix("lich_tower/7x7/jar_study"),
				TwilightForestMod.prefix("lich_tower/7x7/elbow_chest"),
				TwilightForestMod.prefix("lich_tower/7x7/guarded_chest"),
				TwilightForestMod.prefix("lich_tower/7x7/garden_lab"),
				TwilightForestMod.prefix("lich_tower/7x7/potion_lab")
			),
			4, List.of(
				TwilightForestMod.prefix("lich_tower/7x7/full_junction_1"),
				TwilightForestMod.prefix("lich_tower/7x7/desk"),
				TwilightForestMod.prefix("lich_tower/7x7/altars"),
				TwilightForestMod.prefix("lich_tower/7x7/jar_study"),
				TwilightForestMod.prefix("lich_tower/7x7/altar")
			)
		)),
		new Int2ObjectArrayMap<>(Map.of(
			1, List.of(
				TwilightForestMod.prefix("lich_tower/9x9/straight_junction"),
				TwilightForestMod.prefix("lich_tower/9x9/elbow_junction"),
				TwilightForestMod.prefix("lich_tower/9x9/t_junction"),
				TwilightForestMod.prefix("lich_tower/9x9/full_junction"),
				TwilightForestMod.prefix("lich_tower/9x9/keepsake_casket"),
				TwilightForestMod.prefix("lich_tower/9x9/archives"),
				TwilightForestMod.prefix("lich_tower/9x9/enchanting_prison"),
				TwilightForestMod.prefix("lich_tower/9x9/mossy_junction"),
				TwilightForestMod.prefix("lich_tower/9x9/study"),
				TwilightForestMod.prefix("lich_tower/9x9/centerpiece"),
				TwilightForestMod.prefix("lich_tower/9x9/altar"),
				TwilightForestMod.prefix("lich_tower/9x9/lectern_hall")
			),
			2, List.of(
				TwilightForestMod.prefix("lich_tower/9x9/classic_library"),
				TwilightForestMod.prefix("lich_tower/9x9/straight_junction"),
				TwilightForestMod.prefix("lich_tower/9x9/elbow_junction"),
				TwilightForestMod.prefix("lich_tower/9x9/t_junction"),
				TwilightForestMod.prefix("lich_tower/9x9/keepsake_casket"),
				TwilightForestMod.prefix("lich_tower/9x9/study"),
				TwilightForestMod.prefix("lich_tower/9x9/library_junction")
			),
			4, List.of(
				TwilightForestMod.prefix("lich_tower/9x9/classic_library"),
				TwilightForestMod.prefix("lich_tower/9x9/straight_junction"),
				TwilightForestMod.prefix("lich_tower/9x9/elbow_junction"),
				TwilightForestMod.prefix("lich_tower/9x9/t_junction"),
				TwilightForestMod.prefix("lich_tower/9x9/keepsake_casket"),
				TwilightForestMod.prefix("lich_tower/9x9/study"),
				TwilightForestMod.prefix("lich_tower/9x9/library_junction")
			),
			5, List.of(
				TwilightForestMod.prefix("lich_tower/9x9/straight_junction"),
				TwilightForestMod.prefix("lich_tower/9x9/elbow_junction"),
				TwilightForestMod.prefix("lich_tower/9x9/t_junction"),
				TwilightForestMod.prefix("lich_tower/9x9/full_junction"),
				TwilightForestMod.prefix("lich_tower/9x9/keepsake_casket"),
				TwilightForestMod.prefix("lich_tower/9x9/archives"),
				TwilightForestMod.prefix("lich_tower/9x9/enchanting_prison"),
				TwilightForestMod.prefix("lich_tower/9x9/mossy_junction"),
				TwilightForestMod.prefix("lich_tower/9x9/study"),
				TwilightForestMod.prefix("lich_tower/9x9/centerpiece"),
				TwilightForestMod.prefix("lich_tower/9x9/altar"),
				TwilightForestMod.prefix("lich_tower/9x9/lectern_hall")
			)
		))
	);

	private LichTowerPieces() {
		throw new IllegalStateException("How did we get here?");
	}
}

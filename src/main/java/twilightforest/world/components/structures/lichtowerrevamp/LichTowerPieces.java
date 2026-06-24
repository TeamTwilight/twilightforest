package twilightforest.world.components.structures.lichtowerrevamp;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.resources.Identifier;
import twilightforest.TwilightForestMod;
import tamaized.beanification.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public final class LichTowerPieces {
	public static final int YARD_SPAWNS = 0;
	public static final int INTERIOR_SPAWNS = 1;
	public static final int EMPTY = 2;

	// Junction names
	final Set<String> ladderPlacements1 = new HashSet<>(List.of(
		"twilightforest:ladder_below/0",
		"twilightforest:ladder_below/2"
	));
	final Set<String> ladderPlacements2 = new HashSet<>(List.of(
		"twilightforest:ladder_below/0",
		"twilightforest:ladder_below/1",
		"twilightforest:ladder_below/3",
		"twilightforest:ladder_below/4"
	));
	final Set<String> ladderPlacements3 = new HashSet<>(List.of(
		"twilightforest:ladder_below/1",
		"twilightforest:ladder_below/2",
		"twilightforest:ladder_below/4",
		"twilightforest:ladder_below/5"
	));

	public static final Identifier BRIDGE_FROM_CENTRAL = TwilightForestMod.prefix("lich_tower/bridge_from_central");
	public static final Identifier BRIDGE_FROM_CENTRAL_FALLBACK = TwilightForestMod.prefix("lich_tower/bridge_from_central_fallback");
	public static final Identifier END_BRIDGE = TwilightForestMod.prefix("lich_tower/end_bridge");
	public static final Identifier ROOM_BRIDGE = TwilightForestMod.prefix("lich_tower/room_bridge");
	public static final Identifier ROOM_BRIDGE_FALLBACK = TwilightForestMod.prefix("lich_tower/room_bridge_fallback");

	public static final Identifier DOOR_STOPPER = TwilightForestMod.prefix("lich_tower/door_stopper");
	public static final Identifier DOOR_STOPPER_FALLBACK = TwilightForestMod.prefix("lich_tower/door_stopper_fallback");
	public static final Identifier MOB_BRIDGE = TwilightForestMod.prefix("lich_tower/mob_bridge");
	public static final Identifier CENTER_DECOR = TwilightForestMod.prefix("lich_tower/center_decor");
	public static final Identifier ROOM_DECOR = TwilightForestMod.prefix("lich_tower/room_decor");

	public static final Identifier ROOM_3 = TwilightForestMod.prefix("lich_tower/3x3");
	public static final Identifier ROOM_5 = TwilightForestMod.prefix("lich_tower/5x5");
	public static final Identifier ROOM_7 = TwilightForestMod.prefix("lich_tower/7x7");
	public static final Identifier ROOM_9 = TwilightForestMod.prefix("lich_tower/9x9");
	public static final Identifier ROOM_9_SPECIAL = TwilightForestMod.prefix("lich_tower/9x9/special");
	public static final Identifier ROOM_3_ROOF = TwilightForestMod.prefix("lich_tower/3x3/roof");
	public static final Identifier ROOM_5_ROOF = TwilightForestMod.prefix("lich_tower/5x5/roof");
	public static final Identifier ROOM_7_ROOF = TwilightForestMod.prefix("lich_tower/7x7/roof");
	public static final Identifier ROOM_9_ROOF = TwilightForestMod.prefix("lich_tower/9x9/roof");
	public static final Identifier ROOM_3_ROOF_FALLBACK = TwilightForestMod.prefix("lich_tower/3x3/roof_fallback");
	public static final Identifier ROOM_5_ROOF_FALLBACK = TwilightForestMod.prefix("lich_tower/5x5/roof_fallback");
	public static final Identifier ROOM_7_ROOF_FALLBACK = TwilightForestMod.prefix("lich_tower/7x7/roof_fallback");
	public static final Identifier ROOM_9_ROOF_FALLBACK = TwilightForestMod.prefix("lich_tower/9x9/roof_fallback");
	public static final Identifier ROOM_3_SIDE_ROOF = TwilightForestMod.prefix("lich_tower/3x3/side_roof");
	public static final Identifier ROOM_5_SIDE_ROOF = TwilightForestMod.prefix("lich_tower/5x5/side_roof");
	public static final Identifier ROOM_7_SIDE_ROOF = TwilightForestMod.prefix("lich_tower/7x7/side_roof");
	public static final Identifier ROOM_9_SIDE_ROOF = TwilightForestMod.prefix("lich_tower/9x9/side_roof");
	public static final Identifier ROOM_3_SIDE_ROOF_FALLBACK = TwilightForestMod.prefix("lich_tower/3x3/roof_fallback");
	public static final Identifier ROOM_5_SIDE_ROOF_FALLBACK = TwilightForestMod.prefix("lich_tower/5x5/roof_fallback");
	public static final Identifier ROOM_7_SIDE_ROOF_FALLBACK = TwilightForestMod.prefix("lich_tower/7x7/roof_fallback");
	public static final Identifier ROOM_9_SIDE_ROOF_FALLBACK = TwilightForestMod.prefix("lich_tower/9x9/roof_fallback");
	public static final Identifier ROOM_5_BEARD = TwilightForestMod.prefix("lich_tower/5x5/beard");
	public static final Identifier ROOM_7_BEARD = TwilightForestMod.prefix("lich_tower/7x7/beard");
	public static final Identifier ROOM_9_BEARD = TwilightForestMod.prefix("lich_tower/9x9/beard");
	public static final Identifier ROOM_5_BEARD_FALLBACK = TwilightForestMod.prefix("lich_tower/5x5/beard_fallback");
	public static final Identifier ROOM_7_BEARD_FALLBACK = TwilightForestMod.prefix("lich_tower/7x7/beard_fallback");
	public static final Identifier ROOM_9_BEARD_FALLBACK = TwilightForestMod.prefix("lich_tower/9x9/beard_fallback");
	public static final Identifier ROOM_5_TRIM = TwilightForestMod.prefix("lich_tower/5x5/trim");
	public static final Identifier ROOM_7_TRIM = TwilightForestMod.prefix("lich_tower/7x7/trim");
	public static final Identifier ROOM_9_TRIM = TwilightForestMod.prefix("lich_tower/9x9/trim");

	public static final Identifier GALLERY = TwilightForestMod.prefix("lich_tower/gallery");
	public static final Identifier GALLERY_ROOF_EVEN = TwilightForestMod.prefix("lich_tower/gallery_even");
	public static final Identifier GALLERY_ROOF_ODD = TwilightForestMod.prefix("lich_tower/gallery_odd");

	public static final Identifier ROOM_5_LADDER_0 = TwilightForestMod.prefix("lich_tower/5x5/ladder_0");
	public static final Identifier ROOM_5_LADDER_2 = TwilightForestMod.prefix("lich_tower/5x5/ladder_2");

	public static final Identifier ROOM_7_LADDER_0 = TwilightForestMod.prefix("lich_tower/7x7/ladder_0");
	public static final Identifier ROOM_7_LADDER_1 = TwilightForestMod.prefix("lich_tower/7x7/ladder_1");
	public static final Identifier ROOM_7_LADDER_3 = TwilightForestMod.prefix("lich_tower/7x7/ladder_3");
	public static final Identifier ROOM_7_LADDER_4 = TwilightForestMod.prefix("lich_tower/7x7/ladder_4");

	public static final Identifier ROOM_9_LADDER_1 = TwilightForestMod.prefix("lich_tower/9x9/ladder_1");
	public static final Identifier ROOM_9_LADDER_2 = TwilightForestMod.prefix("lich_tower/9x9/ladder_2");
	public static final Identifier ROOM_9_LADDER_4 = TwilightForestMod.prefix("lich_tower/9x9/ladder_4");
	public static final Identifier ROOM_9_LADDER_5 = TwilightForestMod.prefix("lich_tower/9x9/ladder_5");

	public static final Identifier YARD_GRAVE = TwilightForestMod.prefix("lich_tower/grave");

	final List<Int2ObjectMap<Identifier>> ladderRooms = List.of(
		new Int2ObjectArrayMap<>(Map.of(
			0, ROOM_5_LADDER_0,
			2, ROOM_5_LADDER_2
		)),
		new Int2ObjectArrayMap<>(Map.of(
			0, ROOM_7_LADDER_0,
			1, ROOM_7_LADDER_1,
			3, ROOM_7_LADDER_3,
			4, ROOM_7_LADDER_4
		)),
		new Int2ObjectArrayMap<>(Map.of(
			1, ROOM_9_LADDER_1,
			2, ROOM_9_LADDER_2,
			4, ROOM_9_LADDER_4,
			5, ROOM_9_LADDER_5
		))
	);
}

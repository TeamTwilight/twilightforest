package twilightforest.init;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import twilightforest.TwilightForestMod;

import java.util.Map;

public class TFMapDecorations {
	public static final DeferredRegister<MapDecorationType> DECORATIONS = DeferredRegister.create(Registries.MAP_DECORATION_TYPE, TwilightForestMod.ID);

	public static final DeferredHolder<MapDecorationType, MapDecorationType> HEDGE_MAZE = register("hedge_maze");
	public static final DeferredHolder<MapDecorationType, MapDecorationType> SMALL_HOLLOW_HILL = register("small_hollow_hill");
	public static final DeferredHolder<MapDecorationType, MapDecorationType> MEDIUM_HOLLOW_HILL = register("medium_hollow_hill");
	public static final DeferredHolder<MapDecorationType, MapDecorationType> LARGE_HOLLOW_HILL = register("large_hollow_hill");
	public static final DeferredHolder<MapDecorationType, MapDecorationType> QUEST_GROVE = register("quest_grove");
	public static final DeferredHolder<MapDecorationType, MapDecorationType> NAGA_COURTYARD = register("naga_courtyard");
	public static final DeferredHolder<MapDecorationType, MapDecorationType> LICH_TOWER = register("lich_tower");
	public static final DeferredHolder<MapDecorationType, MapDecorationType> LABYRINTH = register("labyrinth");
	public static final DeferredHolder<MapDecorationType, MapDecorationType> HYDRA_LAIR = register("hydra_lair");
	public static final DeferredHolder<MapDecorationType, MapDecorationType> KNIGHT_STRONGHOLD = register("knight_stronghold");
	public static final DeferredHolder<MapDecorationType, MapDecorationType> DARK_TOWER = register("dark_tower");
	public static final DeferredHolder<MapDecorationType, MapDecorationType> YETI_LAIR = register("yeti_lair");
	public static final DeferredHolder<MapDecorationType, MapDecorationType> AURORA_PALACE = register("aurora_palace");
	public static final DeferredHolder<MapDecorationType, MapDecorationType> TROLL_CAVES = register("troll_caves");
	public static final DeferredHolder<MapDecorationType, MapDecorationType> FINAL_CASTLE = register("final_castle");

	private static DeferredHolder<MapDecorationType, MapDecorationType> register(String name) {
		return DECORATIONS.register(name, () -> new MapDecorationType(TwilightForestMod.prefix(name), true, -1, false, true));
	}
}

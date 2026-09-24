package twilightforest.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.JukeboxSong;
import twilightforest.TwilightForestMod;

public class TFJukeboxSongs {

	public static final ResourceKey<JukeboxSong> RADIANCE = registerKey("radiance");
	public static final ResourceKey<JukeboxSong> STEPS = registerKey("steps");
	public static final ResourceKey<JukeboxSong> SUPERSTITIOUS = registerKey("superstitious");
	public static final ResourceKey<JukeboxSong> HOME = registerKey("home");
	public static final ResourceKey<JukeboxSong> WAYFARER = registerKey("warfarer");
	public static final ResourceKey<JukeboxSong> FINDINGS = registerKey("findings");
	public static final ResourceKey<JukeboxSong> MAKER = registerKey("maker");
	public static final ResourceKey<JukeboxSong> THREAD = registerKey("thread");
	public static final ResourceKey<JukeboxSong> MOTION = registerKey("motion");

	private static ResourceKey<JukeboxSong> registerKey(String name) {
		return ResourceKey.create(Registries.JUKEBOX_SONG, TwilightForestMod.prefix(name));
	}

}

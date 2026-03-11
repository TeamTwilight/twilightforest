package twilightforest.init;

import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
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

	public static void bootstrap(BootstrapContext<JukeboxSong> context) {
		register(context, RADIANCE, TFSounds.MUSIC_DISC_RADIANCE, 123, 15);
		register(context, STEPS, TFSounds.MUSIC_DISC_STEPS, 195, 15);
		register(context, SUPERSTITIOUS, TFSounds.MUSIC_DISC_SUPERSTITIOUS, 192, 15);
		register(context, HOME, TFSounds.MUSIC_DISC_HOME, 215, 15);
		register(context, WAYFARER, TFSounds.MUSIC_DISC_WAYFARER, 173, 15);
		register(context, FINDINGS, TFSounds.MUSIC_DISC_FINDINGS, 196, 15);
		register(context, MAKER, TFSounds.MUSIC_DISC_MAKER, 207, 15);
		register(context, THREAD, TFSounds.MUSIC_DISC_THREAD, 201, 15);
		register(context, MOTION, TFSounds.MUSIC_DISC_MOTION, 169, 15);
	}

	private static void register(BootstrapContext<JukeboxSong> context, ResourceKey<JukeboxSong> key, Holder<SoundEvent> sound, float length, int output) {
		context.register(key, new JukeboxSong(sound, Component.translatable(Util.makeDescriptionId("jukebox_song", key.location())), length, output));
	}
}

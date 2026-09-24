package twilightforest.datagen.data;

import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Util;
import net.minecraft.world.item.JukeboxSong;
import twilightforest.init.TFJukeboxSongs;
import twilightforest.init.TFSounds;

public class TFJukeboxSongGenerator {

    public static void bootstrap(BootstrapContext<JukeboxSong> context) {
        register(context, TFJukeboxSongs.RADIANCE, TFSounds.MUSIC_DISC_RADIANCE, 123, 15);
        register(context, TFJukeboxSongs.STEPS, TFSounds.MUSIC_DISC_STEPS, 195, 15);
        register(context, TFJukeboxSongs.SUPERSTITIOUS, TFSounds.MUSIC_DISC_SUPERSTITIOUS, 192, 15);
        register(context, TFJukeboxSongs.HOME, TFSounds.MUSIC_DISC_HOME, 215, 15);
        register(context, TFJukeboxSongs.WAYFARER, TFSounds.MUSIC_DISC_WAYFARER, 173, 15);
        register(context, TFJukeboxSongs.FINDINGS, TFSounds.MUSIC_DISC_FINDINGS, 196, 15);
        register(context, TFJukeboxSongs.MAKER, TFSounds.MUSIC_DISC_MAKER, 207, 15);
        register(context, TFJukeboxSongs.THREAD, TFSounds.MUSIC_DISC_THREAD, 201, 15);
        register(context, TFJukeboxSongs.MOTION, TFSounds.MUSIC_DISC_MOTION, 169, 15);
    }

    private static void register(BootstrapContext<JukeboxSong> context, ResourceKey<JukeboxSong> key, Holder<SoundEvent> sound, float length, int output) {
        context.register(key, new JukeboxSong(sound, Component.translatable(Util.makeDescriptionId("jukebox_song", key.identifier())), length, output));
    }

}

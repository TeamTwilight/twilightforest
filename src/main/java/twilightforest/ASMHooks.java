package twilightforest;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.BackgroundMusicSelector;
import net.minecraft.client.audio.BackgroundMusicTracks;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.util.registry.SimpleRegistry;
import twilightforest.world.TFDimensions;

import java.util.Optional;

@SuppressWarnings({"JavadocReference", "unused", "RedundantSuppression"})
public class ASMHooks {

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.world.gen.settings.DimensionGeneratorSettings#DimensionGeneratorSettings(long, boolean, boolean, SimpleRegistry, Optional)}<br>
	 * [BEFORE FIRST PUTFIELD]
	 */
	public static long seed(long seed) {
		TFDimensions.seed = seed;
		return seed;
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.client.gui.MapItemRenderer.Instance#func_228089_a_(MatrixStack, IRenderTypeBuffer, boolean, int)}<br>
	 * [BEFORE FIRST ISTORE]
	 */
	public static void mapRenderContext(MatrixStack stack, IRenderTypeBuffer buffer, int light) {
		TFMagicMapData.TFMapDecoration.RenderContext.stack = stack;
		TFMagicMapData.TFMapDecoration.RenderContext.buffer = buffer;
		TFMagicMapData.TFMapDecoration.RenderContext.light = light;
	}

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.client.audio.MusicTicker#tick()}<br>
	 * [AFTER FIRST INVOKEVIRTUAL]
	 */
	public static BackgroundMusicSelector music(BackgroundMusicSelector music) {
		if (Minecraft.getInstance().world != null && Minecraft.getInstance().player != null && (music == BackgroundMusicTracks.CREATIVE_MODE_MUSIC || music == BackgroundMusicTracks.UNDER_WATER_MUSIC) && Minecraft.getInstance().world.getDimensionKey().getLocation().toString().equals(TFConfig.COMMON_CONFIG.DIMENSION.twilightForestID.get()))
			return Minecraft.getInstance().world.getBiomeManager().getBiomeAtPosition(Minecraft.getInstance().player.getPosition()).getBackgroundMusic().orElse(BackgroundMusicTracks.WORLD_MUSIC);
		return music;
	}

}

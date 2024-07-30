package twilightforest.util.datamaps;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.material.MapColor;
import twilightforest.util.Codecs;

public record MagicMapBiomeColor(MapColor color, int brightness) {

	public static final Codec<MagicMapBiomeColor> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		Codecs.COLOR_CODEC.fieldOf("map_color").forGetter(MagicMapBiomeColor::color),
		Codec.INT.fieldOf("brightness").forGetter(MagicMapBiomeColor::brightness)
	).apply(instance, MagicMapBiomeColor::new));

	public MagicMapBiomeColor(MapColor color) {
		this(color, 1);
	}
}

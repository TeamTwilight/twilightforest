package twilightforest.util.datamaps;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.material.MapColor;
import twilightforest.util.Codecs;

public record OreMapOreColor(MapColor color) {

	public static final Codec<OreMapOreColor> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		Codecs.COLOR_CODEC.fieldOf("map_color").forGetter(OreMapOreColor::color)
	).apply(instance, OreMapOreColor::new));
}

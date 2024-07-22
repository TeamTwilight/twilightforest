package twilightforest.components.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

public record JarLid(Item lid) {
	public static final Codec<JarLid> CODEC = RecordCodecBuilder.create(inst -> inst.group(
		BuiltInRegistries.ITEM.byNameCodec().fieldOf("lid").forGetter(JarLid::lid)
	).apply(inst, JarLid::new));
}

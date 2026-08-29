package twilightforest.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public record JarLid(ResourceKey<Item> item) {
	public static final Codec<JarLid> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		ResourceKey.codec(Registries.ITEM)
			.fieldOf("item")
			.forGetter(JarLid::item)
	).apply(instance, JarLid::new));
}
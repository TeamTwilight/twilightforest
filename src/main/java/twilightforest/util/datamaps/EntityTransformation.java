package twilightforest.util.datamaps;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;

public record EntityTransformation(EntityType<?> result) {

	public static final Codec<EntityTransformation> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("transform_to").forGetter(EntityTransformation::result)
	).apply(instance, EntityTransformation::new));
}

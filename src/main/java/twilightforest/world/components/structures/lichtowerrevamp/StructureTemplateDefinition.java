package twilightforest.world.components.structures.lichtowerrevamp;

import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

/**
 * @param poolWeights Weights in assigning this template to their respective pools
 */
public record StructureTemplateDefinition(Map<ResourceLocation, Integer> poolWeights) {
	public final static Codec<StructureTemplateDefinition> CODEC = Codec.unboundedMap(ResourceLocation.CODEC, Codec.INT).xmap(StructureTemplateDefinition::new, StructureTemplateDefinition::poolWeights);
}

package twilightforest.world.components.structures.util;

import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

/**
 * @param poolWeights Weights in assigning this template to their respective pools
 */
public record StructureTemplateDefinition(Map<ResourceLocation, TemplatePoolInstance> poolWeights) {
	public final static Codec<StructureTemplateDefinition> CODEC = Codec.unboundedMap(ResourceLocation.CODEC, TemplatePoolInstance.CODEC).xmap(StructureTemplateDefinition::new, StructureTemplateDefinition::poolWeights);
}

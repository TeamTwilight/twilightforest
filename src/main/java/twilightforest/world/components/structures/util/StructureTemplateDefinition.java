package twilightforest.world.components.structures.util;

import com.mojang.serialization.Codec;
import net.minecraft.resources.Identifier;

import java.util.Map;

/**
 * @param poolWeights Weights in assigning this template to their respective pools
 */
public record StructureTemplateDefinition(Map<Identifier, TemplatePoolInstance> poolWeights) {
	public final static Codec<StructureTemplateDefinition> CODEC = Codec.unboundedMap(Identifier.CODEC, TemplatePoolInstance.CODEC).xmap(StructureTemplateDefinition::new, StructureTemplateDefinition::poolWeights);
}

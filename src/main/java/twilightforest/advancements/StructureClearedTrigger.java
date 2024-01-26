package twilightforest.advancements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.levelgen.structure.Structure;
import twilightforest.init.TFAdvancements;

import java.util.Optional;

public class StructureClearedTrigger extends SimpleCriterionTrigger<StructureClearedTrigger.TriggerInstance> {

	@Override
	public Codec<StructureClearedTrigger.TriggerInstance> codec() {
		return StructureClearedTrigger.TriggerInstance.CODEC;
	}

	public void trigger(ServerPlayer player, ResourceKey<Structure> structure) {
		this.trigger(player, (instance) -> instance.test(structure));
	}

	public record TriggerInstance(Optional<ContextAwarePredicate> player, ResourceKey<Structure> structure) implements SimpleInstance {

		public static final Codec<StructureClearedTrigger.TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
						ExtraCodecs.strictOptionalField(EntityPredicate.ADVANCEMENT_CODEC, "player").forGetter(StructureClearedTrigger.TriggerInstance::player),
						ResourceKey.codec(Registries.STRUCTURE).fieldOf("structure").forGetter(StructureClearedTrigger.TriggerInstance::structure))
				.apply(instance, StructureClearedTrigger.TriggerInstance::new));

		public static Criterion<StructureClearedTrigger.TriggerInstance> clearedStructure(ResourceKey<Structure> structure) {
			return TFAdvancements.STRUCTURE_CLEARED.get().createCriterion(new StructureClearedTrigger.TriggerInstance(Optional.empty(), structure));
		}

		boolean test(ResourceKey<Structure> structure) {
			return this.structure.equals(structure);
		}
	}
}

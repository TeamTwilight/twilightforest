package twilightforest.advancements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import twilightforest.init.TFAdvancements;

import java.util.Optional;

public class AddModifierTrigger extends SimpleCriterionTrigger<AddModifierTrigger.TriggerInstance> {

	@Override
	public Codec<AddModifierTrigger.TriggerInstance> codec() {
		return AddModifierTrigger.TriggerInstance.CODEC;
	}

	public void trigger(ServerPlayer player, ResourceLocation modifier) {
		this.trigger(player, (instance) -> instance.test(modifier));
	}

	public record TriggerInstance(Optional<ContextAwarePredicate> player, Optional<ResourceLocation> modifier) implements SimpleInstance {

		public static final Codec<AddModifierTrigger.TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(AddModifierTrigger.TriggerInstance::player),
				ResourceLocation.CODEC.optionalFieldOf("modifier").forGetter(AddModifierTrigger.TriggerInstance::modifier))
			.apply(instance, AddModifierTrigger.TriggerInstance::new));

		public static Criterion<AddModifierTrigger.TriggerInstance> addedAnyModifier() {
			return TFAdvancements.ADD_MODIFIER.get().createCriterion(new AddModifierTrigger.TriggerInstance(Optional.empty(), Optional.empty()));
		}

		public static Criterion<AddModifierTrigger.TriggerInstance> addedModifier(ResourceLocation modifier) {
			return TFAdvancements.ADD_MODIFIER.get().createCriterion(new AddModifierTrigger.TriggerInstance(Optional.empty(), Optional.of(modifier)));
		}

		boolean test(ResourceLocation modifier) {
			return this.modifier().isEmpty() || this.modifier().get().equals(modifier);
		}
	}
}

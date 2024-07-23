package twilightforest.advancements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.alchemy.Potion;
import twilightforest.init.TFAdvancements;

import java.util.Optional;

public class DrinkFromFlaskTrigger extends SimpleCriterionTrigger<DrinkFromFlaskTrigger.TriggerInstance> {

	@Override
	public Codec<DrinkFromFlaskTrigger.TriggerInstance> codec() {
		return DrinkFromFlaskTrigger.TriggerInstance.CODEC;
	}

	public void trigger(ServerPlayer player, int doses, int seconds, Holder<Potion> potion) {
		this.trigger(player, (instance) -> instance.matches(doses, seconds, potion));
	}

	public record TriggerInstance(Optional<ContextAwarePredicate> player, MinMaxBounds.Ints doses, MinMaxBounds.Ints seconds, Holder<Potion> potion) implements SimpleInstance {

		public static final Codec<DrinkFromFlaskTrigger.TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(DrinkFromFlaskTrigger.TriggerInstance::player),
				MinMaxBounds.Ints.CODEC.optionalFieldOf("doses", MinMaxBounds.Ints.between(0, 4)).forGetter(DrinkFromFlaskTrigger.TriggerInstance::doses),
				MinMaxBounds.Ints.CODEC.optionalFieldOf("seconds", MinMaxBounds.Ints.exactly(8)).forGetter(DrinkFromFlaskTrigger.TriggerInstance::seconds),
				BuiltInRegistries.POTION.holderByNameCodec().fieldOf("potion").forGetter(DrinkFromFlaskTrigger.TriggerInstance::potion))
			.apply(instance, DrinkFromFlaskTrigger.TriggerInstance::new));

		public boolean matches(int doses, int seconds, Holder<Potion> potion) {
			return this.doses().matches(doses) && this.seconds().matches(seconds) && this.potion() == potion;
		}

		public static Criterion<DrinkFromFlaskTrigger.TriggerInstance> drankPotion(int doses, MinMaxBounds.Ints seconds, Holder<Potion> potion) {
			return TFAdvancements.DRINK_FROM_FLASK.get().createCriterion(new TriggerInstance(Optional.empty(), MinMaxBounds.Ints.exactly(doses), seconds, potion));
		}

		public static Criterion<DrinkFromFlaskTrigger.TriggerInstance> drankPotion(MinMaxBounds.Ints doses, MinMaxBounds.Ints seconds, Holder<Potion> potion) {
			return TFAdvancements.DRINK_FROM_FLASK.get().createCriterion(new TriggerInstance(Optional.empty(), doses, seconds, potion));
		}
	}
}

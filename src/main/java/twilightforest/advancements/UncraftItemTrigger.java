package twilightforest.advancements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import twilightforest.init.TFAdvancements;

import java.util.Optional;

public class UncraftItemTrigger extends SimpleCriterionTrigger<UncraftItemTrigger.TriggerInstance> {

	@Override
	public Codec<UncraftItemTrigger.TriggerInstance> codec() {
		return UncraftItemTrigger.TriggerInstance.CODEC;
	}

	public void trigger(ServerPlayer player, ItemStack stack) {
		this.trigger(player, (instance) -> instance.matches(stack));
	}

	public record TriggerInstance(Optional<ContextAwarePredicate> player, Optional<ItemPredicate> item) implements SimpleInstance {

		public static final Codec<UncraftItemTrigger.TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
						ExtraCodecs.strictOptionalField(EntityPredicate.ADVANCEMENT_CODEC, "player").forGetter(UncraftItemTrigger.TriggerInstance::player),
						ExtraCodecs.strictOptionalField(ItemPredicate.CODEC, "item").forGetter(UncraftItemTrigger.TriggerInstance::item))
				.apply(instance, UncraftItemTrigger.TriggerInstance::new));

		public static Criterion<UncraftItemTrigger.TriggerInstance> uncraftedItem(ItemPredicate predicate) {
			return TFAdvancements.UNCRAFT_ITEM.get().createCriterion(new UncraftItemTrigger.TriggerInstance(Optional.empty(), Optional.of(predicate)));
		}

		public static Criterion<UncraftItemTrigger.TriggerInstance> uncraftedItem(ItemLike item) {
			return uncraftedItem(ItemPredicate.Builder.item().of(item).build());
		}

		public boolean matches(ItemStack item) {
			return this.item.isEmpty() || this.item.get().matches(item);
		}
	}
}

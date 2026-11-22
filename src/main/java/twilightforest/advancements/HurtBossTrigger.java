package twilightforest.advancements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.advancements.triggers.SimpleCriterionTrigger;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import twilightforest.init.TFAdvancements;

import java.util.Optional;

public class HurtBossTrigger extends SimpleCriterionTrigger<HurtBossTrigger.TriggerInstance> {

	@Override
	public Codec<HurtBossTrigger.TriggerInstance> codec() {
		return HurtBossTrigger.TriggerInstance.CODEC;
	}

	public void trigger(ServerPlayer player, Entity hurt) {
		LootContext entity = EntityPredicate.createContext(player, hurt);
		this.trigger(player, (instance) -> instance.matches(entity));
	}

	public record TriggerInstance(Optional<Holder<LootItemCondition>> player, Optional<Holder<LootItemCondition>> hurt) implements SimpleInstance {

		public static final Codec<HurtBossTrigger.TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				LootItemCondition.CODEC.optionalFieldOf("player").forGetter(HurtBossTrigger.TriggerInstance::player),
				LootItemCondition.CODEC.optionalFieldOf("hurt_entity").forGetter(HurtBossTrigger.TriggerInstance::hurt))
			.apply(instance, HurtBossTrigger.TriggerInstance::new));

		public boolean matches(LootContext hurt) {
			return this.hurt.isEmpty() || this.hurt.get().value().test(hurt);
		}

		public static Criterion<HurtBossTrigger.TriggerInstance> hurtBoss(EntityPredicate.Builder hurt) {
			return TFAdvancements.HURT_BOSS.get().createCriterion(new TriggerInstance(Optional.empty(), Optional.of(EntityPredicate.wrap(hurt.build()))));
		}
	}
}

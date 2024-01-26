package twilightforest.advancements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
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

	public record TriggerInstance(Optional<ContextAwarePredicate> player, Optional<ContextAwarePredicate> hurt) implements SimpleInstance {

		public static final Codec<HurtBossTrigger.TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
						ExtraCodecs.strictOptionalField(EntityPredicate.ADVANCEMENT_CODEC, "player").forGetter(HurtBossTrigger.TriggerInstance::player),
						ExtraCodecs.strictOptionalField(EntityPredicate.ADVANCEMENT_CODEC, "hurt_entity").forGetter(HurtBossTrigger.TriggerInstance::hurt))
				.apply(instance, HurtBossTrigger.TriggerInstance::new));

		public boolean matches(LootContext hurt) {
			return this.hurt.isEmpty() || this.hurt.get().matches(hurt);
		}

		public static Criterion<HurtBossTrigger.TriggerInstance> hurtBoss(EntityPredicate.Builder hurt) {
			return TFAdvancements.HURT_BOSS.get().createCriterion(new TriggerInstance(Optional.empty(), Optional.of(EntityPredicate.wrap(hurt.build()))));
		}
	}
}

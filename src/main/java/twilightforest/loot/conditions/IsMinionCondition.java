package twilightforest.loot.conditions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import twilightforest.entity.monster.CarminiteGhastling;
import twilightforest.init.TFLoot;

import javax.annotation.Nonnull;

public record IsMinionCondition(boolean inverse) implements LootItemCondition {

	public static final Codec<IsMinionCondition> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codec.BOOL.fieldOf("inverse").forGetter(o -> o.inverse)).apply(instance, IsMinionCondition::new));

	@Override
	public LootItemConditionType getType() {
		return TFLoot.IS_MINION.get();
	}

	@Override
	public boolean test(@Nonnull LootContext context) {
		return context.getParamOrNull(LootContextParams.THIS_ENTITY) instanceof CarminiteGhastling ghastling && ghastling.isMinion() == !inverse;
	}

	public static Builder builder(boolean inverse) {
		return () -> new IsMinionCondition(inverse);
	}
}

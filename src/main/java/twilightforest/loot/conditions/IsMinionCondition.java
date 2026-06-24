package twilightforest.loot.conditions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import twilightforest.entity.monster.CarminiteGhastling;

import javax.annotation.Nonnull;

public record IsMinionCondition(boolean inverse) implements LootItemCondition {

	public static final MapCodec<IsMinionCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(Codec.BOOL.fieldOf("inverse").forGetter(o -> o.inverse)).apply(instance, IsMinionCondition::new));

	@Override
	public MapCodec<? extends LootItemCondition> codec() {
		return CODEC;
	}

	@Override
	public boolean test(@Nonnull LootContext context) {
		return context.getOptionalParameter(LootContextParams.THIS_ENTITY) instanceof CarminiteGhastling ghastling && ghastling.isMinion() == !inverse;
	}

	public static Builder builder(boolean inverse) {
		return () -> new IsMinionCondition(inverse);
	}
}

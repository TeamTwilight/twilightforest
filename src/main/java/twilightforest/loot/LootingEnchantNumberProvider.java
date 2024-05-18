package twilightforest.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.providers.number.LootNumberProviderType;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
import twilightforest.init.TFLoot;

import java.util.Set;

public record LootingEnchantNumberProvider(NumberProvider baseValue) implements NumberProvider {
	public static final MapCodec<LootingEnchantNumberProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			NumberProviders.CODEC.fieldOf("base_value").forGetter(LootingEnchantNumberProvider::baseValue))
		.apply(instance, LootingEnchantNumberProvider::new)
	);

	@Override
	public LootNumberProviderType getType() {
		return TFLoot.LOOTING_ROLLS.get();
	}

	public static LootingEnchantNumberProvider applyLootingLevelTo(NumberProvider baseValue) {
		return new LootingEnchantNumberProvider(baseValue);
	}

	@Override
	public float getFloat(LootContext context) {
		if (context.getParamOrNull(LootContextParams.KILLER_ENTITY) instanceof LivingEntity) return context.getLootingModifier() + this.baseValue.getFloat(context);
		return this.baseValue.getFloat(context);
	}

	/**
	 * Get the parameters used by this object.
	 */
	@Override
	public Set<LootContextParam<?>> getReferencedContextParams() {
		return this.baseValue.getReferencedContextParams();
	}
}

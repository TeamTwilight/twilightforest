package twilightforest.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProvider;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProviders;

public class LootingEnchantNumberProvider implements ContextFloatProvider {
	public static final MapCodec<LootingEnchantNumberProvider> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			Enchantment.CODEC.fieldOf("enchantment").forGetter(provider -> provider.enchantment),
			ContextFloatProviders.DIRECT_CODEC.fieldOf("base_value").forGetter(provider -> provider.baseValue))
		.apply(instance, LootingEnchantNumberProvider::new)
	);

	private final Holder<Enchantment> enchantment;
	private final ContextFloatProvider baseValue;

	protected LootingEnchantNumberProvider(Holder<Enchantment> enchantment, ContextFloatProvider baseValue) {
		this.enchantment = enchantment;
		this.baseValue = baseValue;
	}

	@Override
	public MapCodec<? extends ContextFloatProvider> codec() {
		return MAP_CODEC;
	}

	public static LootingEnchantNumberProvider applyLootingLevelTo(HolderGetter<Enchantment> enchantments, ContextFloatProvider baseValue) {
		return new LootingEnchantNumberProvider(enchantments.getOrThrow(Enchantments.LOOTING), baseValue);
	}

	@Override
	public float getFloatUnsafe(LootContext lootContext) throws ArithmeticException {
		if (lootContext.getOptional(LootContextParams.ATTACKING_ENTITY) instanceof LivingEntity living) {
			return EnchantmentHelper.getEnchantmentLevel(enchantment, living) + baseValue.getFloat(lootContext);
		}
		return baseValue.getFloat(lootContext);
	}

	@Override
	public void validate(ValidationContext validationContext) {
		baseValue.validate(validationContext);
	}
}

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
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;

public class LootingEnchantNumberProvider implements ContextIntProvider {
	public static final MapCodec<LootingEnchantNumberProvider> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			Enchantment.CODEC.fieldOf("enchantment").forGetter(provider -> provider.enchantment),
			ContextIntProviders.DIRECT_CODEC.fieldOf("base_value").forGetter(provider -> provider.baseValue))
		.apply(instance, LootingEnchantNumberProvider::new)
	);

	private final Holder<Enchantment> enchantment;
	private final ContextIntProvider baseValue;

	protected LootingEnchantNumberProvider(Holder<Enchantment> enchantment, ContextIntProvider baseValue) {
		this.enchantment = enchantment;
		this.baseValue = baseValue;
	}

	@Override
	public MapCodec<? extends ContextIntProvider> codec() {
		return MAP_CODEC;
	}

	public static Holder<ContextIntProvider> applyLootingLevelTo(HolderGetter<Enchantment> enchantments, ContextIntProvider baseValue) {
		return Holder.direct(new LootingEnchantNumberProvider(enchantments.getOrThrow(Enchantments.LOOTING), baseValue));
	}

	@Override
	public int getIntUnsafe(LootContext lootContext) throws ArithmeticException {
		if (lootContext.getOptional(LootContextParams.ATTACKING_ENTITY) instanceof LivingEntity living) {
			return EnchantmentHelper.getEnchantmentLevel(enchantment, living) + baseValue.getInt(lootContext);
		}
		return baseValue.getInt(lootContext);
	}

	@Override
	public void validate(ValidationContext validationContext) {
		baseValue.validate(validationContext);
	}
}

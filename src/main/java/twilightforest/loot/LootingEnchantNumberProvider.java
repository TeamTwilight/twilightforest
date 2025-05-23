package twilightforest.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.providers.number.LootNumberProviderType;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
import twilightforest.init.TFLoot;

import java.util.Set;

public class LootingEnchantNumberProvider implements NumberProvider {
	public static final MapCodec<LootingEnchantNumberProvider> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			Enchantment.CODEC.fieldOf("enchantment").forGetter(provider -> provider.enchantment),
			NumberProviders.CODEC.fieldOf("base_value").forGetter(provider -> provider.baseValue))
		.apply(instance, LootingEnchantNumberProvider::new)
	);

	private final Holder<Enchantment> enchantment;
	private final NumberProvider baseValue;

	protected LootingEnchantNumberProvider(Holder<Enchantment> enchantment, NumberProvider baseValue) {
		this.enchantment = enchantment;
		this.baseValue = baseValue;
	}

	@Override
	public LootNumberProviderType getType() {
		return TFLoot.LOOTING_ROLLS.get();
	}

	public static LootingEnchantNumberProvider applyLootingLevelTo(HolderLookup.Provider provider, NumberProvider baseValue) {
		HolderLookup.RegistryLookup<Enchantment> registrylookup = provider.lookupOrThrow(Registries.ENCHANTMENT);
		return new LootingEnchantNumberProvider(registrylookup.getOrThrow(Enchantments.LOOTING), baseValue);
	}

	@Override
	public float getFloat(LootContext context) {
		if (context.getOptionalParameter(LootContextParams.ATTACKING_ENTITY) instanceof LivingEntity living) {
			return EnchantmentHelper.getEnchantmentLevel(this.enchantment, living) + this.baseValue.getFloat(context);
		}
		return this.baseValue.getFloat(context);
	}

	@Override
	public Set<ContextKey<?>> getReferencedContextParams() {
		return this.baseValue.getReferencedContextParams();
	}
}

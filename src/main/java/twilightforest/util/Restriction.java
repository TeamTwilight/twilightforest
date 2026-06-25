package twilightforest.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.world.level.levelgen.structure.Structure;
import twilightforest.TFRegistries;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

/**
 * @param hintStructureKey ResourceKey of a structure that extends the StructureHints interface, so that the correct hint book mob spawns
 * @param enforcement      ResourceKey of the Enforcement that gets used whenever a player is in a restricted biome
 * @param multiplier       A value dictating how adverse the negative effect of a restricted area should be
 * @param lockedBiomeToast Item that is used as an icon for the notification that tells the player that the area is locked
 * @param advancements     List of advancements that are required to make a biome no longer restricted
 */

public record Restriction(@Nullable ResourceKey<Structure> hintStructureKey, ResourceKey<Enforcement> enforcement,
						  float multiplier, @Nullable ItemStack lockedBiomeToast, List<Identifier> advancements) {

	// Custom codec that avoids Item.CODEC_WITH_BOUND_COMPONENTS validation
	// This is needed because ItemStack.CODEC uses CODEC_WITH_BOUND_COMPONENTS which fails
	// during registry loading when item components haven't been bound yet
	private static final Codec<ItemStack> SAFE_ITEM_CODEC = RecordCodecBuilder.<ItemStack>create(instance -> instance.group(
		Item.CODEC.fieldOf("id").forGetter(stack -> stack.typeHolder()),
		ExtraCodecs.intRange(1, 99).fieldOf("count").orElse(1).forGetter(ItemStack::getCount),
		DataComponentPatch.CODEC.optionalFieldOf("components", DataComponentPatch.EMPTY).forGetter(ItemStack::getComponentsPatch)
	).apply(instance, (item, count, patch) -> createItemStackSafely(item, count, patch)));

	@SuppressWarnings("deprecation")
	private static ItemStack createItemStackSafely(Holder<Item> item, int count, DataComponentPatch patch) {
		try {
			java.lang.reflect.Constructor<ItemStack> constructor = ItemStack.class.getDeclaredConstructor(Holder.class, int.class, PatchedDataComponentMap.class);
			constructor.setAccessible(true);
			return constructor.newInstance(item, count, PatchedDataComponentMap.fromPatch(DataComponentMap.EMPTY, patch));
		} catch (Exception e) {
			throw new RuntimeException("Failed to create ItemStack without components validation", e);
		}
	}

	public static final Codec<Restriction> CODEC = RecordCodecBuilder.create((recordCodecBuilder) -> recordCodecBuilder.group(
		ResourceKey.codec(Registries.STRUCTURE).optionalFieldOf("structure_key").forGetter((restriction) -> Optional.ofNullable(restriction.hintStructureKey())),
		ResourceKey.codec(TFRegistries.Keys.ENFORCEMENT).fieldOf("enforcement").forGetter(Restriction::enforcement),
		Codec.FLOAT.fieldOf("multiplier").forGetter(Restriction::multiplier),
		SAFE_ITEM_CODEC.optionalFieldOf("locked_biome_toast").forGetter((restriction) -> Optional.ofNullable(restriction.lockedBiomeToast())),
		ExtraCodecs.nonEmptyList(Identifier.CODEC.listOf()).fieldOf("advancements").forGetter(Restriction::advancements)
	).apply(recordCodecBuilder, Restriction::create));

	@SuppressWarnings("OptionalUsedAsFieldOrParameterType") // Vanilla does this too
	private static Restriction create(Optional<ResourceKey<Structure>> hintStructureKey, ResourceKey<Enforcement> enforcer, float multiplier, Optional<ItemStack> lockedBiomeToast, List<Identifier> advancements) {
		return new Restriction(hintStructureKey.orElse(null), enforcer, multiplier, lockedBiomeToast.orElse(null), advancements);
	}

	public static Optional<Restriction> getRestrictionForBiome(Biome biome, Entity entity) {
		if (!(entity instanceof Player player))
			return Optional.empty();

		RegistryAccess access = entity.level().registryAccess();
		Identifier biomeLocation = access.lookupOrThrow(Registries.BIOME).getKey(biome);
		if (biomeLocation == null)
			return Optional.empty();

		Optional<Registry<Restriction>> restrictionsRegistry = access.lookup(TFRegistries.Keys.RESTRICTIONS);
		if (restrictionsRegistry.isEmpty())
			return Optional.empty();

		Holder<Restriction> restrictions = restrictionsRegistry.get().get(biomeLocation).orElse(null);
		if (restrictions == null || PlayerHelper.doesPlayerHaveRequiredAdvancements(player, restrictions.value().advancements())) {
			return Optional.empty();
		}

		return Optional.of(restrictions.value());
	}

	public static boolean isBiomeSafeFor(Biome biome, Entity entity) {
		return getRestrictionForBiome(biome, entity).isEmpty();
	}
}

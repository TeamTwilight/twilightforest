package twilightforest.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.jetbrains.annotations.NotNull;
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
public record Restriction(@Nullable ResourceKey<@NotNull Structure> hintStructureKey, ResourceKey<@NotNull Enforcement> enforcement,
						  float multiplier, @Nullable ItemStack lockedBiomeToast, List<Identifier> advancements) {
	//TODO: refactor this CODEC
	public static final Codec<Restriction> CODEC = RecordCodecBuilder.create((recordCodecBuilder) -> recordCodecBuilder.group(
		ResourceKey.codec(Registries.STRUCTURE).optionalFieldOf("structure_key").forGetter((restriction) -> Optional.ofNullable(restriction.hintStructureKey())),
		ResourceKey.codec(TFRegistries.Keys.ENFORCEMENT).fieldOf("enforcement").forGetter(Restriction::enforcement),
		Codec.FLOAT.fieldOf("multiplier").forGetter(Restriction::multiplier),
		Codec.PASSTHROUGH.optionalFieldOf("locked_biome_toast").xmap(
			optionalDynamic -> optionalDynamic.map(dynamic -> {
				JsonElement json = (JsonElement) dynamic.getValue();
				DynamicOps<JsonElement> ops = RegistryOps.create(
					JsonOps.INSTANCE,
					RegistryAccess.EMPTY
				);
				return ItemStack.CODEC.parse(ops, json).result().orElse(ItemStack.EMPTY);
			}),
			optionalStack -> optionalStack.map(stack -> {
				DynamicOps<JsonElement> ops = RegistryOps.create(
					JsonOps.INSTANCE,
					RegistryAccess.EMPTY
				);
				JsonElement json = ItemStack.CODEC.encodeStart(ops, stack).result().orElse(new JsonObject());
				return new Dynamic<>(JsonOps.INSTANCE, json);
			})
		).forGetter((restriction) -> Optional.ofNullable(restriction.lockedBiomeToast())),

		ExtraCodecs.nonEmptyList(Identifier.CODEC.listOf()).fieldOf("advancements").forGetter(Restriction::advancements)
	).apply(recordCodecBuilder, Restriction::create));


	public static Optional<Restriction> getRestrictionForBiome(Biome biome, Entity entity) {
		if (!(entity instanceof Player player))
			return Optional.empty();

		RegistryAccess access = entity.level().registryAccess();
		Identifier biomeLocation = access.lookupOrThrow(Registries.BIOME).getKey(biome);
		if (biomeLocation == null)
			return Optional.empty();

		Optional<Registry<@NotNull Restriction>> restrictionsRegistry = access.lookup(TFRegistries.Keys.RESTRICTIONS);
		if (restrictionsRegistry.isEmpty())
			return Optional.empty();

		if (restrictionsRegistry.get().get(biomeLocation).isPresent()) {
			Restriction restrictions = restrictionsRegistry.get().get(biomeLocation).get().value();
			if (PlayerHelper.doesPlayerHaveRequiredAdvancements(player, restrictions.advancements())) {
				return Optional.empty();
			}

			return Optional.of(restrictions);
		}

		return Optional.empty();
	}

	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	private static Restriction create(Optional<ResourceKey<@NotNull Structure>> hintStructureKey, ResourceKey<@NotNull Enforcement> enforcer, float multiplier, Optional<ItemStack> lockedBiomeToast, List<Identifier> advancements) {
		return new Restriction(hintStructureKey.orElse(null), enforcer, multiplier, lockedBiomeToast.orElse(null), advancements);
	}

	public static boolean isBiomeSafeFor(Biome biome, Entity entity) {
		return getRestrictionForBiome(biome, entity).isEmpty();
	}
}

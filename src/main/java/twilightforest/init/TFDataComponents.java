package twilightforest.init;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Unit;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.components.item.*;
import twilightforest.entity.MagicPaintingVariant;
import twilightforest.init.custom.MagicPaintingVariants;

import java.util.UUID;

public class TFDataComponents {
	public static final DeferredRegister<DataComponentType<?>> COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, TwilightForestMod.ID);

	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Unit>> EMPERORS_CLOTH = COMPONENTS.register("emperors_cloth", () -> DataComponentType.<Unit>builder().persistent(Unit.CODEC).networkSynchronized(Unit.STREAM_CODEC).build());
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<PotionFlaskComponent>> POTION_FLASK_CONTENTS = COMPONENTS.register("flask_contents", () -> DataComponentType.<PotionFlaskComponent>builder().persistent(PotionFlaskComponent.CODEC).networkSynchronized(PotionFlaskComponent.STREAM_CODEC).build());
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Unit>> INFINITE_GLASS_SWORD = COMPONENTS.register("infinite_glass_sword", () -> DataComponentType.<Unit>builder().persistent(Unit.CODEC).networkSynchronized(Unit.STREAM_CODEC).build());
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<UUID>> THROWN_PROJECTILE = COMPONENTS.register("thrown_projectile", () -> DataComponentType.<UUID>builder().persistent(UUIDUtil.CODEC).networkSynchronized(UUIDUtil.STREAM_CODEC).build());
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> EXPERIMENT_115_VARIANTS = COMPONENTS.register("e115_variant", () -> DataComponentType.<String>builder().persistent(Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8).build());
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<SkullCandles>> SKULL_CANDLES = COMPONENTS.register("skull_candles", () -> DataComponentType.<SkullCandles>builder().persistent(SkullCandles.CODEC).networkSynchronized(SkullCandles.STREAM_CODEC).build());
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<CandelabraData>> CANDELABRA_DATA = COMPONENTS.register("candelabra_data", () -> DataComponentType.<CandelabraData>builder().persistent(CandelabraData.CODEC).build());
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Holder<MagicPaintingVariant>>> MAGIC_PAINTING_VARIANT = COMPONENTS.register("magic_painting_variant", () -> DataComponentType.<Holder<MagicPaintingVariant>>builder().persistent(MagicPaintingVariants.CODEC).networkSynchronized(MagicPaintingVariants.STREAM_CODEC).build());
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Unit>> TRANSLATABLE_BOOK = COMPONENTS.register("translatable_book", () -> DataComponentType.<Unit>builder().persistent(Unit.CODEC).networkSynchronized(Unit.STREAM_CODEC).build());
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<JarLid>> JAR_LID = register("jar_lid", JarLid.CODEC);
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> CASKET_DAMAGE = COMPONENTS.register("casket_damage", () -> DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT).build());

	public static final DeferredHolder<DataComponentType<?>, DataComponentType<OreScannerComponent>> ORE_SCANNING = register("ore_scanner", OreScannerComponent.CODEC);
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<OreScannerData>> ORE_DATA = register("ore_data", OreScannerData.CODEC, OreScannerData.STREAM_CODEC);
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> ORE_LOADING = COMPONENTS.register("ore_loading", () -> DataComponentType.<Integer>builder().persistent(ExtraCodecs.NON_NEGATIVE_INT.orElse(0)).networkSynchronized(ByteBufCodecs.VAR_INT).cacheEncoding().build());
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> ORE_RANGE = COMPONENTS.register("ore_range", () -> DataComponentType.<Integer>builder().persistent(ExtraCodecs.NON_NEGATIVE_INT.orElse(1)).networkSynchronized(ByteBufCodecs.VAR_INT).cacheEncoding().build());
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Block>> ORE_FILTER = COMPONENTS.register("ore_filter", () -> DataComponentType.<Block>builder().persistent(BuiltInRegistries.BLOCK.byNameCodec().orElse(Blocks.AIR)).networkSynchronized(ByteBufCodecs.registry(Registries.BLOCK)).cacheEncoding().build());

	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Unit>> IS_TRAVELLERS_GEAR = COMPONENTS.register("travellers_armor", () -> DataComponentType.<Unit>builder().persistent(Unit.CODEC).networkSynchronized(Unit.STREAM_CODEC).cacheEncoding().build());
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<ItemAttributeModifiers>> STORED_BROKEN_ATTRIBUTES = COMPONENTS.register("stored_broken_attributes", () -> DataComponentType.<ItemAttributeModifiers>builder().persistent(ItemAttributeModifiers.CODEC).networkSynchronized(ItemAttributeModifiers.STREAM_CODEC).cacheEncoding().build());

	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Unit>> TRAVELLERS_HAS_CHESTPLATE = COMPONENTS.register("has_travellers_chestplate", () -> DataComponentType.<Unit>builder().persistent(Unit.CODEC).networkSynchronized(Unit.STREAM_CODEC).cacheEncoding().build());
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Unit>> TRAVELLERS_HAS_GLOVES = COMPONENTS.register("has_travellers_gloves", () -> DataComponentType.<Unit>builder().persistent(Unit.CODEC).networkSynchronized(Unit.STREAM_CODEC).cacheEncoding().build());
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Unit>> TRAVELLERS_HAS_BELT = COMPONENTS.register("has_travellers_belt", () -> DataComponentType.<Unit>builder().persistent(Unit.CODEC).networkSynchronized(Unit.STREAM_CODEC).cacheEncoding().build());
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Unit>> TRAVELLERS_HAS_WINGS = COMPONENTS.register("has_travellers_wings", () -> DataComponentType.<Unit>builder().persistent(Unit.CODEC).networkSynchronized(Unit.STREAM_CODEC).cacheEncoding().build());
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Unit>> TRAVELLERS_HAS_BOOTS = COMPONENTS.register("has_travellers_boots", () -> DataComponentType.<Unit>builder().persistent(Unit.CODEC).networkSynchronized(Unit.STREAM_CODEC).cacheEncoding().build());

	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Float>> AUTO_REPAIR_PROBABILITY = COMPONENTS.register("auto_repair_probability", () -> DataComponentType.<Float>builder().persistent(ExtraCodecs.POSITIVE_FLOAT).networkSynchronized(ByteBufCodecs.FLOAT).cacheEncoding().build());
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Float>> ZOOM_ABILITY_MODIFIER = COMPONENTS.register("zoom_ability_modifier", () -> DataComponentType.<Float>builder().persistent(ExtraCodecs.POSITIVE_FLOAT).networkSynchronized(ByteBufCodecs.FLOAT).cacheEncoding().build());
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Unit>> RED_THREAD_VISION = COMPONENTS.register("red_thread_vision", () -> DataComponentType.<Unit>builder().persistent(Unit.CODEC).networkSynchronized(Unit.STREAM_CODEC).cacheEncoding().build());
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Unit>> STEALTH_CROUCHING = COMPONENTS.register("stealth_crouching", () -> DataComponentType.<Unit>builder().persistent(Unit.CODEC).networkSynchronized(Unit.STREAM_CODEC).cacheEncoding().build());
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Unit>> ARROW_MAGNETISM = COMPONENTS.register("arrow_magnetism", () -> DataComponentType.<Unit>builder().persistent(Unit.CODEC).networkSynchronized(Unit.STREAM_CODEC).cacheEncoding().build());
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Float>> EFFICIENT_EATER = COMPONENTS.register("efficient_eater", () -> DataComponentType.<Float>builder().persistent(Codec.FLOAT).networkSynchronized(ByteBufCodecs.FLOAT).cacheEncoding().build());
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Float>> PERFECT_DODGE_PROBABILITY = COMPONENTS.register("perfect_dodge_probability", () -> DataComponentType.<Float>builder().persistent(ExtraCodecs.POSITIVE_FLOAT).networkSynchronized(ByteBufCodecs.FLOAT).cacheEncoding().build());
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> HASTE_AMPLIFIER = COMPONENTS.register("haste_amplifier", () -> DataComponentType.<Integer>builder().persistent(ExtraCodecs.UNSIGNED_BYTE).networkSynchronized(ByteBufCodecs.INT).cacheEncoding().build());
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Unit>> SWAP_HOTBAR_ABILITY = COMPONENTS.register("swap_hotbar_ability", () -> DataComponentType.<Unit>builder().persistent(Unit.CODEC).networkSynchronized(Unit.STREAM_CODEC).cacheEncoding().build());
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Unit>> SWAP_HOTBAR_MODIFIER = COMPONENTS.register("swap_hotbar_modifier", () -> DataComponentType.<Unit>builder().persistent(Unit.CODEC).networkSynchronized(Unit.STREAM_CODEC).cacheEncoding().build());
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> HIGH_JUMP_AMPLIFIER = COMPONENTS.register("high_jump_amplifier", () -> DataComponentType.<Integer>builder().persistent(ExtraCodecs.UNSIGNED_BYTE).networkSynchronized(ByteBufCodecs.INT).cacheEncoding().build());
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Float>> GRADUALLY_GLIDING_MULTIPLIER = COMPONENTS.register("gradually_gliding_multiplier", () -> DataComponentType.<Float>builder().persistent(ExtraCodecs.POSITIVE_FLOAT).networkSynchronized(ByteBufCodecs.FLOAT).cacheEncoding().build());
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Float>> AGILE_RANGER_MODIFIER = COMPONENTS.register("agile_ranger_modifier", () -> DataComponentType.<Float>builder().persistent(ExtraCodecs.POSITIVE_FLOAT).networkSynchronized(ByteBufCodecs.FLOAT).cacheEncoding().build());
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Unit>> DOUBLE_JUMP = COMPONENTS.register("double_jump", () -> DataComponentType.<Unit>builder().persistent(Unit.CODEC).networkSynchronized(Unit.STREAM_CODEC).cacheEncoding().build());
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Long>> SIDESTEP_COOLDOWN = COMPONENTS.register("sidestep_cooldown", () -> DataComponentType.<Long>builder().persistent(Codec.LONG).networkSynchronized(ByteBufCodecs.VAR_LONG).cacheEncoding().build());
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Double>> STRAIGHT_AHEAD_MULTIPLIER = COMPONENTS.register("straight_ahead_multiplier", () -> DataComponentType.<Double>builder().persistent(Codec.DOUBLE).networkSynchronized(ByteBufCodecs.DOUBLE).cacheEncoding().build());
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Float>> SLIMY_SOLES_COEFFICIENT = COMPONENTS.register("slimy_soles_coefficient", () -> DataComponentType.<Float>builder().persistent(ExtraCodecs.POSITIVE_FLOAT).networkSynchronized(ByteBufCodecs.FLOAT).cacheEncoding().build());
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Unit>> WATER_WALK = COMPONENTS.register("water_walk", () -> DataComponentType.<Unit>builder().persistent(Unit.CODEC).networkSynchronized(Unit.STREAM_CODEC).cacheEncoding().build());
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Unit>> ALL_NIGHT_GOGGLES = COMPONENTS.register("all_night_goggles", () -> DataComponentType.<Unit>builder().persistent(Unit.CODEC).networkSynchronized(Unit.STREAM_CODEC).cacheEncoding().build());
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<ItemDisplayContents>> ITEM_DISPLAY = COMPONENTS.register("item_display", () -> DataComponentType.<ItemDisplayContents>builder().persistent(ItemDisplayContents.CODEC).networkSynchronized(ItemDisplayContents.STREAM_CODEC).cacheEncoding().build());
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Unit>> UNRESTRAINED = COMPONENTS.register("unrestrained", () -> DataComponentType.<Unit>builder().persistent(Unit.CODEC).networkSynchronized(Unit.STREAM_CODEC).cacheEncoding().build());

	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Unit>> SWIFT_SWIM = COMPONENTS.register("swift_swim", () -> DataComponentType.<Unit>builder().persistent(Unit.CODEC).networkSynchronized(Unit.STREAM_CODEC).cacheEncoding().build());
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Unit>> HIGH_STEP = COMPONENTS.register("high_step", () -> DataComponentType.<Unit>builder().persistent(Unit.CODEC).networkSynchronized(Unit.STREAM_CODEC).cacheEncoding().build());
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Unit>> AQUATIC_AGILITY = COMPONENTS.register("aquatic_agility", () -> DataComponentType.<Unit>builder().persistent(Unit.CODEC).networkSynchronized(Unit.STREAM_CODEC).cacheEncoding().build());

	private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name, final Codec<T> codec) {
		return register(name, codec, null);
	}

	private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name, final Codec<T> codec, @Nullable final StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
		if (streamCodec == null) {
			return COMPONENTS.register(name, () -> DataComponentType.<T>builder().persistent(codec).build());
		} else {
			return COMPONENTS.register(name, () -> DataComponentType.<T>builder().persistent(codec).networkSynchronized(streamCodec).build());
		}
	}
}

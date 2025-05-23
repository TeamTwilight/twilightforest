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

	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Unit>> EMPERORS_CLOTH = COMPONENTS.register("emperors_cloth", () -> DataComponentType.<Unit>builder().persistent(Codec.unit(Unit.INSTANCE)).networkSynchronized(StreamCodec.unit(Unit.INSTANCE)).build());
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<PotionFlaskComponent>> POTION_FLASK_CONTENTS = COMPONENTS.register("flask_contents", () -> DataComponentType.<PotionFlaskComponent>builder().persistent(PotionFlaskComponent.CODEC).networkSynchronized(PotionFlaskComponent.STREAM_CODEC).build());
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Unit>> INFINITE_GLASS_SWORD = COMPONENTS.register("infinite_glass_sword", () -> DataComponentType.<Unit>builder().persistent(Codec.unit(Unit.INSTANCE)).networkSynchronized(StreamCodec.unit(Unit.INSTANCE)).build());
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<UUID>> THROWN_PROJECTILE = COMPONENTS.register("thrown_projectile", () -> DataComponentType.<UUID>builder().persistent(UUIDUtil.CODEC).networkSynchronized(UUIDUtil.STREAM_CODEC).build());
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> EXPERIMENT_115_VARIANTS = COMPONENTS.register("e115_variant", () -> DataComponentType.<String>builder().persistent(Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8).build());
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<SkullCandles>> SKULL_CANDLES = COMPONENTS.register("skull_candles", () -> DataComponentType.<SkullCandles>builder().persistent(SkullCandles.CODEC).networkSynchronized(SkullCandles.STREAM_CODEC).build());
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<CandelabraData>> CANDELABRA_DATA = COMPONENTS.register("candelabra_data", () -> DataComponentType.<CandelabraData>builder().persistent(CandelabraData.CODEC).build());
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Holder<MagicPaintingVariant>>> MAGIC_PAINTING_VARIANT = COMPONENTS.register("magic_painting_variant", () -> DataComponentType.<Holder<MagicPaintingVariant>>builder().persistent(MagicPaintingVariants.CODEC).networkSynchronized(MagicPaintingVariants.STREAM_CODEC).build());
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Unit>> TRANSLATABLE_BOOK = COMPONENTS.register("translatable_book", () -> DataComponentType.<Unit>builder().persistent(Codec.unit(Unit.INSTANCE)).networkSynchronized(StreamCodec.unit(Unit.INSTANCE)).build());
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<JarLid>> JAR_LID = register("jar_lid", JarLid.CODEC);
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> CASKET_DAMAGE = COMPONENTS.register("casket_damage", () -> DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT).build());

	public static final DeferredHolder<DataComponentType<?>, DataComponentType<OreScannerComponent>> ORE_SCANNING = register("ore_scanner", OreScannerComponent.CODEC);
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<OreScannerData>> ORE_DATA = register("ore_data", OreScannerData.CODEC, OreScannerData.STREAM_CODEC);
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> ORE_LOADING = COMPONENTS.register("ore_loading", () -> DataComponentType.<Integer>builder().persistent(ExtraCodecs.NON_NEGATIVE_INT.orElse(0)).networkSynchronized(ByteBufCodecs.VAR_INT).cacheEncoding().build());
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> ORE_RANGE = COMPONENTS.register("ore_range", () -> DataComponentType.<Integer>builder().persistent(ExtraCodecs.NON_NEGATIVE_INT.orElse(1)).networkSynchronized(ByteBufCodecs.VAR_INT).cacheEncoding().build());
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Block>> ORE_FILTER = COMPONENTS.register("ore_filter", () -> DataComponentType.<Block>builder().persistent(BuiltInRegistries.BLOCK.byNameCodec().orElse(Blocks.AIR)).networkSynchronized(ByteBufCodecs.registry(Registries.BLOCK)).cacheEncoding().build());

	private static @NotNull <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name, final Codec<T> codec) {
		return register(name, codec, null);
	}

	private static @NotNull <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name, final Codec<T> codec, @Nullable final StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
		if (streamCodec == null) {
			return COMPONENTS.register(name, () -> DataComponentType.<T>builder().persistent(codec).build());
		} else {
			return COMPONENTS.register(name, () -> DataComponentType.<T>builder().persistent(codec).networkSynchronized(streamCodec).build());
		}
	}
}

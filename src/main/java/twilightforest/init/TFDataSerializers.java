package twilightforest.init;

import net.minecraft.core.Holder;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import twilightforest.TFRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.entity.MagicPaintingVariant;
import twilightforest.entity.passive.DwarfRabbitVariant;
import twilightforest.entity.passive.TinyBirdVariant;

import java.util.List;

public class TFDataSerializers {

	public static final DeferredRegister<EntityDataSerializer<?>> DATA_SERIALIZERS = DeferredRegister.create(NeoForgeRegistries.ENTITY_DATA_SERIALIZERS, TwilightForestMod.ID);

	public static final DeferredHolder<EntityDataSerializer<?>, EntityDataSerializer<List<String>>> STRING_LIST = DATA_SERIALIZERS.register("string_list", () -> EntityDataSerializer.forValueType(ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list())));
	public static final DeferredHolder<EntityDataSerializer<?>, EntityDataSerializer<Holder<DwarfRabbitVariant>>> DWARF_RABBIT_VARIANT = DATA_SERIALIZERS.register("dwarf_rabbit_variant", () -> EntityDataSerializer.forValueType(ByteBufCodecs.holderRegistry(TFRegistries.Keys.DWARF_RABBIT_VARIANT)));
	public static final DeferredHolder<EntityDataSerializer<?>, EntityDataSerializer<Holder<TinyBirdVariant>>> TINY_BIRD_VARIANT = DATA_SERIALIZERS.register("tiny_bird_variant", () -> EntityDataSerializer.forValueType(ByteBufCodecs.holderRegistry(TFRegistries.Keys.TINY_BIRD_VARIANT)));
	public static final DeferredHolder<EntityDataSerializer<?>, EntityDataSerializer<Holder<MagicPaintingVariant>>> MAGIC_PAINTING_VARIANT = DATA_SERIALIZERS.register("magic_painting_variant", () -> EntityDataSerializer.forValueType(ByteBufCodecs.holderRegistry(TFRegistries.Keys.MAGIC_PAINTINGS)));
}

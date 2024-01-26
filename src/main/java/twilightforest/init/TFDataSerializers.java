package twilightforest.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import twilightforest.TFRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.entity.passive.DwarfRabbitVariant;
import twilightforest.entity.passive.TinyBirdVariant;

import java.util.ArrayList;
import java.util.List;

public class TFDataSerializers {

	public static final DeferredRegister<EntityDataSerializer<?>> DATA_SERIALIZERS = DeferredRegister.create(NeoForgeRegistries.ENTITY_DATA_SERIALIZERS, TwilightForestMod.ID);

	public static final DeferredHolder<EntityDataSerializer<?>, EntityDataSerializer<List<String>>> STRING_LIST = DATA_SERIALIZERS.register("string_list", () -> new EntityDataSerializer.ForValueType<>() {
		@Override
		public void write(FriendlyByteBuf buf, List<String> strings) {
			buf.writeCollection(strings, FriendlyByteBuf::writeUtf);
		}

		@Override
		public List<String> read(FriendlyByteBuf buf) {
			return buf.readList(FriendlyByteBuf::readUtf);
		}

		@Override
		public List<String> copy(List<String> strings) {
			return new ArrayList<>(strings);
		}
	});
	public static final DeferredHolder<EntityDataSerializer<?>, EntityDataSerializer<DwarfRabbitVariant>> DWARF_RABBIT_VARIANT = DATA_SERIALIZERS.register("dwarf_rabbit_variant", () -> EntityDataSerializer.simpleId(TFRegistries.DWARF_RABBIT_VARIANT));
	public static final DeferredHolder<EntityDataSerializer<?>, EntityDataSerializer<TinyBirdVariant>> TINY_BIRD_VARIANT = DATA_SERIALIZERS.register("tiny_bird_variant", () -> EntityDataSerializer.simpleId(TFRegistries.TINY_BIRD_VARIANT));
}

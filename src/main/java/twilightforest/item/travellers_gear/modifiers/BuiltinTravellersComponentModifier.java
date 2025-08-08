package twilightforest.item.travellers_gear.modifiers;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public class BuiltinTravellersComponentModifier implements TravellersModifier {
	@Nullable
	private static String TYPE_NAME = null;
	protected final DataComponentType<?> dataComponentType;
	protected final String tooltipTranslationKey;
	protected final ResourceLocation name;

	public BuiltinTravellersComponentModifier(ResourceLocation name, DataComponentType<?> dataComponentType) {
		this.dataComponentType = dataComponentType;
		this.name = name;
		this.tooltipTranslationKey = "travellers_gear.ability." + name.toString().replace(":", ".");
	}

	@Override
	public boolean hasModifier(ItemStack stack) {
		return stack.get(dataComponentType) != null;
	}

	@Override
	public ResourceLocation getName() {
		return name;
	}

	public static void setTypeName(String typeName) {
		TYPE_NAME = typeName;
		TravellersModifierTypes.TYPE_TO_CODEC.put(typeName, MAP_CODEC);
	}

	@Override
	public String getTypeName() {
		TravellersModifierTypes.initialize();
		return TYPE_NAME;
	}

	public static final MapCodec<BuiltinTravellersComponentModifier> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			ResourceLocation.CODEC.fieldOf("name")
				.forGetter(o -> o.name),
			BuiltInRegistries.DATA_COMPONENT_TYPE.byNameCodec()
				.fieldOf("data_component_type")
				.forGetter(o -> o.dataComponentType)
		).apply(instance, BuiltinTravellersComponentModifier::new));
}

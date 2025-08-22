package twilightforest.item.travellers_gear.modifiers;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.ItemStack;

public record BuiltinTravellersComponentModifier(EquipmentSlotGroup group, DataComponentType<?> component) implements TravellersModifier {

	public static final MapCodec<BuiltinTravellersComponentModifier> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		EquipmentSlotGroup.CODEC.fieldOf("equipment_slots").validate(TravellersModifier::validateEquipment).forGetter(BuiltinTravellersComponentModifier::group),
		BuiltInRegistries.DATA_COMPONENT_TYPE.byNameCodec().fieldOf("component").forGetter(BuiltinTravellersComponentModifier::component)
	).apply(instance, BuiltinTravellersComponentModifier::new));

	@Override
	public boolean hasModifier(ItemStack stack) {
		return stack.get(this.component()) != null;
	}

	@Override
	public boolean isAbility() {
		return true;
	}

	@Override
	public MapCodec<? extends TravellersModifier> codec() {
		return CODEC;
	}
}

package twilightforest.item.travellers_gear.modifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.ItemStack;
import twilightforest.TFRegistries;
import twilightforest.init.custom.TravellersModifiersManager;
import twilightforest.item.travellers_gear.TravellersArmorItem;

import java.util.List;
import java.util.function.Function;

public interface TravellersModifier {

	Codec<TravellersModifier> CODEC = TFRegistries.TRAVELLERS_MODIFIER_TYPE.byNameCodec().dispatch(TravellersModifier::codec, Function.identity());

	MapCodec<? extends TravellersModifier> codec();

	EquipmentSlotGroup group();

	boolean hasModifier(ItemStack stack);

	boolean isAbility();

	default List<Component> getDescription() {
		return List.of();
	}

	default String getPrefix() {
		return "travellers_gear.modifier";
	}

	default boolean isActive(ItemStack stack, ResourceKey<TravellersModifier> modifier, boolean spectator) {
		return this.hasModifier(stack) && !spectator && (!TravellersArmorItem.isTravellersArmorAndBroken(stack) || TravellersModifiersManager.ALWAYS_ACTIVE.contains(modifier));
	}

	static DataResult<EquipmentSlotGroup> validateEquipment(EquipmentSlotGroup group) {
		for (EquipmentSlot slot : EquipmentSlot.values()) {
			if (!slot.isArmor() && group.test(slot)) {
				return DataResult.error(() -> "EquipmentSlotGroup must only use armor slots");
			}
		}
		return DataResult.success(group);
	}
}

package twilightforest.item;

import com.google.common.base.Suppliers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

public class WearableLichCrown extends Item implements Equipable {
	private final Supplier<ItemAttributeModifiers> defaultModifiers;

	public WearableLichCrown(Properties properties, final float armor, final float toughness) {
		super(properties);

		this.defaultModifiers = Suppliers.memoize(() -> {
			ResourceLocation attribForEquipSlot = ResourceLocation.withDefaultNamespace("armor." + EquipmentSlot.HEAD.getName());

			ItemAttributeModifiers.Builder attribBuilder = ItemAttributeModifiers.builder();
			attribBuilder.add(Attributes.ARMOR, new AttributeModifier(attribForEquipSlot, armor, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.HEAD);
			if (toughness > 0.0F)
				attribBuilder.add(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(attribForEquipSlot, toughness, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.HEAD);

			return attribBuilder.build();
		});
	}

	@Override
	public EquipmentSlot getEquipmentSlot() {
		return EquipmentSlot.HEAD;
	}

	@SuppressWarnings("deprecation")
	@Override
	public ItemAttributeModifiers getDefaultAttributeModifiers() {
		return this.defaultModifiers.get();
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		return this.swapWithEquipmentSlot(this, level, player, hand);
	}
}

package twilightforest.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.component.TooltipDisplay;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.armor.TFArmorModel;
import twilightforest.client.renderer.armor.TFSimpleArmorRenderer;
import twilightforest.init.TFItems;

import java.util.function.Consumer;

public class ArcticArmorItem extends Item {
	private static final MutableComponent TOOLTIP = Component.translatable("item.twilightforest.arctic_armor.desc").withStyle(ChatFormatting.GRAY);
	public static final int DEFAULT_COLOR = 0xFFBDCFD9;

	public ArcticArmorItem(Properties properties) {
		super(properties);
	}

	@Override
	public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
		super.appendHoverText(itemStack, context, display, builder, tooltipFlag);
		builder.accept(TOOLTIP);
	}

	@Override
	public boolean canWalkOnPowderedSnow(ItemStack stack, LivingEntity wearer) {
		return stack.is(TFItems.ARCTIC_BOOTS.get());
	}

	public static final class ArmorRender extends TFSimpleArmorRenderer {
		public ArmorRender() {
			super(TFArmorModel::new, TFModelLayers.ARCTIC_ARMOR_INNER, TFModelLayers.ARCTIC_ARMOR_OUTER);
		}

		@Override
		public int getDefaultDyeColor(ItemStack stack) {
			return ARGB.opaque(DyedItemColor.getOrDefault(stack, DEFAULT_COLOR));
		}
	}
}
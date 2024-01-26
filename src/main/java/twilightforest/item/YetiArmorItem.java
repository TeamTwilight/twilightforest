package twilightforest.item;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.armor.YetiArmorModel;
import twilightforest.init.TFEnchantments;
import twilightforest.init.TFItems;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class YetiArmorItem extends ArmorItem {
	private static final MutableComponent TOOLTIP = Component.translatable("item.twilightforest.yeti_armor.desc").setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY));

	public YetiArmorItem(ArmorMaterial material, Type type, Properties properties) {
		super(material, type, properties);
	}

	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		AtomicBoolean badEnchant = new AtomicBoolean();
		EnchantmentHelper.getEnchantments(book).forEach((enchantment, integer) -> {
			if (Objects.equals(Enchantments.THORNS, enchantment) || Objects.equals(TFEnchantments.FIRE_REACT.get(), enchantment) || Objects.equals(TFEnchantments.CHILL_AURA.get(), enchantment)) {
				badEnchant.set(true);
			}
		});

		return !badEnchant.get();
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		return !TFEnchantments.FIRE_REACT.get().equals(enchantment) &&
				!Enchantments.THORNS.equals(enchantment) &&
				!TFEnchantments.CHILL_AURA.get().equals(enchantment) &&
				super.canApplyAtEnchantingTable(stack, enchantment);
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String layer) {
		if (slot == EquipmentSlot.LEGS) {
			return TwilightForestMod.ARMOR_DIR + "yetiarmor_2.png";
		} else {
			return TwilightForestMod.ARMOR_DIR + "yetiarmor_1.png";
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, level, tooltip, flag);
		tooltip.add(TOOLTIP);
	}

	@Override
	public boolean canWalkOnPowderedSnow(ItemStack stack, LivingEntity wearer) {
		return stack.is(TFItems.YETI_BOOTS);
	}

	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(ArmorRender.INSTANCE);
	}

	private static final class ArmorRender implements IClientItemExtensions {
		private static final ArmorRender INSTANCE = new ArmorRender();

		@Override
		public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity living, ItemStack stack, EquipmentSlot slot, HumanoidModel<?> model) {
			EntityModelSet models = Minecraft.getInstance().getEntityModels();
			ModelPart root = models.bakeLayer(slot == EquipmentSlot.LEGS ? TFModelLayers.YETI_ARMOR_INNER : TFModelLayers.YETI_ARMOR_OUTER);
			return new YetiArmorModel(root);
		}
	}
}
package twilightforest.item;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.BindingCurseEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.VanishingCurseEnchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.armor.ModelTFPhantomArmor;

import javax.annotation.Nullable;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class ItemTFPhantomArmor extends ArmorItem {

	private static final Map<EquipmentSlotType, BipedModel<?>> phantomArmorModel = new EnumMap<>(EquipmentSlotType.class);

	public ItemTFPhantomArmor(IArmorMaterial armorMaterial, EquipmentSlotType armorType, Properties props) {
		super(armorMaterial, armorType, props);
	}

	@Override
	public String getArmorTexture(ItemStack itemstack, Entity entity, EquipmentSlotType slot, String layer) {
		// there's no legs, so let's not worry about them
		return TwilightForestMod.ARMOR_DIR + "phantom_1.png";
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	@SuppressWarnings("unchecked")
	public <A extends BipedModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlotType armorSlot, A original) {
		return (A) phantomArmorModel.get(armorSlot);
	}

	@OnlyIn(Dist.CLIENT)
	public static void initArmorModel() {
		phantomArmorModel.put(EquipmentSlotType.HEAD, new ModelTFPhantomArmor(EquipmentSlotType.HEAD, 0.5F));
		phantomArmorModel.put(EquipmentSlotType.CHEST, new ModelTFPhantomArmor(EquipmentSlotType.CHEST, 0.5F));
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		return !(enchantment instanceof VanishingCurseEnchantment) && !(enchantment instanceof BindingCurseEnchantment) && enchantment.type.canEnchantItem(stack.getItem());
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
		tooltip.add(new TranslationTextComponent(getTranslationKey() + ".tooltip"));
	}
}

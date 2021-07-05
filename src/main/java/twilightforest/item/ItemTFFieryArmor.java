package twilightforest.item;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.armor.ModelTFFieryArmor;

import javax.annotation.Nullable;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class ItemTFFieryArmor extends ArmorItem {

	private static final Map<EquipmentSlotType, BipedModel<?>> fieryArmorModel = new EnumMap<>(EquipmentSlotType.class);

	public ItemTFFieryArmor(IArmorMaterial armorMaterial, EquipmentSlotType armorType, Properties props) {
		super(armorMaterial, armorType, props);
	}

	@Override
	public String getArmorTexture(ItemStack itemstack, Entity entity, EquipmentSlotType slot, String layer) {
		if (slot == EquipmentSlotType.LEGS) {
			return TwilightForestMod.ARMOR_DIR + "fiery_2.png";
		} else {
			return TwilightForestMod.ARMOR_DIR + "fiery_1.png";
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flags) {
		super.addInformation(stack, world, tooltip, flags);
		tooltip.add(new TranslationTextComponent(getTranslationKey() + ".tooltip"));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	@SuppressWarnings("unchecked")
	public <A extends BipedModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlotType armorSlot, A oldModel) {
		return (A) fieryArmorModel.get(armorSlot);
	}

	@OnlyIn(Dist.CLIENT)
	public static void initArmorModel() {
		fieryArmorModel.put(EquipmentSlotType.HEAD, new ModelTFFieryArmor(0.75F));
		fieryArmorModel.put(EquipmentSlotType.CHEST, new ModelTFFieryArmor(1.0F));
		fieryArmorModel.put(EquipmentSlotType.LEGS, new ModelTFFieryArmor(0.5F));
		fieryArmorModel.put(EquipmentSlotType.FEET, new ModelTFFieryArmor(1.0F));
	}
}

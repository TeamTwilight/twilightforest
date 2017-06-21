package twilightforest.item;

import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.TwilightForestMod;
import twilightforest.client.ModelRegisterCallback;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemTFYetiArmor extends ItemArmor implements ModelRegisterCallback {

	public ItemTFYetiArmor(ItemArmor.ArmorMaterial material, EntityEquipmentSlot slot) {
		super(material, 0, slot);
		this.setCreativeTab(TFItems.creativeTab);
	}

	@Nonnull
	@Override
	public EnumRarity getRarity(ItemStack par1ItemStack) {
		return EnumRarity.EPIC;
	}

	@Override
	public String getArmorTexture(ItemStack itemstack, Entity entity, EntityEquipmentSlot slot, String layer) {
		if (slot == EntityEquipmentSlot.LEGS) {
			return TwilightForestMod.ARMOR_DIR + "yetiarmor_1.png";
		} else {
			return TwilightForestMod.ARMOR_DIR + "yetiarmor_2.png";
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(@Nonnull Item item, CreativeTabs tab, NonNullList<ItemStack> list) {
		ItemStack istack = new ItemStack(item);
		switch (this.armorType) {
			case HEAD:
			case CHEST:
			case LEGS:
				istack.addEnchantment(Enchantments.PROTECTION, 2);
				break;
			case FEET:
				istack.addEnchantment(Enchantments.PROTECTION, 2);
				istack.addEnchantment(Enchantments.FEATHER_FALLING, 4);
				break;
		}
		list.add(istack);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public net.minecraft.client.model.ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, net.minecraft.client.model.ModelBiped _default) {
		return TwilightForestMod.proxy.getYetiArmorModel(armorSlot);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltips, boolean advanced) {
		super.addInformation(stack, player, tooltips, advanced);
		tooltips.add(I18n.format(getUnlocalizedName() + ".tooltip"));
	}
}

package twilightforest.item;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.TwilightForestMod;
import twilightforest.client.ModelRegisterCallback;

public class ItemTFArcticArmor extends ItemArmor implements ModelRegisterCallback {

	public ItemTFArcticArmor(ItemArmor.ArmorMaterial par2EnumArmorMaterial, EntityEquipmentSlot armorType) {
		super(par2EnumArmorMaterial, 0, armorType);
		this.setCreativeTab(TFItems.creativeTab);
	}

	@Override
	public EnumRarity getRarity(ItemStack par1ItemStack) {
		return EnumRarity.UNCOMMON;
	}

	@Override
	public String getArmorTexture(ItemStack itemstack, Entity entity, EntityEquipmentSlot slot, String layer) {
		if (slot == EntityEquipmentSlot.LEGS) {
			return TwilightForestMod.ARMOR_DIR + "arcticarmor_2" + (layer == null ? "_dyed" : "_overlay") + ".png";
		} else {
			return TwilightForestMod.ARMOR_DIR + "arcticarmor_1" + (layer == null ? "_dyed" : "_overlay") + ".png";
		}
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {
		if (isInCreativeTab(tab)) {
			ItemStack istack = new ItemStack(this);
			//istack.addEnchantment(TFEnchantment.reactFire, 2);
			list.add(istack);
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped oldM) {
		return TwilightForestMod.proxy.getArcticArmorModel(armorSlot);
	}

	@Override
	public boolean hasOverlay(ItemStack stack) {
		return getColor(stack) != 0xFFFFFF;
	}

	@Override
	public boolean hasColor(ItemStack stack) {
		NBTTagCompound nbttagcompound = stack.getTagCompound();
		return (nbttagcompound != null && nbttagcompound.hasKey("display", 10)) && nbttagcompound.getCompoundTag("display").hasKey("color", 3);
	}

	@Override
	public int getColor(ItemStack stack) {
		return this.getColor(stack, 1);
	}

	@Override
	public void removeColor(ItemStack stack) {
		this.removeColor(stack, 1);
	}

	@Override
	public void setColor(ItemStack stack, int color) {
		this.setColor(stack, color, 1);
	}

	public int getColor(ItemStack stack, int type) {
		String string = "";//type == 0 ? "" : ("" + type);
		NBTTagCompound stackTagCompound = stack.getTagCompound();

		if (stackTagCompound != null) {
			NBTTagCompound displayCompound = stackTagCompound.getCompoundTag("display");

			if (displayCompound.hasKey("color" + string, 3))
				return displayCompound.getInteger("color" + string);
		}

		switch (type) {
			//case 0:
				//return stack.getItem() != TFItems.arcticHelm ? 0x793828 : 0xFFFFFF;
			case 1:
				return 0xBDCFD9;
			default:
				return 0xFFFFFF;
		}
	}

	public void removeColor(ItemStack stack, int type) {
		String string = "";//type == 0 ? "" : ("" + type);
		NBTTagCompound stackTagCompound = stack.getTagCompound();

		if (stackTagCompound != null) {
			NBTTagCompound displayCompound = stackTagCompound.getCompoundTag("display");

			if (displayCompound.hasKey("color" + string))
				displayCompound.removeTag("color" + string);
		}
	}

	public void setColor(ItemStack stack, int color, int type) {
		String string = "";//type == 0 ? "" : ("" + type);
		NBTTagCompound stackTagCompound = stack.getTagCompound();

		if (stackTagCompound == null) {
			stackTagCompound = new NBTTagCompound();
			stack.setTagCompound(stackTagCompound);
		}

		NBTTagCompound displayCompound = stackTagCompound.getCompoundTag("display");

		if (!stackTagCompound.hasKey("display", 10)) {
			stackTagCompound.setTag("display", displayCompound);
		}

		displayCompound.setInteger("color" + string, color);
	}
}

package twilightforest.item;

import java.util.List;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import twilightforest.TwilightForestMod;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemTFArcticArmor extends ItemArmor {

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
		switch (slot) {
		case HEAD :
		case CHEST :
		case FEET :
		default :
			return TwilightForestMod.ARMOR_DIR + "arcticarmor_1.png";
		case LEGS :
			return TwilightForestMod.ARMOR_DIR + "arcticarmor_2.png";

		}
	}
	
	@Override
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List) {
    	ItemStack istack = new ItemStack(par1, 1, 0);
    	//istack.addEnchantment(TFEnchantment.reactFire, 2);
        par3List.add(istack);
    }
    
    @Override
	public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack)
    {
    	// repair with ?????
        return par2ItemStack.getItem() == TFItems.arcticFur ? true : super.getIsRepairable(par1ItemStack, par2ItemStack);
    }

    @SideOnly(Side.CLIENT)
	@Override
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped oldM)
    {
        return TwilightForestMod.proxy.getArcticArmorModel(armorSlot);
    }
}

package twilightforest.item;

import java.util.List;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import twilightforest.TwilightForestMod;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemTFFieryArmor extends ItemArmor {

	public ItemTFFieryArmor(ItemArmor.ArmorMaterial par2EnumArmorMaterial, EntityEquipmentSlot armorType) {
		super(par2EnumArmorMaterial, 0, armorType);
		this.setCreativeTab(TFItems.creativeTab);
	}

	@Override
	public EnumRarity getRarity(ItemStack par1ItemStack) {
		return EnumRarity.EPIC;
	}

	@Override
	public String getArmorTexture(ItemStack itemstack, Entity entity, EntityEquipmentSlot slot, String layer) {


		if(itemstack.getItem() == TFItems.fieryPlate || itemstack.getItem() == TFItems.fieryHelm || itemstack.getItem() == TFItems.fieryBoots)
		{
			return TwilightForestMod.ARMOR_DIR + "fiery_1.png";
		}
		if(itemstack.getItem() == TFItems.fieryLegs)
		{
			return TwilightForestMod.ARMOR_DIR + "fiery_2.png";
		}
		return TwilightForestMod.ARMOR_DIR + "fiery_1.png";
	}
	
	@Override
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List)
    {
    	ItemStack istack = new ItemStack(par1, 1, 0);
    	//istack.addEnchantment(TFEnchantment.fieryAura, 2);
        par3List.add(istack);
    }
    
    @Override
	public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack)
    {
    	// repair with fiery ingots
        return par2ItemStack.getItem() == TFItems.fieryIngot ? true : super.getIsRepairable(par1ItemStack, par2ItemStack);
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List<String> par3List, boolean par4) {
		super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);
		par3List.add(I18n.format(getUnlocalizedName() + ".tooltip"));
	}
    
    @Override
	@SideOnly(Side.CLIENT)
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped oldModel)
    {
        return TwilightForestMod.proxy.getFieryArmorModel(armorSlot);
    }
}

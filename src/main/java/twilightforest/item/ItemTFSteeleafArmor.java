package twilightforest.item;

import java.util.List;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.TwilightForestMod;
import twilightforest.client.ModelRegisterCallback;

public class ItemTFSteeleafArmor extends ItemArmor implements ModelRegisterCallback {

	public ItemTFSteeleafArmor(ItemArmor.ArmorMaterial par2EnumArmorMaterial, EntityEquipmentSlot armorType) {
		super(par2EnumArmorMaterial, 0, armorType);
		this.setCreativeTab(TFItems.creativeTab);
	}

	@Override
	public EnumRarity getRarity(ItemStack par1ItemStack) {
		return EnumRarity.UNCOMMON;
	}

	@Override
	public String getArmorTexture(ItemStack itemstack, Entity entity, EntityEquipmentSlot slot, String layer) {
		if(itemstack.getItem() == TFItems.steeleafPlate || itemstack.getItem() == TFItems.steeleafHelm || itemstack.getItem() == TFItems.steeleafBoots)
		{
			return TwilightForestMod.ARMOR_DIR + "steeleaf_1.png";
		}
		if(itemstack.getItem() == TFItems.steeleafLegs)
		{
			return TwilightForestMod.ARMOR_DIR + "steeleaf_2.png";
		}
		return TwilightForestMod.ARMOR_DIR + "steeleaf_1.png";
	}
	
	@Override
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List) {
    	ItemStack istack = new ItemStack(par1, 1, 0);
    	switch (this.armorType) {
    	case HEAD:
    		istack.addEnchantment(Enchantments.PROJECTILE_PROTECTION, 2);
            break;	
    	case CHEST:
    		istack.addEnchantment(Enchantments.BLAST_PROTECTION, 2);
            break;	
    	case LEGS:
    		istack.addEnchantment(Enchantments.FIRE_PROTECTION, 2);
            break;	
    	case FEET:
    		istack.addEnchantment(Enchantments.FEATHER_FALLING, 2);
            break;	
    	}
    	par3List.add(istack);
    }
}

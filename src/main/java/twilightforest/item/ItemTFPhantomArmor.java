package twilightforest.item;

import java.util.List;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import twilightforest.TwilightForestMod;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.client.ModelRegisterCallback;

public class ItemTFPhantomArmor extends ItemArmor implements ModelRegisterCallback {

	public ItemTFPhantomArmor(ItemArmor.ArmorMaterial par2EnumArmorMaterial, EntityEquipmentSlot armorType) {
		super(par2EnumArmorMaterial, 0, armorType);
		this.setCreativeTab(TFItems.creativeTab);
	}

	@Override
	public EnumRarity getRarity(ItemStack par1ItemStack) {
		return EnumRarity.EPIC;
	}
	
    @Override
    public String getArmorTexture(ItemStack itemstack, Entity entity, EntityEquipmentSlot slot, String layer)
    {
    	// there's no legs, so let's not worry about them
		return TwilightForestMod.ARMOR_DIR + "phantom_1.png";
	}
	
	@Override
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List)
    {
    	ItemStack istack = new ItemStack(par1, 1, 0);
    	//istack.addEnchantment(TFEnchantment.reactFire, 2);
        par3List.add(istack);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped original)
    {
        return TwilightForestMod.proxy.getPhantomArmorModel(armorSlot);
    }
}

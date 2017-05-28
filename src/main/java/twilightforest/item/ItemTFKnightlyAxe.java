package twilightforest.item;

import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.client.ModelRegisterCallback;

public class ItemTFKnightlyAxe extends ItemAxe implements ModelRegisterCallback {

	protected ItemTFKnightlyAxe(Item.ToolMaterial par2EnumToolMaterial) {
		super(par2EnumToolMaterial, par2EnumToolMaterial.getDamageVsEntity(), -3.0f);
		this.setCreativeTab(TFItems.creativeTab);
	}

    @Override
	public EnumRarity getRarity(ItemStack par1ItemStack) {
    	return EnumRarity.RARE;
	}
    
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List<String> par3List, boolean par4) {
		super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);
		par3List.add(I18n.format(getUnlocalizedName() + ".tooltip"));
	}
}

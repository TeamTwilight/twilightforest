package twilightforest.item;

import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.client.ModelRegisterCallback;

public class ItemTFMinotaurAxe extends ItemAxe implements ModelRegisterCallback {

	public static final int BONUS_CHARGING_DAMAGE = 7;
	private Entity bonusDamageEntity;
	private EntityPlayer bonusDamagePlayer;

	protected ItemTFMinotaurAxe(Item.ToolMaterial par2EnumToolMaterial) {
		super(par2EnumToolMaterial, par2EnumToolMaterial.getDamageVsEntity(), -3.0f);
		this.damageVsEntity = 4 + par2EnumToolMaterial.getDamageVsEntity();
		this.setCreativeTab(TFItems.creativeTab);
	}

    @Override
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List)
    {
    	ItemStack istack = new ItemStack(par1, 1, 0);
    	//istack.addEnchantment(Enchantments.EFFICIENCY, 2);
        par3List.add(istack);
    }
    
    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) 
    {
    	// if the player is sprinting, keep the entity, we will do extra damage to it
    	if (player.isSprinting())
    	{
    		this.bonusDamageEntity = entity;
    		this.bonusDamagePlayer = player;
    	}
    	
        return false;
    }
    
    /**
     * Returns the damage against a given entity.
     */
    public float getDamageVsEntity(Entity par1Entity, ItemStack itemStack)
    {
       	if (this.bonusDamagePlayer != null && this.bonusDamageEntity != null && par1Entity == this.bonusDamageEntity)
       	{
       		//System.out.println("Minotaur Axe extra damage!");
       		this.bonusDamagePlayer.onEnchantmentCritical(par1Entity);
       		this.bonusDamagePlayer = null;
       		this.bonusDamageEntity = null;
       		return damageVsEntity + BONUS_CHARGING_DAMAGE;
       	}
       	else
       	{
       		return damageVsEntity;
       	}
    }
    
    @Override
    public int getItemEnchantability()
    {
        return Item.ToolMaterial.GOLD.getEnchantability();
    }
    
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List<String> par3List, boolean par4) {
		super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);
		par3List.add(I18n.format(getUnlocalizedName() + ".tooltip"));
	}
}


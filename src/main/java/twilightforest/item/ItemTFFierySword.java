package twilightforest.item;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.client.ModelRegisterCallback;

import java.util.List;

public class ItemTFFierySword extends ItemSword implements ModelRegisterCallback {

	public ItemTFFierySword(Item.ToolMaterial par2EnumToolMaterial) {
		super(par2EnumToolMaterial);
		this.setCreativeTab(TFItems.creativeTab);
	}

	@Override
	public EnumRarity getRarity(ItemStack par1ItemStack) {
		return EnumRarity.RARE;
	}

	@Override
	public boolean hitEntity(ItemStack par1ItemStack, EntityLivingBase par2EntityLiving, EntityLivingBase par3EntityLiving) {
		boolean result = super.hitEntity(par1ItemStack, par2EntityLiving, par3EntityLiving);

		if (result && !par2EntityLiving.isImmuneToFire()) {
			if (par2EntityLiving.world.isRemote) {
				for (int var1 = 0; var1 < 20; ++var1) {
					double var2 = itemRand.nextGaussian() * 0.02D;
					double var4 = itemRand.nextGaussian() * 0.02D;
					double var6 = itemRand.nextGaussian() * 0.02D;
					double var8 = 10.0D;
					par2EntityLiving.world.spawnParticle(EnumParticleTypes.FLAME, par2EntityLiving.posX + itemRand.nextFloat() * par2EntityLiving.width * 2.0F - par2EntityLiving.width - var2 * var8, par2EntityLiving.posY + itemRand.nextFloat() * par2EntityLiving.height - var4 * var8, par2EntityLiving.posZ + itemRand.nextFloat() * par2EntityLiving.width * 2.0F - par2EntityLiving.width - var6 * var8, var2, var4, var6);
				}
			}
		}

		return result;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flags) {
		super.addInformation(stack, world, tooltip, flags);
		tooltip.add(I18n.format(getUnlocalizedName() + ".tooltip"));
	}
}

package twilightforest.item;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Enchantments;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.client.ModelRegisterCallback;

import java.util.List;

public class ItemTFFierySword extends ItemSword implements ModelRegisterCallback {

	public ItemTFFierySword(Item.ToolMaterial toolMaterial) {
		super(toolMaterial);
		this.setCreativeTab(TFItems.creativeTab);
	}

	@Override
	public EnumRarity getRarity(ItemStack stack) {
		return EnumRarity.RARE;
	}

	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
		boolean result = super.hitEntity(stack, target, attacker);

		if (result && !target.isImmuneToFire()) {
			if (target.world.isRemote) {
				for (int i = 0; i < 20; ++i) {
					double vx = itemRand.nextGaussian() * 0.02D;
					double vy = itemRand.nextGaussian() * 0.02D;
					double vz = itemRand.nextGaussian() * 0.02D;
					double var8 = 10.0D;
					target.world.spawnParticle(EnumParticleTypes.FLAME, target.posX + itemRand.nextFloat() * target.width * 2.0F - target.width - vx * var8, target.posY + itemRand.nextFloat() * target.height - vy * var8, target.posZ + itemRand.nextFloat() * target.width * 2.0F - target.width - vz * var8, vx, vy, vz);
				}
			}
		}

		return result;
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {
		if (isInCreativeTab(tab)) {
			ItemStack istack = new ItemStack(this);
			istack.addEnchantment(Enchantments.FIRE_ASPECT, 2);
			list.add(istack);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flags) {
		super.addInformation(stack, world, tooltip, flags);
		tooltip.add(I18n.format(getTranslationKey() + ".tooltip"));
	}
}

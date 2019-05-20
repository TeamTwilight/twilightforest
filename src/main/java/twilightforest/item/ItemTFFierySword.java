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
import twilightforest.util.ParticleHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemTFFierySword extends ItemSword implements ModelRegisterCallback {

	public ItemTFFierySword(Item.ToolMaterial toolMaterial) {
		super(toolMaterial);
		this.setCreativeTab(TFItems.creativeTab);
	}

	private static final EnumRarity RARITY = EnumRarity.UNCOMMON;

	@Nonnull
	@Override
	public EnumRarity getRarity(ItemStack stack) {
		return stack.isItemEnchanted() ? EnumRarity.RARE.compareTo(RARITY) > 0 ? EnumRarity.RARE : RARITY : RARITY;
	}

	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
		boolean result = super.hitEntity(stack, target, attacker);

		if (result && !target.world.isRemote && !target.isImmuneToFire()) {
			ParticleHelper.spawnParticles(target, EnumParticleTypes.FLAME, 20, 0.02);
		}

		return result;
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {
		if (isInCreativeTab(tab)) {
			ItemStack stack = new ItemStack(this);
			stack.addEnchantment(Enchantments.FIRE_ASPECT, 2);
			list.add(stack);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flags) {
		super.addInformation(stack, world, tooltip, flags);
		tooltip.add(I18n.format(getTranslationKey() + ".tooltip"));
	}
}

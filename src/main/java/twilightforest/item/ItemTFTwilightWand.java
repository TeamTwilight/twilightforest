package twilightforest.item;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.entity.EntityTFTwilightWandBolt;

import java.util.List;

public class ItemTFTwilightWand extends ItemTF {

	protected ItemTFTwilightWand() {
		this.maxStackSize = 1;
		this.setMaxDamage(99);
		this.setCreativeTab(TFItems.creativeTab);
	}

	//Atomic: Not sure why getMaxDamage was deprecated since it's actively used?
	@SuppressWarnings("deprecation")
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);

		if (stack.getItemDamage() == stack.getMaxDamage()) {
			return ActionResult.newResult(EnumActionResult.FAIL, player.getHeldItem(hand));
		} else {
			player.playSound(SoundEvents.ENTITY_GHAST_SHOOT, 1.0F, (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F + 1.0F);

			if (!world.isRemote) {
				world.spawnEntity(new EntityTFTwilightWandBolt(world, player));
				stack.damageItem(1, player);
			}

			return ActionResult.newResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
		}
	}

	@Override
	public EnumRarity getRarity(ItemStack par1ItemStack) {
		return EnumRarity.RARE;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flags) {
		super.addInformation(stack, world, tooltip, flags);
		tooltip.add(I18n.format("twilightforest.scepter_charges", stack.getMaxDamage() - stack.getItemDamage()));
	}
}

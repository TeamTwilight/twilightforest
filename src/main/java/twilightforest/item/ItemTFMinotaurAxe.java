package twilightforest.item;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketAnimation;
import net.minecraft.util.DamageSource;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.TwilightForestMod;
import twilightforest.client.ModelRegisterCallback;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@Mod.EventBusSubscriber(modid = TwilightForestMod.ID)
public class ItemTFMinotaurAxe extends ItemAxe implements ModelRegisterCallback {

	private static final int BONUS_CHARGING_DAMAGE = 7;
	private final EnumRarity RARITY;

	protected ItemTFMinotaurAxe(Item.ToolMaterial material, EnumRarity rarity) {
		super(material, 6F + material.getAttackDamage(), material.getEfficiency() * 0.05f - 3.4f);
		this.setCreativeTab(TFItems.creativeTab);

		this.RARITY = rarity;
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {
		if (isInCreativeTab(tab)) {
			ItemStack istack = new ItemStack(this);
			//istack.addEnchantment(Enchantments.EFFICIENCY, 2);
			list.add(istack);
		}
	}

	@SubscribeEvent
	public static void onAttack(LivingAttackEvent evt) {
		EntityLivingBase target = evt.getEntityLiving();
		Entity source = evt.getSource().getImmediateSource();

		if (!target.world.isRemote && source instanceof EntityLivingBase && source.isSprinting()) {
			ItemStack weapon = ((EntityLivingBase) evt.getSource().getImmediateSource()).getHeldItemMainhand();

			if (!weapon.isEmpty() && weapon.getItem() == TFItems.minotaur_axe) {
				target.attackEntityFrom(DamageSource.MAGIC, BONUS_CHARGING_DAMAGE);
				// don't prevent main damage from applying
				target.hurtResistantTime = 0;
				// enchantment attack sparkles
				((WorldServer) target.world).getEntityTracker().sendToTrackingAndSelf(target, new SPacketAnimation(target, 5));
			}
		}
	}

	@Override
	public int getItemEnchantability() {
		return Item.ToolMaterial.GOLD.getEnchantability();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flags) {
		super.addInformation(stack, world, tooltip, flags);
		tooltip.add(I18n.format(getTranslationKey() + ".tooltip"));
	}

	@Nonnull
	@Override
	public EnumRarity getRarity(ItemStack stack) {
		return stack.isItemEnchanted() ? EnumRarity.RARE.compareTo(RARITY) > 0 ? EnumRarity.RARE : RARITY : RARITY;
	}
}

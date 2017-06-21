package twilightforest.item;

import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketAnimation;
import net.minecraft.util.DamageSource;
import net.minecraft.util.NonNullList;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.client.ModelRegisterCallback;

import javax.annotation.Nonnull;
import java.util.List;

@Mod.EventBusSubscriber
public class ItemTFMinotaurAxe extends ItemAxe implements ModelRegisterCallback {
	private static final int BONUS_CHARGING_DAMAGE = 7;

	protected ItemTFMinotaurAxe(Item.ToolMaterial material) {
		super(material, material.getDamageVsEntity(), -3.0f);
		this.damageVsEntity = 4 + material.getDamageVsEntity();
		this.setCreativeTab(TFItems.creativeTab);
	}

	@Override
	public void getSubItems(@Nonnull Item item, CreativeTabs tab, NonNullList<ItemStack> list) {
		ItemStack istack = new ItemStack(item, 1, 0);
		//istack.addEnchantment(Enchantments.EFFICIENCY, 2);
		list.add(istack);
	}

	@SubscribeEvent
	public static void onAttack(LivingAttackEvent evt) {
		EntityLivingBase target = evt.getEntityLiving();

		if (!target.world.isRemote && evt.getSource().getSourceOfDamage() instanceof EntityLivingBase
				&& evt.getSource().getSourceOfDamage().isSprinting()) {
			ItemStack weapon = ((EntityLivingBase) evt.getSource().getSourceOfDamage()).getHeldItemMainhand();

			if (!weapon.isEmpty() && weapon.getItem() == TFItems.minotaurAxe) {
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
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean advanced) {
		super.addInformation(stack, player, list, advanced);
		list.add(I18n.format(getUnlocalizedName() + ".tooltip"));
	}
}


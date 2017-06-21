package twilightforest.item;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.server.SPacketAnimation;
import net.minecraft.util.DamageSource;
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
public class ItemTFKnightlySword extends ItemSword implements ModelRegisterCallback {

	private static final int BONUS_DAMAGE = 2;

	public ItemTFKnightlySword(Item.ToolMaterial material) {
		super(material);
		this.setCreativeTab(TFItems.creativeTab);
	}

	@SubscribeEvent
	public static void onDamage(LivingAttackEvent evt) {
		EntityLivingBase target = evt.getEntityLiving();

		if (!target.world.isRemote && evt.getSource().getSourceOfDamage() instanceof EntityLivingBase) {
			ItemStack weapon = ((EntityLivingBase) evt.getSource().getSourceOfDamage()).getHeldItemMainhand();

			if (target.getTotalArmorValue() > 0 && !weapon.isEmpty() && (weapon.getItem() == TFItems.knightlyAxe || weapon.getItem() == TFItems.knightlyPick || weapon.getItem() == TFItems.knightlySword)) {
				// TODO scale bonus dmg with the amount of armor?
				target.attackEntityFrom(DamageSource.MAGIC, BONUS_DAMAGE);
				// don't prevent main damage from applying
				target.hurtResistantTime = 0;
				// enchantment attack sparkles
				((WorldServer) target.world).getEntityTracker().sendToTrackingAndSelf(target, new SPacketAnimation(target, 5));
			}
		}
	}

	@Nonnull
	@Override
	public EnumRarity getRarity(ItemStack par1ItemStack) {
		return EnumRarity.RARE;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean advanced) {
		super.addInformation(stack, player, list, advanced);
		list.add(I18n.format(getUnlocalizedName() + ".tooltip"));
	}
}

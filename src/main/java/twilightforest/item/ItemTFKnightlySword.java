package twilightforest.item;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.server.SPacketAnimation;
import net.minecraft.util.DamageSource;
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
import java.util.List;

@Mod.EventBusSubscriber(modid = TwilightForestMod.ID)
public class ItemTFKnightlySword extends ItemSword implements ModelRegisterCallback {

	private static final int BONUS_DAMAGE = 2;

	public ItemTFKnightlySword(Item.ToolMaterial material) {
		super(material);
		this.setCreativeTab(TFItems.creativeTab);
	}

	@SubscribeEvent
	public static void onDamage(LivingAttackEvent evt) {
		EntityLivingBase target = evt.getEntityLiving();

		if (!target.world.isRemote && evt.getSource().getImmediateSource() instanceof EntityLivingBase) {
			ItemStack weapon = ((EntityLivingBase) evt.getSource().getImmediateSource()).getHeldItemMainhand();

			if (!weapon.isEmpty() && ((target.getTotalArmorValue() > 0 && (weapon.getItem() == TFItems.knightmetal_pickaxe || weapon.getItem() == TFItems.knightmetal_sword)) || (target.getTotalArmorValue() == 0 && weapon.getItem() == TFItems.knightmetal_axe))) {
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
	public void addInformation(ItemStack stack, World world, List<String> list, ITooltipFlag flags) {
		super.addInformation(stack, world, list, flags);
		list.add(I18n.format(getUnlocalizedName() + ".tooltip"));
	}
}

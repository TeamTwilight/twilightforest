package twilightforest.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.TwilightForestMod;

import javax.annotation.Nullable;
import java.util.List;

@Mod.EventBusSubscriber(modid = TwilightForestMod.ID)
public class ItemTFKnightlySword extends SwordItem {

	private static final int BONUS_DAMAGE = 2;

	public ItemTFKnightlySword(IItemTier material, Properties props) {
		super(material, 3, -2.4F, props);
	}

	@SubscribeEvent
	public static void onDamage(LivingAttackEvent evt) {
		LivingEntity target = evt.getEntityLiving();

		if (!target.world.isRemote && evt.getSource().getImmediateSource() instanceof LivingEntity) {
			ItemStack weapon = ((LivingEntity) evt.getSource().getImmediateSource()).getHeldItemMainhand();

			if (!weapon.isEmpty() && ((target.getTotalArmorValue() > 0 && (weapon.getItem() == TFItems.knightmetal_pickaxe.get() || weapon.getItem() == TFItems.knightmetal_sword.get())) || (target.getTotalArmorValue() == 0 && weapon.getItem() == TFItems.knightmetal_axe.get()))) {
				// TODO scale bonus dmg with the amount of armor?
				target.attackEntityFrom(DamageSource.MAGIC, BONUS_DAMAGE);
				// don't prevent main damage from applying
				target.hurtResistantTime = 0;
				// enchantment attack sparkles
				//((ServerWorld) target.world).getEntityTracker().sendToTrackingAndSelf(target, new SPacketAnimation(target, 5));
			}
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> list, ITooltipFlag flags) {
		super.addInformation(stack, world, list, flags);
		list.add(new TranslationTextComponent(getTranslationKey() + ".tooltip"));
	}
}

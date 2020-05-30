package twilightforest.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.*;
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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@Mod.EventBusSubscriber(modid = TwilightForestMod.ID)
public class ItemTFMinotaurAxe extends AxeItem {

	private static final int BONUS_CHARGING_DAMAGE = 7;

	protected ItemTFMinotaurAxe(IItemTier material, Properties props) {
		super(material, 6F + material.getAttackDamage(), material.getEfficiency() * 0.05f - 3.4f, props);
	}

	@SubscribeEvent
	public static void onAttack(LivingAttackEvent evt) {
		LivingEntity target = evt.getEntityLiving();
		Entity source = evt.getSource().getImmediateSource();

		if (!target.world.isRemote && source instanceof LivingEntity && source.isSprinting()) {
			ItemStack weapon = ((LivingEntity) evt.getSource().getImmediateSource()).getHeldItemMainhand();

			if (!weapon.isEmpty() && weapon.getItem() == TFItems.minotaur_axe.get()) {
				target.attackEntityFrom(DamageSource.MAGIC, BONUS_CHARGING_DAMAGE);
				// don't prevent main damage from applying
				target.hurtResistantTime = 0;
				// enchantment attack sparkles
				//((ServerWorld) target.world).getEntityTracker().sendToTrackingAndSelf(target, new SPacketAnimation(target, 5));
			}
		}
	}

	@Override
	public int getItemEnchantability() {
		return ItemTier.GOLD.getEnchantability();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flags) {
		super.addInformation(stack, world, tooltip, flags);
		tooltip.add(new TranslationTextComponent(getTranslationKey() + ".tooltip"));
	}
}

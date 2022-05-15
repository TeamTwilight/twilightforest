package twilightforest.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import twilightforest.TwilightForestMod;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Mod.EventBusSubscriber(modid = TwilightForestMod.ID)
public class FierySwordItem extends SwordItem {

	public FierySwordItem(Tier toolMaterial, Properties props) {
		super(toolMaterial, 3, -2.4F, props);
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		return enchantment != Enchantments.FIRE_ASPECT && super.canApplyAtEnchantingTable(stack, enchantment);
	}

	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(book);

		for (Enchantment ench : enchants.keySet()) {
			if (Objects.equals(ench.getRegistryName(), Enchantments.FIRE_ASPECT.getRegistryName())) {
				return false;
			}
		}
		return false;
	}

	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		boolean result = super.hurtEnemy(stack, target, attacker);

		if (result && !target.getLevel().isClientSide() && !target.fireImmune()) {
			target.setSecondsOnFire(15);
		} else {
			for (int var1 = 0; var1 < 20; ++var1) {
				double px = target.getX() + target.getLevel().getRandom().nextFloat() * target.getBbWidth() * 2.0F - target.getBbWidth();
				double py = target.getY() + target.getLevel().getRandom().nextFloat() * target.getBbHeight();
				double pz = target.getZ() + target.getLevel().getRandom().nextFloat() * target.getBbWidth() * 2.0F - target.getBbWidth();
				target.getLevel().addParticle(ParticleTypes.FLAME, px, py, pz, 0.02, 0.02, 0.02);
			}
		}

		return result;
	}

	//we have to set the entity on fire early in order to actually cook the food
	@SubscribeEvent
	public static void setFireBeforeDeath(LivingAttackEvent event) {
		if(event.getSource().getEntity() instanceof LivingEntity living && living.getMainHandItem().is(TFItems.FIERY_SWORD.get()) && !event.getEntityLiving().fireImmune()) {
			event.getEntityLiving().setSecondsOnFire(1);
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, level, tooltip, flag);
		tooltip.add(new TranslatableComponent(getDescriptionId() + ".tooltip").withStyle(ChatFormatting.GRAY));
	}
}

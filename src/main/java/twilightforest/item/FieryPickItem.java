package twilightforest.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FieryPickItem extends PickaxeItem {

	public FieryPickItem(Tier toolMaterial, Properties properties) {
		super(toolMaterial, 1, -2.8F, properties);
	}

	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		boolean result = super.hurtEnemy(stack, target, attacker);

		if (result && !target.fireImmune()) {
			if (!target.level().isClientSide()) {
				target.setSecondsOnFire(15);
			} else {
				target.level().addParticle(ParticleTypes.FLAME, target.getX(), target.getY() + target.getBbHeight() * 0.5, target.getZ(), target.getBbWidth() * 0.5, target.getBbHeight() * 0.5, target.getBbWidth() * 0.5);
			}
		}

		return result;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flags) {
		super.appendHoverText(stack, level, tooltip, flags);
		tooltip.add(Component.translatable(getDescriptionId() + ".desc").withStyle(ChatFormatting.GRAY));
	}
}
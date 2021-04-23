package twilightforest.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.*;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class ItemTFFierySword extends SwordItem {

	public ItemTFFierySword(IItemTier toolMaterial, Properties props) {
		super(toolMaterial, 3, -2.4F, props);
	}

	@Override
	public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		boolean result = super.hitEntity(stack, target, attacker);

		if (result && !target.world.isRemote && !target.isImmuneToFire()) {
			target.setFire(15);
		} else {
			for (int var1 = 0; var1 < 20; ++var1) {
				double px = target.getPosX() + random.nextFloat() * target.getWidth() * 2.0F - target.getWidth();
				double py = target.getPosY() + random.nextFloat() * target.getHeight();
				double pz = target.getPosZ() + random.nextFloat() * target.getWidth() * 2.0F - target.getWidth();
				target.world.addParticle(ParticleTypes.FLAME, px, py, pz, 0.02, 0.02, 0.02);
			}
		}

		return result;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flags) {
		super.addInformation(stack, world, tooltip, flags);
		tooltip.add(new TranslationTextComponent(getTranslationKey() + ".tooltip"));
	}
}

package twilightforest.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.potion.EffectInstance;
import twilightforest.client.particle.TFParticleType;
import twilightforest.potions.TFPotions;
import twilightforest.util.ParticleHelper;

public class ItemTFIceSword extends SwordItem {

	public ItemTFIceSword(IItemTier toolMaterial, Properties props) {
		super(toolMaterial, 3, -2.4F, props);
	}

	@Override
	public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		boolean result = super.hitEntity(stack, target, attacker);

		if (result && !target.world.isRemote) {
			target.addPotionEffect(new EffectInstance(TFPotions.frosty.get(), 20 * 10, 2));
			//TODO: Move to regular particle spawner?
			ParticleHelper.spawnParticles(target, TFParticleType.SNOW.get());
		}

		return result;
	}
}

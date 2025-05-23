package twilightforest.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.ToolMaterial;
import net.neoforged.neoforge.network.PacketDistributor;
import twilightforest.enchantment.ApplyFrostedEffect;
import twilightforest.init.TFParticleType;
import twilightforest.network.ParticlePacket;

public class IceSwordItem extends SwordItem {

	public IceSwordItem(ToolMaterial material, Properties properties) {
		super(material, 3.0F, -2.4F, properties);
	}

	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		if (super.hurtEnemy(stack, target, attacker)) {
			ApplyFrostedEffect.doChillAuraEffect(target, 200, 2, true);

			ParticlePacket particlePacket = new ParticlePacket();
			for (int i = 0; i < 20; i++) {
				particlePacket.queueParticle(TFParticleType.SNOW.get(),
					target.getX() + (target.getRandom().nextGaussian() * target.getBbWidth() * 0.5),
					target.getY() + target.getBbHeight() * 0.5F + (target.getRandom().nextGaussian() * target.getBbHeight() * 0.5),
					target.getZ() + (target.getRandom().nextGaussian() * target.getBbWidth() * 0.5),
					0, 0, 0);
			}
			PacketDistributor.sendToPlayersTrackingEntity(target, particlePacket);

			return true;
		}
		return false;
	}
}
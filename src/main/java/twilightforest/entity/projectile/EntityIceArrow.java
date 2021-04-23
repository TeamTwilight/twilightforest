package twilightforest.entity.projectile;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import twilightforest.entity.TFEntities;
import twilightforest.potions.TFPotions;

public class EntityIceArrow extends EntityTFArrow {

	public EntityIceArrow(EntityType<? extends EntityIceArrow> type, World world) {
		super(type, world);
	}

	public EntityIceArrow(World world, LivingEntity shooter) {
		super(TFEntities.ice_arrow, world, shooter);
	}

	@Override
	public void tick() {
		super.tick();
		if (world.isRemote && !inGround) {
			BlockState stateId = Blocks.SNOW.getDefaultState();
			for (int i = 0; i < 4; ++i) {
				this.world.addParticle(new BlockParticleData(ParticleTypes.FALLING_DUST, stateId), this.getPosX() + this.getMotion().getX() * i / 4.0D, this.getPosY() + this.getMotion().getY() * i / 4.0D, this.getPosZ() + this.getMotion().getZ() * i / 4.0D, -this.getMotion().getX(), -this.getMotion().getY() + 0.2D, -this.getMotion().getZ());
			}
		}
	}

	@Override
	protected void onImpact(RayTraceResult ray) {
		super.onImpact(ray);
		if (ray instanceof EntityRayTraceResult) {
			if (!world.isRemote && ((EntityRayTraceResult)ray).getEntity() instanceof LivingEntity) {
				int chillLevel = 2;
				((LivingEntity) ((EntityRayTraceResult)ray).getEntity()).addPotionEffect(new EffectInstance(TFPotions.frosty.get(), 20 * 10, chillLevel));
			}
		}
	}
}

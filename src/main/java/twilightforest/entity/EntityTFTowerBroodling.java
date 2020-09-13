package twilightforest.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import twilightforest.TFSounds;

public class EntityTFTowerBroodling extends EntityTFSwarmSpider {

	public EntityTFTowerBroodling(EntityType<? extends EntityTFTowerBroodling> type, World world) {
		this(type, world, true);
	}

	public EntityTFTowerBroodling(EntityType<? extends EntityTFTowerBroodling> type, World world, boolean spawnMore) {
		super(type, world, spawnMore);
		experienceValue = 3;
	}

	public static AttributeModifierMap.MutableAttribute registerAttributes() {
		return EntityTFSwarmSpider.registerAttributes()
				.createMutableAttribute(Attributes.MAX_HEALTH, 7.0D)
				.createMutableAttribute(Attributes.ATTACK_DAMAGE, 4.0D);
	}
	
	@Override
	protected SoundEvent getAmbientSound() {
	      return TFSounds.BROODLING_AMBIENT;
	   }

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
	      return TFSounds.BROODLING_HURT;
	   }

	@Override
	protected SoundEvent getDeathSound() {
	      return TFSounds.BROODLING_DEATH;
	   }

	@Override
	protected void playStepSound(BlockPos pos, BlockState blockIn) {
	      this.playSound(TFSounds.BROODLING_STEP, 0.15F, 1.0F);
	   }

	@Override
	protected boolean spawnAnother() {
		EntityTFSwarmSpider another = new EntityTFTowerBroodling(TFEntities.tower_broodling, world, false);

		double sx = getPosX() + (rand.nextBoolean() ? 0.9 : -0.9);
		double sy = getPosY();
		double sz = getPosZ() + (rand.nextBoolean() ? 0.9 : -0.9);
		another.setLocationAndAngles(sx, sy, sz, rand.nextFloat() * 360F, 0.0F);
		if (!another.canSpawn(world, SpawnReason.MOB_SUMMONED)) {
			another.remove();
			return false;
		}
		world.addEntity(another);
		another.spawnExplosionParticle();

		return true;
	}
}

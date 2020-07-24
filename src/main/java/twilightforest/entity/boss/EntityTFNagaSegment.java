package twilightforest.entity.boss;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import twilightforest.entity.MultiPartEntityPart;

import java.util.List;

public class EntityTFNagaSegment extends MultiPartEntityPart {

	private EntityTFNaga naga;
	private int segment;
	private int deathCounter;
	private EntitySize entitySize;

	public EntityTFNagaSegment(EntityType<? extends EntityTFNagaSegment> type, World world) {
		super(type, world);
		this.stepHeight = 2;
		deactivate();
		this.ignoreFrustumCheck = true;
	}

	@Override
	public boolean attackEntityFrom(DamageSource src, float damage) {
		return !isInvisible() && super.attackEntityFrom(src, damage * 2F / 3F);
	}

	@Override
	public void tick() {
		super.tick();

		++this.ticksExisted;

		if (!isInvisible())
			collideWithOthers();

		if (deathCounter > 0)
		{
			deathCounter--;
			if (deathCounter <= 0)
			{
				for (int k = 0; k < 20; k++)
				{
					double d = rand.nextGaussian() * 0.02D;
					double d1 = rand.nextGaussian() * 0.02D;
					double d2 = rand.nextGaussian() * 0.02D;
					BasicParticleType explosionType = rand.nextBoolean() ? ParticleTypes.EXPLOSION_EMITTER : ParticleTypes.EXPLOSION;

					this.world.addParticle(explosionType, (getPosX() + rand.nextFloat() * getWidth() * 2.0F) - getWidth(), getPosY() + rand.nextFloat() * getHeight(), (getPosZ() + rand.nextFloat() * getWidth() * 2.0F) - getWidth(), d, d1, d2);
				}

				deactivate();
			}
		}
	}

	private void collideWithOthers() {
		List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getBoundingBox().grow(0.2D, 0.0D, 0.2D));

		for (Entity entity : list) {
			if (entity.canBePushed()) {
				this.collideWithEntity(entity);
			}
		}
	}

	@Override
	public boolean canRemove() {
		return this.deathCounter <= -1 || super.canRemove();
	}

	private void collideWithEntity(Entity entity) {
		entity.applyEntityCollision(this);

		// attack anything that's not us
		if ((entity instanceof LivingEntity) && !(entity instanceof EntityTFNaga) && !(entity instanceof EntityTFNagaSegment)) {
			int attackStrength = 2;

			// get rid of nearby deer & look impressive
			if (entity instanceof AnimalEntity) {
				attackStrength *= 3;
			}

			entity.attackEntityFrom(DamageSource.causeMobDamage(naga), attackStrength);
		}
	}

	public void deactivate() {
		//setSize(0, 0);
		this.entitySize = EntitySize.flexible(0.0F, 0.0F);
		setInvisible(true);
	}

	public void activate() {
		//setSize(1.8F, 1.8F);
		this.size = EntitySize.flexible(1.8F, 1.8F);
		setInvisible(false);
	}

	// make public
	@Override
	public void setRotation(float yaw, float pitch) {
		super.setRotation(yaw, pitch);
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState block) {}

	public void selfDestruct() {
		this.deathCounter = 10;
	}

	@Override
	public boolean isNonBoss() {
		return false;
	}

	@Override
	public EntitySize getSize(Pose pose) {
		return size;
	}
}

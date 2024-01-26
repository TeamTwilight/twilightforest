package twilightforest.entity.boss;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import twilightforest.TwilightForestMod;
import twilightforest.entity.TFPart;

import java.util.List;

public class NagaSegment extends TFPart<Naga> {

	public static final ResourceLocation RENDERER = TwilightForestMod.prefix("naga_segment");

	private int deathCounter;

	public NagaSegment(Naga naga) {
		super(naga);
		this.setPos(naga.getX(), naga.getY(), naga.getZ());
	}

	@Override
	protected void defineSynchedData() {
		this.deactivate();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public ResourceLocation renderer() {
		return RENDERER;
	}

	@Override
	public boolean hurt(DamageSource src, float damage) {
		return !this.isInvisible() && this.getParent().hurt(src, damage * 2.0F / 3.0F);
	}

	@Override
	public boolean is(Entity entity) {
		return entity == this || entity == this.getParent();
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag compound) {

	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compound) {

	}

	@Override
	public void tick() {
		super.tick();

		++this.tickCount;

		if (!this.isInvisible())
			this.collideWithOthers();

		if (this.deathCounter > 0) {
			this.deathCounter--;
			if (this.deathCounter <= 0) {
				Vec3 pos = this.position();
				float width = this.getBbWidth();
				float height = this.getBbHeight();
				for (int k = 0; k < 20; k++) {
					this.level().addParticle(this.random.nextBoolean() ? ParticleTypes.EXPLOSION : ParticleTypes.EXPLOSION_EMITTER,
							(pos.x() + this.random.nextFloat() * width * 2.0F) - width,
							pos.y() + this.random.nextFloat() * height,
							(pos.z() + this.random.nextFloat() * width * 2.0F) - width,
							this.random.nextGaussian() * 0.02D, this.random.nextGaussian() * 0.02D, this.random.nextGaussian() * 0.02D);
				}

				this.getParent().deathTime = 0;
				this.deactivate();
			}
		}
	}

	private void collideWithOthers() {
		List<Entity> list = this.level().getEntities(this, this.getBoundingBox());

		for (Entity entity : list) {
			if (entity.isPushable()) {
				this.collideWithEntity(entity);
			}
		}
	}

	private void collideWithEntity(Entity entity) {
		entity.push(this);

		// attack anything that's not us
		if (entity instanceof LivingEntity && !(entity instanceof Naga) && !this.getParent().isDazed() && !this.getParent().isDeadOrDying()) {
			int attackStrength = 2;

			// get rid of nearby deer & look impressive
			if (entity instanceof Animal) {
				attackStrength *= 3;
			}

			entity.hurt(entity.level().damageSources().mobAttack(this.getParent()), attackStrength);
		}
	}

	public void deactivate() {
		this.setSize(EntityDimensions.scalable(0.0F, 0.0F));
		this.setInvisible(true);
	}

	public void activate() {
		this.setSize(EntityDimensions.scalable(2.0F, 2.0F));
		this.setInvisible(false);
	}

	// make public
	@Override
	public void setRot(float yaw, float pitch) {
		super.setRot(yaw, pitch);
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState block) {
	}

	public void selfDestruct(int counter) {
		this.deathCounter = counter;
	}

	@Override
	public boolean canChangeDimensions() {
		return false;
	}

	@Override
	public float getStepHeight() {
		return 2.0F;
	}
}

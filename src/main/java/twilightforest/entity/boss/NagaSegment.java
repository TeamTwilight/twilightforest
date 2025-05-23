package twilightforest.entity.boss;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.TwilightForestMod;
import twilightforest.entity.TFPart;
import twilightforest.init.TFSounds;

import java.util.List;

public class NagaSegment extends TFPart<Naga> {

	public static final ResourceLocation RENDERER = TwilightForestMod.prefix("naga_segment");

	private int deathCounter;

	public NagaSegment(Naga naga) {
		super(naga);
		this.setPos(naga.getX(), naga.getY(), naga.getZ());
		this.deactivate();
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {

	}

	@Override
	public ResourceLocation renderer() {
		return RENDERER;
	}

	@Override
	public boolean hurtServer(ServerLevel level, DamageSource src, float damage) {
		return !this.isInvisible() && this.getParent().hurtServer(level, src, damage * 2.0F / 3.0F);
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
				Naga naga = this.getParent();
				naga.makePoofAt(this.position());
				naga.playSound(TFSounds.NAGA_HURT.get(), 0.25F, (naga.getVoicePitch() * 0.75F) + (0.5F * naga.getRandom().nextFloat()));
				naga.deathTime = 0;
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

			if (this.level() instanceof ServerLevel level) {
				entity.hurtServer(level, level.damageSources().mobAttack(this.getParent()), attackStrength);
			}
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
	public boolean canUsePortal(boolean force) {
		return false;
	}
}

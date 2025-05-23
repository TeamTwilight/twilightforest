package twilightforest.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFEntities;
import twilightforest.init.TFSounds;

public class KingSpider extends Spider {

	public KingSpider(EntityType<? extends KingSpider> type, Level world) {
		super(type, world);
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Spider.createAttributes()
			.add(Attributes.MAX_HEALTH, 30.0D)
			.add(Attributes.MOVEMENT_SPEED, 0.35D)
			.add(Attributes.ATTACK_DAMAGE, 6.0D);
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.KING_SPIDER_AMBIENT.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.KING_SPIDER_HURT.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.KING_SPIDER_DEATH.get();
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState blockIn) {
		this.playSound(TFSounds.KING_SPIDER_STEP.get(), 0.15F, 1.0F);
	}

	@Override
	public boolean onClimbable() {
		// let's not climb
		return false;
	}

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficulty, EntitySpawnReason reason, @Nullable SpawnGroupData data) {
		data = super.finalizeSpawn(accessor, difficulty, reason, data);

		// will always have a druid riding the spider or whatever is riding the spider
		SkeletonDruid druid = TFEntities.SKELETON_DRUID.get().create(this.level(), EntitySpawnReason.JOCKEY);
		if (druid != null) {
			druid.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
			druid.finalizeSpawn(accessor, difficulty, EntitySpawnReason.JOCKEY, null);
			Entity lastRider = this;
			while (!lastRider.getPassengers().isEmpty())
				lastRider = lastRider.getPassengers().getFirst();
			druid.startRiding(lastRider);
		}
		return data;
	}

	@Override
	protected Vec3 getPassengerAttachmentPoint(Entity entity, EntityDimensions dimensions, float yRot) {
		return new Vec3(0.0F, dimensions.height() * 0.85F, 0.0F);
	}
}

package twilightforest.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
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
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
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
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData data, @Nullable CompoundTag tag) {
		data = super.finalizeSpawn(accessor, difficulty, reason, data, tag);

		// will always have a druid riding the spider or whatever is riding the spider
		SkeletonDruid druid = TFEntities.SKELETON_DRUID.get().create(this.level());
		druid.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
		druid.finalizeSpawn(accessor, difficulty, MobSpawnType.JOCKEY, null, null);
		Entity lastRider = this;
		while (!lastRider.getPassengers().isEmpty())
			lastRider = lastRider.getPassengers().get(0);
		druid.startRiding(lastRider);

		return data;
	}

	@Override
	protected Vector3f getPassengerAttachmentPoint(Entity entity, EntityDimensions dimensions, float yRot) {
		return new Vector3f(0.0F, dimensions.height * 0.85F, 0.0F);
	}
}

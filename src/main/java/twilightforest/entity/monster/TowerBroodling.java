package twilightforest.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFEntities;
import twilightforest.init.TFSounds;

public class TowerBroodling extends SwarmSpider {

	public TowerBroodling(EntityType<? extends TowerBroodling> type, Level world) {
		this(type, world, true);
	}

	public TowerBroodling(EntityType<? extends TowerBroodling> type, Level world, boolean spawnMore) {
		super(type, world, spawnMore);
		this.xpReward = 3;
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return SwarmSpider.registerAttributes()
				.add(Attributes.MAX_HEALTH, 7.0D)
				.add(Attributes.ATTACK_DAMAGE, 4.0D);
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.CARMINITE_BROODLING_AMBIENT.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.CARMINITE_BROODLING_HURT.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.CARMINITE_BROODLING_DEATH.get();
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState state) {
		this.playSound(TFSounds.CARMINITE_BROODLING_STEP.get(), 0.15F, 1.0F);
	}

	@Override
	protected boolean spawnAnother() {
		SwarmSpider another = new TowerBroodling(TFEntities.CARMINITE_BROODLING.get(), this.level(), false);

		double sx = getX() + (this.getRandom().nextBoolean() ? 0.9D : -0.9D);
		double sy = getY();
		double sz = getZ() + (this.getRandom().nextBoolean() ? 0.9D : -0.9D);
		another.moveTo(sx, sy, sz, this.getRandom().nextFloat() * 360.0F, 0.0F);
		if (!another.checkSpawnRules(this.level(), MobSpawnType.MOB_SUMMONED)) {
			another.discard();
			return false;
		}
		this.level().addFreshEntity(another);
		another.spawnAnim();

		return true;
	}

	//no skeleton druid jockeys for us
	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData data, @Nullable CompoundTag tag) {
		return data;
	}
}

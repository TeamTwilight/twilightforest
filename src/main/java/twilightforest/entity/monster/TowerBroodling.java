package twilightforest.entity.monster;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import twilightforest.TFSounds;
import twilightforest.entity.TFEntities;

import javax.annotation.Nullable;

public class TowerBroodling extends SwarmSpider {

	public TowerBroodling(EntityType<? extends TowerBroodling> type, Level world) {
		this(type, world, true);
	}

	public TowerBroodling(EntityType<? extends TowerBroodling> type, Level world, boolean spawnMore) {
		super(type, world, spawnMore);
		xpReward = 3;
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return SwarmSpider.registerAttributes()
				.add(Attributes.MAX_HEALTH, 7.0D)
				.add(Attributes.ATTACK_DAMAGE, 4.0D);
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
		SwarmSpider another = new TowerBroodling(TFEntities.CARMINITE_BROODLING, level, false);

		double sx = getX() + (random.nextBoolean() ? 0.9 : -0.9);
		double sy = getY();
		double sz = getZ() + (random.nextBoolean() ? 0.9 : -0.9);
		another.moveTo(sx, sy, sz, random.nextFloat() * 360F, 0.0F);
		if (!another.checkSpawnRules(level, MobSpawnType.MOB_SUMMONED)) {
			another.discard();
			return false;
		}
		level.addFreshEntity(another);
		another.spawnAnim();

		return true;
	}

	//no skeleton druid jockeys for us
	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData livingData, @Nullable CompoundTag dataTag) {
		return livingData;
	}
}

package twilightforest.entity.passive;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFEntities;
import twilightforest.init.TFSounds;

public class Bighorn extends Sheep {

	public Bighorn(EntityType<? extends Bighorn> type, Level world) {
		super(type, world);
	}

	private static DyeColor getRandomFleeceColor(RandomSource random) {
		return random.nextBoolean()
			? DyeColor.BROWN
			: DyeColor.byId(random.nextInt(16));
	}

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficulty, EntitySpawnReason reason, @Nullable SpawnGroupData livingdata) {
		livingdata = super.finalizeSpawn(accessor, difficulty, reason, livingdata);
		this.setColor(getRandomFleeceColor(accessor.getRandom()));
		return livingdata;
	}

	@Nullable
	@Override
	public Sheep getBreedOffspring(ServerLevel level, AgeableMob ageable) {
		if (!(ageable instanceof Bighorn otherParent)) {
			TwilightForestMod.LOGGER.error("Code was called to breed a Bighorn with a non Bighorn! Cancelling!");
			return null;
		}

		Bighorn babySheep = TFEntities.BIGHORN_SHEEP.get().create(level, EntitySpawnReason.BREEDING);
		if (babySheep != null) babySheep.setColor(DyeColor.getMixedColor(level, this.getColor(), otherParent.getColor()));
		return babySheep;
	}

	@Override
	public float getWalkTargetValue(BlockPos pos, LevelReader reader) {
		BlockState state = reader.getBlockState(pos.below());
		return state.is(Blocks.GRASS_BLOCK) || state.is(Blocks.PODZOL) ? 10.0F : reader.getPathfindingCostFromLightLevels(pos);
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.BIGHORN_SHEEP_AMBIENT.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.BIGHORN_SHEEP_HURT.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.BIGHORN_SHEEP_DEATH.get();
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState state) {
		this.playSound(TFSounds.BIGHORN_SHEEP_STEP.get(), 0.15F, 1.0F);
	}
}

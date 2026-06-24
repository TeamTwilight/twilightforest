package twilightforest.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.init.TFEntities;
import twilightforest.init.TFSounds;

public class TowerBroodling extends SwarmSpider {

	public TowerBroodling(EntityType<? extends TowerBroodling> type, Level world) {
		super(type, world);
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
	public EntityType<? extends SwarmSpider> getReinforcementType() {
		return TFEntities.CARMINITE_BROODLING.get();
	}

	//no skeleton druid jockeys for us
	@Override
	public void summonJockey(ServerLevelAccessor accessor, DifficultyInstance difficulty) {

	}
}

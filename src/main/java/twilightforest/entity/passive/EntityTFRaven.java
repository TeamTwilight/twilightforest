package twilightforest.entity.passive;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.Pose;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import twilightforest.TFSounds;

public class EntityTFRaven extends EntityTFTinyBird {

	public EntityTFRaven(EntityType<? extends EntityTFRaven> type, World world) {
		super(type, world);

		// maybe this will help them move cuter?
		this.stepHeight = 1;
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new SwimGoal(this));
		this.goalSelector.addGoal(1, new PanicGoal(this, 1.5F));
		this.goalSelector.addGoal(2, new TemptGoal(this, 0.85F, true, SEEDS));
		this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 1.0F));
		this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 6F));
		this.goalSelector.addGoal(7, new LookRandomlyGoal(this));
	}

	public static AttributeModifierMap.MutableAttribute registerAttributes() {
		return EntityTFTinyBird.registerAttributes()
				.createMutableAttribute(Attributes.MAX_HEALTH, 10.0D)
				.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.2);
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.RAVEN_CAW;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.RAVEN_SQUAWK;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.RAVEN_SQUAWK;
	}

	@Override
	public float getEyeHeight(Pose pose) {
		return this.getHeight() * 0.75F;
	}

	@Override
	public boolean isSpooked() {
		return this.hurtTime > 0;
	}
}

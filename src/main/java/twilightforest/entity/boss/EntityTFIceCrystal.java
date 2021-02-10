package twilightforest.entity.boss;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import twilightforest.TFSounds;
import twilightforest.entity.EntityTFIceMob;
import twilightforest.entity.TFEntities;

public class EntityTFIceCrystal extends EntityTFIceMob {

	private int crystalAge;
	private int maxCrystalAge = -1;

	public EntityTFIceCrystal(World worldIn) {
		super(TFEntities.ice_crystal, worldIn);
	}

	public EntityTFIceCrystal(EntityType<? extends EntityTFIceCrystal> type, World world) {
		super(type, world);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new SwimGoal(this));
		this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, false));
		this.goalSelector.addGoal(2, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
		this.goalSelector.addGoal(3, new LookAtGoal(this, PlayerEntity.class, 8.0F));
		this.goalSelector.addGoal(3, new LookRandomlyGoal(this));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
	}

	public static AttributeModifierMap.MutableAttribute registerAttributes() {
		return MonsterEntity.func_234295_eP_()
				.createMutableAttribute(Attributes.MAX_HEALTH, 10.0D)
				.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.23D)
				.createMutableAttribute(Attributes.ATTACK_DAMAGE, 5.0D);
	}

	@Override
	public int getMaxSpawnedInChunk() {
		return 8;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.ICE_CORE_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.ICE_CORE_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.ICE_CORE_DEATH;
	}

	public void setToDieIn30Seconds() {
		this.maxCrystalAge = 600;
	}

	@Override
	public void livingTick() {
		super.livingTick();

		if (!world.isRemote) {
			this.crystalAge++;
			if (this.maxCrystalAge > 0 && this.crystalAge >= this.maxCrystalAge) {
				this.remove();
			}
		}
	}
}

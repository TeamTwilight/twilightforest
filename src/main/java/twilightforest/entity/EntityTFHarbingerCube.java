package twilightforest.entity;

import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import twilightforest.TwilightForestMod;

public class EntityTFHarbingerCube extends MobEntity {

	public static final ResourceLocation LOOT_TABLE = TwilightForestMod.prefix("entities/harbinger_cube");

	public EntityTFHarbingerCube(World world) {
		super(world);
		this.setSize(1.9F, 2.4F);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(1, new SwimGoal(this));
		this.goalSelector.addGoal(1, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
		this.goalSelector.addGoal(2, new LookAtGoal(this, PlayerEntity.class, 8.0F));
		this.goalSelector.addGoal(2, new LookRandomlyGoal(this));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this, false));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
	}

	@Override
	protected void registerAttributes() {
		super.registerAttributes();
		this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40.0D);
		this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23D);
	}

	@Override
	protected ResourceLocation getLootTable() {
		return LOOT_TABLE;
	}
}

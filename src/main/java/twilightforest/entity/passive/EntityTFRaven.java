package twilightforest.entity.passive;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import twilightforest.TFSounds;
import twilightforest.TwilightForestMod;
import twilightforest.entity.ai.EntityAITFTempt;

public class EntityTFRaven extends EntityTFTinyBird {

	public static final ResourceLocation LOOT_TABLE = TwilightForestMod.prefix("entities/raven");

	public EntityTFRaven(World world) {
		super(world);
		this.setSize(0.3F, 0.5F);

		// maybe this will help them move cuter?
		this.stepHeight = 1;
	}

	@Override
	protected void initEntityAI() {
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAIPanic(this, 1.5F));
		this.tasks.addTask(2, new EntityAITFTempt(this, 0.85F, true, SEEDS));
		this.tasks.addTask(5, new EntityAIWanderAvoidWater(this, 1.0F));
		this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 6F));
		this.tasks.addTask(7, new EntityAILookIdle(this));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.20000001192092896D);
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
	public ResourceLocation getLootTable() {
		return LOOT_TABLE;
	}

	@Override
	public float getEyeHeight() {
		return this.height * 0.75F;
	}

	@Override
	public float getRenderSizeModifier() {
		return 0.3F;
	}

	@Override
	public boolean isSpooked() {
		return this.hurtTime > 0;
	}

}

package twilightforest.entity.passive;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.LookRandomlyGoal;
import net.minecraft.entity.ai.PanicGoal;
import net.minecraft.entity.ai.SwimGoal;
import net.minecraft.entity.ai.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.ai.LookAtGoal;
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
	protected void registerGoals() {
		this.tasks.addTask(0, new SwimGoal(this));
		this.tasks.addTask(1, new PanicGoal(this, 1.5F));
		this.tasks.addTask(2, new EntityAITFTempt(this, 0.85F, true, SEEDS));
		this.tasks.addTask(5, new WaterAvoidingRandomWalkingGoal(this, 1.0F));
		this.tasks.addTask(6, new LookAtGoal(this, EntityPlayer.class, 6F));
		this.tasks.addTask(7, new LookRandomlyGoal(this));
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

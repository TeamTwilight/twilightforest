package twilightforest.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Difficulty;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import twilightforest.TFFeature;
import twilightforest.TFSounds;

import javax.annotation.Nullable;
import java.util.Random;

public class EntityTFHostileWolf extends WolfEntity implements IMob {

	public EntityTFHostileWolf(EntityType<? extends EntityTFHostileWolf> type, World world) {
		super(type, world);
		setAngry(true);
		setCollarColor(DyeColor.BLACK);
		setAttributes(); // Must call this again because EntityWolf calls setTamed(false) which messes with our changes
	}

	// Split out from registerAttributes because of above comment
	protected void setAttributes() {
		this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0D);
	}

	@Override
	protected final void registerAttributes() {
		super.registerAttributes();
		setAttributes();
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
	}

	@Override
	public void tick() {
		super.tick();
		if (!world.isRemote && world.getDifficulty() == Difficulty.PEACEFUL) {
			remove();
		}
	}

	public static boolean getCanSpawnHere(EntityType<? extends EntityTFHostileWolf> type, IWorld world, SpawnReason reason, BlockPos pos, Random random) {
		// are we near a hedge maze?
		int chunkX = MathHelper.floor(pos.getX()) >> 4;
		int chunkZ = MathHelper.floor(pos.getZ()) >> 4;
		return (TFFeature.getNearestFeature(chunkX, chunkZ, world.getWorld()) == TFFeature.HEDGE_MAZE || MonsterEntity.isValidLightLevel(world, pos, random));
				/*&& world.checkNoEntityCollision(this)
				&& world.getCollisionBoxes(this, getBoundingBox()).size() == 0
				&& !world.containsAnyLiquid(getBoundingBox());*/
	}

	@Override
	public void setAttackTarget(@Nullable LivingEntity entity) {
		if (entity != null && entity != getAttackTarget())
			playSound(TFSounds.MISTWOLF_TARGET, 4F, getSoundPitch());
		super.setAttackTarget(entity);
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.MISTWOLF_IDLE;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return TFSounds.MISTWOLF_HURT;
	}

	@Override
	public boolean isBreedingItem(ItemStack stack) {
		return false;
	}

	@Override
	public boolean processInteract(PlayerEntity player, Hand hand) {
		return false;
	}
}

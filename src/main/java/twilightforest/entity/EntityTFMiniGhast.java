package twilightforest.entity;

import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import twilightforest.TFSounds;

import java.util.Random;

public class EntityTFMiniGhast extends EntityTFTowerGhast {

	private boolean isMinion = false;

	public EntityTFMiniGhast(EntityType<? extends EntityTFMiniGhast> type, World world) {
		super(type, world);
		this.wanderFactor = 4.0F;
	}

	@Override
	public int getMaxSpawnedInChunk() {
		return 16;
	}

	public static AttributeModifierMap.MutableAttribute registerAttributes() {
		return EntityTFTowerGhast.registerAttributes()
				.createMutableAttribute(Attributes.MAX_HEALTH,/* this.isMinion ? 6 :*/ 10) //TODO
				.createMutableAttribute(Attributes.FOLLOW_RANGE, 16.0D);
	}

	@Override
	protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
		return 0.5F;
	}
	
	@Override
	protected SoundEvent getAmbientSound() {
	      return TFSounds.GHASTLING_AMBIENT;
	   }

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
	      return TFSounds.GHASTLING_HURT;
	   }

	@Override
	protected SoundEvent getDeathSound() {
	      return TFSounds.GHASTLING_DEATH;
	   }

	// Loosely based on EntityEnderman.shouldAttackPlayer
	@Override
	protected boolean shouldAttack(LivingEntity living) {
		ItemStack helmet = living.getItemStackFromSlot(EquipmentSlotType.HEAD);
		if (!helmet.isEmpty() && helmet.getItem() == Item.getItemFromBlock(Blocks.PUMPKIN)) {
			return false;
		} else if (living.getDistance(this) <= 3.5F) {
			return living.canEntityBeSeen(this);
		} else {
			Vector3d vec3d = living.getLook(1.0F).normalize();
			Vector3d vec3d1 = new Vector3d(this.getPosX() - living.getPosX(), this.getBoundingBox().minY + this.getEyeHeight() - (living.getPosY() + living.getEyeHeight()), this.getPosZ() - living.getPosZ());
			double d0 = vec3d1.length();
			vec3d1 = vec3d1.normalize();
			double d1 = vec3d.dotProduct(vec3d1);
			return d1 > 1.0D - 0.025D / d0 ? living.canEntityBeSeen(this) : false;
		}
	}

	//This does not factor into whether the entity is a Minion or not. However, since it is spawned via MOB_SUMMONED, it will always spawn if that is the SpawnReason
	public static boolean canSpawnHere(EntityType<EntityTFMiniGhast> entity, IServerWorld world, SpawnReason reason, BlockPos pos, Random random) {
		return world.getDifficulty() != Difficulty.PEACEFUL && (reason == SpawnReason.MOB_SUMMONED || MonsterEntity.isValidLightLevel(world, pos, random)) && canSpawnOn(entity, world, reason, pos, random);
	}

	public void makeBossMinion() {
		this.wanderFactor = 0.005F;
		this.isMinion = true;

		this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(6);
		this.setHealth(6);
	}

	public boolean isMinion() {
		return isMinion;
	}

	@Override
	public void writeAdditional(CompoundNBT compound) {
		compound.putBoolean("isMinion", this.isMinion);
		super.writeAdditional(compound);
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);
		if (compound.getBoolean("isMinion")) {
			makeBossMinion();
		}
	}
}

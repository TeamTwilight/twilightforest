package twilightforest.entity.monster;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class RisingZombie extends Zombie {

	public RisingZombie(EntityType<? extends RisingZombie> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	protected void registerGoals() {
		// NO-OP
	}

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData livingdata, @Nullable CompoundTag dataTag) {
		// NO-OP
		return livingdata;
	}

	@Override
	public void aiStep() {
		setDeltaMovement(new Vec3(0, 0, 0));
		super.aiStep();
		if (!level.isClientSide && tickCount % 130 == 0) {
			discard();
			Zombie zombie = new Zombie(level);
			zombie.teleportTo(getX(), getY(), getZ());
			zombie.getAttribute(Attributes.MAX_HEALTH).setBaseValue(getMaxHealth());
			zombie.setHealth(getHealth());
			zombie.setBaby(isBaby());
			level.addFreshEntity(zombie);
			if (random.nextBoolean() && level.getBlockState(blockPosition().below()).getBlock() == Blocks.GRASS_BLOCK)
				level.setBlockAndUpdate(blockPosition().below(), Blocks.DIRT.defaultBlockState());
		}
		if (level.isClientSide && !level.isEmptyBlock(blockPosition().below())) {
			for (int i = 0; i < 5; i++)
				level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, level.getBlockState(blockPosition().below())), getX() + random.nextGaussian() * 0.01F, getY() + random.nextGaussian() * 0.01F, getZ() + random.nextGaussian() * 0.01F, 0, 0, 0);
		}
	}

	@Override
	public void knockback(double strength, double xRatio, double zRatio) {
		//NO-OP
	}

	@Override
	protected boolean canRide(Entity entityIn) {
		return false;
	}
}

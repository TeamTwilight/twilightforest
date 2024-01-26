package twilightforest.entity.monster;

import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

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
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData data, @Nullable CompoundTag tag) {
		// NO-OP
		return data;
	}

	@Override
	public void aiStep() {
		this.setDeltaMovement(new Vec3(0, 0, 0));
		super.aiStep();
		if (!this.level().isClientSide() && this.tickCount % 130 == 0) {
			this.discard();
			Zombie zombie = new Zombie(this.level());
			zombie.teleportTo(this.getX(), this.getY(), this.getZ());
			Objects.requireNonNull(zombie.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(this.getMaxHealth());
			zombie.setHealth(this.getHealth());
			zombie.setBaby(this.isBaby());
			this.level().addFreshEntity(zombie);
			if (this.getRandom().nextBoolean() && this.level().getBlockState(blockPosition().below()).is(Blocks.GRASS_BLOCK))
				this.level().setBlockAndUpdate(blockPosition().below(), Blocks.DIRT.defaultBlockState());
		}
		if (this.level().isClientSide() && !this.level().isEmptyBlock(blockPosition().below())) {
			for (int i = 0; i < 5; i++)
				this.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, this.level().getBlockState(blockPosition().below())), getX() + this.getRandom().nextGaussian() * 0.01F, getY() + this.getRandom().nextGaussian() * 0.01F, getZ() + this.getRandom().nextGaussian() * 0.01F, 0, 0, 0);
		}
	}

	@Override
	public void knockback(double strength, double xRatio, double zRatio) {
		//NO-OP
	}

	@Override
	protected boolean canRide(Entity entity) {
		return false;
	}
}

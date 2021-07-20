package twilightforest.entity;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EntityTFRisingZombie extends ZombieEntity {

	public EntityTFRisingZombie(EntityType<? extends EntityTFRisingZombie> type, World worldIn) {
		super(type, worldIn);
	}

	@Override
	protected void registerGoals() {
		// NO-OP
	}

	@Nullable
	@Override
	public ILivingEntityData onInitialSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData livingdata, @Nullable CompoundNBT dataTag) {
		// NO-OP
		return livingdata;
	}

	@Override
	public void livingTick() {
		setMotion(new Vector3d(0, 0, 0));
		super.livingTick();
		if (!world.isRemote && ticksExisted % 130 == 0) {
			remove();
			ZombieEntity zombie = new ZombieEntity(world);
			zombie.setPositionAndUpdate(getPosX(), getPosY(), getPosZ());
			zombie.getAttribute(Attributes.MAX_HEALTH).setBaseValue(getMaxHealth());
			zombie.setHealth(getHealth());
			zombie.setChild(isChild());
			world.addEntity(zombie);
			if (rand.nextBoolean() && world.getBlockState(getPosition().down()).getBlock() == Blocks.GRASS_BLOCK)
				world.setBlockState(getPosition().down(), Blocks.DIRT.getDefaultState());
		}
		if (world.isRemote && !world.isAirBlock(getPosition().down())) {
			for (int i = 0; i < 5; i++)
				world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, world.getBlockState(getPosition().down())), getPosX() + rand.nextGaussian() * 0.01F, getPosY() + rand.nextGaussian() * 0.01F, getPosZ() + rand.nextGaussian() * 0.01F, 0, 0, 0);
		}
	}

	@Override
	public void applyKnockback(float strength, double xRatio, double zRatio) {
		//NO-OP
	}

	@Override
	protected boolean canBeRidden(Entity entityIn) {
		return false;
	}
}

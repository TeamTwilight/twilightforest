package twilightforest.entity;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EntityTFRisingZombie extends EntityZombie {

	public EntityTFRisingZombie(World worldIn) {
		super(worldIn);
	}

	@Override
	protected void initEntityAI() {
		// NO-OP
	}

	@Nullable
	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
		// NO-OP
		return livingdata;
	}

	@Override
	public void onLivingUpdate() {
		motionX = 0;
		motionY = 0;
		motionZ = 0;
		super.onLivingUpdate();
		if (!world.isRemote && ticksExisted % 130 == 0) {
			setDead();
			EntityZombie zombie = new EntityZombie(world);
			zombie.setPositionAndUpdate(posX, posY, posZ);
			zombie.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(getMaxHealth());
			zombie.setHealth(getHealth());
			zombie.setChild(isChild());
			world.spawnEntity(zombie);
			if (rand.nextBoolean() && world.getBlockState(getPosition().down()).getBlock() == Blocks.GRASS)
				world.setBlockState(getPosition().down(), Blocks.DIRT.getDefaultState());
		}
		if (world.isRemote && !world.isAirBlock(getPosition().down())) {
			for (int i = 0; i < 5; i++)
				world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, posX + rand.nextGaussian() * 0.01F, posY + rand.nextGaussian() * 0.01F, posZ + rand.nextGaussian() * 0.01F, 0, 0, 0, Block.getStateId(world.getBlockState(getPosition().down())));
		}
	}

	@Override
	public void knockBack(Entity entityIn, float strength, double xRatio, double zRatio) {
		//NO-OP
	}
}

package twilightforest.entity;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import twilightforest.TFFeature;


public class EntityTFHostileWolf extends EntityWolf implements IMob {

	public EntityTFHostileWolf(World world) {
		super(world);
		setAngry(true);
		setCollarColor(EnumDyeColor.BLACK);
		setAttributes(); // Must call this again because EntityWolf calls setTamed(false) which messes with our changes
	}

	// Split out from applyEntityAttributes because of above comment
	protected void setAttributes() {
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0D);
	}

	@Override
	protected final void applyEntityAttributes() {
		super.applyEntityAttributes();
		setAttributes();
	}

	@Override
	protected void initEntityAI() {
		super.initEntityAI();
		this.targetTasks.addTask(4, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		if (!world.isRemote && world.getDifficulty() == EnumDifficulty.PEACEFUL) {
			setDead();
		}
	}

	@Override
	public boolean getCanSpawnHere() {
		// are we near a hedge maze?
		int chunkX = MathHelper.floor(posX) >> 4;
		int chunkZ = MathHelper.floor(posZ) >> 4;
		return (TFFeature.getNearestFeature(chunkX, chunkZ, world) == TFFeature.HEDGE_MAZE || isValidLightLevel())
				&& world.checkNoEntityCollision(getEntityBoundingBox())
				&& world.getCollisionBoxes(this, getEntityBoundingBox()).size() == 0
				&& !world.containsAnyLiquid(getEntityBoundingBox());
	}

	// [VanillaCopy] Direct copy of EntityMob.isValidLightLevel
	protected boolean isValidLightLevel() {
		BlockPos blockpos = new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ);

		if (this.world.getLightFor(EnumSkyBlock.SKY, blockpos) > this.rand.nextInt(32)) {
			return false;
		} else {
			int i = this.world.getLightFromNeighbors(blockpos);

			if (this.world.isThundering()) {
				int j = this.world.getSkylightSubtracted();
				this.world.setSkylightSubtracted(10);
				i = this.world.getLightFromNeighbors(blockpos);
				this.world.setSkylightSubtracted(j);
			}

			return i <= this.rand.nextInt(8);
		}
	}

	@Override
	public boolean isBreedingItem(ItemStack par1ItemStack) {
		return false;
	}

	@Override
	public boolean processInteract(EntityPlayer player, EnumHand hand) {
		return false;
	}

}

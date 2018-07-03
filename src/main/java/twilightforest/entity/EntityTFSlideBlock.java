package twilightforest.entity;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.TFSounds;

import javax.annotation.Nonnull;
import java.util.List;

public class EntityTFSlideBlock extends Entity implements IEntityAdditionalSpawnData {
	private static final int WARMUP_TIME = 20;
	private static final DataParameter<EnumFacing> MOVE_DIRECTION = EntityDataManager.createKey(EntityTFSlideBlock.class, DataSerializers.FACING);

	private IBlockState myState;
	private int slideTime;

	public EntityTFSlideBlock(World world) {
		super(world);
		this.preventEntitySpawning = true;
		this.entityCollisionReduction = 1F;
		this.setSize(0.98F, 0.98F);
		//this.yOffset = this.height / 2.0F;
	}


	public EntityTFSlideBlock(World world, double x, double y, double z, IBlockState state) {
		super(world);

		this.myState = state;
		this.preventEntitySpawning = true;
		this.entityCollisionReduction = 1F;
		this.setSize(0.98F, 0.98F);
		//this.yOffset = this.height / 2.0F;
		this.setPosition(x, y, z);
		this.motionX = 0.0D;
		this.motionY = 0.0D;
		this.motionZ = 0.0D;
		this.prevPosX = x;
		this.prevPosY = y;
		this.prevPosZ = z;

		this.determineMoveDirection();
	}

	private void determineMoveDirection() {
		BlockPos pos = new BlockPos(this);

		EnumFacing[] toCheck;

		switch (myState.getValue(BlockRotatedPillar.AXIS)) {
			case X: // horizontal blocks will go up or down if there is a block on one side and air on the other
				toCheck = new EnumFacing[]{EnumFacing.DOWN, EnumFacing.UP, EnumFacing.NORTH, EnumFacing.SOUTH};
				break;
			case Z: // horizontal blocks will go up or down if there is a block on one side and air on the other
				toCheck = new EnumFacing[]{EnumFacing.DOWN, EnumFacing.UP, EnumFacing.WEST, EnumFacing.EAST};
				break;
			default:
			case Y: // vertical blocks priority is -x, +x, -z, +z
				toCheck = new EnumFacing[]{EnumFacing.WEST, EnumFacing.EAST, EnumFacing.NORTH, EnumFacing.SOUTH};
				break;
		}

		for (EnumFacing e : toCheck) {
			if (world.isAirBlock(pos.offset(e)) && !world.isAirBlock(pos.offset(e.getOpposite()))) {
				dataManager.set(MOVE_DIRECTION, e);
				return;
			}
		}

		// if no wall, travel towards open air
		for (EnumFacing e : toCheck) {
			if (world.isAirBlock(pos.offset(e))) {
				dataManager.set(MOVE_DIRECTION, e);
				return;
			}
		}
	}

	@Override
	protected void entityInit() {
		dataManager.register(MOVE_DIRECTION, EnumFacing.DOWN);
	}

	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	@Override
	public boolean canBeCollidedWith() {
		return !this.isDead;
	}

	@Override
	public void onUpdate() {
		if (this.myState == null || this.myState.getMaterial() == Material.AIR) {
			this.setDead();
		} else {
			this.prevPosX = this.posX;
			this.prevPosY = this.posY;
			this.prevPosZ = this.posZ;
			++this.slideTime;
			// start moving after warmup
			if (this.slideTime > WARMUP_TIME) {
				this.motionX += dataManager.get(MOVE_DIRECTION).getFrontOffsetX() * 0.04;
				this.motionY += dataManager.get(MOVE_DIRECTION).getFrontOffsetY() * 0.04;
				this.motionZ += dataManager.get(MOVE_DIRECTION).getFrontOffsetZ() * 0.04;
				this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
			}
			this.motionX *= 0.98;
			this.motionY *= 0.98;
			this.motionZ *= 0.98;

			if (!this.world.isRemote) {
				if (this.slideTime % 5 == 0) {
					playSound(TFSounds.SLIDER, 1.0F, 0.9F + (this.rand.nextFloat() * 0.4F));
				}

				BlockPos pos = new BlockPos(this);

				if (this.slideTime == 1) {
					if (this.world.getBlockState(pos) != this.myState) {
						this.setDead();
						return;
					}

					this.world.setBlockToAir(pos);
				}

				if (this.slideTime == WARMUP_TIME + 40) {
					this.motionX = 0;
					this.motionY = 0;
					this.motionZ = 0;

					dataManager.set(MOVE_DIRECTION, dataManager.get(MOVE_DIRECTION).getOpposite());
				}

				if (this.collided) {
					this.motionX *= 0.699999988079071D;
					this.motionZ *= 0.699999988079071D;
					this.motionY *= 0.699999988079071D;

					this.setDead();

					if (this.world.mayPlace(myState.getBlock(), pos, true, EnumFacing.UP, null)) {
						world.setBlockState(pos, myState);
					} else {
						this.entityDropItem(new ItemStack(myState.getBlock(), 1, myState.getBlock().damageDropped(myState)), 0.0F);
					}
				} else if (this.slideTime > 100 && (pos.getY() < 1 || pos.getY() > 256) || this.slideTime > 600) {
					this.entityDropItem(new ItemStack(this.myState.getBlock(), 1, this.myState.getBlock().damageDropped(this.myState)), 0.0F);
					this.setDead();
				}

				// push things out and damage them
				this.damageKnockbackEntities(this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox()));
			}
		}
	}

	private void damageKnockbackEntities(List<Entity> par1List) {
		for (Entity entity : par1List) {
			if (entity instanceof EntityLivingBase) {
				entity.attackEntityFrom(DamageSource.GENERIC, 5);

				double kx = (this.posX - entity.posX) * 2.0;
				double kz = (this.posZ - entity.posZ) * 2.0;

				((EntityLivingBase) entity).knockBack(this, 2, kx, kz);
			}
		}
	}

	@Override
	public AxisAlignedBB getCollisionBox(Entity p_70114_1_) {
		return null;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean canRenderOnFire() {
		return false;
	}

	//Atomic: Suppressed deprecation, Ideally I'd use a state string here, but that is more work than I'm willing to put in right now.
	@SuppressWarnings("deprecation")
	@Override
	protected void readEntityFromNBT(@Nonnull NBTTagCompound nbtTagCompound) {
		Block b = Block.REGISTRY.getObject(new ResourceLocation(nbtTagCompound.getString("TileID")));
		int meta = nbtTagCompound.getByte("Meta");
		this.myState = b.getStateFromMeta(meta);
		this.slideTime = nbtTagCompound.getInteger("Time");
		dataManager.set(MOVE_DIRECTION, EnumFacing.getFront(nbtTagCompound.getByte("Direction")));
	}

	@Override
	protected void writeEntityToNBT(@Nonnull NBTTagCompound nbtTagCompound) {
		nbtTagCompound.setString("TileID", myState.getBlock().getRegistryName().toString());
		nbtTagCompound.setByte("Meta", (byte) this.myState.getBlock().getMetaFromState(myState));
		nbtTagCompound.setInteger("Time", this.slideTime);
		nbtTagCompound.setByte("Direction", (byte) dataManager.get(MOVE_DIRECTION).getIndex());
	}

	@Override
	public void writeSpawnData(ByteBuf buffer) {
		buffer.writeInt(Block.getStateId(myState));
	}

	@Override
	public void readSpawnData(ByteBuf additionalData) {
		myState = Block.getStateById(additionalData.readInt());
	}

	@Override
	public boolean canBePushed() {
		return false;
	}

	@Override
	public boolean isPushedByWater() {
		return false;
	}

	public IBlockState getBlockState() {
		return myState;
	}
}

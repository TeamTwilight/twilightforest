package twilightforest.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;
import twilightforest.TFSounds;

import javax.annotation.Nonnull;
import java.util.List;

public class EntityTFSlideBlock extends Entity implements IEntityAdditionalSpawnData {

	private static final int WARMUP_TIME = 20;
	private static final DataParameter<Direction> MOVE_DIRECTION = EntityDataManager.createKey(EntityTFSlideBlock.class, DataSerializers.DIRECTION);

	private BlockState myState;
	private int slideTime;

	public EntityTFSlideBlock(EntityType<? extends EntityTFSlideBlock> type, World world) {
		super(type, world);
		this.preventEntitySpawning = true;
		this.entityCollisionReduction = 1F;
		//this.yOffset = this.height / 2.0F;
	}

	public EntityTFSlideBlock(EntityType<? extends EntityTFSlideBlock> type, World world, double x, double y, double z, BlockState state) {
		super(type, world);

		this.myState = state;
		this.preventEntitySpawning = true;
		this.entityCollisionReduction = 1F;
		//this.yOffset = this.height / 2.0F;
		this.setPosition(x, y, z);
		this.setMotion(new Vec3d(0, 0, 0));
		this.prevPosX = x;
		this.prevPosY = y;
		this.prevPosZ = z;

		this.determineMoveDirection();
	}

	private void determineMoveDirection() {
		BlockPos pos = new BlockPos(this);

		Direction[] toCheck;

		switch (myState.get(RotatedPillarBlock.AXIS)) {
			case X: // horizontal blocks will go up or down if there is a block on one side and air on the other
				toCheck = new Direction[]{Direction.DOWN, Direction.UP, Direction.NORTH, Direction.SOUTH};
				break;
			case Z: // horizontal blocks will go up or down if there is a block on one side and air on the other
				toCheck = new Direction[]{Direction.DOWN, Direction.UP, Direction.WEST, Direction.EAST};
				break;
			default:
			case Y: // vertical blocks priority is -x, +x, -z, +z
				toCheck = new Direction[]{Direction.WEST, Direction.EAST, Direction.NORTH, Direction.SOUTH};
				break;
		}

		for (Direction e : toCheck) {
			if (world.isAirBlock(pos.offset(e)) && !world.isAirBlock(pos.offset(e.getOpposite()))) {
				dataManager.set(MOVE_DIRECTION, e);
				return;
			}
		}

		// if no wall, travel towards open air
		for (Direction e : toCheck) {
			if (world.isAirBlock(pos.offset(e))) {
				dataManager.set(MOVE_DIRECTION, e);
				return;
			}
		}
	}

	@Override
	protected void registerData() {
		dataManager.register(MOVE_DIRECTION, Direction.DOWN);
	}

	@Override
	public boolean bypassesSteppingEffects() {
		return false;
	}

	@Override
	public boolean canBeCollidedWith() {
		return this.isAlive();
	}

	@Override
	public void tick() {
		if (this.myState == null || this.myState.getMaterial() == Material.AIR) {
			this.remove();
		} else {
			this.prevPosX = this.getX();
			this.prevPosY = this.getY();
			this.prevPosZ = this.getZ();
			++this.slideTime;
			// start moving after warmup
			if (this.slideTime > WARMUP_TIME) {
				final double moveAcceleration = 0.04;
				Direction moveDirection = dataManager.get(MOVE_DIRECTION);
//				this.motionX += moveDirection.getXOffset() * moveAcceleration;
//				this.motionY += moveDirection.getYOffset() * moveAcceleration;
//				this.motionZ += moveDirection.getZOffset() * moveAcceleration;
				this.getMotion().add(moveDirection.getXOffset() * moveAcceleration, moveDirection.getYOffset() * moveAcceleration, moveDirection.getZOffset() * moveAcceleration);
				this.move(MoverType.SELF, new Vec3d(this.getMotion().getX(), this.getMotion().getY(), this.getMotion().getZ()));
			}
//			this.motionX *= 0.98;
//			this.motionY *= 0.98;
//			this.motionZ *= 0.98;
			this.getMotion().mul(0.98, 0.98, 0.98);

			if (!this.world.isRemote) {
				if (this.slideTime % 5 == 0) {
					playSound(TFSounds.SLIDER, 1.0F, 0.9F + (this.rand.nextFloat() * 0.4F));
				}

				BlockPos pos = new BlockPos(this);

				if (this.slideTime == 1) {
					if (this.world.getBlockState(pos) != this.myState) {
						this.remove();
						return;
					}

					this.world.removeBlock(pos, false);
				}

				if (this.slideTime == WARMUP_TIME + 40) {
					this.setMotion(new Vec3d(0, 0, 0));

					dataManager.set(MOVE_DIRECTION, dataManager.get(MOVE_DIRECTION).getOpposite());
				}

				if (this.collided) {
					this.setMotion(this.getMotion().mul(0.699999988079071D, 0.699999988079071D, 0.699999988079071D));

					this.remove();

					if (this.world.canPlace(myState, pos, ISelectionContext.dummy())) {
						world.setBlockState(pos, myState);
					} else {
						// TODO: This and the below item might not be correctly reflecting the state
						this.entityDropItem(new ItemStack(myState.getBlock()), 0.0F);
					}
				} else if (this.slideTime > 100 && (pos.getY() < 1 || pos.getY() > 256) || this.slideTime > 600) {
					this.entityDropItem(new ItemStack(this.myState.getBlock()), 0.0F);
					this.remove();
				}

				// push things out and damage them
				this.damageKnockbackEntities(this.world.getEntitiesWithinAABBExcludingEntity(this, this.getBoundingBox()));
			}
		}
	}

	private void damageKnockbackEntities(List<Entity> entities) {
		for (Entity entity : entities) {
			if (entity instanceof LivingEntity) {
				entity.attackEntityFrom(DamageSource.GENERIC, 5);

				double kx = (this.getX() - entity.getX()) * 2.0;
				double kz = (this.getZ() - entity.getZ()) * 2.0;

				((LivingEntity) entity).knockBack(this, 2, kx, kz);
			}
		}
	}

	@Override
	public AxisAlignedBB getCollisionBox(Entity entity) {
		return null;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean canRenderOnFire() {
		return false;
	}

	//Atomic: Suppressed deprecation, Ideally I'd use a state string here, but that is more work than I'm willing to put in right now.
	// TODO: use NBTUtil functions
	// TODO: Flattening happened. Meta does not exist
	@SuppressWarnings("deprecation")
	@Override
	protected void readAdditional(@Nonnull CompoundNBT compound) {
		//Block b = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(compound.getString("TileID")));
		//int meta = compound.getByte("Meta");
		//this.myState = b.getStateFromMeta(meta);
		this.slideTime = compound.getInt("Time");
		dataManager.set(MOVE_DIRECTION, Direction.byIndex(compound.getByte("Direction")));
	}

	@Override
	protected void writeAdditional(@Nonnull CompoundNBT compound) {
		compound.putString("TileID", myState.getBlock().getRegistryName().toString());
		//compound.putByte("Meta", (byte) this.myState.getBlock().getMetaFromState(myState));
		compound.putInt("Time", this.slideTime);
		compound.putByte("Direction", (byte) dataManager.get(MOVE_DIRECTION).getIndex());
	}

	@Override
	public void writeSpawnData(PacketBuffer buffer) {
		buffer.writeInt(Block.getStateId(myState));
	}

	@Override
	public void readSpawnData(PacketBuffer additionalData) {
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

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	public BlockState getBlockState() {
		return myState;
	}
}

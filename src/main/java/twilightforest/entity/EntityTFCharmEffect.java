package twilightforest.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@OnlyIn(
				value = Dist.CLIENT,
				_interface = IRendersAsItem.class
)
public class EntityTFCharmEffect extends Entity implements IRendersAsItem {
	private static final DataParameter<Integer> DATA_OWNER = EntityDataManager.createKey(EntityTFCharmEffect.class, DataSerializers.VARINT);
	private static final DataParameter<ItemStack> DATA_ITEMID = EntityDataManager.createKey(EntityTFCharmEffect.class, DataSerializers.ITEMSTACK);
	private static final double DISTANCE = 1.75;
	private double interpTargetX;
	private double interpTargetY;
	private double interpTargetZ;
	private double interpTargetYaw;
	private double interpTargetPitch;
	private int newPosRotationIncrements;

	public float offset;

	public EntityTFCharmEffect(EntityType<? extends EntityTFCharmEffect> type, World world) {
		super(type, world);
	}

	public EntityTFCharmEffect(EntityType<? extends EntityTFCharmEffect> type, World world, LivingEntity owner, Item item) {
		this(type, world);

		this.setOwner(owner);
		this.setItemID(item);

		Vector3d look = new Vector3d(DISTANCE, 0, 0);

		this.setLocationAndAngles(owner.getPosX(), owner.getPosY() + owner.getEyeHeight(), owner.getPosZ(), owner.rotationYaw, owner.rotationPitch);
		double x = getPosX() + look.x * DISTANCE;
		//this.getPosY() += look.y * DISTANCE;
		double z = getPosZ() + look.z * DISTANCE;
		this.setPosition(x, this.getPosY(), z);
	}

	@Override
	public void tick() {
		this.lastTickPosX = this.getPosX();
		this.lastTickPosY = this.getPosY();
		this.lastTickPosZ = this.getPosZ();
		super.tick();

		//[VanillaCopy] Beginning of LivingEntity.onLivingUpdate
		if (this.newPosRotationIncrements > 0) {
			double d0 = this.getPosX() + (this.interpTargetX - this.getPosX()) / this.newPosRotationIncrements;
			double d1 = this.getPosY() + (this.interpTargetY - this.getPosY()) / this.newPosRotationIncrements;
			double d2 = this.getPosZ() + (this.interpTargetZ - this.getPosZ()) / this.newPosRotationIncrements;
			double d3 = MathHelper.wrapDegrees(this.interpTargetYaw - this.rotationYaw);
			this.rotationYaw = (float) (this.rotationYaw + d3 / this.newPosRotationIncrements);
			this.rotationPitch = (float) (this.rotationPitch + (this.interpTargetPitch - this.rotationPitch) / this.newPosRotationIncrements);
			--this.newPosRotationIncrements;
			this.setPosition(d0, d1, d2);
			this.setRotation(this.rotationYaw, this.rotationPitch);
		}

		LivingEntity orbiting = getOwner();

		if (orbiting != null) {
			this.setLocationAndAngles(orbiting.getPosX(), orbiting.getPosY() + orbiting.getEyeHeight(), orbiting.getPosZ(), orbiting.rotationYaw, orbiting.rotationPitch);

			float rotation = this.ticksExisted / 5.0F + offset;
			Vector3d look = new Vector3d(DISTANCE, 0, 0).rotateYaw(rotation);
			//this.getX() += look.x;
//        	this.getY() += Math.sin(this.ticksExisted / 3.0F + offset);
			//this.getZ() += look.z;
			this.getPositionVec().add(look.x, 0.0D, look.z);

			this.setPosition(this.getPosX(), this.getPosY(), this.getPosZ());
		}

		if (!this.getItemID().isEmpty()) {
			for (int i = 0; i < 3; i++) {
				double dx = getPosX() + 0.5 * (rand.nextDouble() - rand.nextDouble());
				double dy = getPosY() + 0.5 * (rand.nextDouble() - rand.nextDouble());
				double dz = getPosZ() + 0.5 * (rand.nextDouble() - rand.nextDouble());

				world.addParticle(new ItemParticleData(ParticleTypes.ITEM, getItemID()), dx, dy, dz, 0, 0.2, 0);
			}
		}

		if (!this.world.isRemote
				&& (this.ticksExisted > 200 || (orbiting != null && !orbiting.isAlive()))) {
			this.remove();
		}
	}

	@Override
	public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
		this.interpTargetX = x;
		this.interpTargetY = y;
		this.interpTargetZ = z;
		this.interpTargetYaw = yaw;
		this.interpTargetPitch = pitch;
		this.newPosRotationIncrements = posRotationIncrements;
	}

	@Nonnull
	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	protected void registerData() {
		dataManager.register(DATA_ITEMID, ItemStack.EMPTY);
		dataManager.register(DATA_OWNER, -1);
	}

	public void setOwner(LivingEntity owner) {
		dataManager.set(DATA_OWNER, owner.getEntityId());
	}

	@Nullable
	public LivingEntity getOwner() {
		Entity e = this.world.getEntityByID(dataManager.get(DATA_OWNER));
		if (e instanceof LivingEntity)
			return (LivingEntity) e;
		else return null;
	}

	public ItemStack getItemID() {
		return dataManager.get(DATA_ITEMID);
	}

	public void setItemID(Item item) {
		dataManager.set(DATA_ITEMID, new ItemStack(item));
	}

	@Override
	protected void readAdditional(CompoundNBT cmp) {}

	@Override
	protected void writeAdditional(CompoundNBT cmp) {}

	@Nonnull
	@Override
	public ItemStack getItem() {
		return getItemID();
	}
}

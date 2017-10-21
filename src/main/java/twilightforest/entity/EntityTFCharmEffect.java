package twilightforest.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityTFCharmEffect extends Entity {
	private static final DataParameter<Integer> DATA_OWNER = EntityDataManager.createKey(EntityTFCharmEffect.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> DATA_ITEMID = EntityDataManager.createKey(EntityTFCharmEffect.class, DataSerializers.VARINT);
	private static final double DISTANCE = 1.75;
	private double interpTargetX;
	private double interpTargetY;
	private double interpTargetZ;
	private double interpTargetYaw;
	private double interpTargetPitch;
	private int newPosRotationIncrements;

	public float offset;

	public EntityTFCharmEffect(World par1World) {
		super(par1World);
		this.setSize(0.25F, 0.25F);
	}

	public EntityTFCharmEffect(World par1World, EntityLivingBase owner, Item item) {
		this(par1World);

		this.setOwner(owner);
		this.setItemID(item);

		Vec3d look = new Vec3d(DISTANCE, 0, 0);

		this.setLocationAndAngles(owner.posX, owner.posY + owner.getEyeHeight(), owner.posZ, owner.rotationYaw, owner.rotationPitch);
		this.posX += look.x * DISTANCE;
		//this.posY += look.y * DISTANCE;
		this.posZ += look.z * DISTANCE;
		this.setPosition(this.posX, this.posY, this.posZ);
	}

	@Override
	public void onUpdate() {
		this.lastTickPosX = this.posX;
		this.lastTickPosY = this.posY;
		this.lastTickPosZ = this.posZ;
		super.onUpdate();

		//[VanillaCopy] Beginning of EntityLivingBase.onLivingUpdate
		if (this.newPosRotationIncrements > 0) {
			double d0 = this.posX + (this.interpTargetX - this.posX) / (double) this.newPosRotationIncrements;
			double d1 = this.posY + (this.interpTargetY - this.posY) / (double) this.newPosRotationIncrements;
			double d2 = this.posZ + (this.interpTargetZ - this.posZ) / (double) this.newPosRotationIncrements;
			double d3 = MathHelper.wrapDegrees(this.interpTargetYaw - (double) this.rotationYaw);
			this.rotationYaw = (float) ((double) this.rotationYaw + d3 / (double) this.newPosRotationIncrements);
			this.rotationPitch = (float) ((double) this.rotationPitch + (this.interpTargetPitch - (double) this.rotationPitch) / (double) this.newPosRotationIncrements);
			--this.newPosRotationIncrements;
			this.setPosition(d0, d1, d2);
			this.setRotation(this.rotationYaw, this.rotationPitch);
		}

		EntityLivingBase orbiting = getOwner();

		if (orbiting != null) {
			this.setLocationAndAngles(orbiting.posX, orbiting.posY + orbiting.getEyeHeight(), orbiting.posZ, orbiting.rotationYaw, orbiting.rotationPitch);

			float rotation = this.ticksExisted / 5.0F + offset;
			Vec3d look = new Vec3d(DISTANCE, 0, 0).rotateYaw(rotation);
			this.posX += look.x;
//        	this.posY += Math.sin(this.ticksExisted / 3.0F + offset);
			this.posZ += look.z;

			this.setPosition(this.posX, this.posY, this.posZ);
		}

		if (this.getItemID() > -1) {
			for (int i = 0; i < 3; i++) {
				double dx = posX + 0.5 * (rand.nextDouble() - rand.nextDouble());
				double dy = posY + 0.5 * (rand.nextDouble() - rand.nextDouble());
				double dz = posZ + 0.5 * (rand.nextDouble() - rand.nextDouble());

				world.spawnParticle(EnumParticleTypes.ITEM_CRACK, dx, dy, dz, 0, 0.2, 0, getItemID());
			}
		}

		if (!this.world.isRemote
				&& (this.ticksExisted > 200 || (orbiting != null && orbiting.isDead))) {
			this.setDead();
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

	@Override
	protected void entityInit() {
		dataManager.register(DATA_ITEMID, -1);
		dataManager.register(DATA_OWNER, -1);
	}

	public void setOwner(EntityLivingBase owner) {
		dataManager.set(DATA_OWNER, owner.getEntityId());
	}

	public EntityLivingBase getOwner() {
		Entity e = this.world.getEntityByID(dataManager.get(DATA_OWNER));
		if (e instanceof EntityLivingBase)
			return (EntityLivingBase) e;
		else return null;
	}

	public int getItemID() {
		return dataManager.get(DATA_ITEMID);
	}

	public void setItemID(Item item) {
		dataManager.set(DATA_ITEMID, Item.getIdFromItem(item));
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound cmp) {}

	@Override
	protected void writeEntityToNBT(NBTTagCompound cmp) {}

}

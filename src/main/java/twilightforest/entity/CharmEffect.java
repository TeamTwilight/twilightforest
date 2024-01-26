package twilightforest.entity;

import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

@OnlyIn(value = Dist.CLIENT, _interface = ItemSupplier.class)
public class CharmEffect extends Entity implements ItemSupplier {
	private static final EntityDataAccessor<Integer> DATA_OWNER = SynchedEntityData.defineId(CharmEffect.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<ItemStack> DATA_ITEMID = SynchedEntityData.defineId(CharmEffect.class, EntityDataSerializers.ITEM_STACK);
	private static final double DISTANCE = 0.75D;
	private double interpTargetX;
	private double interpTargetY;
	private double interpTargetZ;
	private double interpTargetYaw;
	private double interpTargetPitch;
	private int newPosRotationIncrements;

	public float offset;

	public CharmEffect(EntityType<? extends CharmEffect> type, Level world) {
		super(type, world);
	}

	public CharmEffect(EntityType<? extends CharmEffect> type, Level world, LivingEntity owner, Item item) {
		this(type, world);

		this.setOwner(owner);
		this.setItemID(item);

		this.moveTo(owner.getX(), owner.getY() + owner.getEyeHeight(), owner.getZ(), owner.getYRot(), owner.getXRot());

		Vec3 look = new Vec3(DISTANCE, 0, 0);
		double x = getX() + (look.x() * DISTANCE);
		double z = getZ() + (look.z() * DISTANCE);
		this.setPos(x, this.getY(), z);
	}

	@Override
	public void tick() {
		this.xOld = this.getX();
		this.yOld = this.getY();
		this.zOld = this.getZ();
		super.tick();

		//[VanillaCopy] Beginning of LivingEntity.livingTick
		if (this.newPosRotationIncrements > 0) {
			double d0 = this.getX() + (this.interpTargetX - this.getX()) / this.newPosRotationIncrements;
			double d1 = this.getY() + (this.interpTargetY - this.getY()) / this.newPosRotationIncrements;
			double d2 = this.getZ() + (this.interpTargetZ - this.getZ()) / this.newPosRotationIncrements;
			double d3 = Mth.wrapDegrees(this.interpTargetYaw - this.getYRot());
			this.setYRot((float) (this.getYRot() + d3 / this.newPosRotationIncrements));
			this.setXRot((float) (this.getXRot() + (this.interpTargetPitch - this.getXRot()) / this.newPosRotationIncrements));
			--this.newPosRotationIncrements;
			this.setPos(d0, d1, d2);
			this.setRot(this.getYRot(), this.getXRot());
		}

		LivingEntity orbiting = this.getOwner();

		if (orbiting != null) {
			float rotation = this.tickCount / 10.0F + this.offset;
			Vec3 look = new Vec3(DISTANCE, 0, 0).yRot(rotation);
			this.moveTo(orbiting.getX() + look.x(), orbiting.getY() + orbiting.getEyeHeight(), orbiting.getZ() + look.z(), orbiting.getYRot(), orbiting.getXRot());
		}

		if (!this.getItemID().isEmpty()) {
			double dx = getX() + 0.25 * (this.random.nextDouble() - this.random.nextDouble());
			double dy = getY() + 0.25 * (this.random.nextDouble() - this.random.nextDouble());
			double dz = getZ() + 0.25 * (this.random.nextDouble() - this.random.nextDouble());

			this.level().addParticle(new ItemParticleOption(ParticleTypes.ITEM, getItemID()), dx, dy, dz, 0, 0.2, 0);
		}

		if (!this.level().isClientSide() && (this.tickCount > 200 || (orbiting != null && !orbiting.isAlive()))) {
			this.discard();
		}
	}

	@Override
	public void lerpTo(double x, double y, double z, float yaw, float pitch, int posRotationIncrements) {
		this.interpTargetX = x;
		this.interpTargetY = y;
		this.interpTargetZ = z;
		this.interpTargetYaw = yaw;
		this.interpTargetPitch = pitch;
		this.newPosRotationIncrements = posRotationIncrements;
	}

	@Override
	protected void defineSynchedData() {
		this.getEntityData().define(DATA_ITEMID, ItemStack.EMPTY);
		this.getEntityData().define(DATA_OWNER, -1);
	}

	public void setOwner(LivingEntity owner) {
		this.getEntityData().set(DATA_OWNER, owner.getId());
	}

	@Nullable
	public LivingEntity getOwner() {
		Entity e = this.level().getEntity(this.getEntityData().get(DATA_OWNER));
		if (e instanceof LivingEntity living)
			return living;
		else return null;
	}

	public ItemStack getItemID() {
		return this.getEntityData().get(DATA_ITEMID);
	}

	public void setItemID(Item item) {
		this.getEntityData().set(DATA_ITEMID, new ItemStack(item));
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag cmp) {
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag cmp) {
	}

	@Nonnull
	@Override
	public ItemStack getItem() {
		return this.getItemID();
	}
}

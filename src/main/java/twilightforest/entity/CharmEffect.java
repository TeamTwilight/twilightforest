package twilightforest.entity;

import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class CharmEffect extends Entity implements ItemSupplier {
	private static final double DISTANCE = 0.75D;

	public float offset;

	@Nullable
	private LivingEntity orbiter;
	private ItemStack displayItem = new ItemStack(Items.BARRIER);

	public CharmEffect(EntityType<? extends CharmEffect> type, Level level) {
		super(type, level);
	}

	@SuppressWarnings("this-escape")
	public CharmEffect(EntityType<? extends CharmEffect> type, Level level, LivingEntity owner, ItemStack item) {
		this(type, level);
		this.orbiter = owner;
		this.displayItem = item;

		this.snapTo(owner.getX(), owner.getY() + owner.getEyeHeight(), owner.getZ(), owner.getYRot(), owner.getXRot());

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

		if (this.orbiter != null) {
			float rotation = this.tickCount / 10.0F + this.offset;
			Vec3 look = new Vec3(DISTANCE, 0, 0).yRot(rotation);
			this.setPos(this.orbiter.getX() + look.x(), this.orbiter.getY() + this.orbiter.getEyeHeight(), this.orbiter.getZ() + look.z());
			this.setRot(this.orbiter.getYRot(), this.orbiter.getXRot());
		}

		if (!this.displayItem.isEmpty()) {
			double dx = getX() + 0.25 * (this.random.nextDouble() - this.random.nextDouble());
			double dy = getY() + 0.25 * (this.random.nextDouble() - this.random.nextDouble());
			double dz = getZ() + 0.25 * (this.random.nextDouble() - this.random.nextDouble());

			this.level().addParticle(new ItemParticleOption(ParticleTypes.ITEM, this.displayItem.getItem()), dx, dy, dz, 0, 0.2, 0);
		}

		if (this.tickCount > 200 || (this.orbiter != null && (!this.orbiter.isAlive() || this.orbiter.isInvisible()))) {
			this.discard();
		}
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {

	}

	@Override
	protected void readAdditionalSaveData(ValueInput valueInput) {
	}

	@Override
	protected void addAdditionalSaveData(ValueOutput valueOutput) {
	}

	@Override
	public boolean hurtServer(ServerLevel serverLevel, DamageSource damageSource, float v) {
		return false;
	}

	@Override
	public ItemStack getItem() {
		return this.displayItem;
	}

	@Override
	public boolean displayFireAnimation() {
		return false;
	}

	@Override
	protected boolean canRide(Entity entity) {
		return false;
	}

	@Override
	public boolean shouldRenderAtSqrDistance(double distance) {
		double d0 = 64.0 * getViewScale();
		return distance < d0 * d0;
	}
}

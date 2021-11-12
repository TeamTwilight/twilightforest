package twilightforest.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ProtectionBox extends Entity {

	public int lifeTime;

	public final int sizeX;
	public final int sizeY;
	public final int sizeZ;

	private final BoundingBox sbb;

	public ProtectionBox(EntityType<?> type, Level world) {
		super(type, world);
		sizeX = sizeY = sizeZ = 0;
		sbb = null;
	}

	public ProtectionBox(Level world, BoundingBox sbb) {
		super(TFEntities.PROTECTION_BOX, world);

		this.sbb = new BoundingBox(sbb.getCenter());

		this.moveTo(sbb.minX(), sbb.minY(), sbb.minZ(), 0.0F, 0.0F);

		sizeX = sbb.getXSpan();
		sizeY = sbb.getYSpan();
		sizeZ = sbb.getZSpan();

		this.dimensions = EntityDimensions.fixed(Math.max(sizeX, sizeZ), sizeY);

		lifeTime = 60;
	}

	@Override
	public void tick() {
		super.tick();

		if (lifeTime <= 1) {
			discard();
		} else {
			lifeTime--;
		}
	}

	public boolean matches(BoundingBox sbb) {
		return this.sbb.minX() == sbb.minX() && this.sbb.minY() == sbb.minY() && this.sbb.minZ() == sbb.minZ()
				&& this.sbb.maxX() == sbb.maxX() && this.sbb.maxY() == sbb.maxY() && this.sbb.maxZ() == sbb.maxZ();
	}

	public void resetLifetime() {
		lifeTime = 60;
	}

	@Override
	public float getBrightness() {
		return 1.0F;
	}

	@Override
	protected void defineSynchedData() {}

	@Override
	protected void readAdditionalSaveData(CompoundTag compound) {}

	@Override
	protected void addAdditionalSaveData(CompoundTag compound) {}

	@OnlyIn(Dist.CLIENT)
	@Override
	public boolean displayFireAnimation() {
		return false;
	}

	@Override
	protected boolean canRide(Entity entityIn) {
		return false;
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		throw new IllegalStateException("should never be spawned on server");
	}
}

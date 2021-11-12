package twilightforest.entity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.entity.PartEntity;
import twilightforest.TwilightForestMod;

import java.util.Objects;

public abstract class TFPart<T extends Entity> extends PartEntity<T> {

	public static final ResourceLocation RENDERER = TwilightForestMod.prefix("noop");

	protected EntityDimensions realSize;

	protected int newPosRotationIncrements;
	protected double interpTargetX;
	protected double interpTargetY;
	protected double interpTargetZ;
	protected double interpTargetYaw;
	protected double interpTargetPitch;
	public float renderYawOffset;
	public float prevRenderYawOffset;

	public int deathTime;
	public int hurtTime;

	public TFPart(T parent) {
		super(parent);
	}

	@OnlyIn(Dist.CLIENT)
	public ResourceLocation renderer() {
		return RENDERER;
	}

	@OnlyIn(Dist.CLIENT)
	public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements) {
		interpTargetX = x;
		interpTargetY = y;
		interpTargetZ = z;
		interpTargetYaw = yaw;
		interpTargetPitch = pitch;
		newPosRotationIncrements = posRotationIncrements;
	}

	@Override
	public void tick() {
		updateLastPos();
		super.tick();
		if (this.newPosRotationIncrements > 0) {
			double d0 = this.getX() + (this.interpTargetX - this.getX()) / (double)this.newPosRotationIncrements;
			double d2 = this.getY() + (this.interpTargetY - this.getY()) / (double)this.newPosRotationIncrements;
			double d4 = this.getZ() + (this.interpTargetZ - this.getZ()) / (double)this.newPosRotationIncrements;
			double d6 = Mth.wrapDegrees(this.interpTargetYaw - (double)this.getYRot());
			this.setYRot((float)((double)this.getYRot() + d6 / (double)this.newPosRotationIncrements));
			this.setXRot((float)((double)this.getXRot() + (this.interpTargetPitch - (double)this.getXRot()) / (double)this.newPosRotationIncrements));
			--this.newPosRotationIncrements;
			this.setPos(d0, d2, d4);
			this.setRot(this.getYRot(), this.getXRot());
		}

		while (getYRot() - yRotO < -180F) yRotO -= 360F;
		while (getYRot() - yRotO >= 180F) yRotO += 360F;

		while (renderYawOffset - prevRenderYawOffset < -180F) prevRenderYawOffset -= 360F;
		while (renderYawOffset - prevRenderYawOffset >= 180F) prevRenderYawOffset += 360F;

		while (getXRot() - xRotO < -180F) xRotO -= 360F;
		while (getXRot() - xRotO >= 180F) xRotO += 360F;
	}

	public final void updateLastPos() {
		moveTo(getX(), getY(), getZ());
		yRotO = getYRot();
		xRotO = getXRot();
		tickCount++;
	}

	protected void setSize(EntityDimensions size) {
		this.realSize = size;
		refreshDimensions();
	}

	@Override
	public EntityDimensions getDimensions(Pose poseIn) {
		return realSize;
	}

	@Override
	public boolean isPickable() {
		return true;
	}

	@Override
	public void setId(int id) {
		super.setId(id + 1);
	}

	public void writeData(FriendlyByteBuf buffer) {
		buffer.writeDouble(getX());
		buffer.writeDouble(getY());
		buffer.writeDouble(getZ());
		buffer.writeFloat(getYRot());
		buffer.writeFloat(getXRot());
		buffer.writeFloat(dimensions.width);
		buffer.writeFloat(dimensions.height);
		buffer.writeBoolean(dimensions.fixed);

	}

	public void readData(FriendlyByteBuf buffer) {
		Vec3 vec = new Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
		setPositionAndRotationDirect(vec.x, vec.y, vec.z, buffer.readFloat(), buffer.readFloat(), 3);
		final float w = buffer.readFloat();
		final float h = buffer.readFloat();
		setSize(buffer.readBoolean() ? EntityDimensions.fixed(w, h) : EntityDimensions.scalable(w, h));
		refreshDimensions();
	}

	public static void assignPartIDs(Entity parent) {
		PartEntity<?>[] parts = parent.getParts();
		for (int i = 0, partsLength = Objects.requireNonNull(parts).length; i < partsLength; i++) {
			PartEntity<?> part = parts[i];
			part.setId(parent.getId() + i);
		}
	}
}

package twilightforest.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.Pose;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.entity.PartEntity;
import net.minecraftforge.fml.common.thread.SidedThreadGroups;
import twilightforest.client.renderer.entity.NoopRenderer;

import static twilightforest.client.TFClientSetup.optifinePresent;

public abstract class TFPartEntity<T extends Entity> extends PartEntity<T> {

	protected EntitySize realSize;

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

	public TFPartEntity(T parent) {
		super(parent);
		if(optifinePresent){
			//if Optifine is present the default renderinjection is no longer called
			//this enabled a different way of rendering the partentitys
			addRenderer();
		}
	}

	@OnlyIn(Dist.CLIENT)
	public void addRenderer() {
		//this is a pretty bad way to get them rendering.
		//the client actually things they are seperate entitys now
		//this way the render function gets the correct calls and everything
		//but some other stuff starts to influence the entity on the client and im unsure where and how exactly i could stop that
		if(Thread.currentThread().getThreadGroup() != SidedThreadGroups.SERVER ){
			Minecraft.getInstance().player.worldClient.addEntity(this.getEntityId(),this);
		}
	}

	@OnlyIn(Dist.CLIENT)
	public EntityRenderer<?> renderer(EntityRendererManager manager) {
		return new NoopRenderer<>(manager);
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
			double d0 = this.getPosX() + (this.interpTargetX - this.getPosX()) / (double)this.newPosRotationIncrements;
			double d2 = this.getPosY() + (this.interpTargetY - this.getPosY()) / (double)this.newPosRotationIncrements;
			double d4 = this.getPosZ() + (this.interpTargetZ - this.getPosZ()) / (double)this.newPosRotationIncrements;
			double d6 = MathHelper.wrapDegrees(this.interpTargetYaw - (double)this.rotationYaw);
			this.rotationYaw = (float)((double)this.rotationYaw + d6 / (double)this.newPosRotationIncrements);
			this.rotationPitch = (float)((double)this.rotationPitch + (this.interpTargetPitch - (double)this.rotationPitch) / (double)this.newPosRotationIncrements);
			--this.newPosRotationIncrements;
			this.setPosition(d0, d2, d4);
			this.setRotation(this.rotationYaw, this.rotationPitch);
		}

		while (rotationYaw - prevRotationYaw < -180F) prevRotationYaw -= 360F;
		while (rotationYaw - prevRotationYaw >= 180F) prevRotationYaw += 360F;

		while (renderYawOffset - prevRenderYawOffset < -180F) prevRenderYawOffset -= 360F;
		while (renderYawOffset - prevRenderYawOffset >= 180F) prevRenderYawOffset += 360F;

		while (rotationPitch - prevRotationPitch < -180F) prevRotationPitch -= 360F;
		while (rotationPitch - prevRotationPitch >= 180F) prevRotationPitch += 360F;
	}

	public final void updateLastPos() {
		forceSetPosition(getPosX(), getPosY(), getPosZ());
		prevRotationYaw = rotationYaw;
		prevRotationPitch = rotationPitch;
		ticksExisted++;
	}

	protected void setSize(EntitySize size) {
		this.realSize = size;
		recalculateSize();
	}

	@Override
	public EntitySize getSize(Pose poseIn) {
		return realSize;
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	@Override
	public void setEntityId(int id) {
		super.setEntityId(id + 1);
	}

	public void writeData(PacketBuffer buffer) {
		buffer.writeDouble(getPosX());
		buffer.writeDouble(getPosY());
		buffer.writeDouble(getPosZ());
		buffer.writeFloat(rotationYaw);
		buffer.writeFloat(rotationPitch);
		buffer.writeFloat(size.width);
		buffer.writeFloat(size.height);
		buffer.writeBoolean(size.fixed);

	}

	public void readData(PacketBuffer buffer) {
		Vector3d vec = new Vector3d(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
		setPositionAndRotationDirect(vec.x, vec.y, vec.z, buffer.readFloat(), buffer.readFloat(), 3);
		final float w = buffer.readFloat();
		final float h = buffer.readFloat();
		setSize(buffer.readBoolean() ? EntitySize.fixed(w, h) : EntitySize.flexible(w, h));
		recalculateSize();
	}
}

package twilightforest.entity;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.PartEntity;
import twilightforest.TwilightForestMod;
import twilightforest.network.UpdateTFMultipartPacket;

import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public abstract class TFPart<T extends Entity> extends PartEntity<T> {

	public static final ResourceLocation RENDERER = TwilightForestMod.prefix("noop");

	protected EntityDimensions realSize = EntityDimensions.fixed(1F, 1F);

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

	public ResourceLocation renderer() {
		return RENDERER;
	}

	public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements) {
		this.interpTargetX = x;
		this.interpTargetY = y;
		this.interpTargetZ = z;
		this.interpTargetYaw = yaw;
		this.interpTargetPitch = pitch;
		this.newPosRotationIncrements = posRotationIncrements;
	}

	@Override
	public void tick() {
		updateLastPos();
		super.tick();
		if (this.newPosRotationIncrements > 0) {
			double d0 = this.getX() + (this.interpTargetX - this.getX()) / (double) this.newPosRotationIncrements;
			double d2 = this.getY() + (this.interpTargetY - this.getY()) / (double) this.newPosRotationIncrements;
			double d4 = this.getZ() + (this.interpTargetZ - this.getZ()) / (double) this.newPosRotationIncrements;
			double d6 = Mth.wrapDegrees(this.interpTargetYaw - (double) this.getYRot());
			this.setYRot((float) ((double) this.getYRot() + d6 / (double) this.newPosRotationIncrements));
			this.setXRot((float) ((double) this.getXRot() + (this.interpTargetPitch - (double) this.getXRot()) / (double) this.newPosRotationIncrements));
			--this.newPosRotationIncrements;
			this.setPos(d0, d2, d4);
			this.setRot(this.getYRot(), this.getXRot());
		}

		while (getYRot() - this.yRotO < -180F) this.yRotO -= 360F;
		while (getYRot() - this.yRotO >= 180F) this.yRotO += 360F;

		while (this.renderYawOffset - this.prevRenderYawOffset < -180F) this.prevRenderYawOffset -= 360F;
		while (this.renderYawOffset - this.prevRenderYawOffset >= 180F) this.prevRenderYawOffset += 360F;

		while (getXRot() - this.xRotO < -180F) this.xRotO -= 360F;
		while (getXRot() - this.xRotO >= 180F) this.xRotO += 360F;
	}

	public final void updateLastPos() {
		this.moveTo(this.getX(), this.getY(), this.getZ());
		this.yRotO = this.getYRot();
		this.xRotO = this.getXRot();
		this.tickCount++;
	}

	protected void setSize(EntityDimensions size) {
		this.realSize = size;
		this.refreshDimensions();
	}

	@Override
	public boolean isCurrentlyGlowing() {
		return this.getParent().isCurrentlyGlowing();
	}

	@Override
	public boolean isInvisible() {
		return this.getParent().isInvisible();
	}

	@Override
	public EntityDimensions getDimensions(Pose pose) {
		return this.realSize;
	}

	@Override
	public boolean isPickable() {
		return true;
	}

	@Override
	public InteractionResult interact(Player player, InteractionHand hand) {
		return this.getParent().interact(player, hand);
	}

	@Override
	public void setId(int id) {
		super.setId(id + 1);
	}

	// What trackers were last sent, so unchanged parts can be left out of UpdateTFMultipartPacket.
	private static final double POS_EPSILON = 1.0D / 4096.0D;  // vanilla's delta-move precision
	private static final float ROT_EPSILON = 0.01F;
	private double lastSentX = Double.NaN, lastSentY, lastSentZ;
	private float lastSentYRot, lastSentXRot, lastSentWidth, lastSentHeight;
	private boolean lastSentFixed;

	public UpdateTFMultipartPacket.PartDataHolder writeData() {
		return this.createData(getEntityData().packDirty());
	}

	/**
	 * Server-side delta for the regular tracker update: the part's state if its transform/size changed since it was
	 * last sent or it has dirty entity data, otherwise {@code null} (the part is omitted and the client keeps its
	 * current target). Consumes the dirty data, like {@link #writeData()}.
	 */
	@Nullable
	public UpdateTFMultipartPacket.PartDataHolder writeDataIfChanged() {
		List<SynchedEntityData.DataValue<?>> dirty = getEntityData().packDirty();
		boolean changed = Double.isNaN(this.lastSentX)
			|| Math.abs(this.getX() - this.lastSentX) > POS_EPSILON
			|| Math.abs(this.getY() - this.lastSentY) > POS_EPSILON
			|| Math.abs(this.getZ() - this.lastSentZ) > POS_EPSILON
			|| Math.abs(this.getYRot() - this.lastSentYRot) > ROT_EPSILON
			|| Math.abs(this.getXRot() - this.lastSentXRot) > ROT_EPSILON
			|| this.dimensions.width() != this.lastSentWidth
			|| this.dimensions.height() != this.lastSentHeight
			|| this.dimensions.fixed() != this.lastSentFixed;
		if (!changed && dirty == null)
			return null;
		this.lastSentX = this.getX();
		this.lastSentY = this.getY();
		this.lastSentZ = this.getZ();
		this.lastSentYRot = this.getYRot();
		this.lastSentXRot = this.getXRot();
		this.lastSentWidth = this.dimensions.width();
		this.lastSentHeight = this.dimensions.height();
		this.lastSentFixed = this.dimensions.fixed();
		return this.createData(dirty);
	}

	/**
	 * Full snapshot for a player that just started tracking the parent: includes all non-default entity data, which
	 * dirty-only updates would never deliver to a late tracker. Doesn't touch dirty flags or the delta baseline.
	 */
	public UpdateTFMultipartPacket.PartDataHolder writeFullData() {
		return this.createData(getEntityData().getNonDefaultValues());
	}

	private UpdateTFMultipartPacket.PartDataHolder createData(@Nullable List<SynchedEntityData.DataValue<?>> data) {
		return new UpdateTFMultipartPacket.PartDataHolder(
			this.getX(),
			this.getY(),
			this.getZ(),
			this.getYRot(),
			this.getXRot(),
			this.dimensions.width(),
			this.dimensions.height(),
			this.dimensions.fixed(),
			data);
	}

	public void readData(UpdateTFMultipartPacket.PartDataHolder data) {
		Vec3 vec = new Vec3(data.x(), data.y(), data.z());
		this.setPositionAndRotationDirect(vec.x(), vec.y(), vec.z(), data.yRot(), data.xRot(), 3);
		final float w = data.width();
		final float h = data.height();
		this.setSize(data.fixed() ? EntityDimensions.fixed(w, h) : EntityDimensions.scalable(w, h));
		if (data.data() != null)
			getEntityData().assignValues(data.data());
		this.refreshDimensions();
	}

	public static void assignPartIDs(Entity parent) {
		PartEntity<?>[] parts = parent.getParts();
		for (int i = 0, partsLength = Objects.requireNonNull(parts).length; i < partsLength; i++) {
			PartEntity<?> part = parts[i];
			part.setId(parent.getId() + i);
		}
	}
}

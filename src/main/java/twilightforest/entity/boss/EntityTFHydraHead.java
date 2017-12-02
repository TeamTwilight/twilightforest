package twilightforest.entity.boss;

import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityTFHydraHead extends EntityTFHydraPart {

	private static final DataParameter<Float> DATA_MOUTH_POSITION = EntityDataManager.createKey(EntityTFHydraHead.class, DataSerializers.FLOAT);
	private static final DataParameter<Byte> DATA_STATE = EntityDataManager.createKey(EntityTFHydraHead.class, DataSerializers.BYTE);

	public EntityTFHydraHead(World world) {
		super(world);

		// the necks draw with the head, so we just draw the head at all times, sorry
		this.ignoreFrustumCheck = true;
	}

	public EntityTFHydraHead(EntityTFHydra hydra, String s, float f, float f1) {
		super(hydra, s, f, f1);
	}

	@Override
	public int getVerticalFaceSpeed() {
		return 500;
	}

	@Override
	protected void onDeathUpdate() {
		++this.deathTime;
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataManager.register(DATA_MOUTH_POSITION, 0F);
		dataManager.register(DATA_STATE, (byte) 0);
	}

	public float getMouthOpen() {
		return dataManager.get(DATA_MOUTH_POSITION);
	}

	public HydraHeadContainer.State getState() {
		return HydraHeadContainer.State.values()[dataManager.get(DATA_STATE)];
	}

	public void setMouthOpen(float openness) {
		dataManager.set(DATA_MOUTH_POSITION, openness);
	}

	public void setState(HydraHeadContainer.State state) {
		dataManager.set(DATA_STATE, (byte) state.ordinal());
	}

}

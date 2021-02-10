package twilightforest.entity.boss;

import net.minecraft.entity.EntityType;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import twilightforest.entity.TFEntities;

public class EntityTFHydraHead extends EntityTFHydraPart {

	private static final DataParameter<Float> DATA_MOUTH_POSITION = EntityDataManager.createKey(EntityTFHydraHead.class, DataSerializers.FLOAT);
	private static final DataParameter<Byte> DATA_STATE = EntityDataManager.createKey(EntityTFHydraHead.class, DataSerializers.BYTE);

	public EntityTFHydraHead(EntityTFHydra hydra) {
		super(hydra, 3F, 3F);
	}

	@Override
	protected void registerData() {
		super.registerData();
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

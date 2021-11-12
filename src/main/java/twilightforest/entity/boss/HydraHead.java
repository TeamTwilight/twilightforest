package twilightforest.entity.boss;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.TwilightForestMod;

public class HydraHead extends HydraPart {

	public static final ResourceLocation RENDERER = TwilightForestMod.prefix("hydra_head");

	private static final EntityDataAccessor<Float> DATA_MOUTH_POSITION = SynchedEntityData.defineId(HydraHead.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> DATA_MOUTH_POSITION_LAST = SynchedEntityData.defineId(HydraHead.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Byte> DATA_STATE = SynchedEntityData.defineId(HydraHead.class, EntityDataSerializers.BYTE);

	public HydraHead(Hydra hydra) {
		super(hydra, 4F, 4F);
	}

	@OnlyIn(Dist.CLIENT)
	public ResourceLocation renderer() {
		return RENDERER;
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(DATA_MOUTH_POSITION, 0F);
		entityData.define(DATA_MOUTH_POSITION_LAST, 0F);
		entityData.define(DATA_STATE, (byte) 0);
	}

	public float getMouthOpen() {
		return entityData.get(DATA_MOUTH_POSITION);
	}

	public float getMouthOpenLast() {
		return entityData.get(DATA_MOUTH_POSITION_LAST);
	}

	public HydraHeadContainer.State getState() {
		return HydraHeadContainer.State.values()[entityData.get(DATA_STATE)];
	}

	public void setMouthOpen(float openness) {
		entityData.set(DATA_MOUTH_POSITION_LAST, getMouthOpen());
		entityData.set(DATA_MOUTH_POSITION, openness);
	}

	public void setState(HydraHeadContainer.State state) {
		entityData.set(DATA_STATE, (byte) state.ordinal());
	}
}

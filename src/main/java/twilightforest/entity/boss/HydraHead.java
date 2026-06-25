package twilightforest.entity.boss;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import twilightforest.TwilightForestMod;

public class HydraHead extends HydraPart {

	public static final Identifier RENDERER = TwilightForestMod.prefix("hydra_head");

	private static final EntityDataAccessor<Float> DATA_MOUTH_POSITION = SynchedEntityData.defineId(HydraHead.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> DATA_MOUTH_POSITION_LAST = SynchedEntityData.defineId(HydraHead.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Byte> DATA_STATE = SynchedEntityData.defineId(HydraHead.class, EntityDataSerializers.BYTE);
	private static final EntityDataAccessor<Float> DATA_HEAD_POS_X = SynchedEntityData.defineId(HydraHead.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> DATA_HEAD_POS_Y = SynchedEntityData.defineId(HydraHead.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> DATA_HEAD_POS_Z = SynchedEntityData.defineId(HydraHead.class, EntityDataSerializers.FLOAT);

	public HydraHead(Hydra hydra) {
		super(hydra, 4F, 4F);
	}

	@Override
	public Identifier renderer() {
		return RENDERER;
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(DATA_MOUTH_POSITION, 0F);
		builder.define(DATA_MOUTH_POSITION_LAST, 0F);
		builder.define(DATA_STATE, (byte) 0);
		builder.define(DATA_HEAD_POS_X, 0F);
		builder.define(DATA_HEAD_POS_Y, 0F);
		builder.define(DATA_HEAD_POS_Z, 0F);
	}

	public float getMouthOpen() {
		return this.getEntityData().get(DATA_MOUTH_POSITION);
	}

	public float getMouthOpenLast() {
		return this.getEntityData().get(DATA_MOUTH_POSITION_LAST);
	}

	public HydraHeadContainer.State getState() {
		return HydraHeadContainer.State.values()[this.getEntityData().get(DATA_STATE)];
	}

	public void setMouthOpen(float openness) {
		this.getEntityData().set(DATA_MOUTH_POSITION_LAST, getMouthOpen());
		this.getEntityData().set(DATA_MOUTH_POSITION, openness);
	}

	public void setState(HydraHeadContainer.State state) {
		this.getEntityData().set(DATA_STATE, (byte) state.ordinal());
	}

	public float getHeadPosX() {
		return this.getEntityData().get(DATA_HEAD_POS_X);
	}

	public float getHeadPosY() {
		return this.getEntityData().get(DATA_HEAD_POS_Y);
	}

	public float getHeadPosZ() {
		return this.getEntityData().get(DATA_HEAD_POS_Z);
	}

	public void setHeadPos(float x, float y, float z) {
		this.getEntityData().set(DATA_HEAD_POS_X, x);
		this.getEntityData().set(DATA_HEAD_POS_Y, y);
		this.getEntityData().set(DATA_HEAD_POS_Z, z);
	}

	@Override
	public InteractionResult interact(Player player, InteractionHand hand, Vec3 location) {
		ItemStack stack = player.getItemInHand(hand);
		Component tagName = stack.get(DataComponents.CUSTOM_NAME);
		if (stack.is(Items.NAME_TAG) && tagName != null) {
			if (!this.level().isClientSide() && this.isAlive()) {
				this.setCustomName(tagName);
				stack.shrink(1);

				//save name to main hydra
				Hydra hydra = this.getParent();
				if (hydra != null) {
					for (int i = 0; i < Hydra.MAX_HEADS; i++) {
						if (hydra.hc[i].headEntity == this) {
							hydra.setHeadNameFor(i, tagName.getString());
						}
					}
				}
			}

			return InteractionResult.SUCCESS;
		}
		return super.interact(player, hand, location);
	}
}

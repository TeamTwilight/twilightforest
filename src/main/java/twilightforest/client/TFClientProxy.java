package twilightforest.client;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.world.server.ServerWorld;
import twilightforest.ASMHooks;
import twilightforest.TFCommonProxy;

import javax.annotation.Nullable;

public class TFClientProxy extends TFCommonProxy {

	private boolean isDangerOverlayShown;

	@Nullable
	public static Iterable<Entity> getEntityListForASM() {
		return ASMHooks.world instanceof ServerWorld ? ((ServerWorld) ASMHooks.world).getEntitiesIteratable() : ASMHooks.world instanceof ClientWorld ? ((ClientWorld) ASMHooks.world).getAllEntities() : null;
	}

	@Override
	public void init() {}

//	public boolean isDangerOverlayShown() {
//		return isDangerOverlayShown;
//	}
//
//	public void setDangerOverlayShown(boolean isDangerOverlayShown) {
//		this.isDangerOverlayShown = isDangerOverlayShown;
//	}

}

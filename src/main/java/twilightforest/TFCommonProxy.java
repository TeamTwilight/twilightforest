package twilightforest;

import net.minecraft.entity.Entity;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;

public class TFCommonProxy {

	@Nullable
	public static Iterable<Entity> getEntityListForASM() {
		return ASMHooks.world instanceof ServerWorld ? ((ServerWorld) ASMHooks.world).func_241136_z_() : null;
	}

	public void init() {}
}

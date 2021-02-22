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

//	public boolean doesPlayerHaveAdvancement(PlayerEntity player, ResourceLocation advId) {
//		if (player instanceof ServerPlayerEntity) {
//			ServerWorld world = ((ServerPlayerEntity) player).getServerWorld();
//			Advancement adv = world.getServer().getAdvancementManager().getAdvancement(advId);
//			return adv != null && ((ServerPlayerEntity) player).getAdvancements().getProgress(adv).isDone();
//		}
//		return false;
//	}
}

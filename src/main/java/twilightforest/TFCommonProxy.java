package twilightforest;

import net.minecraft.advancements.Advancement;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;

public class TFCommonProxy {
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

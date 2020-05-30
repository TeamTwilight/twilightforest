package twilightforest.util;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.multiplayer.ClientAdvancementManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

public class PlayerHelper {
	/**
	 * Fulfills all remaining criteria of the given advancement
	 */
	@Deprecated
	public static void grantAdvancement(ServerPlayerEntity player, ResourceLocation id) {
		PlayerAdvancements advancements = player.getAdvancements();
		Advancement advancement = player.getServer().getAdvancementManager().getAdvancement(id);
		if (advancement != null) {
			for (String criterion : advancements.getProgress(advancement).getRemaningCriteria()) {
				advancements.grantCriterion(advancement, criterion);
			}
		}
	}

	@Deprecated
	public static void grantCriterion(ServerPlayerEntity player, ResourceLocation id, String criterion) {
		PlayerAdvancements advancements = player.getAdvancements();
		Advancement advancement = player.getServer().getAdvancementManager().getAdvancement(id);
		if (advancement != null) {
			advancements.grantCriterion(advancement, criterion);
		}
	}

	//TODO: The proxy methods it is referring to might need to be in here, using runWhenOn for each side
	public static boolean doesPlayerHaveRequiredAdvancements(PlayerEntity player, ResourceLocation... requiredAdvancements) {

		for (ResourceLocation advancementLocation : requiredAdvancements) {
			DistExecutor.runWhenOn(Dist.DEDICATED_SERVER, () -> () -> doesPlayerHaveAdvancementS(player, advancementLocation));
			DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> doesPlayerHaveAdvancementC(player, advancementLocation));
//			if (!TwilightForestMod.proxy.doesPlayerHaveAdvancement(player, advancementLocation)) {
//				return false;
//			}
		}
		return true;
	}

	public static boolean doesPlayerHaveAdvancementS(PlayerEntity player, ResourceLocation advId) {
		if (player instanceof ServerPlayerEntity) {
			ServerWorld world = ((ServerPlayerEntity) player).getServerWorld();
			Advancement adv = world.getServer().getAdvancementManager().getAdvancement(advId);
			return adv != null && ((ServerPlayerEntity) player).getAdvancements().getProgress(adv).isDone();
		}
		return false;
	}

	public static boolean doesPlayerHaveAdvancementC(PlayerEntity player, ResourceLocation advId) {
		if (player instanceof ClientPlayerEntity) {
			ClientAdvancementManager manager = ((ClientPlayerEntity) player).connection.getAdvancementManager();
			Advancement adv = manager.getAdvancementList().getAdvancement(advId);
			if (adv == null) return false;
			AdvancementProgress progress = manager.advancementToProgress.get(adv);
			return progress != null && progress.isDone();
		}

		return doesPlayerHaveAdvancementS(player, advId);
	}
}

package twilightforest.util;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;

public class PlayerHelper {
	/**
	 * Fulfills all remaining criteria of the given advancement
	 */
	@Deprecated
	public static void grantAdvancement(ServerPlayerEntity player, ResourceLocation id) {
		PlayerAdvancements advancements = player.getAdvancements();
		Advancement advancement = player.getServerWorld().getAdvancementManager().getAdvancement(id);
		if (advancement != null) {
			for (String criterion : advancements.getProgress(advancement).getRemaningCriteria()) {
				advancements.grantCriterion(advancement, criterion);
			}
		}
	}

	@Deprecated
	public static void grantCriterion(ServerPlayerEntity player, ResourceLocation id, String criterion) {
		PlayerAdvancements advancements = player.getAdvancements();
		Advancement advancement = player.getServerWorld().getAdvancementManager().getAdvancement(id);
		if (advancement != null) {
			advancements.grantCriterion(advancement, criterion);
		}
	}

	public static boolean doesPlayerHaveRequiredAdvancements(PlayerEntity player, ResourceLocation... requiredAdvancements) {
		for (ResourceLocation advancementLocation : requiredAdvancements) {
			if (!TwilightForestMod.proxy.doesPlayerHaveAdvancement(player, advancementLocation)) {
				return false;
			}
		}
		return true;
	}
}

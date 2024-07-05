package twilightforest.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import twilightforest.TwilightForestMod;
import twilightforest.config.TFConfig;
import twilightforest.util.LandmarkUtil;
import twilightforest.util.Restriction;

import java.util.Optional;

@EventBusSubscriber(value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME, modid = TwilightForestMod.ID)
public class LockedBiomeListener {

	private static boolean shownToast = false;
	private static int timeUntilToast = 60;

	@SubscribeEvent
	public static void clientTick(ClientTickEvent.Post event) {
		Player player = Minecraft.getInstance().player;
		if (player == null || !(player.level() instanceof ClientLevel level))
			return;

		//attempt to send a biome locked toast if our player is in a locked biome, only every 5 ticks
		if (level.isClientSide() && player.tickCount % 5 == 0
			&& LandmarkUtil.isProgressionEnforced(level)
			&& !player.isCreative() && !player.isSpectator() && !TFConfig.disableLockedBiomeToasts) {
			Optional<Restriction> restriction = Restriction.getRestrictionForBiome(level.getBiome(player.blockPosition()).value(), player);
			if (restriction.isPresent() && restriction.get().lockedBiomeToast() != null) {
				timeUntilToast--;
				if (!shownToast && timeUntilToast <= 0) {
					Minecraft.getInstance().getToasts().addToast(new LockedBiomeToast(restriction.get().lockedBiomeToast()));
					shownToast = true;
				}
			} else {
				if (shownToast) {
					timeUntilToast = 60;
					shownToast = false;
				}
			}
		}
	}
}

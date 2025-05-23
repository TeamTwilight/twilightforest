package twilightforest.world;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;
import twilightforest.TwilightForestMod;

public class NoReturnTeleporter extends TFTeleporter {

	public static TeleportTransition createNoPortalTransition(ServerLevel dest, Entity entity, BlockPos destPos) {
		TeleportTransition info = moveToSafeCoords(dest, entity, destPos);
		info = placePosition(entity, dest, info.position());
		return info;
	}

	private static TeleportTransition placePosition(Entity entity, ServerLevel level, Vec3 pos) {
		// ensure area is populated first
		loadSurroundingArea(level, pos);

		BlockPos spot = findPortalCoords(level, pos, blockPos -> isPortalAt(level, blockPos));
		String name = entity.getName().getString();

		if (spot != null) {
			TwilightForestMod.LOGGER.debug("Found existing teleportation for {} at {}", name, spot);
			return makePortalInfo(level, entity, Vec3.atCenterOf(spot.above()));
		}

		spot = findPortalCoords(level, pos, blockpos -> isIdealForPortal(level, blockpos));

		if (spot != null) {
			TwilightForestMod.LOGGER.debug("Found ideal teleportation spot for {} at {}", name, spot);
			return makePortalInfo(level, entity, Vec3.atCenterOf(spot.above()));
		}

		TwilightForestMod.LOGGER.debug("Did not find ideal teleportation spot, shooting for okay one for {}", name);
		spot = findPortalCoords(level, pos, blockPos -> isOkayForPortal(level, blockPos));

		if (spot != null) {
			TwilightForestMod.LOGGER.debug("Found okay teleportation spot for {} at {}", name, spot);
			return makePortalInfo(level, entity, Vec3.atCenterOf(spot.above()));
		}

		// well I don't think we can actually just return and fail here
		TwilightForestMod.LOGGER.debug("Did not even find an okay teleportation spot, just making a random one for {}", name);

		// adjust the portal height based on what level we're traveling to
		double yFactor = getYFactor(level);
		// modified copy of base Teleporter method:
		// + 2 to make it above bedrock
		return makePortalInfo(level, entity, entity.getX() * getHorizontalScale(level), (entity.getY() * yFactor) + 2, entity.getZ() * getHorizontalScale(level));
	}
}

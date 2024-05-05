package twilightforest.world;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;

public class NoReturnTeleporter extends TFTeleporter {
	public NoReturnTeleporter() {
		super(false);
	}

	@Override
	protected @Nullable PortalInfo createPosition(ServerLevel dest, Entity entity, BlockPos destPos, TeleporterCache cache) {
		PortalInfo info = moveToSafeCoords(dest, entity, destPos);
		info = placePosition(entity, dest, info.pos);
		return info;
	}

	private static PortalInfo placePosition(Entity entity, ServerLevel world, Vec3 pos) {
		// ensure area is populated first
		loadSurroundingArea(world, pos);

		BlockPos spot = findPortalCoords(world, pos, blockPos -> isPortalAt(world, blockPos));
		String name = entity.getName().getString();

		if (spot != null) {
			TwilightForestMod.LOGGER.debug("Found existing teleportation for {} at {}", name, spot);
			return makePortalInfo(entity, Vec3.atCenterOf(spot.above()));
		}

		spot = findPortalCoords(world, pos, blockpos -> isIdealForPortal(world, blockpos));

		if (spot != null) {
			TwilightForestMod.LOGGER.debug("Found ideal teleportation spot for {} at {}", name, spot);
			return makePortalInfo(entity, Vec3.atCenterOf(spot.above()));
		}

		TwilightForestMod.LOGGER.debug("Did not find ideal teleportation spot, shooting for okay one for {}", name);
		spot = findPortalCoords(world, pos, blockPos -> isOkayForPortal(world, blockPos));

		if (spot != null) {
			TwilightForestMod.LOGGER.debug("Found okay teleportation spot for {} at {}", name, spot);
			return makePortalInfo(entity, Vec3.atCenterOf(spot.above()));
		}

		// well I don't think we can actually just return and fail here
		TwilightForestMod.LOGGER.debug("Did not even find an okay teleportation spot, just making a random one for {}", name);

		// adjust the portal height based on what world we're traveling to
		double yFactor = getYFactor(world);
		// modified copy of base Teleporter method:
		// + 2 to make it above bedrock
		return makePortalInfo(entity, entity.getX() * getHorizontalScale(world), (entity.getY() * yFactor) + 2, entity.getZ() * getHorizontalScale(world));
	}
}

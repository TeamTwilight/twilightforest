package twilightforest.asm;

import net.minecraft.block.PortalInfo;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import twilightforest.world.TFDimensions;
import twilightforest.world.TFTeleporter;

@SuppressWarnings({"unused", "JavadocReference"})
// Not possible for the IDE to detect usages from runtime class transformations
public class PortalInfoHook {

	/**
	 * Injection Point:<br>
	 * {@link net.minecraft.entity.Entity#func_241829_a}<br>
	 * First [ARETURN] (return null)
	 */
	public static PortalInfo portalInfo(Entity entity, ServerWorld world) {
		if (checkDim(entity.world) || checkDim(world))
			return TFTeleporter.reposition(entity, world);
		return null;
	}

	private static boolean checkDim(World world) {
		return world.getDimensionKey().getLocation().equals(TFDimensions.twilightForest.getLocation());
	}
}

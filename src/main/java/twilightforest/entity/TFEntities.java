package twilightforest.entity;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import twilightforest.TwilightForestMod;

public class TFEntities {

	public static void registerEntity(ResourceLocation registryName, Class<? extends Entity> entityClass, int id, int backgroundEggColour, int foregroundEggColour) {
		registerEntity(registryName, entityClass, id, backgroundEggColour, foregroundEggColour, 80, 3, true);
	}

	public static void registerEntity(ResourceLocation registryName, Class<? extends Entity> entityClass, int id, int backgroundEggColour, int foregroundEggColour, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates) {
		EntityRegistry.registerModEntity(registryName, entityClass, registryName.getResourceDomain() + "." + registryName.getResourcePath(), id, TwilightForestMod.instance, trackingRange, updateFrequency, sendsVelocityUpdates, backgroundEggColour, foregroundEggColour);
	}

	public static void registerEntity(ResourceLocation registryName, Class<? extends Entity> entityClass, int id) {
		EntityRegistry.registerModEntity(registryName, entityClass, registryName.getResourceDomain() + "." + registryName.getResourcePath(), id, TwilightForestMod.instance, 80, 3, true);
	}

	public static void registerEntity(ResourceLocation registryName, Class<? extends Entity> clazz, int id, int trackingRange, int updateFrequency, boolean sendVelocityUpdates) {
		EntityRegistry.registerModEntity(registryName, clazz, registryName.getResourceDomain() + "." + registryName.getResourcePath(), id, TwilightForestMod.instance, trackingRange, updateFrequency, sendVelocityUpdates);
	}
}

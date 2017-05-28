package twilightforest.entity;

import net.minecraft.entity.Entity;
import twilightforest.TwilightForestMod;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class TFCreatures {

	public static void registerTFCreature(Class <? extends Entity > entityClass, String entityName, int id, int backgroundEggColour, int foregroundEggColour) {
		registerTFCreature(entityClass, entityName, id, backgroundEggColour, foregroundEggColour, 80, 3, true);
	}	
	
	public static void registerTFCreature(Class <? extends Entity > entityClass, String entityName, int id, int backgroundEggColour, int foregroundEggColour, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates) {
		EntityRegistry.registerModEntity(entityClass, entityName, id, TwilightForestMod.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
		// todo 1.9 add to creative tab
		EntityRegistry.registerEgg(entityClass, backgroundEggColour, foregroundEggColour);
	}

	public static void registerTFCreature(Class <? extends Entity > entityClass, String entityName, int id) {
		EntityRegistry.registerModEntity(entityClass, entityName, id, TwilightForestMod.instance, 80, 3, true);
	}

	public static String getSpawnerNameFor(String baseName) {
		return "twilightforest." + baseName; // todo 1.9 is this needed?
	}
	
	
}

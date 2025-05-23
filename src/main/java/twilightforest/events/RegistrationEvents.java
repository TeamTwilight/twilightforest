package twilightforest.events;

import net.minecraft.world.entity.*;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFEntities;

@EventBusSubscriber(modid = TwilightForestMod.ID, bus = EventBusSubscriber.Bus.MOD)
public class RegistrationEvents {

	@SubscribeEvent
	@SuppressWarnings("unchecked") //entities added this way will always extend LivingEntity
	public static void registerAttributes(EntityAttributeCreationEvent event) {
		TFEntities.ATTRIBUTES.forEach((type, builder) -> event.put((EntityType<? extends LivingEntity>) type.value(), builder.get().build()));
	}

	@SubscribeEvent
	@SuppressWarnings("unchecked") //PAIN
	public static void registerPlacements(RegisterSpawnPlacementsEvent event) {
		TFEntities.SPAWN_PREDICATES.forEach((type, predicate) -> event.register((EntityType<Entity>) type.value(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (SpawnPlacements.SpawnPredicate<Entity>) predicate, RegisterSpawnPlacementsEvent.Operation.REPLACE));
	}
}

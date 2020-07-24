package twilightforest.entity.boss;

import net.minecraft.entity.EntityType;
import net.minecraft.world.World;
import twilightforest.entity.TFEntities;

public class EntityTFHydraNeck extends EntityTFHydraPart {

	public EntityTFHydraNeck(EntityType<? extends EntityTFHydraNeck> type, World world) {
		super(type, world);
	}

	public EntityTFHydraNeck(EntityTFHydra hydra, String name, float width, float height) {
		super(TFEntities.hydra_neck, hydra, name, width, height);
	}
}

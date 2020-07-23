package twilightforest.entity.boss;

import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;
import twilightforest.entity.MultiPartEntityPart;
import twilightforest.entity.TFEntities;

public class EntityTFHydraSmallPart extends MultiPartEntityPart {
	public EntityTFHydraSmallPart(EntityType<? extends EntityTFHydraSmallPart> type, World world) {
		super(type, world);
	}

	public EntityTFHydraSmallPart(World world, float width, float height) {
		super(TFEntities.hydra_part, world);
		this.setSize(width, height);
	}

	public void setSize(float width, float height){
		this.size = EntitySize.flexible(width, height);
	}
}

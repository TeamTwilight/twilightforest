package twilightforest.entity;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public interface IEntityMultiPart {
	World getWorld();

	boolean attackEntityFromPart(MultiPartEntityPart part, DamageSource source, float damage);

	Entity[] getParts();
}

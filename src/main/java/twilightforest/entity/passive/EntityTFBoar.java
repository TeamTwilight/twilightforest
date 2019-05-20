package twilightforest.entity.passive;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import twilightforest.TwilightForestMod;

public class EntityTFBoar extends EntityPig {

	public static final ResourceLocation LOOT_TABLE = TwilightForestMod.prefix("entities/boar");

	public EntityTFBoar(World world) {
		super(world);
		setSize(0.9F, 0.9F);
	}

	public EntityTFBoar(World world, double x, double y, double z) {
		this(world);
		this.setPosition(x, y, z);
	}

	@Override
	public ResourceLocation getLootTable() {
		return LOOT_TABLE;
	}

	@Override
	public EntityPig createChild(EntityAgeable entityanimal) {
		return new EntityTFBoar(world);
	}

}

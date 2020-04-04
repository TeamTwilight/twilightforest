package twilightforest.entity.passive;

import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import twilightforest.TwilightForestMod;
import twilightforest.entity.TFEntities;

public class EntityTFBoar extends PigEntity {

	public static final ResourceLocation LOOT_TABLE = TwilightForestMod.prefix("entities/boar");

	public EntityTFBoar(EntityType<? extends EntityTFBoar> type, World world) {
		super(type, world);
	}

	public EntityTFBoar(World world, double x, double y, double z) {
		this(TFEntities.wild_boar.get(), world);
		this.setPosition(x, y, z);
	}

	@Override
	public ResourceLocation getLootTable() {
		return LOOT_TABLE;
	}

	@Override
	public PigEntity createChild(AgeableEntity entityanimal) {
		return new EntityTFBoar(TFEntities.wild_boar.get(), world);
	}
}

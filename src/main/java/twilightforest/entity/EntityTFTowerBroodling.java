package twilightforest.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import twilightforest.TwilightForestMod;

public class EntityTFTowerBroodling extends EntityTFSwarmSpider {
	public static final ResourceLocation LOOT_TABLE = TwilightForestMod.prefix("entities/tower_broodling");

	public EntityTFTowerBroodling(EntityType<? extends EntityTFTowerBroodling> type, World world) {
		this(type, world, true);
	}

	public EntityTFTowerBroodling(EntityType<? extends EntityTFTowerBroodling> type, World world, boolean spawnMore) {
		super(type, world, spawnMore);
		experienceValue = 3;
	}

	@Override
	protected void registerAttributes() {
		super.registerAttributes();
		this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(7.0D);
		this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
	}

	@Override
	protected boolean spawnAnother() {
		EntityTFSwarmSpider another = new EntityTFTowerBroodling(TFEntities.tower_broodling.get(), world, false);

		double sx = getX() + (rand.nextBoolean() ? 0.9 : -0.9);
		double sy = getY();
		double sz = getZ() + (rand.nextBoolean() ? 0.9 : -0.9);
		another.setLocationAndAngles(sx, sy, sz, rand.nextFloat() * 360F, 0.0F);
		if (!another.getCanSpawnHere()) {
			another.remove();
			return false;
		}
		world.addEntity(another);
		another.spawnExplosionParticle();

		return true;
	}

	@Override
	protected ResourceLocation getLootTable() {
		return LOOT_TABLE;
	}
}

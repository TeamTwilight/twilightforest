package twilightforest.entity;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import twilightforest.TwilightForestMod;

public class EntityTFTowerBroodling extends EntityTFSwarmSpider {
	public static final ResourceLocation LOOT_TABLE = TwilightForestMod.prefix("entities/tower_broodling");

	public EntityTFTowerBroodling(World world) {
		this(world, true);
	}

	public EntityTFTowerBroodling(World world, boolean spawnMore) {
		super(world, spawnMore);
		experienceValue = 3;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(7.0D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
	}

	@Override
	protected boolean spawnAnother() {
		EntityTFSwarmSpider another = new EntityTFTowerBroodling(world, false);

		double sx = posX + (rand.nextBoolean() ? 0.9 : -0.9);
		double sy = posY;
		double sz = posZ + (rand.nextBoolean() ? 0.9 : -0.9);
		another.setLocationAndAngles(sx, sy, sz, rand.nextFloat() * 360F, 0.0F);
		if (!another.getCanSpawnHere()) {
			another.setDead();
			return false;
		}
		world.spawnEntity(another);
		another.spawnExplosionParticle();

		return true;
	}

	@Override
	protected ResourceLocation getLootTable() {
		return LOOT_TABLE;
	}
}

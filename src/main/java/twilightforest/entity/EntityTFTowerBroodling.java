package twilightforest.entity;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.CombatEntry;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class EntityTFTowerBroodling extends EntityTFSwarmSpider implements ISavedCombatEntriesOnDeath {
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

	private ArrayList<CombatEntry> combatList;

	@Override
	public void sendEndCombat() {
		super.sendEndCombat();
		combatList = new ArrayList<>(this.getCombatTracker().combatEntries);
	}

	@Override
	public List<CombatEntry> getCombatList() {
		return combatList;
	}
}

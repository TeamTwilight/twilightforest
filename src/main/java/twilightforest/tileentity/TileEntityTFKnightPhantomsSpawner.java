package twilightforest.tileentity;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import twilightforest.entity.boss.EntityTFKnightPhantom;

public class TileEntityTFKnightPhantomsSpawner extends TileEntityTFBossSpawner {

	public TileEntityTFKnightPhantomsSpawner() {
		this.mobID = EntityList.getKey(EntityTFKnightPhantom.class);
	}

	@Override
	public boolean anyPlayerInRange() {
		EntityPlayer closestPlayer = world.getClosestPlayer(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 9D, false);

		return closestPlayer != null && closestPlayer.posY > pos.getY() - 2;
	}

	@Override
	protected void spawnMyBoss() {

		for (int i = 0; i < 6; i++) {

			// spawn creature
			EntityLiving myCreature = makeMyCreature();

			float angle = 60F * i;
			float distance = 4F;

			double rx = pos.getX() + 0.5D + Math.cos((angle) * Math.PI / 180.0D) * distance;
			double ry = pos.getY() + 0.5D;
			double rz = pos.getZ() + 0.5D + Math.sin((angle) * Math.PI / 180.0D) * distance;

			myCreature.setLocationAndAngles(rx, ry, rz, world.rand.nextFloat() * 360F, 0.0F);

			// set creature's home to this
			initializeCreature(myCreature);

			((EntityTFKnightPhantom) myCreature).setNumber(i);

			// spawn it
			world.spawnEntity(myCreature);
		}
	}

	@Override
	protected void initializeCreature(EntityLiving myCreature) {
		if (myCreature instanceof EntityTFKnightPhantom) {
			((EntityTFKnightPhantom) myCreature).setHomePosAndDistance(pos, 46);
		}
	}

}

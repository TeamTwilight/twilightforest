package twilightforest.tileentity.spawner;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;
import twilightforest.entity.boss.EntityTFKnightPhantom;
import twilightforest.item.TFItems;

public class TileEntityTFKnightPhantomsSpawner extends TileEntityTFBossSpawner {

	private static final int COUNT = 6;

	private int spawned = 0;

	public TileEntityTFKnightPhantomsSpawner() {
		super(EntityList.getKey(EntityTFKnightPhantom.class));
	}

	@Override
	public boolean anyPlayerInRange() {
		EntityPlayer closestPlayer = world.getClosestPlayer(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, getRange(), false);
		return closestPlayer != null && closestPlayer.posY > pos.getY() - 2;
	}

	@Override
	protected boolean spawnMyBoss() {
		for (int i = spawned; i < COUNT; i++) {
			// create creature
			EntityLiving myCreature = makeMyCreature();

			float angle = (360F / COUNT) * i;
			final float distance = 4F;

			double rx = pos.getX() + 0.5D + Math.cos(angle * Math.PI / 180.0D) * distance;
			double ry = pos.getY();
			double rz = pos.getZ() + 0.5D + Math.sin(angle * Math.PI / 180.0D) * distance;

			myCreature.setLocationAndAngles(rx, ry, rz, world.rand.nextFloat() * 360F, 0.0F);
			myCreature.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(myCreature)), null);

			if(i == 5 && world.getDifficulty() == EnumDifficulty.HARD){
				myCreature.setItemStackToSlot(EntityEquipmentSlot.OFFHAND,new ItemStack(TFItems.knightmetal_shield));
			}

			// set creature's home to this
			initializeCreature(myCreature);

			((EntityTFKnightPhantom) myCreature).setNumber(i);

			// spawn it
			if (world.spawnEntity(myCreature)) {
				spawned++;
			}
		}
		return spawned == COUNT;
	}

	@Override
	protected void initializeCreature(EntityLiving myCreature) {
		if (myCreature instanceof EntityTFKnightPhantom) {
			((EntityTFKnightPhantom) myCreature).setHomePosAndDistance(pos, 46);
		}
	}
}

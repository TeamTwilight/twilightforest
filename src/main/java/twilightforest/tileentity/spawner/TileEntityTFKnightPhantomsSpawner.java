package twilightforest.tileentity.spawner;

import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.IServerWorld;
import twilightforest.entity.TFEntities;
import twilightforest.entity.boss.EntityTFKnightPhantom;
import twilightforest.item.TFItems;
import twilightforest.tileentity.TFTileEntities;

public class TileEntityTFKnightPhantomsSpawner extends TileEntityTFBossSpawner<EntityTFKnightPhantom> {

	private static final int COUNT = 6;

	private int spawned = 0;

	public TileEntityTFKnightPhantomsSpawner() {
		super(TFTileEntities.KNIGHT_PHANTOM_SPAWNER.get(), TFEntities.knight_phantom);
	}

	@Override
	public boolean anyPlayerInRange() {
		PlayerEntity closestPlayer = world.getClosestPlayer(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, getRange(), false);
		return closestPlayer != null && closestPlayer.getPosY() > pos.getY() - 2;
	}

	@Override
	protected boolean spawnMyBoss(IServerWorld world) {
		for (int i = spawned; i < COUNT; i++) {
			// create creature
			EntityTFKnightPhantom myCreature = makeMyCreature();

			float angle = (360F / COUNT) * i;
			final float distance = 4F;

			double rx = pos.getX() + 0.5D + Math.cos(angle * Math.PI / 180.0D) * distance;
			double ry = pos.getY();
			double rz = pos.getZ() + 0.5D + Math.sin(angle * Math.PI / 180.0D) * distance;

			myCreature.setLocationAndAngles(rx, ry, rz, world.getWorld().rand.nextFloat() * 360F, 0.0F);
			myCreature.onInitialSpawn(world, world.getDifficultyForLocation(new BlockPos(myCreature.getPosition())), SpawnReason.SPAWNER, null, null);

			if(i == 5 && world.getDifficulty() == Difficulty.HARD){
				myCreature.setItemStackToSlot(EquipmentSlotType.OFFHAND,new ItemStack(TFItems.knightmetal_shield.get()));
			}

			// set creature's home to this
			initializeCreature(myCreature);

			myCreature.setNumber(i);

			// spawn it
			if (world.addEntity(myCreature)) {
				spawned++;
			}
		}
		return spawned == COUNT;
	}
}

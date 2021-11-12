package twilightforest.block.entity.spawner;

import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.entity.TFEntities;
import twilightforest.entity.boss.KnightPhantom;
import twilightforest.item.TFItems;
import twilightforest.block.entity.TFBlockEntities;

public class KnightPhantomSpawnerBlockEntity extends BossSpawnerBlockEntity<KnightPhantom> {

	private static final int COUNT = 6;

	private int spawned = 0;

	public KnightPhantomSpawnerBlockEntity(BlockPos pos, BlockState state) {
		super(TFBlockEntities.KNIGHT_PHANTOM_SPAWNER.get(), TFEntities.KNIGHT_PHANTOM, pos, state);
	}

	@Override
	public boolean anyPlayerInRange() {
		Player closestPlayer = level.getNearestPlayer(worldPosition.getX() + 0.5D, worldPosition.getY() + 0.5D, worldPosition.getZ() + 0.5D, getRange(), false);
		return closestPlayer != null && closestPlayer.getY() > worldPosition.getY() - 2;
	}

	@Override
	protected boolean spawnMyBoss(ServerLevelAccessor world) {
		for (int i = spawned; i < COUNT; i++) {
			// create creature
			KnightPhantom myCreature = makeMyCreature();

			float angle = (360F / COUNT) * i;
			final float distance = 4F;

			double rx = worldPosition.getX() + 0.5D + Math.cos(angle * Math.PI / 180.0D) * distance;
			double ry = worldPosition.getY();
			double rz = worldPosition.getZ() + 0.5D + Math.sin(angle * Math.PI / 180.0D) * distance;

			myCreature.moveTo(rx, ry, rz, world.getLevel().random.nextFloat() * 360F, 0.0F);
			myCreature.finalizeSpawn(world, world.getCurrentDifficultyAt(new BlockPos(myCreature.blockPosition())), MobSpawnType.SPAWNER, null, null);

			if(i == 5 && world.getDifficulty() == Difficulty.HARD){
				myCreature.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(TFItems.KNIGHTMETAL_SHIELD.get()));
			}

			// set creature's home to this
			initializeCreature(myCreature);

			myCreature.setNumber(i);

			// spawn it
			if (world.addFreshEntity(myCreature)) {
				spawned++;
			}
		}
		return spawned == COUNT;
	}
}

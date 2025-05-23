package twilightforest.block.entity.spawner;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.event.EventHooks;
import twilightforest.entity.boss.KnightPhantom;
import twilightforest.init.TFBlockEntities;
import twilightforest.init.TFEntities;
import twilightforest.init.TFItems;
import twilightforest.init.TFParticleType;

public class KnightPhantomSpawnerBlockEntity extends BossSpawnerBlockEntity<KnightPhantom> {

	private static final int COUNT = 6;

	private int spawned = 0;

	public KnightPhantomSpawnerBlockEntity(BlockPos pos, BlockState state) {
		super(TFBlockEntities.KNIGHT_PHANTOM_SPAWNER.get(), TFEntities.KNIGHT_PHANTOM.get(), pos, state);
	}

	@Override
	public boolean anyPlayerInRange(Level level) {
		Player closestPlayer =level.getNearestPlayer(this.getBlockPos().getX() + 0.5D, this.getBlockPos().getY() + 0.5D, this.getBlockPos().getZ() + 0.5D, this.getRange(), false);
		return closestPlayer != null && closestPlayer.getY() > this.getBlockPos().getY() - 2;
	}

	@Override
	protected boolean spawnMyBoss(ServerLevelAccessor accessor) {
		for (int i = spawned; i < COUNT; i++) {
			// create creature
			KnightPhantom myCreature = this.makeMyCreature();

			float angle = (360F / COUNT) * i;
			final float distance = 4F;

			double rx = this.getBlockPos().getX() + 0.5D + Math.cos(angle * Math.PI / 180.0D) * distance;
			double ry = this.getBlockPos().getY();
			double rz = this.getBlockPos().getZ() + 0.5D + Math.sin(angle * Math.PI / 180.0D) * distance;

			myCreature.moveTo(rx, ry, rz, accessor.getLevel().getRandom().nextFloat() * 360F, 0.0F);
			EventHooks.finalizeMobSpawn(myCreature, accessor, accessor.getCurrentDifficultyAt(new BlockPos(myCreature.blockPosition())), EntitySpawnReason.SPAWNER, null);

			if (i == 5 && accessor.getDifficulty() == Difficulty.HARD) {
				myCreature.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(TFItems.KNIGHTMETAL_SHIELD.get()));
			}

			// set creature's home to this
			this.initializeCreature(myCreature);

			myCreature.setNumber(i);

			// spawn it
			if (accessor.addFreshEntity(myCreature)) {
				spawned++;
			}
		}
		return spawned == COUNT;
	}

	@Override
	public ParticleOptions getSpawnerParticle() {
		return TFParticleType.OMINOUS_FLAME.get();
	}
}

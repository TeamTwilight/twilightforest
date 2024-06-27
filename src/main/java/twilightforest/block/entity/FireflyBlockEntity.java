package twilightforest.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.init.TFBlockEntities;
import twilightforest.init.TFParticleType;

public class FireflyBlockEntity extends BlockEntity {

	private int yawDelay;
	public int currentYaw;
	private int desiredYaw;

	public float glowIntensity;
	private boolean glowing;
	private int glowDelay;
	public final float randRot = RandomSource.create().nextInt(4) * 90.0F;

	public FireflyBlockEntity(BlockPos pos, BlockState state) {
		super(TFBlockEntities.FIREFLY.get(), pos, state);
	}

	public static void tick(Level level, BlockPos pos, BlockState state, FireflyBlockEntity te) {
		if (level.isClientSide()) {
			if (te.anyPlayerInRange() && level.getRandom().nextInt(20) == 0) {
				te.spawnParticles();
			}

			if (te.yawDelay > 0) {
				te.yawDelay--;
			} else {
				if (te.currentYaw == 0 && te.desiredYaw == 0) {
					// make it rotate!
					te.yawDelay = 200 + level.getRandom().nextInt(200);
					te.desiredYaw = level.getRandom().nextInt(15) - level.getRandom().nextInt(15);
				}

				if (te.currentYaw < te.desiredYaw) {
					te.currentYaw++;
				}
				if (te.currentYaw > te.desiredYaw) {
					te.currentYaw--;
				}
				if (te.currentYaw == te.desiredYaw) {
					te.desiredYaw = 0;
				}
			}

			if (te.glowDelay > 0) {
				te.glowDelay--;
			} else {
				if (te.glowing && te.glowIntensity >= 1.0) {
					te.glowing = false;
				}
				if (te.glowing && te.glowIntensity < 1.0) {
					te.glowIntensity += 0.05F;
				}
				if (!te.glowing && te.glowIntensity > 0) {
					te.glowIntensity -= 0.05F;
				}
				if (!te.glowing && te.glowIntensity <= 0) {
					te.glowing = true;
					te.glowDelay = level.getRandom().nextInt(50);
				}
			}
		}
	}

	private boolean anyPlayerInRange() {
		return this.getLevel().getNearestPlayer(this.getBlockPos().getX() + 0.5D, this.getBlockPos().getY() + 0.5D, this.getBlockPos().getZ() + 0.5D, 16D, false) != null;
	}

	private void spawnParticles() {
		double rx = this.getBlockPos().getX() + this.getLevel().getRandom().nextFloat();
		double ry = this.getBlockPos().getY() + this.getLevel().getRandom().nextFloat();
		double rz = this.getBlockPos().getZ() + this.getLevel().getRandom().nextFloat();
//    	ModLoader.getMinecraftInstance().effectRenderer.addEffect(fireflyfx);
		// ^ keeping here only for pure lolz
		this.getLevel().addParticle(TFParticleType.FIREFLY.get(), rx, ry, rz, 0, 0, 0);
	}
}

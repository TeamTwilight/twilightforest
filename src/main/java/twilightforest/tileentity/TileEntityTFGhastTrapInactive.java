package twilightforest.tileentity;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.SoundEvents;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import twilightforest.block.TFBlocks;
import twilightforest.client.particle.TFParticleType;
import twilightforest.entity.EntityTFMiniGhast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TileEntityTFGhastTrapInactive extends TileEntity implements ITickableTileEntity {

	private int counter;
	private final Random rand = new Random();
	private final List<EntityTFMiniGhast> dyingGhasts = new ArrayList<EntityTFMiniGhast>();

	public TileEntityTFGhastTrapInactive() {
		super(TFTileEntities.GHAST_TRAP_INACTIVE.get());
	}

	@Override
	public void tick() {
		// check to see if there are any dying mini ghasts within our scan range
		AxisAlignedBB aabb = new AxisAlignedBB(pos).grow(10D, 16D, 10D);

		List<EntityTFMiniGhast> nearbyGhasts = world.getEntitiesWithinAABB(EntityTFMiniGhast.class, aabb);

		for (EntityTFMiniGhast ghast : nearbyGhasts) {
			if (ghast.deathTime > 0) {
				this.makeParticlesTo(ghast);

				if (!dyingGhasts.contains(ghast)) {
					dyingGhasts.add(ghast);
				}
			}
		}

		// display charge level, up to 3
		int chargeLevel = Math.min(3, dyingGhasts.size());

		counter++;

		if (this.world.isRemote) {
			// occasionally make a redstone line to a mini ghast
			if (this.counter % 20 == 0 && nearbyGhasts.size() > 0) {
				EntityTFMiniGhast highlight = nearbyGhasts.get(rand.nextInt(nearbyGhasts.size()));
				this.makeParticlesTo(highlight);
			}

			if (chargeLevel >= 1 && counter % 10 == 0) {
				TFBlocks.ghast_trap.get().sparkle(world, this.pos);
				world.playSound(this.pos.getX() + 0.5D, this.pos.getY() + 1.5D, this.pos.getZ() + 0.5D, SoundEvents.BLOCK_NOTE_BLOCK_HARP, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
			}
			if (chargeLevel >= 2) {
				world.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.1 + rand.nextFloat() * 0.8, pos.getY() + 1.05, pos.getZ() + 0.1 + rand.nextFloat() * 0.8, (rand.nextFloat() - rand.nextFloat()) * 0.05, 0.00, (rand.nextFloat() - rand.nextFloat()) * 0.05);
				if (counter % 10 == 0) {
					world.playSound(this.pos.getX() + 0.5D, this.pos.getY() + 1.5D, this.pos.getZ() + 0.5D, SoundEvents.BLOCK_NOTE_BLOCK_HARP, SoundCategory.BLOCKS, 1.2F, 0.8F, false);
				}
			}
			if (chargeLevel >= 3) {
				world.addParticle(ParticleTypes.LARGE_SMOKE, this.pos.getX() + 0.1 + rand.nextFloat() * 0.8, this.pos.getY() + 1.05, this.pos.getZ() + 0.1 + rand.nextFloat() * 0.8, (rand.nextFloat() - rand.nextFloat()) * 0.05, 0.05, (rand.nextFloat() - rand.nextFloat()) * 0.05);
				TFBlocks.ghast_trap.get().sparkle(world, this.pos);
				if (counter % 5 == 0) {
					world.playSound(this.pos.getX() + 0.5D, this.pos.getY() + 1.5D, this.pos.getZ() + 0.5D, SoundEvents.BLOCK_NOTE_BLOCK_HARP, SoundCategory.BLOCKS, 1.5F, 2F, false);
				}
			}
		}
	}

	private void makeParticlesTo(Entity highlight) {

		double sx = this.pos.getX() + 0.5D;
		double sy = this.pos.getY() + 1.0D;
		double sz = this.pos.getZ() + 0.5D;

		double dx = sx - highlight.getX();
		double dy = sy - highlight.getY() - highlight.getEyeHeight();
		double dz = sz - highlight.getZ();

		for (int i = 0; i < 5; i++) {
			world.addParticle(TFParticleType.GHAST_TRAP.get(), sx, sy, sz, -dx, -dy, -dz);
		}
	}

	public boolean isCharged() {
		return dyingGhasts.size() >= 3;
	}
}

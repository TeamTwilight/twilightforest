package twilightforest.tileentity;

import net.minecraft.entity.Entity;
import net.minecraft.init.SoundEvents;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import twilightforest.TwilightForestMod;
import twilightforest.block.BlockTFTowerDevice;
import twilightforest.block.TFBlocks;
import twilightforest.client.particle.TFParticleType;
import twilightforest.entity.EntityTFMiniGhast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TileEntityTFGhastTrapInactive extends TileEntity implements ITickable {

	private int counter;
	private final Random rand = new Random();
	private final List<EntityTFMiniGhast> dyingGhasts = new ArrayList<EntityTFMiniGhast>();

	@Override
	public void update() {
		// check to see if there are any dying mini ghasts within our scan range
		AxisAlignedBB aabb = new AxisAlignedBB(pos, pos.add(1, 1, 1)).grow(10D, 16D, 10D);

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
				((BlockTFTowerDevice) TFBlocks.tower_device).sparkle(world, getPos());

				world.playSound(this.pos.getX() + 0.5D, this.pos.getY() + 1.5D, this.pos.getZ() + 0.5D, SoundEvents.BLOCK_NOTE_HARP, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
			}
			if (chargeLevel >= 2) {
				world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos.getX() + 0.1 + rand.nextFloat() * 0.8, pos.getY() + 1.05, pos.getZ() + 0.1 + rand.nextFloat() * 0.8, (rand.nextFloat() - rand.nextFloat()) * 0.05, 0.00, (rand.nextFloat() - rand.nextFloat()) * 0.05);
				if (counter % 10 == 0) {
					world.playSound(this.pos.getX() + 0.5D, this.pos.getY() + 1.5D, this.pos.getZ() + 0.5D, SoundEvents.BLOCK_NOTE_HARP, SoundCategory.BLOCKS, 1.2F, 0.8F, false);
				}
			}
			if (chargeLevel >= 3) {
				world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.pos.getX() + 0.1 + rand.nextFloat() * 0.8, this.pos.getY() + 1.05, this.pos.getZ() + 0.1 + rand.nextFloat() * 0.8, (rand.nextFloat() - rand.nextFloat()) * 0.05, 0.05, (rand.nextFloat() - rand.nextFloat()) * 0.05);
				((BlockTFTowerDevice) TFBlocks.tower_device).sparkle(world, this.pos);
				if (counter % 5 == 0) {
					world.playSound(this.pos.getX() + 0.5D, this.pos.getY() + 1.5D, this.pos.getZ() + 0.5D, SoundEvents.BLOCK_NOTE_HARP, SoundCategory.BLOCKS, 1.5F, 2F, false);
				}
			}
		}
	}

	private void makeParticlesTo(Entity highlight) {
		double sx = this.pos.getX() + 0.5D;
		double sy = this.pos.getY() + 1.0D;
		double sz = this.pos.getZ() + 0.5D;

		double dx = sx - highlight.posX;
		double dy = sy - highlight.posY - highlight.getEyeHeight();
		double dz = sz - highlight.posZ;

		for (int i = 0; i < 5; i++) {
			TwilightForestMod.proxy.spawnParticle(this.world, TFParticleType.GHAST_TRAP, sx, sy, sz, -dx, -dy, -dz);
		}

	}

	public boolean isCharged() {
		return dyingGhasts.size() >= 3;
	}

}

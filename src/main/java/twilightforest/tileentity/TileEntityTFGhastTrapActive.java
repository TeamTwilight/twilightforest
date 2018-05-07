package twilightforest.tileentity;

import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import twilightforest.TFSounds;
import twilightforest.TwilightForestMod;
import twilightforest.block.BlockTFTowerDevice;
import twilightforest.block.TFBlocks;
import twilightforest.enums.TowerDeviceVariant;
import twilightforest.client.particle.TFParticleType;
import twilightforest.entity.EntityTFTowerGhast;
import twilightforest.entity.boss.EntityTFUrGhast;

import java.util.List;
import java.util.Random;

public class TileEntityTFGhastTrapActive extends TileEntity implements ITickable {

	public int counter = 0;

	public Random rand = new Random();

	@Override
	public void update() {

		++counter;

		if (world.isRemote) {
			// smoke when done
			if (counter > 100 && counter % 4 == 0) {
				TwilightForestMod.proxy.spawnParticle(this.world, TFParticleType.HUGE_SMOKE, this.pos.getX() + 0.5, this.pos.getY() + 0.95, this.pos.getZ() + 0.5, Math.cos(counter / 10.0) * 0.05, 0.25D, Math.sin(counter / 10.0) * 0.05);
			} else if (counter < 100) {
				double dx = Math.cos(counter / 10.0) * 2.5;
				double dy = 20D;
				double dz = Math.sin(counter / 10.0) * 2.5;


				TwilightForestMod.proxy.spawnParticle(this.world, TFParticleType.GHAST_TRAP, this.pos.getX() + 0.5D, this.pos.getY() + 1.0D, this.pos.getZ() + 0.5D, dx, dy, dz);
				TwilightForestMod.proxy.spawnParticle(this.world, TFParticleType.GHAST_TRAP, this.pos.getX() + 0.5D, this.pos.getY() + 1.0D, this.pos.getZ() + 0.5D, -dx, dy, -dz);
				TwilightForestMod.proxy.spawnParticle(this.world, TFParticleType.GHAST_TRAP, this.pos.getX() + 0.5D, this.pos.getY() + 1.0D, this.pos.getZ() + 0.5D, -dx, dy / 2, dz);
				TwilightForestMod.proxy.spawnParticle(this.world, TFParticleType.GHAST_TRAP, this.pos.getX() + 0.5D, this.pos.getY() + 1.0D, this.pos.getZ() + 0.5D, dx, dy / 2, -dz);
				TwilightForestMod.proxy.spawnParticle(this.world, TFParticleType.GHAST_TRAP, this.pos.getX() + 0.5D, this.pos.getY() + 1.0D, this.pos.getZ() + 0.5D, dx / 2, dy / 4, dz / 2);
				TwilightForestMod.proxy.spawnParticle(this.world, TFParticleType.GHAST_TRAP, this.pos.getX() + 0.5D, this.pos.getY() + 1.0D, this.pos.getZ() + 0.5D, -dx / 2, dy / 4, -dz / 2);
			}

			// appropriate sound
			if (counter < 30) {
				world.playSound(pos.getX() + 0.5D, pos.getY() + 1.5D, pos.getZ() + 0.5D, TFSounds.URGHAST_TRAP_WARMUP, SoundCategory.BLOCKS, 1.0F, 4.0F, false);
			} else if (counter < 80) {
				world.playSound(pos.getX() + 0.5D, pos.getY() + 1.5D, pos.getZ() + 0.5D, TFSounds.URGHAST_TRAP_ON, SoundCategory.BLOCKS, 1.0F, 4.0F, false);
			} else {
				world.playSound(pos.getX() + 0.5D, pos.getY() + 1.5D, pos.getZ() + 0.5D, TFSounds.URGHAST_TRAP_SPINDOWN, SoundCategory.BLOCKS, 1.0F, 4.0F, false);
			}
		}

		if (!world.isRemote) {
			// trap nearby ghasts
			AxisAlignedBB aabb = new AxisAlignedBB(pos.up(16), pos.up(16).add(1, 1, 1)).grow(6D, 16D, 6D);

			List<EntityGhast> nearbyGhasts = world.getEntitiesWithinAABB(EntityGhast.class, aabb);

			for (EntityGhast ghast : nearbyGhasts) {
				//stop boss tantrum
				if (ghast instanceof EntityTFUrGhast) {
					((EntityTFUrGhast) ghast).setInTantrum(false);
					((EntityTFUrGhast) ghast).noClip = true; // turn this on so we can pull it in close

					// move boss to this point
					ghast.motionX = (ghast.posX - this.pos.getX() - 0.5) * -0.1;
					ghast.motionY = (ghast.posY - this.pos.getY() - 2.5) * -0.1;
					ghast.motionZ = (ghast.posZ - this.pos.getZ() - 0.5) * -0.1;

					if (rand.nextInt(10) == 0) {
						ghast.attackEntityFrom(DamageSource.GENERIC, 3);
					}

				} else {
					// move ghasts to this point
					ghast.motionX = (ghast.posX - this.pos.getX() - 0.5) * -0.1;
					ghast.motionY = (ghast.posY - this.pos.getY() - 1.5) * -0.1;
					ghast.motionZ = (ghast.posZ - this.pos.getZ() - 0.5) * -0.1;

					if (rand.nextInt(10) == 0) {
						ghast.attackEntityFrom(DamageSource.GENERIC, 10);
					}
				}

				if (ghast instanceof EntityTFTowerGhast) {
					((EntityTFTowerGhast) ghast).setInTrap();
				}

			}

			if (counter >= 120) {
				world.setBlockState(pos, TFBlocks.tower_device.getDefaultState().withProperty(BlockTFTowerDevice.VARIANT, TowerDeviceVariant.GHASTTRAP_INACTIVE), 3);
			}
		}
	}
}

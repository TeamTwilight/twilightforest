package twilightforest.tileentity;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import twilightforest.TFSounds;
import twilightforest.block.BlockTFFireJet;
import twilightforest.block.TFBlocks;
import twilightforest.enums.FireJetVariant;
import twilightforest.client.particle.TFParticleType;

import java.util.List;

public class TileEntityTFFlameJet extends TileEntity implements ITickableTileEntity {

	private int counter = 0;

	public TileEntityTFFlameJet() {
		super(TFTileEntities.FLAME_JET.get());
	}

	@Override
	public void tick() {
		if (getBlockState().getBlock() == TFBlocks.fire_jet.get() || getBlockState().getBlock() == TFBlocks.encased_fire_jet.get()) {
			switch (getBlockState().get(BlockTFFireJet.STATE)) {
			case POPPING: tickPopping(); break;
			case FLAME: tickFlame(); break;
			}
		}
	}

	private void tickPopping() {
		if (++counter >= 80) {
			counter = 0;
			// turn to flame
			if (!world.isRemote) {
				if (getBlockState().getBlock() == TFBlocks.fire_jet.get() || getBlockState().getBlock() == TFBlocks.encased_fire_jet.get()) {
					world.setBlockState(pos, getBlockState().with(BlockTFFireJet.STATE, FireJetVariant.FLAME));
				} else {
					world.removeBlock(pos, false);
				}
			}
		} else {
			if (counter % 20 == 0) {
				for (int i = 0; i < 8; i++)
				{
					world.addParticle(ParticleTypes.LAVA, this.pos.getX() + 0.5, this.pos.getY() + 1.5, this.pos.getZ() + 0.5, 0.0D, 0.0D, 0.0D);
				}
				world.playSound(null, pos, TFSounds.JET_POP, SoundCategory.BLOCKS, 0.2F + world.rand.nextFloat() * 0.2F, 0.9F + world.rand.nextFloat() * 0.15F);
			}
		}
	}

	private void tickFlame() {
		double x = this.pos.getX();
		double y = this.pos.getY();
		double z = this.pos.getZ();

		if (++counter > 60) {
			counter = 0;
			// idle again
			if (!world.isRemote) {
				if (getBlockState().getBlock() == TFBlocks.fire_jet.get() || getBlockState().getBlock() == TFBlocks.encased_fire_jet.get()) {
					world.setBlockState(pos, getBlockState().with(BlockTFFireJet.STATE, FireJetVariant.IDLE));
				} else {
					world.removeBlock(pos, false);
				}
			}
		}

		if (world.isRemote) {
			if (counter % 2 == 0) {
				world.addParticle(ParticleTypes.LARGE_SMOKE, x + 0.5, y + 1.0, z + 0.5, 0.0D, 0.0D, 0.0D);
				world.addParticle(TFParticleType.LARGE_FLAME.get(), x + 0.5, y + 1.0, z + 0.5, 0.0D, 0.5D, 0.0D);
				world.addParticle(TFParticleType.LARGE_FLAME.get(), x - 0.5, y + 1.0, z + 0.5, 0.05D, 0.5D, 0.0D);
				world.addParticle(TFParticleType.LARGE_FLAME.get(), x + 0.5, y + 1.0, z - 0.5, 0.0D, 0.5D, 0.05D);
				world.addParticle(TFParticleType.LARGE_FLAME.get(), x + 1.5, y + 1.0, z + 0.5, -0.05D, 0.5D, 0.0D);
				world.addParticle(TFParticleType.LARGE_FLAME.get(), x + 0.5, y + 1.0, z + 1.5, 0.0D, 0.5D, -0.05D);
			}

			// sounds
			if (counter % 4 == 0) {
				world.playSound(x + 0.5, y + 0.5, z + 0.5, TFSounds.JET_ACTIVE, SoundCategory.BLOCKS, 1.0F + world.rand.nextFloat(), world.rand.nextFloat() * 0.7F + 0.3F, false);

			} else if (counter == 1) {
				world.playSound(x + 0.5, y + 0.5, z + 0.5, TFSounds.JET_START, SoundCategory.BLOCKS, 1.0F + world.rand.nextFloat(), world.rand.nextFloat() * 0.7F + 0.3F, false);
			}
		}

		// actual fire effects
		if (!world.isRemote) {
			if (counter % 5 == 0) {
				// find entities in the area of effect
				List<Entity> entitiesInRange = world.getEntitiesWithinAABB(Entity.class,
						new AxisAlignedBB(pos.add(-2, 0, -2), pos.add(2, 4, 2)));
				// fire!
				for (Entity entity : entitiesInRange) {
					if (!entity.isImmuneToFire()) {
						entity.attackEntityFrom(DamageSource.IN_FIRE, 2);
						entity.setFire(15);
					}
				}
			}
		}
	}
}

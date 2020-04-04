package twilightforest.tileentity;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.SoundEvents;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import twilightforest.block.BlockTFFireJet;
import twilightforest.block.TFBlocks;
import twilightforest.enums.FireJetVariant;

public class TileEntityTFPoppingJet extends TileEntity implements ITickableTileEntity {

	private int counter = 0;
	private FireJetVariant nextVariant;

	public TileEntityTFPoppingJet(FireJetVariant variant) {
		super(TFTileEntities.POPPING_JET.get());
		this.nextVariant = variant;
	}

	public TileEntityTFPoppingJet() {
		super(TFTileEntities.POPPING_JET.get());
	}

	@Override
	public void tick() {
		if (++counter >= 80) {
			counter = 0;
			// turn to flame
			if (!world.isRemote) {
				if (world.getBlockState(pos).getBlock() == TFBlocks.fire_jet.get()) {
					world.setBlockState(pos, TFBlocks.fire_jet.get().getDefaultState().with(BlockTFFireJet.STATE, this.nextVariant));
				} else if (world.getBlockState(pos).getBlock() == TFBlocks.encased_fire_jet.get()) {
					world.setBlockState(pos, TFBlocks.encased_fire_jet.get().getDefaultState().with(BlockTFFireJet.STATE, this.nextVariant));
				}
			}
		} else {
			if (counter % 20 == 0) {
				for (int i = 0; i < 8; i++)
				{
					world.addParticle(ParticleTypes.LAVA, this.pos.getX() + 0.5, this.pos.getY() + 1.5, this.pos.getZ() + 0.5, 0.0D, 0.0D, 0.0D);
				}
				world.playSound(null, pos, SoundEvents.BLOCK_LAVA_POP, SoundCategory.BLOCKS, 0.2F + world.rand.nextFloat() * 0.2F, 0.9F + world.rand.nextFloat() * 0.15F);
			}
		}
	}

	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);
		this.nextVariant = FireJetVariant.values()[compound.getInt("NextMeta")];
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		CompoundNBT ret = super.write(compound);
		ret.putInt("NextMeta", nextVariant.ordinal());
		return ret;
	}
}

package twilightforest.tileentity;

import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import twilightforest.block.BlockTFFireJet;
import twilightforest.block.TFBlocks;
import twilightforest.enums.FireJetVariant;

public class TileEntityTFPoppingJet extends TileEntity implements ITickable {

	private int counter = 0;
	private FireJetVariant nextVariant;

	public TileEntityTFPoppingJet(FireJetVariant variant) {
		this.nextVariant = variant;
	}

	public TileEntityTFPoppingJet() {}

	@Override
	public void update() {
		if (++counter >= 80) {
			counter = 0;
			// turn to flame
			if (!world.isRemote && world.getBlockState(pos).getBlock() == TFBlocks.fire_jet) {
				world.setBlockState(pos, TFBlocks.fire_jet.getDefaultState().withProperty(BlockTFFireJet.VARIANT, nextVariant), 3);
			}
		} else {
			if (counter % 20 == 0) {
				for (int i = 0; i < 8; i++)
				{
					world.spawnParticle(EnumParticleTypes.LAVA, this.pos.getX() + 0.5, this.pos.getY() + 1.5, this.pos.getZ() + 0.5, 0.0D, 0.0D, 0.0D);
				}
				world.playSound(null, pos, SoundEvents.BLOCK_LAVA_POP, SoundCategory.BLOCKS, 0.2F + world.rand.nextFloat() * 0.2F, 0.9F + world.rand.nextFloat() * 0.15F);
			}

		}

	}

	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readFromNBT(par1NBTTagCompound);
		this.nextVariant = FireJetVariant.values()[par1NBTTagCompound.getInteger("NextMeta")];
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound par1NBTTagCompound) {
		NBTTagCompound ret = super.writeToNBT(par1NBTTagCompound);
		ret.setInteger("NextMeta", nextVariant.ordinal());
		return ret;
	}
}

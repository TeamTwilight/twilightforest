package twilightforest.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.TwilightForestMod;
import twilightforest.block.TFBlocks;

public class TileEntityTFTrophy extends TileEntity implements ITickableTileEntity {

	private int animatedTicks;
	private boolean animated;

	public TileEntityTFTrophy() {
		super(TFTileEntities.TROPHY.get());
	}

	@Override
	public void tick() {
		BlockState blockstate = this.getBlockState();
		if (blockstate.isIn(TFBlocks.ur_ghast_trophy.get()) || blockstate.isIn(TFBlocks.ur_ghast_wall_trophy.get())) {
			this.animated = true;
			++this.animatedTicks;
		}
	}

	@OnlyIn(Dist.CLIENT)
	public float getAnimationProgress(float time) {
		return this.animated ? (float) this.animatedTicks + time : (float) this.animatedTicks;
	}
}

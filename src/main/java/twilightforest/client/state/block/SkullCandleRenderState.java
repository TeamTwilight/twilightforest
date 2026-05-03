package twilightforest.client.state.block;

import com.mojang.math.Transformation;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.blockentity.state.SkullBlockRenderState;

public class SkullCandleRenderState extends SkullBlockRenderState {

	public Transformation candleTransformation = Transformation.IDENTITY;
	public BlockModelRenderState candle = new BlockModelRenderState();
}

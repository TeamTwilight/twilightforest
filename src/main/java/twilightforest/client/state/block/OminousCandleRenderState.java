package twilightforest.client.state.block;

import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.world.phys.Vec3;

import java.util.Collections;
import java.util.List;

public class OminousCandleRenderState extends BlockEntityRenderState {

	public List<BlockModelRenderState> candles = Collections.emptyList();
	public List<Vec3> offsets = Collections.emptyList();
	public long time;
}

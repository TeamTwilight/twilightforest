package twilightforest.client.state.block;

import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import twilightforest.enums.BrazierLight;

public class BrazierRenderState extends BlockEntityRenderState {

	public BrazierLight light = BrazierLight.OFF;
	public BlockModelRenderState fire = new BlockModelRenderState();
}

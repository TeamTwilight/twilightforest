package twilightforest.client.state.block;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;

public class JarRenderState extends BlockEntityRenderState {

	public DecoratedPotBlockEntity.WobbleStyle lastWobbleStyle;
	public float gameTime;
}

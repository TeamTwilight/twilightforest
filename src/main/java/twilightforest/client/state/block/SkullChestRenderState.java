package twilightforest.client.state.block;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;

public class SkullChestRenderState extends BlockEntityRenderState {

	public Direction facing = Direction.SOUTH;
	public Identifier texture;
	public float open;
}

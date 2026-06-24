package twilightforest.client.state.block;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.core.Direction;

import java.util.HashMap;
import java.util.Map;

public class RedThreadRenderState extends BlockEntityRenderState {

	public boolean glowing;
	public Map<Direction, Map<Direction, Boolean>> connections = new HashMap<>();
}

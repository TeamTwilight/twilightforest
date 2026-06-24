package twilightforest.client.state.block;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.resources.Identifier;
import org.joml.Vector3f;

public class ReactorDebrisRenderState extends BlockEntityRenderState {

	public Identifier[] textures = new Identifier[6];
	public Vector3f minPos;
	public Vector3f maxPos;
}

package twilightforest.client.state.entity;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class ChainBlockRenderState extends EntityRenderState {
	public float xRot;
	public float yRot;
	public boolean isFoil;
	@Nullable
	public Vec3 chainStartPos;
	public int ownerLight;
	public float blockHeight;
}

package twilightforest.client.state.entity;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import twilightforest.entity.MagicPaintingVariant;

import javax.annotation.Nullable;

public class MagicPaintingRenderState extends EntityRenderState {
	public Direction direction = Direction.NORTH;
	public Vec3 position = new Vec3(this.x, this.y, this.z);
	public float yRot;
	@Nullable
	public MagicPaintingVariant variant;
	public Identifier texture;
	public int[] lightCoords = new int[0];
}

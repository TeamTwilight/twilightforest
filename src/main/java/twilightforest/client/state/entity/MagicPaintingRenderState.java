package twilightforest.client.state.entity;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import twilightforest.entity.MagicPaintingVariant;

public class MagicPaintingRenderState extends EntityRenderState {
	public Direction direction = Direction.NORTH;
	public Vec3 position = new Vec3(this.x, this.y, this.z);
	public float yRot;
	public @Nullable MagicPaintingVariant variant;
	public Identifier texture;
	public int[] lightCoords = new int[0];
}

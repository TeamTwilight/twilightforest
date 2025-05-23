package twilightforest.client.state;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.network.chat.Component;

import javax.annotation.Nullable;

public class PartEntityState extends EntityRenderState {
	public float yRot;
	public float yRotO;
	public float xRot;
	public float deathTime;
	public float walkAnimationPos;
	public float walkAnimationSpeed;
	public boolean isUpsideDown;
	public boolean isInWater;
	public boolean hasRedOverlay;
	public boolean isInvisibleToPlayer;
	public boolean appearsGlowing;
	@Nullable
	public Component customName;
}

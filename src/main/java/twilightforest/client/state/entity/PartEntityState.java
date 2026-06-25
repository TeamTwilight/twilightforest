package twilightforest.client.state.entity;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

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
	/** The renderer ID from TFPart.renderer(), used during submit phase to look up the correct renderer */
	@Nullable
	public Identifier partRendererId;
}

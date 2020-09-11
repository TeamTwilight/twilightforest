package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.entity.RenderArrow;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;

public class RenderBoneShard<T extends EntityArrow> extends RenderArrow<T> {
	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("bone_shard.png");

	public RenderBoneShard(RenderManager manager) {
		super(manager);
	}

	@Override
	protected ResourceLocation getEntityTexture(T entity) {
		return textureLoc;
	}
}

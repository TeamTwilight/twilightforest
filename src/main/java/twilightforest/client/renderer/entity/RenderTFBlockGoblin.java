package twilightforest.client.renderer.entity;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.entity.EntityTFBlockGoblin;

public class RenderTFBlockGoblin extends RenderBiped<EntityTFBlockGoblin> {
	private static final ResourceLocation textureLoc = new ResourceLocation(TwilightForestMod.MODEL_DIR + "blockgoblin.png");

	public RenderTFBlockGoblin(RenderManager manager, ModelBiped model, float shadowSize) {
		super(manager, model, shadowSize);
	}

	@Override
	public void doRender(EntityTFBlockGoblin goblin, double x, double y, double z, float yaw, float partialTicks) {
		super.doRender(goblin, x, y, z, yaw, partialTicks);
		renderManager.renderEntityStatic(goblin.block, partialTicks, false);
		renderManager.renderEntityStatic(goblin.chain1, partialTicks, false);
		renderManager.renderEntityStatic(goblin.chain2, partialTicks, false);
		renderManager.renderEntityStatic(goblin.chain3, partialTicks, false);//renderEntity
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityTFBlockGoblin entity) {
		return textureLoc;
	}
}

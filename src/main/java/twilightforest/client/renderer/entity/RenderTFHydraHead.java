package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.ModelTFHydraHead;
import twilightforest.entity.boss.EntityTFHydra;
import twilightforest.entity.boss.EntityTFHydraHead;
import twilightforest.entity.boss.HydraHeadContainer;
//FIXME: merge into the base hydra renderer
public class RenderTFHydraHead {/*extends MobRenderer<EntityTFHydraHead, ModelTFHydraHead> {

	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("hydra4.png");

	public RenderTFHydraHead(EntityRendererManager manager, ModelTFHydraHead modelbase, float shadowSize) {
		super(manager, modelbase, shadowSize);
	}

	@Override
	public void render(EntityTFHydraHead entity, float yaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer, int light) {
		// get the HydraHeadContainer that we're taking about
		HydraHeadContainer headCon = getHeadObject(entity);

		if (headCon != null) {
			// see whether we want to render these
			if (headCon.shouldRenderHead()) {
				super.render(entity, yaw, partialTicks, stack, buffer, light);
			}

			//TODO: Idk what I should do about this
//			if (headCon.shouldRenderNeck(0)) {
//				renderManager.renderEntityStatic(headCon.necka, partialTicks, false);
//			}
//			if (headCon.shouldRenderNeck(1)) {
//				renderManager.renderEntityStatic(headCon.neckb, partialTicks, false);
//			}
//			if (headCon.shouldRenderNeck(2)) {
//				renderManager.renderEntityStatic(headCon.neckc, partialTicks, false);
//			}
//			if (headCon.shouldRenderNeck(3)) {
//				renderManager.renderEntityStatic(headCon.neckd, partialTicks, false);
//			}
//			if (headCon.shouldRenderNeck(4)) {
//				renderManager.renderEntityStatic(headCon.necke, partialTicks, false);
//			}
		} else {
			super.render(entity, yaw, partialTicks, stack, buffer, light);
		}
	}

	private HydraHeadContainer getHeadObject(EntityTFHydraHead entity) {
		EntityTFHydra hydra = entity.hydra;

		if (hydra != null) {
			for (int i = 0; i < hydra.numHeads; i++) {
				if (hydra.hc[i].headEntity == entity) {
					return hydra.hc[i];
				}
			}
		}
		return null;
	}

	@Override
	public ResourceLocation getEntityTexture(EntityTFHydraHead entity) {
		return textureLoc;
	}*/
}

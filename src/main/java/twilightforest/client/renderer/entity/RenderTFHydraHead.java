package twilightforest.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.entity.boss.EntityTFHydra;
import twilightforest.entity.boss.EntityTFHydraHead;
import twilightforest.entity.boss.EntityTFHydraPart;
import twilightforest.entity.boss.HydraHeadContainer;

public class RenderTFHydraHead extends RenderLiving<EntityTFHydraHead> {
	private static final ResourceLocation textureLoc = new ResourceLocation(TwilightForestMod.MODEL_DIR + "hydra4.png");

	public RenderTFHydraHead(RenderManager manager, ModelBase modelbase, float shadowSize) {
		super(manager, modelbase, shadowSize);
	}

	@Override
	public void doRender(EntityTFHydraHead entity, double d, double d1, double d2, float f, float f1) {
		// get the HydraHeadContainer that we're taking about
		HydraHeadContainer headCon = getHeadObject(entity);

		if (headCon != null) {
			// see whether we want to render these
			if (headCon.shouldRenderHead()) {
				super.doRender(entity, d, d1, d2, f, f1);
			}

			if (headCon.shouldRenderNeck(0)) {
				renderManager.renderEntityStatic(headCon.necka, f1, false);
			}
			if (headCon.shouldRenderNeck(1)) {
				renderManager.renderEntityStatic(headCon.neckb, f1, false);
			}
			if (headCon.shouldRenderNeck(2)) {
				renderManager.renderEntityStatic(headCon.neckc, f1, false);
			}
			if (headCon.shouldRenderNeck(3)) {
				renderManager.renderEntityStatic(headCon.neckd, f1, false);
			}
			if (headCon.shouldRenderNeck(4)) {
				renderManager.renderEntityStatic(headCon.necke, f1, false);
			}
		} else {
			super.doRender(entity, d, d1, d2, f, f1);

		}
	}

	private HydraHeadContainer getHeadObject(Entity entity) {
		EntityTFHydra hydra = ((EntityTFHydraPart) entity).hydraObj;

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
	protected ResourceLocation getEntityTexture(EntityTFHydraHead par1Entity) {
		return textureLoc;
	}

}

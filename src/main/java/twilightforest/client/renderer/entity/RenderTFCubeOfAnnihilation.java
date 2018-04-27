package twilightforest.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.ModelTFCubeOfAnnihilation;
import twilightforest.entity.EntityTFCubeOfAnnihilation;

public class RenderTFCubeOfAnnihilation extends Render<EntityTFCubeOfAnnihilation> {
	private static final ResourceLocation textureLoc = new ResourceLocation(TwilightForestMod.MODEL_DIR + "cubeofannihilation.png");
	private final ModelBase model = new ModelTFCubeOfAnnihilation();

	public RenderTFCubeOfAnnihilation(RenderManager manager) {
		super(manager);
	}

	@Override
	public void doRender(EntityTFCubeOfAnnihilation par1Entity, double x, double y, double z, float yaw, float partialTicks) {
		super.doRender(par1Entity, x, y, z, yaw, partialTicks);
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) x, (float) y, (float) z);

		this.bindEntityTexture(par1Entity);

		GlStateManager.scale(-1.0F, -1.0F, 1.0F);

		GlStateManager.rotate(MathHelper.wrapDegrees(((float) x + (float) z + par1Entity.ticksExisted + partialTicks) * 11F), 0, 1, 0);


		GlStateManager.disableLighting();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		GlStateManager.translate(0F, -0.5F, 0F);
		this.model.render(par1Entity, 0.0F, 0.0F, 0.0F, 0.0F, partialTicks, 0.0625F / 2F);
		GlStateManager.enableLighting();
		GlStateManager.disableBlend();


		GlStateManager.popMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityTFCubeOfAnnihilation par1Entity) {
		return textureLoc;
	}
}

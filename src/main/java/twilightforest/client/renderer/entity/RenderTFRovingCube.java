package twilightforest.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.ModelTFCubeOfAnnihilation;
import twilightforest.entity.EntityTFRovingCube;

public class RenderTFRovingCube extends Render<EntityTFRovingCube> {
	private static final ResourceLocation textureLoc = new ResourceLocation(TwilightForestMod.MODEL_DIR + "cubeofannihilation.png");
	private final ModelBase model = new ModelTFCubeOfAnnihilation();

	public RenderTFRovingCube(RenderManager manager) {
		super(manager);
	}

	@Override
	public void doRender(EntityTFRovingCube par1Entity, double par2, double par4, double par6, float par8, float par9) {
		GlStateManager.pushMatrix();
		GlStateManager.translate(par2, par4, par6);

		this.bindEntityTexture(par1Entity);

		GlStateManager.scale(2.0F, 2.0F, 2.0F);

		GlStateManager.rotate(MathHelper.wrapDegrees(((float) par2 + (float) par6 + ((Entity) par1Entity).ticksExisted + par9) * 11F), 0, 1, 0);

		GlStateManager.disableLighting();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		GlStateManager.translate(0F, 0.75F, 0F);
		this.model.render(par1Entity, 0.0F, 0.0F, 0.0F, 0.0F, par9, 0.0625F / 2F);
		GlStateManager.enableLighting();
		GlStateManager.disableBlend();

		GlStateManager.popMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityTFRovingCube par1Entity) {
		return textureLoc;
	}
}

package twilightforest.client.model.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;

public class ModelTFFieryArmor extends ModelTFArmor {

	public ModelTFFieryArmor(float expand) {
		super(expand);
	}

	@Override
	public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7) {
		// FULL BRIGHT
		Minecraft.getMinecraft().entityRenderer.disableLightmap();
		super.render(par1Entity, par2, par3, par4, par5, par6, par7);
		Minecraft.getMinecraft().entityRenderer.enableLightmap();
	}
}

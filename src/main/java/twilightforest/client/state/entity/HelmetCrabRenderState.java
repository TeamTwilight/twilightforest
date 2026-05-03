package twilightforest.client.state.entity;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.util.Mth;
import twilightforest.entity.monster.HelmetCrab;

public class HelmetCrabRenderState extends LivingEntityRenderState {
	public float helmetRot;
	public int id;
	public boolean blue;


    public float getHelmetRotation(HelmetCrab entity, float partialTicks) {
        float f = Mth.rotLerp(partialTicks, entity.yBodyRotO, entity.yBodyRot);
        float f1 = Mth.rotLerp(partialTicks, entity.helmetRotO, entity.helmetRot);
        float f2 = f1 - f;
        return Mth.wrapDegrees(f2) - 25;
    }
}

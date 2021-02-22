package twilightforest.entity.boss;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.client.renderer.entity.RenderTFHydraNeck;

public class EntityTFHydraNeck extends EntityTFHydraPart {

	public final EntityTFHydraHead head;

	public EntityTFHydraNeck(EntityTFHydraHead head) {
		super(head.getParent(), 2F, 2F);
		this.head = head;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public EntityRenderer<?> renderer(EntityRendererManager manager) {
		return new RenderTFHydraNeck(manager);
	}
}

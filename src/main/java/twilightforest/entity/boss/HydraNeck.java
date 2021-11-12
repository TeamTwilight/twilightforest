package twilightforest.entity.boss;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.TwilightForestMod;

public class HydraNeck extends HydraPart {

	public static final ResourceLocation RENDERER = TwilightForestMod.prefix("hydra_neck");

	public final HydraHead head;

	public HydraNeck(HydraHead head) {
		super(head.getParent(), 2F, 2F);
		this.head = head;
	}

	@OnlyIn(Dist.CLIENT)
	public ResourceLocation renderer() {
		return RENDERER;
	}
}

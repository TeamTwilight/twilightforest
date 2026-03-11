package twilightforest.entity.boss;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import twilightforest.TwilightForestMod;

public class HydraNeck extends HydraPart {

	public static final ResourceLocation RENDERER = TwilightForestMod.prefix("hydra_neck");

	public final HydraHead head;

	public HydraNeck(HydraHead head) {
		super(head.getParent(), 2F, 2F);
		this.head = head;
	}

	@Override
	public InteractionResult interact(Player player, InteractionHand hand) {
		return this.head.interact(player, hand);
	}

	@Override
	public ResourceLocation renderer() {
		return RENDERER;
	}
}

package twilightforest.entity.boss;

import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import twilightforest.TwilightForestMod;

public class HydraNeck extends HydraPart {

	public static final Identifier RENDERER = TwilightForestMod.prefix("hydra_neck");

	public final HydraHead head;

	public HydraNeck(HydraHead head) {
		super(head.getParent(), 2F, 2F);
		this.head = head;
	}

	@Override
	public InteractionResult interact(Player player, InteractionHand hand, Vec3 location) {
		return this.head.interact(player, hand, location);
	}

	@Override
	public Identifier renderer() {
		return RENDERER;
	}
}

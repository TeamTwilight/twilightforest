package twilightforest.compat.tcon.traits;

import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerEvent;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import twilightforest.TFConfig;

public class TwilitModifier extends NoLevelsModifier {

	private final ResourceKey<Level> tfDim = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(TFConfig.COMMON_CONFIG.DIMENSION.portalDestinationID.get()));

	@Override
	public float getEntityDamage(IToolStackView tool, int level, ToolAttackContext context, float baseDamage, float damage) {
		return !context.getAttacker().level.dimension().equals(tfDim) ? damage + 2.0F : damage;
	}

	@Override
	public void onBreakSpeed(IToolStackView tool, int level, PlayerEvent.BreakSpeed event, Direction sideHit, boolean isEffective, float miningSpeedModifier) {
		if(event.getEntityLiving().level.dimension().equals(tfDim)) {
			event.setNewSpeed(event.getNewSpeed() + 2.0F);
		}
	}
}

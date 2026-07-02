package twilightforest.compat.jade;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import snownee.jade.api.*;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.JadeUI;
import twilightforest.TwilightForestMod;
import twilightforest.compat.RecipeViewerConstants;

public enum DryingRackComponentProvider implements IBlockComponentProvider {
	INSTANCE;

	@Override
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
		CompoundTag data = accessor.getServerData();
		if (data.contains("progress")) {
			int progress = data.getIntOr("progress", 0);
			int total = data.getIntOr("total", 0);
			tooltip.add(JadeUI.text(Component.translatable("jade.drying_rack.remaining", RecipeViewerConstants.getDryingTime(total - progress))).offset(10, 0));
		}
	}

	@Override
	public Identifier getUid() {
		return TwilightForestMod.prefix("drying_rack");
	}
}
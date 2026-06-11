package twilightforest.compat.jade;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntity;
import snownee.jade.api.*;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.JadeUI;
import twilightforest.TwilightForestMod;
import twilightforest.block.entity.DryingRackBlockEntity;
import twilightforest.compat.RecipeViewerConstants;

public enum DryingRackProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
	INSTANCE;

	@Override
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
		CompoundTag data = accessor.getServerData();
		if (data.contains("progress")) {
			int progress = data.getInt("progress").orElseThrow();
			int total = data.getInt("total").orElseThrow();
			tooltip.add(JadeUI.text(Component.translatable("jade.drying_rack.remaining", RecipeViewerConstants.getDryingTime(total - progress))).offset(10, 0));
		}
	}

	@Override
	public void appendServerData(CompoundTag data, BlockAccessor accessor) {
		BlockEntity entity = accessor.getBlockEntity();
		if (entity instanceof DryingRackBlockEntity rack && rack.isDrying()) {
			CompoundTag tag = rack.saveWithoutMetadata(accessor.getLevel().registryAccess());
			data.putInt("progress", tag.getInt("dry_time").orElseThrow());
			data.putInt("total", tag.getInt("total_dry_time").orElseThrow());
		}
	}

	@Override
	public Identifier getUid() {
		return TwilightForestMod.prefix("drying_rack");
	}
}

package twilightforest.compat.jade;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec2;
import snownee.jade.api.*;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElementHelper;
import twilightforest.TwilightForestMod;
import twilightforest.block.entity.DryingRackBlockEntity;
import twilightforest.compat.RecipeViewerConstants;

public enum DryingRackProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
	INSTANCE;

	@Override
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
		CompoundTag data = accessor.getServerData();
		if (data.contains("progress")) {
			int progress = data.getInt("progress");

			IElementHelper helper = IElementHelper.get();
			int total = data.getInt("total");
			tooltip.add(helper.text(Component.translatable("jade.drying_rack.remaining", RecipeViewerConstants.getDryingTime(total - progress))).translate(new Vec2(10.0F, 0.0F)));
		}
	}

	@Override
	public void appendServerData(CompoundTag data, BlockAccessor accessor) {
		BlockEntity entity = accessor.getBlockEntity();
		if (entity instanceof DryingRackBlockEntity rack && rack.isDrying()) {
			CompoundTag tag = rack.saveWithoutMetadata(accessor.getLevel().registryAccess());
			data.putInt("progress", tag.getInt("dry_time"));
			data.putInt("total", tag.getInt("total_dry_time"));
		}
	}

	@Override
	public Identifier getUid() {
		return TwilightForestMod.prefix("drying_rack");
	}
}

package twilightforest.compat.jade;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntity;
import snownee.jade.api.*;
import twilightforest.TwilightForestMod;
import twilightforest.block.entity.DryingRackBlockEntity;

public enum DryingRackDataProvider implements IServerDataProvider<BlockAccessor> {
	INSTANCE;

	@Override
	public void appendServerData(CompoundTag data, BlockAccessor accessor) {
		BlockEntity entity = accessor.getBlockEntity();
		if (entity instanceof DryingRackBlockEntity rack && rack.isDrying()) {
			data.putInt("progress", rack.getDryTime());
			data.putInt("total", rack.getTotalDryTime());
		}
	}

	@Override
	public Identifier getUid() {
		return TwilightForestMod.prefix("drying_rack");
	}
}
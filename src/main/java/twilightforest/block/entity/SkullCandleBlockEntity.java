package twilightforest.block.entity;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SkullCandleBlockEntity extends SkullBlockEntity {

	public int candleColor;
	public int candleAmount;

	public SkullCandleBlockEntity(BlockPos pos, BlockState state) {
		super(pos, state);
	}

	public SkullCandleBlockEntity(BlockPos pos, BlockState state, int color, int amount) {
		super(pos, state);
		this.candleColor = color;
		this.candleAmount = amount;
	}

	@Override
	public BlockEntityType<?> getType() {
		return TFBlockEntities.SKULL_CANDLE.get();
	}
	@Override
	public CompoundTag save(CompoundTag tag) {
		super.save(tag);
		tag.putInt("CandleColor", candleColor);
		if(candleAmount != 0) tag.putInt("CandleAmount", candleAmount);
		return tag;
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		candleColor = tag.getInt("CandleColor");
		candleAmount = tag.getInt("CandleAmount");
	}
}

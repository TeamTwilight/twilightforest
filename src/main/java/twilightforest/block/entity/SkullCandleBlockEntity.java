package twilightforest.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.init.TFBlockEntities;

public class SkullCandleBlockEntity extends SkullBlockEntity {

	private int candleColor;

	private int animationTickCount;
	private boolean isAnimating;

	public SkullCandleBlockEntity(BlockPos pos, BlockState state) {
		super(pos, state);
	}

	public SkullCandleBlockEntity(BlockPos pos, BlockState state, int color) {
		super(pos, state);
		this.candleColor = color;
	}

	@Override
	public BlockEntityType<?> getType() {
		return TFBlockEntities.SKULL_CANDLE.get();
	}

	@Override
	protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
		super.saveAdditional(tag, provider);
		tag.putInt("CandleColor", this.candleColor);
	}

	@Override
	protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
		super.loadAdditional(tag, provider);
		this.candleColor = tag.getInt("CandleColor");
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
		CompoundTag tag = super.getUpdateTag(provider);
		tag.putInt("CandleColor", this.candleColor);
		return tag;
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	public int getCandleColor() {
		return this.candleColor;
	}

	public void setCandleColor(int color) {
		this.candleColor = color;
		this.setChanged();
		this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
	}

	public static void tick(Level level, BlockPos pos, BlockState state, SkullCandleBlockEntity entity) {
		if (level.hasNeighborSignal(pos)) {
			entity.isAnimating = true;
			++entity.animationTickCount;
		} else {
			entity.isAnimating = false;
		}
	}

	@Override
	public float getAnimation(float partialTick) {
		return this.isAnimating ? (float) this.animationTickCount + partialTick : (float) this.animationTickCount;
	}
}

package twilightforest.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;
import twilightforest.components.item.SkullCandles;
import twilightforest.init.TFBlockEntities;
import twilightforest.init.TFDataComponents;

public class SkullCandleBlockEntity extends SkullBlockEntity {
	private SkullCandles candleInfo = SkullCandles.DEFAULT;

	public SkullCandleBlockEntity(BlockPos pos, BlockState state) {
		super(pos, state);
	}

	@Override
	public boolean isValidBlockState(BlockState state) {
		return this.getType().isValid(state);
	}

	@Override
	public BlockEntityType<?> getType() {
		return TFBlockEntities.SKULL_CANDLE.get();
	}

	@Override
	protected void saveAdditional(ValueOutput output) {
		super.saveAdditional(output);
		output.store("info", SkullCandles.CODEC, this.candleInfo);
	}

	@Override
	protected void loadAdditional(ValueInput input) {
		super.loadAdditional(input);
		this.candleInfo = input.read("info", SkullCandles.CODEC).orElse(SkullCandles.DEFAULT);
	}

	@Override
	protected void applyImplicitComponents(DataComponentGetter components) {
		super.applyImplicitComponents(components);
		this.candleInfo = components.getOrDefault(TFDataComponents.SKULL_CANDLES, SkullCandles.DEFAULT);
	}

	@Override
	protected void collectImplicitComponents(DataComponentMap.Builder components) {
		super.collectImplicitComponents(components);
		components.set(TFDataComponents.SKULL_CANDLES, this.candleInfo);
	}

	@Override
	public void removeComponentsFromTag(ValueOutput output) {
		super.removeComponentsFromTag(output);
		output.discard("info");
	}

	@Override
	public @Nullable ResolvableProfile getOwnerProfile() {
		return this.owner;
	}

	public void setOwnerProfile(ResolvableProfile newProfile) {
		DataComponentPatch patch = DataComponentPatch.builder()
			.set(DataComponents.PROFILE, newProfile)
			.build();
		applyComponents(collectComponents(), patch);
	}

	public SkullCandles getCandleInfo() {
		return this.candleInfo;
	}

	public void setCandleInfo(SkullCandles newInfo) {
		DataComponentPatch patch = DataComponentPatch.builder()
			.set(TFDataComponents.SKULL_CANDLES.get(), newInfo)
			.build();
		applyComponents(collectComponents(), patch);
	}
}
package twilightforest.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
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
	private @Nullable ResolvableProfile owner;

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
		output.storeNullable("profile", ResolvableProfile.CODEC, this.owner);
	}

	@Override
	protected void loadAdditional(ValueInput input) {
		super.loadAdditional(input);
		this.candleInfo = input.read("info", SkullCandles.CODEC).orElse(SkullCandles.DEFAULT);
		this.owner = input.read("profile", ResolvableProfile.CODEC).orElse(null);
	}

	@Override
	protected void applyImplicitComponents(DataComponentGetter components) {
		super.applyImplicitComponents(components);
		this.candleInfo = components.getOrDefault(TFDataComponents.SKULL_CANDLES, SkullCandles.DEFAULT);
		this.owner = components.get(DataComponents.PROFILE);
	}

	@Override
	protected void collectImplicitComponents(DataComponentMap.Builder components) {
		super.collectImplicitComponents(components);
		components.set(TFDataComponents.SKULL_CANDLES, this.candleInfo);
		components.set(DataComponents.PROFILE, this.owner);
	}

	@Override
	public void removeComponentsFromTag(ValueOutput output) {
		super.removeComponentsFromTag(output);
		output.discard("info");
		output.discard("profile");
	}

	@Override
	public @Nullable ResolvableProfile getOwnerProfile() {
		return this.owner;
	}

	public SkullCandles getCandleInfo() {
		return this.candleInfo;
	}
}

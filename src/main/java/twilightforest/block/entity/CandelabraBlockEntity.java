package twilightforest.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import twilightforest.block.CandelabraBlock;
import twilightforest.block.LightableBlock;
import twilightforest.components.item.CandelabraData;
import twilightforest.init.TFBlockEntities;
import twilightforest.init.TFDataComponents;

import java.util.ArrayList;
import java.util.List;

public class CandelabraBlockEntity extends BlockEntity {

	private CandelabraData data;

	public CandelabraBlockEntity(BlockPos pos, BlockState state) {
		super(TFBlockEntities.CANDELABRA.get(), pos, state);
		this.data = CandelabraData.EMPTY;
	}

	public CandelabraData getCandles() {
		return this.data;
	}

	public void setData(CandelabraData data) {
		this.data = data;
	}

	public Block removeCandle(int index) {
		Block block = CandelabraData.getItem(this.data.ordered(), index).orElse(Blocks.AIR);
		this.setCandle(index, Blocks.AIR);
		return block;
	}

	public void setCandle(int index, Block block) {
		List<Block> list = new ArrayList<>(this.data.ordered());
		list.set(index, block);
		this.data = new CandelabraData(list);
		this.updateState(index);
		this.setChanged();
		this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
	}

	public Block getCandle(int index) {
		return CandelabraData.getItem(this.data.ordered(), index).orElse(Blocks.AIR);
	}

	@Override
	protected void saveAdditional(ValueOutput output) {
		super.saveAdditional(output);
		output.store("Candles", CandelabraData.CODEC, this.data);
	}

	@Override
	protected void loadAdditional(ValueInput input) {
		super.loadAdditional(input);
		this.data = input.read("Candles", CandelabraData.CODEC).orElse(CandelabraData.EMPTY);
	}

	public void updateState(int index) {
		if (index >= 0 && index < 3) {
			BlockState blockstate = this.getBlockState();

			for (int i = 0; i < CandelabraBlock.CANDLES.size(); ++i) {
				boolean flag = !this.getCandle(i).defaultBlockState().isAir();
				BooleanProperty booleanproperty = CandelabraBlock.CANDLES.get(i);
				blockstate = blockstate.setValue(booleanproperty, flag);
			}

			if (CandelabraBlock.getCandleCount(blockstate) == 0 && blockstate.getValue(CandelabraBlock.LIGHTING) != LightableBlock.Lighting.NONE) {
				blockstate = blockstate.setValue(CandelabraBlock.LIGHTING, LightableBlock.Lighting.NONE);
			}

			this.getLevel().setBlock(this.getBlockPos(), blockstate, Block.UPDATE_ALL);
			this.getLevel().gameEvent(GameEvent.BLOCK_CHANGE, this.getBlockPos(), GameEvent.Context.of(blockstate));
		}
	}

	@Override
	protected void collectImplicitComponents(DataComponentMap.Builder components) {
		super.collectImplicitComponents(components);
		components.set(TFDataComponents.CANDELABRA_DATA, this.data);
	}

	@Override
	protected void applyImplicitComponents(DataComponentGetter components) {
		super.applyImplicitComponents(components);
		this.data = components.getOrDefault(TFDataComponents.CANDELABRA_DATA, CandelabraData.EMPTY);
	}

	@Override
	public void removeComponentsFromTag(ValueOutput output) {
		super.removeComponentsFromTag(output);
		output.discard("Candles");
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
		return this.saveCustomOnly(provider);
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}
}

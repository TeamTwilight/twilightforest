package twilightforest.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.PotDecorations;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import twilightforest.block.CandelabraBlock;
import twilightforest.block.LightableBlock;
import twilightforest.components.item.CandelabraData;
import twilightforest.init.TFBlockEntities;
import twilightforest.init.TFDataComponents;

import java.util.Arrays;
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
		List<Block> list = this.data.ordered();
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
	public void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
		super.saveAdditional(tag, provider);
		this.data.save(tag);
	}

	@Override
	protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
		super.loadAdditional(tag, provider);
		this.data = CandelabraData.load(tag);
	}

	private void updateState(int index) {
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

			this.getLevel().setBlock(this.getBlockPos(), blockstate, 3);
			this.getLevel().gameEvent(GameEvent.BLOCK_CHANGE, this.getBlockPos(), GameEvent.Context.of(blockstate));
		}
	}

	@Override
	protected void collectImplicitComponents(DataComponentMap.Builder components) {
		super.collectImplicitComponents(components);
		components.set(TFDataComponents.CANDELABRA_DATA, this.data);
	}

	@Override
	protected void applyImplicitComponents(BlockEntity.DataComponentInput componentInput) {
		super.applyImplicitComponents(componentInput);
		this.data = componentInput.getOrDefault(TFDataComponents.CANDELABRA_DATA, CandelabraData.EMPTY);
	}

	@Override
	public void removeComponentsFromTag(CompoundTag tag) {
		super.removeComponentsFromTag(tag);
		tag.remove("Candles");
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

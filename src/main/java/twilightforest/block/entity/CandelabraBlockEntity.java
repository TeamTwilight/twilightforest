package twilightforest.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChiseledBookShelfBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import twilightforest.block.CandelabraBlock;
import twilightforest.block.LightableBlock;
import twilightforest.init.TFBlockEntities;

import java.util.Arrays;
import java.util.Objects;

public class CandelabraBlockEntity extends BlockEntity {

	private final Block[] candles = {Blocks.AIR, Blocks.AIR, Blocks.AIR};

	public CandelabraBlockEntity(BlockPos pos, BlockState state) {
		super(TFBlockEntities.CANDELABRA.get(), pos, state);
	}

	public Block[] getCandles() {
		return this.candles;
	}

	public Block removeCandle(int index) {
		Block block = this.candles[index];
		this.setCandle(index, Blocks.AIR);
		return block;
	}

	public void setCandle(int index, Block block) {
		this.candles[index] = block;
		this.updateState(index);
		this.setChanged();
		this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
	}

	public Block getCandle(int index) {
		return this.candles[index];
	}

	@Override
	public void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		ListTag list = new ListTag();
		Arrays.stream(this.candles).toList().forEach(block -> list.add(StringTag.valueOf(BuiltInRegistries.BLOCK.getKey(block).toString())));
		tag.put("Candles", list);

	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		if (tag.contains("Candles", Tag.TAG_LIST)) {
			ListTag list = tag.getList("Candles", Tag.TAG_STRING);
			for (int i = 0; i < list.size(); i++) {
				String name = list.getString(i);
				this.candles[i] = BuiltInRegistries.BLOCK.get(ResourceLocation.tryParse(name));
			}
		}
	}

	private void updateState(int index) {
		if (index >= 0 && index < 3) {
			BlockState blockstate = this.getBlockState();

			for(int i = 0; i < CandelabraBlock.CANDLES.size(); ++i) {
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
	public CompoundTag getUpdateTag() {
		CompoundTag tag = new CompoundTag();
		this.saveAdditional(tag);
		return tag;
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}
}

package twilightforest.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.neoforged.neoforge.registries.DeferredHolder;
import twilightforest.block.BanisterBlock;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WoodPalette {
	public static final Codec<WoodPalette> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			BuiltInRegistries.BLOCK.byNameCodec().fieldOf("planks").forGetter(p -> p.planks),
			BuiltInRegistries.BLOCK.byNameCodec().fieldOf("stairs").forGetter(p -> p.stairs),
			BuiltInRegistries.BLOCK.byNameCodec().fieldOf("slab").forGetter(p -> p.slab),
			BuiltInRegistries.BLOCK.byNameCodec().fieldOf("button").forGetter(p -> p.button),
			BuiltInRegistries.BLOCK.byNameCodec().fieldOf("fence").forGetter(p -> p.fence),
			BuiltInRegistries.BLOCK.byNameCodec().fieldOf("gate").forGetter(p -> p.gate),
			BuiltInRegistries.BLOCK.byNameCodec().fieldOf("plate").forGetter(p -> p.plate),
			BuiltInRegistries.BLOCK.byNameCodec().fieldOf("banister").forGetter(p -> p.banister)
	).apply(instance, WoodPalette::new));

	public WoodPalette(DeferredHolder<Block, Block> planks, DeferredHolder<Block, StairBlock> stairs, DeferredHolder<Block, Block> slab, DeferredHolder<Block, Block> button, DeferredHolder<Block, Block> fence, DeferredHolder<Block, Block> gate, DeferredHolder<Block, Block> plate, DeferredHolder<Block, BanisterBlock> banister) {
		this(planks.get(), stairs.get(), slab.get(), button.get(), fence.get(), gate.get(), plate.get(), banister.get());
	}

	private final Set<Block> blocks;
	private final Block planks;
	private final Block stairs;
	private final Block slab;
	private final Block button;
	private final Block fence;
	private final Block gate;
	private final Block plate;
	private final Block banister;

	public WoodPalette(Block planks, Block stairs, Block slab, Block button, Block fence, Block gate, Block plate, Block banister) {
		this.planks = planks;
		this.stairs = stairs;
		this.slab = slab;
		this.button = button;
		this.fence = fence;
		this.gate = gate;
		this.plate = plate;
		this.banister = banister;
		this.blocks = new HashSet<>(List.of(this.planks, this.stairs, this.slab, this.button, this.fence, this.gate, this.plate, this.banister));
	}

	public boolean contains(Block block) {
		return blocks.contains(block);
	}

	public StructureTemplate.StructureBlockInfo modifyBlockWithType(WoodPalette targetPalette, StructureTemplate.StructureBlockInfo state) {
		BlockState newState = state.state();

		if (targetPalette.contains(newState.getBlock()))
			return new StructureTemplate.StructureBlockInfo(state.pos(), this.modifyBlockWithType(targetPalette, newState), state.nbt());

		return state;
	}

	public BlockState modifyBlockWithType(WoodPalette targetPalette, BlockState state) {
		return switch (targetPalette.getWoodShapeFromBlock(state.getBlock())) {
			case BLOCK -> this.planks.defaultBlockState();
			case STAIRS -> FeaturePlacers.transferAllStateKeys(state, this.stairs);
			case SLAB -> FeaturePlacers.transferAllStateKeys(state, this.slab);
			case FENCE -> FeaturePlacers.transferAllStateKeys(state, this.fence);
			case GATE -> FeaturePlacers.transferAllStateKeys(state, this.gate);
			case BUTTON -> FeaturePlacers.transferAllStateKeys(state, this.button);
			case PLATE -> FeaturePlacers.transferAllStateKeys(state, this.plate);
			case BANISTER -> FeaturePlacers.transferAllStateKeys(state, this.banister);
			default -> state;
		};
	}

	public WoodShapes getWoodShapeFromBlock(Block b) {
		if (b == this.planks) return WoodShapes.BLOCK;
		if (b == this.stairs) return WoodShapes.STAIRS;
		if (b == this.slab) return WoodShapes.SLAB;
		if (b == this.button) return WoodShapes.BUTTON;
		if (b == this.fence) return WoodShapes.FENCE;
		if (b == this.gate) return WoodShapes.GATE;
		if (b == this.plate) return WoodShapes.PLATE;
		if (b == this.banister) return WoodShapes.BANISTER;

		return WoodShapes.INVALID;
	}

	public enum WoodShapes {
		BLOCK,
		STAIRS,
		SLAB,
		BUTTON,
		FENCE,
		GATE,
		PLATE,
		BANISTER,
		INVALID
	}
}

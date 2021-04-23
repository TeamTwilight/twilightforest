package twilightforest.enums;

import net.minecraft.block.*;
import net.minecraft.state.Property;
import net.minecraft.state.properties.SlabType;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.block.TFBlocks;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

public enum StructureWoodVariant implements IStringSerializable {

	OAK(Blocks.OAK_PLANKS, Blocks.OAK_STAIRS, Blocks.OAK_SLAB, Blocks.OAK_BUTTON, Blocks.OAK_FENCE, Blocks.OAK_FENCE_GATE, Blocks.OAK_PRESSURE_PLATE),
	SPRUCE(Blocks.SPRUCE_PLANKS, Blocks.SPRUCE_STAIRS, Blocks.SPRUCE_SLAB, Blocks.SPRUCE_BUTTON, Blocks.SPRUCE_FENCE, Blocks.SPRUCE_FENCE_GATE, Blocks.SPRUCE_PRESSURE_PLATE),
	BIRCH(Blocks.BIRCH_PLANKS, Blocks.BIRCH_STAIRS, Blocks.BIRCH_SLAB, Blocks.BIRCH_BUTTON, Blocks.BIRCH_FENCE, Blocks.BIRCH_FENCE_GATE, Blocks.BIRCH_PRESSURE_PLATE),
	JUNGLE(Blocks.JUNGLE_PLANKS, Blocks.JUNGLE_STAIRS, Blocks.JUNGLE_SLAB, Blocks.JUNGLE_BUTTON, Blocks.JUNGLE_FENCE, Blocks.JUNGLE_FENCE_GATE, Blocks.JUNGLE_PRESSURE_PLATE),
	ACACIA(Blocks.ACACIA_PLANKS, Blocks.ACACIA_STAIRS, Blocks.ACACIA_SLAB, Blocks.ACACIA_BUTTON, Blocks.ACACIA_FENCE, Blocks.ACACIA_FENCE_GATE, Blocks.ACACIA_PRESSURE_PLATE),
	DARK_OAK(Blocks.DARK_OAK_PLANKS, Blocks.DARK_OAK_STAIRS, Blocks.DARK_OAK_SLAB, Blocks.DARK_OAK_BUTTON, Blocks.DARK_OAK_FENCE, Blocks.DARK_OAK_FENCE_GATE, Blocks.DARK_OAK_PRESSURE_PLATE),

	TWILIGHT_OAK(TFBlocks.twilight_oak_planks, TFBlocks.twilight_oak_stairs, TFBlocks.twilight_oak_slab, TFBlocks.twilight_oak_button, TFBlocks.twilight_oak_fence, TFBlocks.twilight_oak_gate, TFBlocks.twilight_oak_plate),
	CANOPY(TFBlocks.canopy_planks, TFBlocks.canopy_stairs, TFBlocks.canopy_slab, TFBlocks.canopy_button, TFBlocks.canopy_fence, TFBlocks.canopy_gate, TFBlocks.canopy_plate),
	MANGROVE(TFBlocks.mangrove_planks, TFBlocks.mangrove_stairs, TFBlocks.mangrove_slab, TFBlocks.mangrove_button, TFBlocks.mangrove_fence, TFBlocks.mangrove_gate, TFBlocks.mangrove_plate),
	DARK(TFBlocks.dark_planks, TFBlocks.dark_stairs, TFBlocks.dark_slab, TFBlocks.dark_button, TFBlocks.dark_fence, TFBlocks.dark_gate, TFBlocks.dark_plate),
	TIME(TFBlocks.time_planks, TFBlocks.time_stairs, TFBlocks.time_slab, TFBlocks.time_button, TFBlocks.time_fence, TFBlocks.time_gate, TFBlocks.time_plate),
	TRANS(TFBlocks.trans_planks, TFBlocks.trans_stairs, TFBlocks.trans_slab, TFBlocks.trans_button, TFBlocks.trans_fence, TFBlocks.trans_gate, TFBlocks.trans_plate),
	MINE(TFBlocks.mine_planks, TFBlocks.mine_stairs, TFBlocks.mine_slab, TFBlocks.mine_button, TFBlocks.mine_fence, TFBlocks.mine_gate, TFBlocks.mine_plate),
	SORT(TFBlocks.sort_planks, TFBlocks.sort_stairs, TFBlocks.sort_slab, TFBlocks.sort_button, TFBlocks.sort_fence, TFBlocks.sort_gate, TFBlocks.sort_plate);

	private final List<Block> blocks;

	StructureWoodVariant(
			Block planks,
			Block stairs,
			Block slab,
			Block button,
			Block fence,
			Block gate,
			Block plate
	) {
		this.planks = planks;
		this.stairs = stairs;
		this.slab = slab;
		this.button = button;
		this.fence = fence;
		this.gate = gate;
		this.plate = plate;
		this.blocks = Arrays.asList(planks, stairs, slab, button, fence, gate, plate);
	}

	StructureWoodVariant(
			Supplier<Block> planks,
			Supplier<StairsBlock> stairs,
			Supplier<Block> slab,
			Supplier<Block> button,
			Supplier<Block> fence,
			Supplier<Block> gate,
			Supplier<Block> plate
	) {
		this.planks = planks.get();
		this.stairs = stairs.get();
		this.slab = slab.get();
		this.button = button.get();
		this.fence = fence.get();
		this.gate = gate.get();
		this.plate = plate.get();
		this.blocks = Arrays.asList(planks.get(), stairs.get(), slab.get(), button.get(), fence.get(), gate.get(), plate.get());
	}

	@Override
	public String getString() {
		return name().toLowerCase(Locale.ROOT);
	}

	private final Block planks;
	private final Block stairs;
	private final Block slab;
	private final Block button;
	private final Block fence;
	private final Block gate;
	private final Block plate;

	@Nullable
	public static StructureWoodVariant getVariantFromBlock(Block block) {
		for (StructureWoodVariant var : values()) {
			if (var.blocks.contains(block)) {
				return var;
			}
		}

		return null;
	}

	public static boolean isPlanks(BlockState stateIn) {
		return isPlanks(stateIn.getBlock());
	}

	public static boolean isPlanks(Block block) {
		StructureWoodVariant var = getVariantFromBlock(block);
		if (var == null) {
			return false;
		}

		return var.planks == block;
	}

	public static BlockState modifyBlockWithType(BlockState stateIn, StructureWoodVariant target) {
		// get all data from stateIn
		Block block = stateIn.getBlock();
		WoodShapes shape = getWoodShapeFromBlock(block);

		ResourceLocation blockRegName = block.getRegistryName();

		if (blockRegName == null) return stateIn;

		if (isPlanks(stateIn)) {
			shape = WoodShapes.BLOCK;
		}

		if (shape == WoodShapes.INVALID || !("minecraft".equals(blockRegName.getNamespace()) || TwilightForestMod.ID.equals(blockRegName.getNamespace()))) {
			return stateIn; // Can't deal with this.
		}

		switch (shape) {
			case BLOCK:
				return target.planks.getDefaultState();
			case STAIRS:
				return transferStateKeys(stateIn, target.stairs.getDefaultState(), StairsBlock.FACING, StairsBlock.HALF, StairsBlock.SHAPE);
			case SLAB:
				return transferStateKey(stateIn, target.slab.getDefaultState(), SlabBlock.TYPE);
			case DOUBLESLAB:
				return target.slab.getDefaultState().with(SlabBlock.TYPE, SlabType.DOUBLE);
			case FENCE:
				return transferStateKeys(stateIn, target.fence.getDefaultState(), FenceBlock.NORTH, FenceBlock.EAST, FenceBlock.WEST, FenceBlock.SOUTH);
			case GATE:
				return transferStateKeys(stateIn, target.gate.getDefaultState(), FenceGateBlock.HORIZONTAL_FACING, FenceGateBlock.OPEN, FenceGateBlock.POWERED, FenceGateBlock.IN_WALL);
			case BUTTON:
				return transferStateKeys(stateIn, target.button.getDefaultState(), HorizontalFaceBlock.FACE, AbstractButtonBlock.POWERED);
			case PLATE:
				return transferStateKey(stateIn, target.plate.getDefaultState(), PressurePlateBlock.POWERED);
			default:
				return stateIn; // Can't deal with this.
		}
	}

	public static BlockState transferStateKeys(BlockState stateIn, BlockState stateOut, Property<?>... properties) {
		for (Property<?> property : properties) {
			stateOut = transferStateKey(stateIn, stateOut, property);
		}
		return stateOut;
	}

	public static <T extends Comparable<T>> BlockState transferStateKey(BlockState stateIn, BlockState stateOut, Property<T> property) {
		return stateOut.with(property, stateIn.get(property));
	}

	public static WoodShapes getWoodShapeFromBlock(Block b) {
		if (isPlanks(b)) {
			return WoodShapes.BLOCK;
		}
		if (b instanceof StairsBlock) return WoodShapes.STAIRS;
		if (b instanceof SlabBlock) {
			if (b.getDefaultState().get(SlabBlock.TYPE) == SlabType.DOUBLE) return WoodShapes.DOUBLESLAB;
			else return WoodShapes.SLAB;
		}
		if (b instanceof AbstractButtonBlock) return WoodShapes.BUTTON;
		if (b instanceof FenceBlock) return WoodShapes.FENCE;
		if (b instanceof FenceGateBlock) return WoodShapes.GATE;
		if (b instanceof PressurePlateBlock) return WoodShapes.PLATE;

		return WoodShapes.INVALID;
	}

	public enum WoodShapes {
		BLOCK,
		STAIRS,
		SLAB,
		DOUBLESLAB,
		BUTTON,
		FENCE,
		GATE,
		PLATE,
		INVALID
	}
}

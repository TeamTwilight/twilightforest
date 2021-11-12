package twilightforest.enums;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.TwilightForestMod;
import twilightforest.block.BanisterBlock;
import twilightforest.block.TFBlocks;
import twilightforest.util.ArrayUtil;
import twilightforest.util.FeaturePlacers;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.function.Supplier;

public enum StructureWoodVariant implements StringRepresentable {
	OAK(Blocks.OAK_PLANKS, Blocks.OAK_STAIRS, Blocks.OAK_SLAB, Blocks.OAK_BUTTON, Blocks.OAK_FENCE, Blocks.OAK_FENCE_GATE, Blocks.OAK_PRESSURE_PLATE, TFBlocks.OAK_BANISTER.get()),
	SPRUCE(Blocks.SPRUCE_PLANKS, Blocks.SPRUCE_STAIRS, Blocks.SPRUCE_SLAB, Blocks.SPRUCE_BUTTON, Blocks.SPRUCE_FENCE, Blocks.SPRUCE_FENCE_GATE, Blocks.SPRUCE_PRESSURE_PLATE, TFBlocks.SPRUCE_BANISTER.get()),
	BIRCH(Blocks.BIRCH_PLANKS, Blocks.BIRCH_STAIRS, Blocks.BIRCH_SLAB, Blocks.BIRCH_BUTTON, Blocks.BIRCH_FENCE, Blocks.BIRCH_FENCE_GATE, Blocks.BIRCH_PRESSURE_PLATE, TFBlocks.BIRCH_BANISTER.get()),
	JUNGLE(Blocks.JUNGLE_PLANKS, Blocks.JUNGLE_STAIRS, Blocks.JUNGLE_SLAB, Blocks.JUNGLE_BUTTON, Blocks.JUNGLE_FENCE, Blocks.JUNGLE_FENCE_GATE, Blocks.JUNGLE_PRESSURE_PLATE, TFBlocks.JUNGLE_BANISTER.get()),
	ACACIA(Blocks.ACACIA_PLANKS, Blocks.ACACIA_STAIRS, Blocks.ACACIA_SLAB, Blocks.ACACIA_BUTTON, Blocks.ACACIA_FENCE, Blocks.ACACIA_FENCE_GATE, Blocks.ACACIA_PRESSURE_PLATE, TFBlocks.ACACIA_BANISTER.get()),
	DARK_OAK(Blocks.DARK_OAK_PLANKS, Blocks.DARK_OAK_STAIRS, Blocks.DARK_OAK_SLAB, Blocks.DARK_OAK_BUTTON, Blocks.DARK_OAK_FENCE, Blocks.DARK_OAK_FENCE_GATE, Blocks.DARK_OAK_PRESSURE_PLATE, TFBlocks.DARK_OAK_BANISTER.get()),
	CRIMSON(Blocks.CRIMSON_PLANKS, Blocks.CRIMSON_STAIRS, Blocks.CRIMSON_SLAB, Blocks.CRIMSON_BUTTON, Blocks.CRIMSON_FENCE, Blocks.CRIMSON_FENCE_GATE, Blocks.CRIMSON_PRESSURE_PLATE, TFBlocks.CRIMSON_BANISTER.get()),
	WARPED(Blocks.WARPED_PLANKS, Blocks.WARPED_STAIRS, Blocks.WARPED_SLAB, Blocks.WARPED_BUTTON, Blocks.WARPED_FENCE, Blocks.WARPED_FENCE_GATE, Blocks.WARPED_PRESSURE_PLATE, TFBlocks.WARPED_BANISTER.get()),

	TWILIGHT_OAK(TFBlocks.TWILIGHT_OAK_PLANKS, TFBlocks.TWILIGHT_OAK_STAIRS, TFBlocks.TWILIGHT_OAK_SLAB, TFBlocks.TWILIGHT_OAK_BUTTON, TFBlocks.TWILIGHT_OAK_FENCE, TFBlocks.TWILIGHT_OAK_GATE, TFBlocks.TWILIGHT_OAK_PLATE, TFBlocks.TWILIGHT_OAK_BANISTER),
	CANOPY(TFBlocks.CANOPY_PLANKS, TFBlocks.CANOPY_STAIRS, TFBlocks.CANOPY_SLAB, TFBlocks.CANOPY_BUTTON, TFBlocks.CANOPY_FENCE, TFBlocks.CANOPY_GATE, TFBlocks.CANOPY_PLATE, TFBlocks.CANOPY_BANISTER),
	MANGROVE(TFBlocks.MANGROVE_PLANKS, TFBlocks.MANGROVE_STAIRS, TFBlocks.MANGROVE_SLAB, TFBlocks.MANGROVE_BUTTON, TFBlocks.MANGROVE_FENCE, TFBlocks.MANGROVE_GATE, TFBlocks.MANGROVE_PLATE, TFBlocks.MANGROVE_BANISTER),
	DARK(TFBlocks.DARK_PLANKS, TFBlocks.DARK_STAIRS, TFBlocks.DARK_SLAB, TFBlocks.DARK_BUTTON, TFBlocks.DARK_FENCE, TFBlocks.DARK_GATE, TFBlocks.DARK_PLATE, TFBlocks.DARKWOOD_BANISTER),
	TIME(TFBlocks.TIME_PLANKS, TFBlocks.TIME_STAIRS, TFBlocks.TIME_SLAB, TFBlocks.TIME_BUTTON, TFBlocks.TIME_FENCE, TFBlocks.TIME_GATE, TFBlocks.TIME_PLATE, TFBlocks.TIME_BANISTER),
	TRANS(TFBlocks.TRANSFORMATION_PLANKS, TFBlocks.TRANSFORMATION_STAIRS, TFBlocks.TRANSFORMATION_SLAB, TFBlocks.TRANSFORMATION_BUTTON, TFBlocks.TRANSFORMATION_FENCE, TFBlocks.TRANSFORMATION_GATE, TFBlocks.TRANSFORMATION_PLATE, TFBlocks.TRANSFORMATION_BANISTER),
	MINE(TFBlocks.MINING_PLANKS, TFBlocks.MINING_STAIRS, TFBlocks.MINING_SLAB, TFBlocks.MINING_BUTTON, TFBlocks.MINING_FENCE, TFBlocks.MINING_GATE, TFBlocks.MINING_PLATE, TFBlocks.MINING_BANISTER),
	SORT(TFBlocks.SORTING_PLANKS, TFBlocks.SORTING_STAIRS, TFBlocks.SORTING_SLAB, TFBlocks.SORTING_BUTTON, TFBlocks.SORTING_FENCE, TFBlocks.SORTING_GATE, TFBlocks.SORTING_PLATE, TFBlocks.SORTING_BANISTER);

	private final List<Block> blocks;
	private static final StructureWoodVariant[] COMMON = new StructureWoodVariant[] { SPRUCE, CANOPY };
	private static final StructureWoodVariant[] UNCOMMON = new StructureWoodVariant[] { OAK, DARK_OAK, TWILIGHT_OAK };
	private static final StructureWoodVariant[] RARE = new StructureWoodVariant[] { BIRCH, JUNGLE, MANGROVE };
	private static final StructureWoodVariant[] TREASURE = new StructureWoodVariant[] { TIME, TRANS, MINE, SORT };

	StructureWoodVariant(Supplier<Block> planks, Supplier<StairBlock> stairs, Supplier<Block> slab, Supplier<Block> button, Supplier<Block> fence, Supplier<Block> gate, Supplier<Block> plate, Supplier<BanisterBlock> banister) {
		this(planks.get(), stairs.get(), slab.get(), button.get(), fence.get(), gate.get(), plate.get(), banister.get());
	}

	StructureWoodVariant(Block planks, Block stairs, Block slab, Block button, Block fence, Block gate, Block plate, BanisterBlock banister) {
		this.planks = planks;
		this.stairs = stairs;
		this.slab = slab;
		this.button = button;
		this.fence = fence;
		this.gate = gate;
		this.plate = plate;
		this.banister = banister;
		this.blocks = Arrays.asList(this.planks, this.stairs, this.slab, this.button, this.fence, this.gate, this.plate, this.banister);
	}

	// Since this is worldgen code, this solution would take the least amount of hits on RNG
	public static StructureWoodVariant getRandomWeighted(Random random) {
		int randomVal = random.nextInt() & Integer.MAX_VALUE;

		if ((randomVal & 0b1) == 0) {
			return ArrayUtil.wrapped(COMMON, randomVal >> 1);
		}

		if ((randomVal & 0b10) == 0) {
			return ArrayUtil.wrapped(UNCOMMON, randomVal >> 2);
		}

		if ((randomVal & 0b1100) != 0) {
			return ArrayUtil.wrapped(RARE, randomVal >> 4);
		}

		return ArrayUtil.wrapped(TREASURE, randomVal >> 4);
	}

	@Override
	public String getSerializedName() {
		return this.name().toLowerCase(Locale.ROOT);
	}

	private final Block planks;
	private final Block stairs;
	private final Block slab;
	private final Block button;
	private final Block fence;
	private final Block gate;
	private final Block plate;
	private final BanisterBlock banister;

	@Nullable
	public static StructureWoodVariant getVariantFromBlock(Block block) {
		for (StructureWoodVariant var : values()) {
			if (var.blocks.contains(block)) {
				return var;
			}
		}

		// None of these wood types
		return null;
	}

	public static boolean isPlanks(Block block) {
		StructureWoodVariant var = getVariantFromBlock(block);
		if (var == null) return false;

		return var.planks == block;
	}

	public static BlockState modifyBlockWithType(BlockState stateIn, StructureWoodVariant target) {
		// get all data from stateIn
		Block block = stateIn.getBlock();
		WoodShapes shape = getWoodShapeFromBlock(block);

		ResourceLocation blockRegName = block.getRegistryName();

		if (blockRegName == null) return stateIn;

		if (shape == WoodShapes.INVALID || !("minecraft".equals(blockRegName.getNamespace()) || TwilightForestMod.ID.equals(blockRegName.getNamespace()))) {
			return stateIn; // Block not recognized
		}

		return switch (shape) {
			case BLOCK -> target.planks.defaultBlockState();
			case STAIRS -> FeaturePlacers.transferAllStateKeys(stateIn, target.stairs);
			case SLAB -> FeaturePlacers.transferAllStateKeys(stateIn, target.slab);
			case FENCE -> FeaturePlacers.transferAllStateKeys(stateIn, target.fence);
			case GATE -> FeaturePlacers.transferAllStateKeys(stateIn, target.gate);
			case BUTTON -> FeaturePlacers.transferAllStateKeys(stateIn, target.button);
			case PLATE -> FeaturePlacers.transferAllStateKeys(stateIn, target.plate);
			case BANISTER -> FeaturePlacers.transferAllStateKeys(stateIn, target.banister);
			default -> stateIn; // Block not recognized
		};
	}

	public static WoodShapes getWoodShapeFromBlock(Block b) {
		if (isPlanks(b)) return WoodShapes.BLOCK;
		if (b instanceof StairBlock) return WoodShapes.STAIRS;
		if (b instanceof SlabBlock) return WoodShapes.SLAB;
		if (b instanceof ButtonBlock) return WoodShapes.BUTTON;
		if (b instanceof FenceBlock) return WoodShapes.FENCE;
		if (b instanceof FenceGateBlock) return WoodShapes.GATE;
		if (b instanceof PressurePlateBlock) return WoodShapes.PLATE;
		if (b instanceof BanisterBlock) return WoodShapes.BANISTER;

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

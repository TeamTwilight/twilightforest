package twilightforest.world.components.structures;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.properties.StructureMode;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import twilightforest.util.ArrayUtil;
import twilightforest.util.BoundingBoxUtils;

import java.util.Comparator;
import java.util.List;

public abstract class TwilightTemplateStructurePiece extends TemplateStructurePiece {
	protected final StructureTemplateManager structureManager;
	private final BlockPos originalPlacement;
	private final BoundingBox originalBox;

	protected int placeFlag;

	@SuppressWarnings("this-escape")
	public TwilightTemplateStructurePiece(StructurePieceType structurePieceType, CompoundTag compoundTag, StructurePieceSerializationContext ctx, StructurePlaceSettings placeSettings) {
		super(structurePieceType, compoundTag, ctx.structureTemplateManager(), rl -> placeSettings);
		this.rotation = this.getRotation();
		this.mirror = this.getMirror();

		this.structureManager = ctx.structureTemplateManager();

		this.originalPlacement = this.templatePosition;
		this.originalBox = BoundingBoxUtils.clone(this.boundingBox);

		// Fixes Ladders
		this.placeSettings.setKnownShape(true);
		this.placeFlag = Block.UPDATE_CLIENTS;
	}

	@SuppressWarnings("this-escape")
	public TwilightTemplateStructurePiece(StructurePieceType type, int genDepth, StructureTemplateManager structureManager, ResourceLocation templateLocation, StructurePlaceSettings placeSettings, BlockPos startPosition) {
		super(type, genDepth, structureManager, templateLocation, templateLocation.toString(), placeSettings, startPosition);
		this.rotation = this.getRotation();
		this.mirror = this.getMirror();

		this.structureManager = structureManager;

		this.originalPlacement = this.templatePosition;
		this.originalBox = BoundingBoxUtils.clone(this.boundingBox);

		// Fixes Ladders
		this.placeSettings.setKnownShape(true);
		this.placeFlag = Block.UPDATE_CLIENTS;
	}

	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext ctx, CompoundTag structureTag) {
		super.addAdditionalSaveData(ctx, structureTag);

		structureTag.putInt("rotation", this.placeSettings.getRotation().ordinal());
		structureTag.putInt("mirror", this.placeSettings.getMirror().ordinal());
		BlockPos pivot = this.placeSettings.getRotationPivot();
		structureTag.putInt("pivot_x", pivot.getX());
		structureTag.putInt("pivot_y", pivot.getY());
		structureTag.putInt("pivot_z", pivot.getZ());
	}

	// This will be required if you want to dig a piece into a noise beard
	protected void placePieceAdjusted(WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource random, BoundingBox boundingBox, BlockPos pos, int dY) {
		this.templatePosition = this.templatePosition.above(dY);

		// Call this class's overridden method instead of the supermethod to ensure execution of our custom handleDataMarker() method
		this.customPostProcess(level, chunkGenerator, random, boundingBox, pos.above(dY));

		this.templatePosition = this.originalPlacement;
		this.boundingBox = BoundingBoxUtils.clone(this.originalBox);

		this.placeSettings.setBoundingBox(this.boundingBox);
	}

	public static StructurePlaceSettings readSettings(CompoundTag compoundTag) {
		return new StructurePlaceSettings()
			.setRotation(ArrayUtil.wrapped(Rotation.values(), compoundTag.getInt("rotation")))
			.setMirror(ArrayUtil.wrapped(Mirror.values(), compoundTag.getInt("mirror")))
			.setRotationPivot(new BlockPos(compoundTag.getInt("pivot_x"), compoundTag.getInt("pivot_y"), compoundTag.getInt("pivot_z")))
			.addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
	}

	public static StructurePlaceSettings makeSettings(Rotation rotation) {
		return new StructurePlaceSettings().setRotation(rotation).addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
	}

	public static StructurePlaceSettings makeSettings(Rotation rotation, Mirror mirror) {
		return new StructurePlaceSettings().setRotation(rotation).setMirror(mirror).addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
	}

	public static StructurePlaceSettings randomRotation(RandomSource random) {
		return makeSettings(Rotation.getRandom(random));
	}

	// VANILLACOPY: Same as the supercall except without the dumb jigsaw code
	@Override
	public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator chunkGen, RandomSource random, BoundingBox chunkBounds, ChunkPos chunkPos, BlockPos structureBottomCenter) {
		this.customPostProcess(level, chunkGen, random, chunkBounds, structureBottomCenter);
	}

	private void customPostProcess(WorldGenLevel level, ChunkGenerator chunkGen, RandomSource random, BoundingBox chunkBounds, BlockPos structureBottomCenter) {
		this.placeSettings.setBoundingBox(chunkBounds);
		this.boundingBox = this.template.getBoundingBox(this.placeSettings, this.templatePosition);
		if (this.template.placeInWorld(level, this.templatePosition, structureBottomCenter, this.placeSettings, random, this.placeFlag)) {
			for (StructureTemplate.StructureBlockInfo structuretemplate$structureblockinfo : this.template
				.filterBlocks(this.templatePosition, this.placeSettings, Blocks.STRUCTURE_BLOCK)) {
				if (structuretemplate$structureblockinfo.nbt() != null) {
					StructureMode structuremode = StructureMode.valueOf(structuretemplate$structureblockinfo.nbt().getString("mode"));
					if (structuremode == StructureMode.DATA) {
						this.handleDataMarker(
							structuretemplate$structureblockinfo.nbt().getString("metadata"),
							structuretemplate$structureblockinfo.pos(),
							level,
							random,
							chunkBounds,
							chunkGen // Additional param, new method callable
						);
					}
				}
			}
		}
	}

	// Enhanced version of handleDataMarker() method that up-levels ServerLevelAccessor into WorldGenLevel while also adding ChunkGenerator parameter
	protected void handleDataMarker(String label, BlockPos pos, WorldGenLevel level, RandomSource random, BoundingBox chunkBounds, ChunkGenerator chunkGen) {
	}

	@Deprecated
	@Override
	protected final void handleDataMarker(String label, BlockPos pos, ServerLevelAccessor level, RandomSource random, BoundingBox chunkBounds) {
		// Deprecated - use above method as that is called instead
	}

	public String getTemplateName() {
		return this.templateName;
	}

	// Worse case scenario if the terrain still isn't being risen for the Lich Tower,
	// then we'll need to do via this. I still have other solutions I'd like to explore first
	//@Override
	//public BoundingBox getBoundingBox() {
	//    return this.boundingBox = StructureBoundingBoxUtils.clone(this.originalBox);
	//}
}

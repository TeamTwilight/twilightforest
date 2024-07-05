package twilightforest.world.components.structures;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import twilightforest.util.ArrayUtil;
import twilightforest.util.BoundingBoxUtils;

public abstract class TwilightTemplateStructurePiece extends TemplateStructurePiece {
	protected final StructureTemplateManager structureManager;
	private final BlockPos originalPlacement;
	private final BoundingBox originalBox;

	@SuppressWarnings("this-escape")
	public TwilightTemplateStructurePiece(StructurePieceType structurePieceType, CompoundTag compoundTag, StructurePieceSerializationContext ctx, StructurePlaceSettings rl2SettingsFunction) {
		super(structurePieceType, compoundTag, ctx.structureTemplateManager(), rl -> rl2SettingsFunction);
		this.rotation = this.getRotation();
		this.mirror = this.getMirror();

		this.structureManager = ctx.structureTemplateManager();

		this.originalPlacement = this.templatePosition;
		this.originalBox = BoundingBoxUtils.clone(this.boundingBox);
	}

	@SuppressWarnings("this-escape")
	public TwilightTemplateStructurePiece(StructurePieceType type, int genDepth, StructureTemplateManager structureManager, ResourceLocation templateLocation, StructurePlaceSettings placeSettings, BlockPos startPosition) {
		super(type, genDepth, structureManager, templateLocation, templateLocation.toString(), placeSettings, startPosition);
		this.rotation = this.getRotation();
		this.mirror = this.getMirror();

		this.structureManager = structureManager;

		this.originalPlacement = this.templatePosition;
		this.originalBox = BoundingBoxUtils.clone(this.boundingBox);
	}

	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext ctx, CompoundTag structureTag) {
		super.addAdditionalSaveData(ctx, structureTag);

		structureTag.putInt("rotation", this.placeSettings.getRotation().ordinal());
		structureTag.putInt("mirror", this.placeSettings.getMirror().ordinal());
	}

	// This will be required if you want to dig a piece into a noise beard
	protected void placePieceAdjusted(WorldGenLevel level, StructureManager structureFeatureManager, ChunkGenerator chunkGenerator, RandomSource random, BoundingBox boundingBox, ChunkPos chunkPos, BlockPos pos, int dY) {
		this.templatePosition = this.templatePosition.above(dY);

		super.postProcess(level, structureFeatureManager, chunkGenerator, random, boundingBox, chunkPos, pos.above(dY));

		this.templatePosition = this.originalPlacement;
		this.boundingBox = BoundingBoxUtils.clone(this.originalBox);

		this.placeSettings.setBoundingBox(this.boundingBox);
	}

	public static StructurePlaceSettings readSettings(CompoundTag compoundTag) {
		return new StructurePlaceSettings()
			.setRotation(ArrayUtil.wrapped(Rotation.values(), compoundTag.getInt("rotation")))
			.setMirror(ArrayUtil.wrapped(Mirror.values(), compoundTag.getInt("mirror")))
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


	// Worse case scenario if the terrain still isn't being risen for the Lich Tower,
	// then we'll need to do via this. I still have other solutions I'd like to explore first
	//@Override
	//public BoundingBox getBoundingBox() {
	//    return this.boundingBox = StructureBoundingBoxUtils.clone(this.originalBox);
	//}
}

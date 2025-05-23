package twilightforest.world.components.structures.hollowtree;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.RegistryOps;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.util.features.FeatureLogic;

public class HollowTreeMedBranch extends HollowTreePiece {
	protected final BlockPos src, dest;  // source and destination of branch, array of 3 ints representing x, y, z
	protected final double length;
	protected final double angle;
	protected final double tilt;
	protected final boolean leafy;

	protected final BlockStateProvider wood;
	protected final BlockStateProvider leaves;

	protected HollowTreeMedBranch(int i, BlockPos src, double length, double angle, double tilt, boolean leafy, BlockStateProvider wood, BlockStateProvider leaves) {
		this(i, src, FeatureLogic.translate(src, length, angle, tilt), length, angle, tilt, leafy, wood, leaves);
	}

	protected HollowTreeMedBranch(int i, BlockPos src, BlockPos dest, double length, double angle, double tilt, boolean leafy, BlockStateProvider wood, BlockStateProvider leaves) {
		this(i, src, dest, branchBoundingBox(src, dest, 3 + Mth.ceil(length)), length, angle, tilt, leafy, wood, leaves);
	}

	protected HollowTreeMedBranch(StructurePieceType type, int i, BlockPos src, BlockPos dest, double length, double angle, double tilt, boolean leafy, BlockStateProvider wood, BlockStateProvider leaves) {
		this(type, i, src, dest, branchBoundingBox(src, dest, 3 + Mth.ceil(length)), length, angle, tilt, leafy, wood, leaves);
	}

	protected HollowTreeMedBranch(int i, BlockPos src, BlockPos dest, BoundingBox boundingBox, double length, double angle, double tilt, boolean leafy, BlockStateProvider wood, BlockStateProvider leaves) {
		this(TFStructurePieceTypes.TFHTMB.value(), i, src, dest, boundingBox, length, angle, tilt, leafy, wood, leaves);
	}

	protected HollowTreeMedBranch(StructurePieceType type, int i, BlockPos src, BlockPos dest, BoundingBox boundingBox, double length, double angle, double tilt, boolean leafy, BlockStateProvider wood, BlockStateProvider leaves) {
		super(type, i, boundingBox);

		this.setOrientation(Direction.SOUTH);

		this.src = src.immutable();
		this.dest = dest.immutable();

		this.boundingBox = boundingBox;

		this.length = length;
		this.angle = angle;
		this.tilt = tilt;
		this.leafy = leafy;

		this.wood = wood;
		this.leaves = leaves;
	}

	public HollowTreeMedBranch(StructurePieceSerializationContext context, CompoundTag tag) {
		this(TFStructurePieceTypes.TFHTMB.value(), context, tag);
	}

	protected HollowTreeMedBranch(StructurePieceType type, StructurePieceSerializationContext context, CompoundTag tag) {
		super(type, tag);

		this.src = new BlockPos(tag.getInt("srcPosX"), tag.getInt("srcPosY"), tag.getInt("srcPosZ"));
		this.dest = new BlockPos(tag.getInt("destPosX"), tag.getInt("destPosY"), tag.getInt("destPosZ"));

		this.length = tag.getDouble("branchLength");
		this.angle = tag.getDouble("branchAngle");
		this.tilt = tag.getDouble("branchTilt");
		this.leafy = tag.getBoolean("branchLeafy");

		RegistryOps<Tag> ops = RegistryOps.create(NbtOps.INSTANCE, context.registryAccess());
		this.wood = BlockStateProvider.CODEC.parse(ops, tag.getCompound("wood")).result().orElse(HollowTreePiece.DEFAULT_WOOD);
		this.leaves = BlockStateProvider.CODEC.parse(ops, tag.getCompound("leaves")).result().orElse(HollowTreePiece.DEFAULT_LEAVES);
	}

	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
		tag.putInt("srcPosX", this.src.getX());
		tag.putInt("srcPosY", this.src.getY());
		tag.putInt("srcPosZ", this.src.getZ());

		tag.putInt("destPosX", this.dest.getX());
		tag.putInt("destPosY", this.dest.getY());
		tag.putInt("destPosZ", this.dest.getZ());

		tag.putDouble("branchLength", this.length);
		tag.putDouble("branchAngle", this.angle);
		tag.putDouble("branchTilt", this.tilt);
		tag.putBoolean("branchLeafy", this.leafy);

		tag.put("wood", BlockStateProvider.CODEC.encodeStart(NbtOps.INSTANCE, this.wood).resultOrPartial(TwilightForestMod.LOGGER::error).orElseGet(CompoundTag::new));
		tag.put("leaves", BlockStateProvider.CODEC.encodeStart(NbtOps.INSTANCE, this.leaves).resultOrPartial(TwilightForestMod.LOGGER::error).orElseGet(CompoundTag::new));
	}

	@Override
	public void postProcess(WorldGenLevel level, StructureManager manager, ChunkGenerator generator, RandomSource doNotUse, BoundingBox writeableBounds, ChunkPos chunkPos, BlockPos structureBottomCenter) {
		RandomSource decoRNG = this.getInterChunkDecoRNG(level);

		this.drawBresehnam(level, writeableBounds, this.src, this.dest, this.wood, decoRNG);
		this.drawBresehnam(level, writeableBounds, this.src.above(), this.dest, this.wood, decoRNG);

		// and several small branches
		int numShoots = Math.min(decoRNG.nextInt(3) + 1, (int) (this.length / 5));
		double angleInc, angleVar, outVar;

		angleInc = 0.8 / numShoots;

		for (int i = 0; i < numShoots; i++) {
			angleVar = (angleInc * i) - 0.4;
			outVar = (decoRNG.nextDouble() * 0.8) + 0.2;

			BlockPos bSrc = FeatureLogic.translate(this.src, this.length * outVar, this.angle, this.tilt);

			this.drawSmallBranch(level, writeableBounds, bSrc, Math.max(this.length * 0.3F, 2F), this.angle + angleVar, this.tilt, decoRNG, this.wood, this.leaves);
		}

		// with leaves!
		if (this.leafy) {
			int numLeafBalls = Math.min(decoRNG.nextInt(3) + 1, (int) (this.length / 5));

			for (int i = 0; i < numLeafBalls; i++) {

				double slength = (decoRNG.nextFloat() * 0.6F + 0.2F) * this.length;
				BlockPos bdst = FeatureLogic.translate(new BlockPos(this.src.getX() - this.boundingBox.minX(), this.src.getY() - this.boundingBox.minY(), this.src.getZ() - this.boundingBox.minZ()), slength, this.angle, this.tilt);

				int radius = decoRNG.nextBoolean() ? 2 : 3;
				this.drawBlockBlob(level, writeableBounds, bdst.getX(), bdst.getY(), bdst.getZ(), radius, decoRNG, this.leaves, false, false, true);
			}

			this.drawBlockBlob(level, writeableBounds, this.dest.getX() - this.boundingBox.minX(), this.dest.getY() - this.boundingBox.minY(), this.dest.getZ() - this.boundingBox.minZ(), 3, decoRNG, this.leaves, false, false, true);
		}
	}

	/**
	 * This is like the small branch component, but we're just drawing it directly into the world
	 */
	protected void drawSmallBranch(WorldGenLevel world, BoundingBox sbb, BlockPos sourcePos, double branchLength, double branchAngle, double branchTilt, RandomSource random, BlockStateProvider woodProvider, BlockStateProvider leafProvider) {
		// draw a line
		BlockPos branchDest = FeatureLogic.translate(sourcePos, branchLength, branchAngle, branchTilt);

		this.drawBresehnam(world, sbb, sourcePos, branchDest, woodProvider, random);

		// leaf blob at the end
		this.drawBlockBlob(world, sbb, branchDest.getX() - this.boundingBox.minX(), branchDest.getY() - this.boundingBox.minY(), branchDest.getZ() - this.boundingBox.minZ(), 2, random, leafProvider, false, false, true);
	}

	protected static BoundingBox branchBoundingBox(BlockPos src, BlockPos dest, int extraPadding) {
		return BoundingBox.fromCorners(src, dest).inflatedBy(extraPadding);
	}
}

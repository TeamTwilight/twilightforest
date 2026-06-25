package twilightforest.world.components.structures.fallentrunk;

import net.minecraft.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import org.jetbrains.annotations.NotNull;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.Beardifier;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import twilightforest.world.components.chunkgenerators.TanhHillFunction;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class TrunkUnderDensityFunction extends Beardifier {
	private final boolean isBigTree;
	private final boolean isXOriented;
	private final RandomSource random;  // used to create dirt mounds
	private final BoundingBox boundingBox;
	private final List<TanhHillFunction> tanhHillFunctions;
	private final Rigid fallenTrunkPiece;
	protected final BoundingBox moundBase;
	protected static final float MOUND_RADIUS = 3F;
	protected static final int MAX_RANDOM_RADIUS_INCREASE_BIG_TREE = 4;
	protected static final int MAX_RANDOM_RADIUS_INCREASE_NON_BIG_TREE = 2;
	protected static final float BIG_TREE_MOUND_HEIGHT = 3F;
	protected static final float NON_BIG_TREE_MOUND_HEIGHT = 1.5F;

	public TrunkUnderDensityFunction(List<Rigid> pieces, FallenTrunkPiece piece, boolean isBigTree, int minMounds, int maxMounds) {
		super(pieces, List.of(), piece.getBoundingBox());
		this.isBigTree = isBigTree;
		this.fallenTrunkPiece = pieces.getFirst();
		boundingBox = getFallenTrunkPiece().box();
		random = RandomSource.create(boundingBox.minX() * 14413411L + boundingBox.minZ() * 43387781L);
		isXOriented = boundingBox.maxX() - boundingBox.minX() > boundingBox.maxZ() - boundingBox.minZ();
		int length = isXOriented ? boundingBox.getXSpan() : boundingBox.getZSpan();
		Vec3i moundBaseCorner = new Vec3i(
			isXOriented ?  length / 2 : 0,
			1,
			!isXOriented ? length / 2 : 0);
		this.moundBase = BoundingBox.fromCorners(moundBaseCorner, moundBaseCorner);
		this.tanhHillFunctions = new ArrayList<>();
		int attemptedMoundsNumber = random.nextInt(minMounds, maxMounds + 1);
		for(int i = 0; i < attemptedMoundsNumber; i++) {
			Optional<TanhHillFunction> function = getTanhHillFunctionWithoutCoveringHole(piece, i % 2 == 0);
			function.ifPresent(tanhHillFunctions::add);
		}
	}

	@Override
	public double compute(FunctionContext context) {
		int x = context.blockX();
		int y = context.blockY();
		int z = context.blockZ();

		int groundLevelDelta = getFallenTrunkPiece().groundLevelDelta();
		int horizontalDistanceX = Math.max(0, Math.max(boundingBox.minX() - x, x - boundingBox.maxX()));
		int horizontalDistanceZ = Math.max(0, Math.max(boundingBox.minZ() - z, z - boundingBox.maxZ()));
		int adjustedGroundLevel = boundingBox.minY() + groundLevelDelta + (isBigTree ? 1 : 0);
		int verticalDistance = y - adjustedGroundLevel;

		double moundContribution = computeMoundsContribution(context);
		if (moundContribution > 0)
			return moundContribution;
		return flatSurroundingTerrain(horizontalDistanceX, verticalDistance, horizontalDistanceZ);
	}

	public static double flatSurroundingTerrain(double x, double y, double z) {
		if (y > 20 || y < -10)
			return 0;
		if (x == 0 && z == 0 && y > 0)
			return -1;  // flat everything inside the trunk
		final double a = FallenTrunkPiece.TERRAFORM_PIECE_SIZE, c = FallenTrunkPiece.TERRAFORM_PIECE_SIZE;
		final double verticalScale = 0.7;
		double normX = Math.abs(x / a);
		double normZ = Math.abs(z / c);
		double normalizedSquaredDistance = normX * normX + normZ * normZ;

		if (normalizedSquaredDistance >= 1.0)
			return 0.0;

		double normalizedDistance = Math.sqrt(normalizedSquaredDistance);

		double factor = 1.0 - normalizedDistance;
		if (y < 0)  // make less steep to avoid vertical walls around the trunk
			y = normalizedDistance * 0.5 + y / 5;  // add normalizedDistance to avoid axis aligned "stairs"
		// multiply by exponent to make the beardifier weaker with distance
		return -y * factor / verticalScale * (Math.exp(-normalizedDistance / (Math.max(1 - normalizedDistance, 1e-8))));
	}

	protected double computeMoundsContribution(FunctionContext context) {
		int x = context.blockX() - boundingBox.minX();
		int y = context.blockY() - boundingBox.minY();
		int z = context.blockZ() - boundingBox.minZ();
		int radius = getRadius(boundingBox);
		double ax = Math.abs((isXOriented ? z : x) - radius + 1);
		double az = Math.abs(y - radius + 1);
		if (radius == 2D) {  // This case is generated differently
			if (Math.abs((isXOriented ? z : x) - 1.5) + Math.abs(y - 1.5) <= 2)
				return -1;
		} else {
			if ((int) (Math.max(ax, az) + (Math.min(ax, az) * 0.5)) < radius)
				return -1;
		}

		double max = -1D;
		for (TanhHillFunction h : tanhHillFunctions) {
			double value = h.compute(context);
			if (value > max)
				max = value;
		}
		return max;
	}

	protected Optional<TanhHillFunction> getTanhHillFunctionWithoutCoveringHole(FallenTrunkPiece piece, boolean isOnRightSide) {
		if (isBigTree)
			return Optional.ofNullable(getTanhHillFunction((BlockPos) Util.getRandom(piece.getAllPotentialBaseMoundBlockPos(isOnRightSide).toArray(), random), isOnRightSide));  // Big trees don't have holes

		Set<BlockPos> blockPosSet = piece.getAllowedBaseMoundBlockPos(isOnRightSide);
		if (blockPosSet.isEmpty())
			return Optional.empty();
		BlockPos moundBLockPos = (BlockPos) Util.getRandom(blockPosSet.toArray(), random);
		return Optional.ofNullable(getTanhHillFunction(moundBLockPos, isOnRightSide));
	}

	protected TanhHillFunction getTanhHillFunction(BlockPos baseBlockPos, boolean isOnRightSide) {
		int maxRandomRadiusIncrease = isBigTree ? MAX_RANDOM_RADIUS_INCREASE_BIG_TREE : MAX_RANDOM_RADIUS_INCREASE_NON_BIG_TREE;
		float moundRadius = MOUND_RADIUS + random.nextInt(0, maxRandomRadiusIncrease);
		float moundHeight = isBigTree ? BIG_TREE_MOUND_HEIGHT : NON_BIG_TREE_MOUND_HEIGHT;

		float moundBiasAngle = Mth.TWO_PI * random.nextFloat();
		return new TanhHillFunction(baseBlockPos.getX(), baseBlockPos.getY(), baseBlockPos.getZ(), moundRadius, moundHeight, moundBiasAngle, isXOriented, isOnRightSide);
	}


	protected Beardifier.Rigid getFallenTrunkPiece() {
		return this.fallenTrunkPiece;
	}

	private static int getRadius(BoundingBox box) {
		return getRadius(box.getYSpan());
	}

	private static int getRadius(int diameter) {
		return (int) Math.ceil(diameter / 2D);
	}
}
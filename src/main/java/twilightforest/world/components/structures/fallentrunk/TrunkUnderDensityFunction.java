package twilightforest.world.components.structures.fallentrunk;

import it.unimi.dsi.fastutil.objects.ObjectIterators;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import net.minecraft.core.Vec3i;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.Beardifier;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pools.JigsawJunction;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.chunkgenerators.HollowHillFunction;

import java.util.Arrays;

public class TrunkUnderDensityFunction extends Beardifier {
	private final boolean isBigTree;
	private final boolean isXOriented;
	private final RandomSource random;  // used to create dirt mounds
	private final BoundingBox boundingBox;
	private final HollowHillFunction[] hollowHillFunctions;
	protected final BoundingBox moundApex;
	protected static final int moundRadius = 4;

	public TrunkUnderDensityFunction(ObjectListIterator<Rigid> pieceIterator, FallenTrunkPiece piece, boolean isBigTree, int minMounds, int maxMounds) {
		super(pieceIterator, (ObjectListIterator<JigsawJunction>) ObjectIterators.<JigsawJunction>emptyIterator());
		this.isBigTree = isBigTree;
		boundingBox = getFallenTrunkPiece().box();
		random = RandomSource.create(boundingBox.minX() * 14413411L + boundingBox.minZ() * 43387781L);
		isXOriented = boundingBox.maxX() - boundingBox.minX() > boundingBox.maxZ() - boundingBox.minZ();
		int length = isXOriented ? boundingBox.getXSpan() : boundingBox.getZSpan();
		Vec3i moundApexCorner = new Vec3i(
			isXOriented ?  length / 2 : 0,
			1,
			!isXOriented ? length / 2 : 0);
		this.moundApex = BoundingBox.fromCorners(moundApexCorner, moundApexCorner);
		this.hollowHillFunctions = new HollowHillFunction[random.nextInt(minMounds, maxMounds + 1)];
		for(int i = 0; i < hollowHillFunctions.length; i++) {
			hollowHillFunctions[i] = getHollowHillFunctionWithoutCoveringHole(piece);
		}
	}

	@Override
	public double compute(FunctionContext context) {  // modified copy of vanilla thin_beardifier
		int x = context.blockX();
		int y = context.blockY();
		int z = context.blockZ();

		int groundLevelDelta = getFallenTrunkPiece().groundLevelDelta();
		int horizontalDistanceX = Math.max(0, Math.max(boundingBox.minX() - x, x - boundingBox.maxX()));
		int horizontalDistanceZ = Math.max(0, Math.max(boundingBox.minZ() - z, z - boundingBox.maxZ()));
		int adjustedGroundLevel = boundingBox.minY() + groundLevelDelta + (isBigTree ? 2 : 1);
		int verticalDistance = y - adjustedGroundLevel;

		return Math.max(getBeardContribution(horizontalDistanceX, verticalDistance, horizontalDistanceZ, verticalDistance) * 5, computeMoundsContribution(context));
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
				return 0;
		} else {
			if ((int) (Math.max(ax, az) + (Math.min(ax, az) * 0.5)) < radius)
				return 0;
		}

		return Arrays.stream(hollowHillFunctions)
			.map(hollowHillFunction -> hollowHillFunction.compute(context))
			.max(Double::compareTo)
			.orElse(Double.NEGATIVE_INFINITY);
	}

	protected HollowHillFunction getHollowHillFunctionWithoutCoveringHole(FallenTrunkPiece piece) {
		if (isBigTree)
			return getHollowHillFunction();  // Big trees don't have holes

		HollowHillFunction hollowHillFunction = getHollowHillFunction();
		for (int tries = 0; tries < 100 && piece.isHoleCoveredByHill(hollowHillFunction); tries++) {
			hollowHillFunction = getHollowHillFunction();
		}
		if (piece.isHoleCoveredByHill(hollowHillFunction))
			TwilightForestMod.LOGGER.error("Too many tries during generation of mounds in Fallen Trunk! Please report to https://github.com/TeamTwilight/twilightforest/issues the with seed and {}", piece.getBoundingBox().getCenter().toString());

		return hollowHillFunction;
	}

	protected HollowHillFunction getHollowHillFunction() {
		int length = isXOriented ? boundingBox.getXSpan() : boundingBox.getZSpan();
		int coordinateOffset = random.nextInt(-length / 4, length / 4);
		BoundingBox absouluteMoundApex = moundApex.moved(boundingBox.minX(), boundingBox.minY(), boundingBox.minZ());
		int radius = getRadius(boundingBox);
		float hollowHillX = absouluteMoundApex.getCenter().getX() + (isXOriented ? coordinateOffset : random.nextBoolean() ? boundingBox.getXSpan() - 1 : 0);
		float hollowHillY = absouluteMoundApex.getCenter().getY() + radius / 3f + random.nextInt(-1, 3);
		float hollowHillZ = absouluteMoundApex.getCenter().getZ() + (!isXOriented ? coordinateOffset : random.nextBoolean() ? boundingBox.getZSpan() - 1 : 0);
		float hollowHillRadius = moundRadius + random.nextInt(0, 3);
		return new HollowHillFunction(
			hollowHillX,
			hollowHillY,
			hollowHillZ,
			hollowHillRadius, 1);
	}

	protected Beardifier.Rigid getFallenTrunkPiece() {
		Beardifier.Rigid piece = pieceIterator.next();
		this.pieceIterator.back(Integer.MAX_VALUE);
		return piece;
	}

	private static int getRadius(BoundingBox box) {
		return getRadius(box.getYSpan());
	}

	private static int getRadius(int diameter) {
		return (int) Math.ceil(diameter / 2D);
	}
}
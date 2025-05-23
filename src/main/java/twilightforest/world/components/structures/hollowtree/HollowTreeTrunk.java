package twilightforest.world.components.structures.hollowtree;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.storage.loot.LootTable;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.util.features.FeatureLogic;

public class HollowTreeTrunk extends HollowTreePiece {
	private final int height;
	private final int radius;

	private final BlockStateProvider log;
	private final BlockStateProvider wood;
	private final BlockStateProvider root;
	private final BlockStateProvider leaves;
	private final BlockStateProvider vine;
	private final BlockStateProvider bug;

	private final BlockStateProvider dungeonWood;
	private final BlockStateProvider dungeonAir;
	private final BlockStateProvider dungeonLootBlock;
	private final ResourceKey<LootTable> dungeonLootTable;
	private final Holder<EntityType<?>> dungeonMonster;

	public HollowTreeTrunk(int height, int radius, BoundingBox pBoundingBox, BlockStateProvider log1, BlockStateProvider wood, BlockStateProvider root, BlockStateProvider leaves, BlockStateProvider vine, BlockStateProvider bug, BlockStateProvider dungeonWood, BlockStateProvider dungeonAir, BlockStateProvider dungeonLootBlock, ResourceKey<LootTable> dungeonLootTable, Holder<EntityType<?>> dungeonMonster) {
		super(TFStructurePieceTypes.TFHTTr.value(), 0, pBoundingBox);

		this.setOrientation(Direction.SOUTH);

		this.height = height;
		this.radius = radius;

		this.log = log1;
		this.wood = wood;
		this.root = root;
		this.leaves = leaves;
		this.vine = vine;
		this.bug = bug;

		this.dungeonWood = dungeonWood;
		this.dungeonAir = dungeonAir;
		this.dungeonLootBlock = dungeonLootBlock;
		this.dungeonLootTable = dungeonLootTable;
		this.dungeonMonster = dungeonMonster;
	}

	/**
	 * Load from NBT
	 */
	public HollowTreeTrunk(StructurePieceSerializationContext context, CompoundTag tag) {
		super(TFStructurePieceTypes.TFHTTr.value(), tag);

		this.height = tag.getInt("trunkHeight");
		this.radius = tag.getInt("trunkRadius");

		RegistryOps<Tag> ops = RegistryOps.create(NbtOps.INSTANCE, context.registryAccess());

		this.log = BlockStateProvider.CODEC.parse(ops, tag.getCompound("log")).result().orElse(HollowTreePiece.DEFAULT_LOG);
		this.wood = BlockStateProvider.CODEC.parse(ops, tag.getCompound("wood")).result().orElse(HollowTreePiece.DEFAULT_WOOD);
		this.root = BlockStateProvider.CODEC.parse(ops, tag.getCompound("root")).result().orElse(HollowTreePiece.DEFAULT_ROOT);
		this.leaves = BlockStateProvider.CODEC.parse(ops, tag.getCompound("leaves")).result().orElse(HollowTreePiece.DEFAULT_LEAVES);
		this.vine = BlockStateProvider.CODEC.parse(ops, tag.getCompound("vine")).result().orElse(HollowTreePiece.DEFAULT_VINE);
		this.bug = BlockStateProvider.CODEC.parse(ops, tag.getCompound("bug")).result().orElse(HollowTreePiece.DEFAULT_BUG);
		this.dungeonWood = BlockStateProvider.CODEC.parse(ops, tag.getCompound("dungeon_wood")).result().orElse(HollowTreePiece.DEFAULT_WOOD);
		this.dungeonAir = BlockStateProvider.CODEC.parse(ops, tag.getCompound("dungeon_air")).result().orElse(HollowTreePiece.DEFAULT_DUNGEON_AIR);
		this.dungeonLootBlock = BlockStateProvider.CODEC.parse(ops, tag.getCompound("dungeon_loot_block")).result().orElse(HollowTreePiece.DEFAULT_DUNGEON_LOOT_BLOCK);

		this.dungeonLootTable = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.parse(tag.getString("dungeon_loot_table")));

		ResourceKey<EntityType<?>> dungeonMonster = ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.parse(tag.getString("dungeon_monster")));
		this.dungeonMonster = context.registryAccess().lookup(Registries.ENTITY_TYPE)
			.<Holder<EntityType<?>>>flatMap(reg -> reg.get(dungeonMonster))
			.orElse(HollowTreePiece.DEFAULT_DUNGEON_MONSTER);
	}

	/**
	 * Save to NBT
	 */
	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
		tag.putInt("trunkHeight", this.height);
		tag.putInt("trunkRadius", this.radius);

		tag.put("log", BlockStateProvider.CODEC.encodeStart(NbtOps.INSTANCE, this.log).resultOrPartial(TwilightForestMod.LOGGER::error).orElseGet(CompoundTag::new));
		tag.put("wood", BlockStateProvider.CODEC.encodeStart(NbtOps.INSTANCE, this.wood).resultOrPartial(TwilightForestMod.LOGGER::error).orElseGet(CompoundTag::new));
		tag.put("root", BlockStateProvider.CODEC.encodeStart(NbtOps.INSTANCE, this.root).resultOrPartial(TwilightForestMod.LOGGER::error).orElseGet(CompoundTag::new));
		tag.put("leaves", BlockStateProvider.CODEC.encodeStart(NbtOps.INSTANCE, this.leaves).resultOrPartial(TwilightForestMod.LOGGER::error).orElseGet(CompoundTag::new));
		tag.put("vine", BlockStateProvider.CODEC.encodeStart(NbtOps.INSTANCE, this.vine).resultOrPartial(TwilightForestMod.LOGGER::error).orElseGet(CompoundTag::new));
		tag.put("bug", BlockStateProvider.CODEC.encodeStart(NbtOps.INSTANCE, this.bug).resultOrPartial(TwilightForestMod.LOGGER::error).orElseGet(CompoundTag::new));
		tag.put("dungeon_wood", BlockStateProvider.CODEC.encodeStart(NbtOps.INSTANCE, this.dungeonWood).resultOrPartial(TwilightForestMod.LOGGER::error).orElseGet(CompoundTag::new));
		tag.put("dungeon_air", BlockStateProvider.CODEC.encodeStart(NbtOps.INSTANCE, this.dungeonAir).resultOrPartial(TwilightForestMod.LOGGER::error).orElseGet(CompoundTag::new));
		tag.put("dungeon_loot_block", BlockStateProvider.CODEC.encodeStart(NbtOps.INSTANCE, this.dungeonLootBlock).resultOrPartial(TwilightForestMod.LOGGER::error).orElseGet(CompoundTag::new));

		tag.putString("dungeon_loot_table", this.dungeonLootTable.location().toString());

		tag.putString("dungeon_monster", BuiltInRegistries.ENTITY_TYPE.getKey(this.dungeonMonster.value()).toString());
	}

	/**
	 * Add on the various bits and doo-dads we need to succeed
	 */
	@Override
	public void addChildren(StructurePiece piece, StructurePieceAccessor list, RandomSource rand) {
		int index = this.getGenDepth();

		// 3-5 couple branches on the way up...
		int numBranches = rand.nextInt(3) + 3;
		for (int i = 0; i <= numBranches; i++) {
			int branchHeight = (int) (this.height * rand.nextDouble() * 0.9) + (this.height / 10);
			double branchRotation = rand.nextDouble();

			this.makeSmallBranch(list, rand, index + i + 1, branchHeight, 4, branchRotation, 0.35D, true);
		}

		// build the crown
		this.buildFullCrown(list, rand, index + numBranches + 1);

		// roots
		// 3-5 roots at the bottom
		this.buildBranchRing(list, rand, index, 4, 2, 6, 0.75D, 3, 5, 3, false);

		// several more taproots
		this.buildBranchRing(list, rand, index, 2, 2, 8, 0.9D, 3, 5, 3, false);
	}

	/**
	 * Build the crown of the tree
	 */
	protected void buildFullCrown(StructurePieceAccessor list, RandomSource rand, int index) {
		int crownRadius = this.radius * 4 + 4;
		int bvar = this.radius + 2;

		// okay, let's do 3-5 main branches starting at the bottom of the crown
		index += this.buildBranchRing(list, rand, index, this.height - crownRadius, 0, crownRadius, 0.35D, bvar, bvar + 2, 2, true);

		// then, let's do 3-5 medium branches at the crown middle
		index += this.buildBranchRing(list, rand, index, this.height - (crownRadius / 2), 0, crownRadius, 0.28D, bvar, bvar + 2, 1, true);

		// finally, let's do 2-4 main branches at the crown top
		index += this.buildBranchRing(list, rand, index, this.height, 0, crownRadius, 0.15D, 2, 4, 2, true);

		// and extra finally, let's do 3-6 medium branches going straight up
		index += this.buildBranchRing(list, rand, index, this.height, 0, (crownRadius / 2), 0.05D, bvar, bvar + 2, 1, true);
	}

	/**
	 * Build a ring of branches around the tree
	 * size 0 = small, 1 = med, 2 = large, 3 = root
	 */
	protected int buildBranchRing(StructurePieceAccessor list, RandomSource rand, int index, int branchHeight, int heightVar, int length, double tilt, int minBranches, int maxBranches, int size, boolean leafy) {
		//let's do this!
		int numBranches = rand.nextInt(maxBranches - minBranches + 1) + minBranches;
		double branchRotation = 1.0 / numBranches;
		double branchOffset = rand.nextDouble();

		for (int i = 0; i <= numBranches; i++) {
			int dHeight;
			if (heightVar > 0) {
				dHeight = branchHeight - heightVar + rand.nextInt(2 * heightVar);
			} else {
				dHeight = branchHeight;
			}

			if (size == 2) {
				this.makeLargeBranch(list, rand, index, dHeight, length, i * branchRotation + branchOffset, tilt, leafy);
			} else if (size == 1) {
				this.makeMedBranch(list, rand, index, dHeight, length, i * branchRotation + branchOffset, tilt, leafy);
			} else if (size == 3) {
				this.makeRoot(list, rand, index, dHeight, length, i * branchRotation + branchOffset, tilt);
			} else {
				this.makeSmallBranch(list, rand, index, dHeight, length, i * branchRotation + branchOffset, tilt, leafy);
			}
		}

		return numBranches;
	}


	public void makeSmallBranch(StructurePieceAccessor list, RandomSource rand, int index, int branchHeight, int branchLength, double branchRotation, double branchAngle, boolean leafy) {
		BlockPos bSrc = this.getBranchSrc(branchHeight, branchRotation);
		HollowTreeSmallBranch branch = new HollowTreeSmallBranch(index, bSrc, branchLength, branchRotation, branchAngle, leafy, this.wood, this.leaves);
		list.addPiece(branch);
		branch.addChildren(this, list, rand);
	}

	public void makeMedBranch(StructurePieceAccessor list, RandomSource rand, int index, int branchHeight, int branchLength, double branchRotation, double branchAngle, boolean leafy) {
		BlockPos bSrc = this.getBranchSrc(branchHeight, branchRotation);
		HollowTreeMedBranch branch = new HollowTreeMedBranch(index, bSrc, branchLength, branchRotation, branchAngle, leafy, this.wood, this.leaves);
		list.addPiece(branch);
		branch.addChildren(this, list, rand);
	}

	public void makeLargeBranch(StructurePieceAccessor list, RandomSource rand, int index, int branchHeight, int branchLength, double branchRotation, double branchAngle, boolean leafy) {
		BlockPos bSrc = this.getBranchSrc(branchHeight, branchRotation);
		HollowTreeLargeBranch branch = new HollowTreeLargeBranch(index, bSrc, branchLength, branchRotation, branchAngle, leafy, rand, this.wood, this.leaves, this.dungeonWood, this.dungeonAir, this.dungeonLootBlock, this.dungeonLootTable, this.dungeonMonster);
		list.addPiece(branch);
		branch.addChildren(this, list, rand);
	}


	public void makeRoot(StructurePieceAccessor list, RandomSource rand, int index, int branchHeight, int branchLength, double branchRotation, double branchAngle) {
		BlockPos bSrc = this.getBranchSrc(branchHeight, branchRotation);
		HollowTreeRoot branch = new HollowTreeRoot(index, bSrc, branchLength, branchRotation, branchAngle, false, this.root, this.wood);
		list.addPiece(branch);
		branch.addChildren(this, list, rand);
	}

	/**
	 * Where should we start this branch?
	 */
	private BlockPos getBranchSrc(int branchHeight, double branchRotation) {
		int sx = this.boundingBox.minX() + this.radius + 1;
		int sy = this.boundingBox.minY() + branchHeight;
		int sz = this.boundingBox.minZ() + this.radius + 1;
		return FeatureLogic.translate(new BlockPos(sx, sy, sz), this.radius, branchRotation, 0.5);
	}

	@Override
	public void postProcess(WorldGenLevel level, StructureManager manager, ChunkGenerator generator, RandomSource doNotUse, BoundingBox writeableBounds, ChunkPos chunkPos, BlockPos structureBottomCenter) {
		RandomSource decoRNG = this.getInterChunkDecoRNG(level);

		int hollow = this.radius / 2;
		Direction vineDirection = Direction.from2DDataValue(decoRNG.nextInt(4));

		for (int dx = 0; dx <= 2 * this.radius; dx++) {
			for (int dz = 0; dz <= 2 * this.radius; dz++) {
				// determine how far we are from the center.
				int ax = Math.abs(dx - this.radius);
				int az = Math.abs(dz - this.radius);
				int dist = (int) (Math.max(ax, az) + (Math.min(ax, az) * 0.5));

				for (int dy = 0; dy <= this.height; dy++) {
					// fill the body of the trunk
					if (dist <= this.radius && dist > hollow) {
						this.placeProvidedBlock(level, this.log, decoRNG, dx + 1, dy, dz + 1, writeableBounds, BlockPos.ZERO, false, false); // offset, since our BB is slightly larger than the trunk
					}
				}

				// fill to ground
				if (dist <= this.radius) {
					this.fillColumnDown(level, this.log, decoRNG, dx + 1, -1, dz + 1, writeableBounds);
				}

				// add vines
				if (dist == hollow && (vineDirection.getAxis() == Direction.Axis.X ? (dx == this.radius + (hollow * vineDirection.getStepX())) : (dz == this.radius + (hollow * vineDirection.getStepZ())))) {
					this.fillVineColumnDown(level, this.vine, decoRNG, dx + 1, this.height, dz + 1, writeableBounds, vineDirection);
				}
			}
		}

		// fireflies & cicadas
		int numInsects = decoRNG.nextInt(3 * this.radius) + decoRNG.nextInt(3 * this.radius) + 10;
		for (int i = 0; i <= numInsects; i++) {
			int fHeight = (int) (this.height * decoRNG.nextDouble() * 0.9) + (this.height / 10);
			double fAngle = decoRNG.nextDouble();
			this.addInsect(level, decoRNG, fHeight, fAngle, writeableBounds);
		}
	}

	/**
	 * Add a random insect
	 */
	protected void addInsect(WorldGenLevel world, RandomSource random, int fHeight, double fAngle, BoundingBox sbb) {
		BlockPos bugSpot = FeatureLogic.translate(new BlockPos(this.radius + 1, fHeight, this.radius + 1), this.radius + 1, fAngle, 0.5);

		int ox = this.getWorldX(bugSpot.getX(), bugSpot.getZ());
		int oy = this.getWorldY(bugSpot.getY());
		int oz = this.getWorldZ(bugSpot.getX(), bugSpot.getZ());

		if (!sbb.isInside(ox, oy, oz)) return;

		BlockPos src = new BlockPos(ox, oy, oz);

		double fAngleWrapped = fAngle % 1.0;
		Rotation facing = Rotation.CLOCKWISE_90;

		if (fAngleWrapped > 0.875 || fAngleWrapped <= 0.125) {
			facing = Rotation.CLOCKWISE_180;
		} else if (fAngleWrapped > 0.375 && fAngleWrapped <= 0.625) {
			facing = Rotation.NONE;
		} else if (fAngleWrapped > 0.625) {
			facing = Rotation.COUNTERCLOCKWISE_90;
		}

		BlockState decor = this.bug.getState(random, src).rotate(world, src, facing);
		if (world.getBlockState(src).canBeReplaced() && decor.canSurvive(world, src)) {
			world.setBlock(src, decor, Block.UPDATE_ALL);
		}
	}
}

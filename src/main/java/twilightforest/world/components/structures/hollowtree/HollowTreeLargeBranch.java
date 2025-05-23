package twilightforest.world.components.structures.hollowtree;

import net.minecraft.core.BlockPos;
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

public class HollowTreeLargeBranch extends HollowTreeMedBranch {
	private static final int LEAF_DUNGEON_CHANCE = 8;

	public final boolean hasLeafDungeon;

	private final BlockStateProvider dungeonWood;
	private final BlockStateProvider dungeonAir;
	private final BlockStateProvider dungeonLootBlock;
	private final ResourceKey<LootTable> dungeonLootTable;
	private final Holder<EntityType<?>> dungeonMonster;

	protected HollowTreeLargeBranch(int i, BlockPos src, double length, double angle, double tilt, boolean leafy, RandomSource rand, BlockStateProvider wood, BlockStateProvider leaves, BlockStateProvider dungeonWood, BlockStateProvider dungeonAir, BlockStateProvider dungeonLootBlock, ResourceKey<LootTable> dungeonLootTable, Holder<EntityType<?>> dungeonMonster) {
		super(TFStructurePieceTypes.TFHTLB.value(), i, src, FeatureLogic.translate(src, length, angle, tilt), length, angle, tilt, leafy, wood, leaves);

		this.hasLeafDungeon = rand.nextInt(LEAF_DUNGEON_CHANCE) == 0;

		this.dungeonWood = dungeonWood;
		this.dungeonAir = dungeonAir;
		this.dungeonLootBlock = dungeonLootBlock;
		this.dungeonLootTable = dungeonLootTable;
		this.dungeonMonster = dungeonMonster;
	}

	public HollowTreeLargeBranch(StructurePieceSerializationContext context, CompoundTag tag) {
		super(TFStructurePieceTypes.TFHTLB.value(), context, tag);

		this.hasLeafDungeon = tag.getBoolean("has_leaf_dungeon");

		RegistryOps<Tag> ops = RegistryOps.create(NbtOps.INSTANCE, context.registryAccess());

		this.dungeonWood = BlockStateProvider.CODEC.parse(ops, tag.getCompound("dungeon_wood")).result().orElse(HollowTreePiece.DEFAULT_WOOD);
		this.dungeonAir = BlockStateProvider.CODEC.parse(ops, tag.getCompound("dungeon_air")).result().orElse(HollowTreePiece.DEFAULT_DUNGEON_AIR);
		this.dungeonLootBlock = BlockStateProvider.CODEC.parse(ops, tag.getCompound("dungeon_loot_block")).result().orElse(HollowTreePiece.DEFAULT_DUNGEON_LOOT_BLOCK);

		this.dungeonLootTable = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.parse(tag.getString("dungeon_loot_table")));

		ResourceKey<EntityType<?>> dungeonMonster = ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.parse(tag.getString("dungeon_monster")));
		this.dungeonMonster = context.registryAccess().lookup(Registries.ENTITY_TYPE)
			.<Holder<EntityType<?>>>flatMap(reg -> reg.get(dungeonMonster))
			.orElse(HollowTreePiece.DEFAULT_DUNGEON_MONSTER);
	}

	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
		super.addAdditionalSaveData(context, tag);

		tag.putBoolean("has_leaf_dungeon", this.hasLeafDungeon);

		tag.put("dungeon_wood", BlockStateProvider.CODEC.encodeStart(NbtOps.INSTANCE, this.dungeonWood).resultOrPartial(TwilightForestMod.LOGGER::error).orElseGet(CompoundTag::new));
		tag.put("dungeon_air", BlockStateProvider.CODEC.encodeStart(NbtOps.INSTANCE, this.dungeonAir).resultOrPartial(TwilightForestMod.LOGGER::error).orElseGet(CompoundTag::new));
		tag.put("dungeon_loot_block", BlockStateProvider.CODEC.encodeStart(NbtOps.INSTANCE, this.dungeonLootBlock).resultOrPartial(TwilightForestMod.LOGGER::error).orElseGet(CompoundTag::new));

		tag.putString("dungeon_loot_table", this.dungeonLootTable.location().toString());

		tag.putString("dungeon_monster", BuiltInRegistries.ENTITY_TYPE.getKey(this.dungeonMonster.value()).toString());
	}

	/**
	 * Add other structure components to this one if needed
	 */
	@Override
	public void addChildren(StructurePiece structurecomponent, StructurePieceAccessor list, RandomSource rand) {
		int index = this.getGenDepth();

		// go about halfway out and make a few medium branches.
		// the number of medium branches we can support depends on the length of the big branch
		// every other branch switches sides
		int numMedBranches = rand.nextInt((int) (this.length / 6)) + (int) (this.length / 8);

		for (int i = 0; i <= numMedBranches; i++) {
			double outVar = (rand.nextDouble() * 0.3) + 0.3;
			double angleVar = rand.nextDouble() * 0.225 * ((i & 1) == 0 ? 1.0 : -1.0);

			BlockPos bsrc = FeatureLogic.translate(this.src, this.length * outVar, this.angle, this.tilt);

			this.makeMedBranch(list, rand, index + 2 + i, bsrc, this.length * 0.6, this.angle + angleVar, this.tilt, this.leafy);
		}

		if (this.hasLeafDungeon) {
			this.makeLeafDungeon(list, rand, index + 1, this.dest.getX(), this.dest.getY(), this.dest.getZ());
		}
	}

	public void makeLeafDungeon(StructurePieceAccessor list, RandomSource rand, int index, int x, int y, int z) {
		HollowTreeLeafDungeon dungeon = new HollowTreeLeafDungeon(index, x, y, z, 4, this.dungeonWood, this.leaves, this.dungeonAir, this.dungeonLootBlock, this.dungeonLootTable, this.dungeonMonster, rand);
		list.addPiece(dungeon);
		dungeon.addChildren(this, list, rand);
	}

	public void makeMedBranch(StructurePieceAccessor list, RandomSource rand, int index, BlockPos src, double branchLength, double branchRotation, double branchAngle, boolean leafy) {
		HollowTreeMedBranch branch = new HollowTreeMedBranch(index, src, branchLength, branchRotation, branchAngle, leafy, this.wood, this.leaves);
		list.addPiece(branch);
		branch.addChildren(this, list, rand);
	}

	/**
	 * Draw this branch
	 */
	@Override
	public void postProcess(WorldGenLevel level, StructureManager manager, ChunkGenerator generator, RandomSource doNotUse, BoundingBox writeableBounds, ChunkPos chunkPos, BlockPos structureBottomCenter) {
		RandomSource decoRNG = this.getInterChunkDecoRNG(level);

		// main branch
		this.drawBresehnam(level, writeableBounds, this.src, this.dest, this.wood, decoRNG);

		// reinforce it
		int reinforcements = 4;
		for (int i = 0; i <= reinforcements; i++) {
			int vx = (i & 2) == 0 ? 1 : 0;
			int vy = (i & 1) == 0 ? 1 : -1;
			int vz = (i & 2) == 0 ? 0 : 1;
			this.drawBresehnam(level, writeableBounds, this.src.offset(vx, vy, vz), this.dest, this.wood, decoRNG);
		}

		// make 1-2 small branches near the base
		//Random decoRNG = new Random(world.getSeed() + (this.boundingBox.minX() * 321534781) ^ (this.boundingBox.minZ() * 756839));
		int numSmallBranches = decoRNG.nextInt(2) + 1;
		for (int i = 0; i <= numSmallBranches; i++) {
			double outVar = (decoRNG.nextFloat() * 0.25F) + 0.25F;
			double angleVar = decoRNG.nextFloat() * 0.25F * ((i & 1) == 0 ? 1.0F : -1.0F);

			BlockPos bsrc = FeatureLogic.translate(this.src, this.length * outVar, this.angle, this.tilt);

			this.drawSmallBranch(level, writeableBounds, bsrc, Math.max(this.length * 0.3F, 2F), this.angle + angleVar, this.tilt, decoRNG, this.wood, this.leaves);
		}

		if (this.leafy && !this.hasLeafDungeon) {
			// leaf blob at the end
			this.drawBlockBlob(level, writeableBounds, this.dest.getX() - this.boundingBox.minX(), this.dest.getY() - this.boundingBox.minY(), this.dest.getZ() - this.boundingBox.minZ(), 3, decoRNG, this.leaves, false, false, true);
		}
	}
}

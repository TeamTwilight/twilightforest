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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.storage.loot.LootTable;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFStructurePieceTypes;

public class HollowTreeLeafDungeon extends HollowTreePiece {
	private final int radius;

	private final BlockStateProvider wood;
	private final BlockStateProvider leaves;
	private final BlockStateProvider inside;
	private final BlockStateProvider lootContainer;
	private final ResourceKey<LootTable> lootTable;
	private final Holder<EntityType<?>> monster;

	/**
	 * Make a blob of leaves
	 */
	protected HollowTreeLeafDungeon(int index, int x, int y, int z, int radius, BlockStateProvider wood, BlockStateProvider leaves, BlockStateProvider inside, BlockStateProvider lootContainer, ResourceKey<LootTable> lootTable, Holder<EntityType<?>> monster, RandomSource random) {
		super(TFStructurePieceTypes.TFHTLD.value(), index, new BoundingBox(x - radius, y - radius, z - radius, x + radius, y + radius, z + radius));

		this.setOrientation(StructurePiece.getRandomHorizontalDirection(random));

		this.radius = radius;

		this.wood = wood;
		this.leaves = leaves;
		this.inside = inside;
		this.lootContainer = lootContainer;
		this.lootTable = lootTable;
		this.monster = monster;
	}

	/**
	 * Load from NBT
	 */
	public HollowTreeLeafDungeon(StructurePieceSerializationContext context, CompoundTag tag) {
		super(TFStructurePieceTypes.TFHTLD.value(), tag);

		this.radius = tag.getInt("leafRadius");

		RegistryOps<Tag> ops = RegistryOps.create(NbtOps.INSTANCE, context.registryAccess());

		this.wood = BlockStateProvider.CODEC.parse(ops, tag.getCompound("wood")).result().orElse(HollowTreePiece.DEFAULT_WOOD);
		this.leaves = BlockStateProvider.CODEC.parse(ops, tag.getCompound("leaves")).result().orElse(HollowTreePiece.DEFAULT_LEAVES);
		this.inside = BlockStateProvider.CODEC.parse(ops, tag.getCompound("air")).result().orElse(HollowTreePiece.DEFAULT_DUNGEON_AIR);
		this.lootContainer = BlockStateProvider.CODEC.parse(ops, tag.getCompound("loot_block")).result().orElse(HollowTreePiece.DEFAULT_DUNGEON_LOOT_BLOCK);

		this.lootTable = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.parse(tag.getString("loot_table")));

		ResourceKey<EntityType<?>> dungeonMonster = ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.parse(tag.getString("monster")));
		this.monster = context.registryAccess().lookup(Registries.ENTITY_TYPE)
			.<Holder<EntityType<?>>>flatMap(reg -> reg.get(dungeonMonster))
			.orElse(HollowTreePiece.DEFAULT_DUNGEON_MONSTER);
	}

	/**
	 * Save to NBT
	 */
	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
		tag.putInt("leafRadius", this.radius);

		tag.put("wood", BlockStateProvider.CODEC.encodeStart(NbtOps.INSTANCE, this.wood).resultOrPartial(TwilightForestMod.LOGGER::error).orElseGet(CompoundTag::new));
		tag.put("leaves", BlockStateProvider.CODEC.encodeStart(NbtOps.INSTANCE, this.leaves).resultOrPartial(TwilightForestMod.LOGGER::error).orElseGet(CompoundTag::new));
		tag.put("air", BlockStateProvider.CODEC.encodeStart(NbtOps.INSTANCE, this.inside).resultOrPartial(TwilightForestMod.LOGGER::error).orElseGet(CompoundTag::new));
		tag.put("loot_block", BlockStateProvider.CODEC.encodeStart(NbtOps.INSTANCE, this.lootContainer).resultOrPartial(TwilightForestMod.LOGGER::error).orElseGet(CompoundTag::new));

		tag.putString("loot_table", this.lootTable.location().toString());

		tag.putString("monster", BuiltInRegistries.ENTITY_TYPE.getKey(this.monster.value()).toString());
	}

	/**
	 * Draw a giant blob of whatevs (okay, it's going to be leaves).
	 */
	@Override
	public void postProcess(WorldGenLevel level, StructureManager manager, ChunkGenerator generator, RandomSource doNotUse, BoundingBox writeableBounds, ChunkPos chunkPos, BlockPos structureBottomCenter) {
		RandomSource decoRNG = this.getInterChunkDecoRNG(level);

		// leaves on the outside
		this.drawBlockBlob(level, writeableBounds, this.radius, this.radius, this.radius, 5, decoRNG, this.leaves, false, true, true);
		// then wood
		this.drawBlockBlob(level, writeableBounds, this.radius, this.radius, this.radius, 3, decoRNG, this.wood, false, false, false);
		// then air
		this.drawBlockBlob(level, writeableBounds, this.radius, this.radius, this.radius, 2, decoRNG, this.inside, true, false, true);

		// then treasure chest
		this.placeTreasureAtCurrentPosition(level, this.radius, this.radius - 1, this.radius, writeableBounds, decoRNG, this.lootContainer, this.lootTable);

		// then spawner
		this.placeSpawnerAtCurrentPosition(level, decoRNG, this.radius, this.radius, this.radius, this.monster.value(), writeableBounds);
	}

	/**
	 * Place a treasure chest at the specified coordinates
	 */
	protected void placeTreasureAtCurrentPosition(WorldGenLevel world, int x, int y, int z, BoundingBox sbb, RandomSource random, BlockStateProvider stateProvider, ResourceKey<LootTable> lootTable) {
		Direction direction = new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST}[random.nextInt(4)];
		BlockPos pos = this.getWorldPos(x, y, z).relative(direction, 2);

		BlockState state = stateProvider.getState(random, pos).mirror(this.mirror).rotate(world, pos, this.rotation);
		if (state.getBlock() instanceof ChestBlock) state = state.setValue(ChestBlock.FACING, direction.getOpposite());

		if (sbb.isInside(pos) && !world.getBlockState(pos).is(state.getBlock())) {
			world.setBlock(pos, state, Block.UPDATE_CLIENTS);

			if (world.getBlockEntity(pos) instanceof RandomizableContainerBlockEntity randomLootContainer)
				randomLootContainer.setLootTable(lootTable, random.nextLong());
		}
	}

	/**
	 * Place a monster spawner at the specified coordinates
	 */
	protected void placeSpawnerAtCurrentPosition(WorldGenLevel world, RandomSource rand, int x, int y, int z, EntityType<? extends Entity> monsterID, BoundingBox sbb) {
		BlockPos pos = this.getWorldPos(x, y, z);

		if (sbb.isInside(pos) && !world.getBlockState(pos).is(Blocks.SPAWNER)) {
			world.setBlock(pos, Blocks.SPAWNER.defaultBlockState(), Block.UPDATE_CLIENTS);

			if (world.getBlockEntity(pos) instanceof SpawnerBlockEntity spawner)
				spawner.setEntityId(monsterID, rand);
		}
	}
}

package twilightforest.world.components.structures.lichtowerrevamp;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.world.components.processors.BoxCuttingProcessor;
import twilightforest.world.components.structures.TwilightTemplateStructurePiece;

import java.util.HashMap;
import java.util.Map;


public final class TowerFoyer extends TwilightTemplateStructurePiece {
    public TowerFoyer(StructurePieceSerializationContext ctx, CompoundTag compoundTag) {
        super(TFStructurePieceTypes.TOWER_FOYER.get(), compoundTag, ctx, readSettings(compoundTag));
    }

    public TowerFoyer(StructureTemplateManager structureManager, BlockPos startPosition) {
        super(TFStructurePieceTypes.TOWER_FOYER.get(), 0, structureManager, TwilightForestMod.prefix("lich_tower/foyer"), makeSettings(Rotation.NONE), startPosition.above(3));
    }

    @Override
    public void addChildren(StructurePiece parent, StructurePieceAccessor structureStart, RandomSource random) {
        super.addChildren(parent, structureStart, random);

        int randomCountTowerSegments = random.nextInt(5) + 15;
        Direction front = this.rotation.rotate(Direction.NORTH);

        BlockPos center = this.boundingBox.getCenter();
        BlockPos centralTowerOrigin = new BlockPos(center.getX() - (CentralTowerSegment.SIDE_LENGTH >> 1), this.boundingBox.maxY() - 30, center.getZ() - (CentralTowerSegment.SIDE_LENGTH >> 1));

        // TODO Add custom processors for chopping holes into the Segments
        //  The sides of the segments are completely available for adding attachments
        //  The fronts and backs will need to wait until halfway upwards

        Map<BlockPos, Direction> sideTowerStarts = new HashMap<>();
        int totalSegments = (randomCountTowerSegments >> 1) - 2;

        this.beginSideTowers(structureStart, random, totalSegments, 0, front, Rotation.CLOCKWISE_90, centralTowerOrigin, sideTowerStarts);
        this.beginSideTowers(structureStart, random, totalSegments, 0, front, Rotation.COUNTERCLOCKWISE_90, centralTowerOrigin, sideTowerStarts);

        this.beginSideTowers(structureStart, random, totalSegments, 7, front, Rotation.NONE, centralTowerOrigin, sideTowerStarts);
        this.beginSideTowers(structureStart, random, totalSegments, 7, front, Rotation.CLOCKWISE_180, centralTowerOrigin, sideTowerStarts);

        BoxCuttingProcessor cuttingProcessor = BoxCuttingProcessor.forLichTower(sideTowerStarts);
        Rotation climbingRotation = this.rotation;

        for (int i = 0; i < randomCountTowerSegments; i++) {
            CentralTowerSegment towerSegment = new CentralTowerSegment(this.structureManager, climbingRotation, cuttingProcessor, StructureTemplate.getZeroPositionWithTransform(centralTowerOrigin, Mirror.NONE, climbingRotation, CentralTowerSegment.SIDE_LENGTH, CentralTowerSegment.SIDE_LENGTH).above(i * 4));
            towerSegment.addChildren(this, structureStart, random);
            structureStart.addPiece(towerSegment);

            // Prime for next placement
            climbingRotation = climbingRotation.getRotated(this.mirror == Mirror.NONE ? Rotation.COUNTERCLOCKWISE_90 : Rotation.CLOCKWISE_90);
        }

		BossRoom bossRoom = new BossRoom(this.structureManager, climbingRotation, cuttingProcessor, StructureTemplate.getZeroPositionWithTransform(centralTowerOrigin, Mirror.NONE, climbingRotation, BossRoom.SIDE_LENGTH, BossRoom.SIDE_LENGTH).above(randomCountTowerSegments * 4).west(7).north(7));
		bossRoom.addChildren(this, structureStart, random);
		structureStart.addPiece(bossRoom);
    }

    private void beginSideTowers(StructurePieceAccessor structureStart, RandomSource random, int totalSegments, int segmentStart, Direction front, Rotation fromFront, BlockPos centralTowerOrigin, Map<BlockPos, Direction> sideTowerStarts) {
        // Pre-process for the side-towers to attach to the sides of the central
        for (int i = segmentStart; i < totalSegments; i++) {
            float weight = (float) i / totalSegments * 2;
            if (random.nextFloat() < weight) {
                Rotation relativeRotation = fromFront.getRotated(this.rotation);
                Direction sideTowerDirection = relativeRotation.rotate(front);

                BlockPos sideTowerPos = LichTowerUtil.getRandomOpeningPlacementPos(centralTowerOrigin,
                        sideTowerDirection,
                        Mirror.NONE,
                        random,
                        i << 1,
                        this.rotation == Rotation.NONE || this.rotation == Rotation.CLOCKWISE_180
                );

                // TODO if you're looking at this code because of placement oddity, be mindful this starts at the
                //  bottom right corner of the entrance, from the perspective of standing inside the central staircase
                sideTowerStarts.put(sideTowerPos.relative(sideTowerDirection), sideTowerDirection);

                CentralTowerAttachment sideTower = CentralTowerAttachment.startRandomAttachment(this.structureManager, relativeRotation, sideTowerPos.relative(sideTowerDirection, 2), random);
                sideTower.addChildren(this, structureStart, random);
                structureStart.addPiece(sideTower);
            }
        }
    }

    @Override
    public void postProcess(WorldGenLevel level, StructureManager structureFeatureManager, ChunkGenerator chunkGenerator, RandomSource random, BoundingBox boundingBox, ChunkPos chunkPos, BlockPos pos) {
        this.placePieceAdjusted(level, structureFeatureManager, chunkGenerator, random, boundingBox, chunkPos, pos, -3);

        BlockPos placement = new BlockPos(this.boundingBox.getCenter().getX() + 1, this.boundingBox.minY() + 7, this.boundingBox.minZ() + 16);

        if (boundingBox.isInside(placement)) {
            final ArmorStand armorStand = new ArmorStand(EntityType.ARMOR_STAND, level.getLevel());
            armorStand.setCustomName(Component.literal("Welcome to the new Lich Tower! The design is heavily WIP and will be fleshed out significantly in later development builds"));
            armorStand.moveTo(placement.getX(), placement.getY() + 0.5, placement.getZ() + 0.5, 0, 0);
            armorStand.setInvulnerable(true);
            armorStand.setInvisible(true);
            armorStand.setCustomNameVisible(true);
            armorStand.setSilent(true);
            armorStand.setNoGravity(true);
            // set marker flag
            armorStand.getEntityData().set(ArmorStand.DATA_CLIENT_FLAGS, (byte) (armorStand.getEntityData().get(ArmorStand.DATA_CLIENT_FLAGS) | 16));
            level.addFreshEntity(armorStand);
        }
    }

    @Override
    protected void handleDataMarker(String label, BlockPos pos, ServerLevelAccessor levelAccessor, RandomSource random, BoundingBox boundingBox) {

    }
}

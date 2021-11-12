package twilightforest.world.components.structures.courtyard;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.feature.NoiseEffect;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.processors.CobbleVariants;
import twilightforest.world.components.processors.NagastoneVariants;
import twilightforest.world.components.processors.SmoothStoneVariants;
import twilightforest.world.components.processors.StoneBricksVariants;
import twilightforest.world.components.structures.TwilightDoubleTemplateStructurePiece;

import java.util.Random;

public class CourtyardWallCornerOuter extends TwilightDoubleTemplateStructurePiece {
    public CourtyardWallCornerOuter(ServerLevel level, CompoundTag nbt) {
        super(NagaCourtyardPieces.TFNCWC,
                nbt,
                level,
                readSettings(nbt).addProcessor(CourtyardMain.WALL_INTEGRITY_PROCESSOR).addProcessor(SmoothStoneVariants.INSTANCE).addProcessor(NagastoneVariants.INSTANCE).addProcessor(StoneBricksVariants.INSTANCE).addProcessor(CobbleVariants.INSTANCE),
                readSettings(nbt).addProcessor(CourtyardMain.WALL_DECAY_PROCESSOR).addProcessor(CobbleVariants.INSTANCE)
        );
    }

    public CourtyardWallCornerOuter(int i, int x, int y, int z, Rotation rotation, StructureManager structureManager) {
        super(NagaCourtyardPieces.TFNCWC,
                i,
                structureManager,
                TwilightForestMod.prefix("courtyard/courtyard_wall_corner"),
                makeSettings(rotation).addProcessor(CourtyardMain.WALL_INTEGRITY_PROCESSOR).addProcessor(SmoothStoneVariants.INSTANCE).addProcessor(NagastoneVariants.INSTANCE).addProcessor(StoneBricksVariants.INSTANCE).addProcessor(CobbleVariants.INSTANCE),
                TwilightForestMod.prefix("courtyard/courtyard_wall_corner_decayed"),
                makeSettings(rotation).addProcessor(CourtyardMain.WALL_DECAY_PROCESSOR).addProcessor(CobbleVariants.INSTANCE),
                new BlockPos(x, y, z)
        );
    }

    @Override
    protected void handleDataMarker(String label, BlockPos pos, ServerLevelAccessor levelAccessor, Random random, BoundingBox boundingBox) {

    }

    @Override
    public NoiseEffect getNoiseEffect() {
        return NoiseEffect.BEARD;
    }
}

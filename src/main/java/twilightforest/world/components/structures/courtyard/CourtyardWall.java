package twilightforest.world.components.structures.courtyard;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.world.components.processors.CobbleVariants;
import twilightforest.world.components.processors.NagastoneVariants;
import twilightforest.world.components.processors.SmoothStoneVariants;
import twilightforest.world.components.processors.StoneBricksVariants;
import twilightforest.world.components.structures.TwilightDoubleTemplateStructurePiece;

public class CourtyardWall extends TwilightDoubleTemplateStructurePiece {
    public CourtyardWall(StructurePieceSerializationContext ctx, CompoundTag nbt) {
        super(TFStructurePieceTypes.TFNCWl.get(),
                nbt,
                ctx,
                readSettings(nbt).addProcessor(CourtyardMain.WALL_INTEGRITY_PROCESSOR).addProcessor(SmoothStoneVariants.INSTANCE).addProcessor(NagastoneVariants.INSTANCE).addProcessor(StoneBricksVariants.INSTANCE).addProcessor(CobbleVariants.INSTANCE),
                readSettings(nbt).addProcessor(CourtyardMain.WALL_DECAY_PROCESSOR).addProcessor(CobbleVariants.INSTANCE)
        );
    }

    public CourtyardWall(int i, int x, int y, int z, Rotation rotation, StructureTemplateManager structureManager) {
        super(TFStructurePieceTypes.TFNCWl.get(),
                i,
                structureManager,
                TwilightForestMod.prefix("courtyard/courtyard_wall"),
                makeSettings(rotation).addProcessor(CourtyardMain.WALL_INTEGRITY_PROCESSOR).addProcessor(SmoothStoneVariants.INSTANCE).addProcessor(NagastoneVariants.INSTANCE).addProcessor(StoneBricksVariants.INSTANCE).addProcessor(CobbleVariants.INSTANCE),
                TwilightForestMod.prefix("courtyard/courtyard_wall_decayed"),
                makeSettings(rotation).addProcessor(CourtyardMain.WALL_DECAY_PROCESSOR).addProcessor(CobbleVariants.INSTANCE),
                new BlockPos(x, y, z)
        );
    }

    @Override
    protected void handleDataMarker(String label, BlockPos pos, ServerLevelAccessor levelAccessor, RandomSource random, BoundingBox boundingBox) {

    }
}

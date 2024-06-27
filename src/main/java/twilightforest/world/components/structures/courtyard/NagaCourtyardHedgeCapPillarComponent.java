package twilightforest.world.components.structures.courtyard;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFStructurePieceTypes;

public class NagaCourtyardHedgeCapPillarComponent extends NagaCourtyardHedgeAbstractComponent {
	public NagaCourtyardHedgeCapPillarComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(ctx, TFStructurePieceTypes.TFNCCpP.get(), nbt, new ResourceLocation(TwilightForestMod.ID, "courtyard/hedge_end_pillar"), new ResourceLocation(TwilightForestMod.ID, "courtyard/hedge_end_pillar_big"));
	}

	public NagaCourtyardHedgeCapPillarComponent(StructureTemplateManager manager, int i, int x, int y, int z, Rotation rotation) {
		super(manager, TFStructurePieceTypes.TFNCCpP.get(), i, x, y, z, rotation, new ResourceLocation(TwilightForestMod.ID, "courtyard/hedge_end_pillar"), new ResourceLocation(TwilightForestMod.ID, "courtyard/hedge_end_pillar_big"));
	}
}
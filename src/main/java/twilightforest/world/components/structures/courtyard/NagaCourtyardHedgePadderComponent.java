package twilightforest.world.components.structures.courtyard;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFStructurePieceTypes;

public class NagaCourtyardHedgePadderComponent extends NagaCourtyardHedgeAbstractComponent {
	public NagaCourtyardHedgePadderComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(ctx, TFStructurePieceTypes.TFNCPd.get(), nbt, new ResourceLocation(TwilightForestMod.ID, "courtyard/hedge_between"), new ResourceLocation(TwilightForestMod.ID, "courtyard/hedge_between_big"));
	}

	public NagaCourtyardHedgePadderComponent(StructureTemplateManager manager, int i, int x, int y, int z, Rotation rotation) {
		super(manager, TFStructurePieceTypes.TFNCPd.get(), i, x, y, z, rotation, new ResourceLocation(TwilightForestMod.ID, "courtyard/hedge_between"), new ResourceLocation(TwilightForestMod.ID, "courtyard/hedge_between_big"));
	}
}

package twilightforest.world.components.structures.courtyard;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFStructurePieceTypes;

public class NagaCourtyardHedgeCapComponent extends NagaCourtyardHedgeAbstractComponent {

	public NagaCourtyardHedgeCapComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(ctx, TFStructurePieceTypes.TFNCCp.get(), nbt, new ResourceLocation(TwilightForestMod.ID, "courtyard/hedge_end"), new ResourceLocation(TwilightForestMod.ID, "courtyard/hedge_end_big"));
	}

	@SuppressWarnings("WeakerAccess")
	public NagaCourtyardHedgeCapComponent(StructureTemplateManager manager, int i, int x, int y, int z, Rotation rotation) {
		super(manager, TFStructurePieceTypes.TFNCCp.get(), i, x, y, z, rotation, new ResourceLocation(TwilightForestMod.ID, "courtyard/hedge_end"), new ResourceLocation(TwilightForestMod.ID, "courtyard/hedge_end_big"));
	}
}
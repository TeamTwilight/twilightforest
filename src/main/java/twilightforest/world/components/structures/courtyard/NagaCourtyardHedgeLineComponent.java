package twilightforest.world.components.structures.courtyard;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFStructurePieceTypes;

public class NagaCourtyardHedgeLineComponent extends NagaCourtyardHedgeAbstractComponent {
	public NagaCourtyardHedgeLineComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(ctx, TFStructurePieceTypes.TFNCLn.get(), nbt, TwilightForestMod.prefix("courtyard/hedge_line"), TwilightForestMod.prefix("courtyard/hedge_line_big"));
	}

	public NagaCourtyardHedgeLineComponent(StructureTemplateManager manager, int i, int x, int y, int z, Rotation rotation) {
		super(manager, TFStructurePieceTypes.TFNCLn.get(), i, x, y, z, rotation, TwilightForestMod.prefix("courtyard/hedge_line"), TwilightForestMod.prefix("courtyard/hedge_line_big"));
	}
}

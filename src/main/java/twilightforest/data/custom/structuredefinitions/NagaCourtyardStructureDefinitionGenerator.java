package twilightforest.data.custom.structuredefinitions;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.structures.courtyard.CourtyardMain;

import java.util.concurrent.CompletableFuture;

public class NagaCourtyardStructureDefinitionGenerator extends StructureTemplateDefinitionProvider {
	public NagaCourtyardStructureDefinitionGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
		super(output, lookupProvider, TwilightForestMod.ID, existingFileHelper, "Naga Courtyard");
	}

	@Override
	protected void generatePools() {
		this.add("courtyard/spawner", CourtyardMain.CENTER_POOL, 100);
	}
}

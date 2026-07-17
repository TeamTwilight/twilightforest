package twilightforest.datagen.data.custom.structuredefinitions;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.structures.finalcastle.FinalCastleBellTower21Component;
import twilightforest.world.components.structures.finalcastle.FinalCastleBossGazeboComponent;
import twilightforest.world.components.structures.finalcastle.FinalCastleLargeTowerComponent;

import java.util.concurrent.CompletableFuture;

public class FinalCastleStructureDefinitionGenerator extends StructureTemplateDefinitionProvider {
	public FinalCastleStructureDefinitionGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(output, lookupProvider, TwilightForestMod.ID, "Final Castle");
	}

	@Override
	protected void generatePools() {
		this.add("final_castle/temp/large_tower", FinalCastleLargeTowerComponent.LARGE_TOWER_TEMP_POOL, 100);
		this.add("final_castle/temp/bell_tower", FinalCastleBellTower21Component.BELL_TOWER_TEMP_POOL, 100);
		this.add("final_castle/temp/gazebo", FinalCastleBossGazeboComponent.GAZEBO_TEMP_POOL, 100);
	}
}

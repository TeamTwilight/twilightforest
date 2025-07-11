package twilightforest.data.custom;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.structures.finalcastle.FinalCastleBellTower21Component;
import twilightforest.world.components.structures.finalcastle.FinalCastleBossGazeboComponent;
import twilightforest.world.components.structures.finalcastle.FinalCastleLargeTowerComponent;
import twilightforest.world.components.structures.lichtowerrevamp.LichTowerPieces;

import java.util.concurrent.CompletableFuture;

public class FinalCastleStructureDefinitionGenerator extends StructureTemplateDefinitionProvider {
	public FinalCastleStructureDefinitionGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
		super(output, lookupProvider, TwilightForestMod.ID, existingFileHelper, "Final Castle");
	}

	@Override
	protected void generatePools() {
		this.add("final_castle/temp/large_tower", FinalCastleLargeTowerComponent.LARGE_TOWER_TEMP_POOL, 100);
		this.add("final_castle/temp/bell_tower", FinalCastleBellTower21Component.BELL_TOWER_TEMP_POOL, 100);
		this.add("final_castle/temp/gazebo", FinalCastleBossGazeboComponent.GAZEBO_TEMP_POOL, 100);
	}
}

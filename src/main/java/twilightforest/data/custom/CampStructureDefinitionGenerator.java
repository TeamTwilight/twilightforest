package twilightforest.data.custom;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import twilightforest.TwilightForestMod;

import java.util.concurrent.CompletableFuture;

public class CampStructureDefinitionGenerator extends StructureTemplateDefinitionProvider {
	public CampStructureDefinitionGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
		super(output, lookupProvider, TwilightForestMod.ID, existingFileHelper, "Camp");
	}

	@Override
	protected void generatePools() {
		ResourceLocation poolId1 = TwilightForestMod.prefix("camp/structure_start");
		for(String roomId1 : new String[]{"camp/tent"}) {
			this.add(roomId1, poolId1, 100);
		}

		ResourceLocation pathPool = TwilightForestMod.prefix("camp/path");
		this.add("camp/path", pathPool, 100);
		this.add("camp/rack", pathPool, 40);
	}
}

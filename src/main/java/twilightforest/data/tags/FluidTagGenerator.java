package twilightforest.data.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;

import java.util.concurrent.CompletableFuture;

public class FluidTagGenerator extends FluidTagsProvider {

    public static final TagKey<Fluid> FIRE_JET_FUEL = FluidTags.create(TwilightForestMod.prefix("fire_jet_fuel"));

    public FluidTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, TwilightForestMod.ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(FIRE_JET_FUEL).addTag(FluidTags.LAVA);
    }

    @Override
    public String getName() {
        return "Twilight Forest Fluid Tags";
    }
}

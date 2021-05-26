package twilightforest.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.FluidTagsProvider;
import net.minecraft.fluid.Fluid;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ITag;
import net.minecraftforge.common.data.ExistingFileHelper;
import twilightforest.TwilightForestMod;

import javax.annotation.Nullable;

public class FluidTagGenerator extends FluidTagsProvider {

    public static final ITag.INamedTag<Fluid> FIRE_JET_FUEL = FluidTags.makeWrapperTag(TwilightForestMod.prefix("fire_jet_fuel").toString());
    public static final ITag.INamedTag<Fluid> PORTAL_FLUID = FluidTags.makeWrapperTag(TwilightForestMod.prefix("portal_fluid").toString());

    public FluidTagGenerator(DataGenerator generatorIn, @Nullable ExistingFileHelper existingFileHelper) {
        super(generatorIn, TwilightForestMod.ID, existingFileHelper);
    }

    @Override
    protected void registerTags() {
        getOrCreateBuilder(FIRE_JET_FUEL).addTag(FluidTags.LAVA);
        getOrCreateBuilder(PORTAL_FLUID).addTag(FluidTags.WATER);
    }
}

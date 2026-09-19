package twilightforest.client.properties;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.numeric.NeedleDirectionHelper;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.MoonPhase;
import net.minecraft.world.level.dimension.DimensionType;
import twilightforest.init.TFDataComponents;
import twilightforest.item.MoonDialItem;
import twilightforest.tags.TFDimensionTypeTags;

import java.util.Optional;

public class MoonDialPhaseProperty extends NeedleDirectionHelper implements RangeSelectItemModelProperty {

	public static final MapCodec<MoonDialPhaseProperty> MAP_CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
			Codec.intRange(1, Integer.MAX_VALUE).optionalFieldOf("phases", 8).forGetter(p -> p.phases)
	).apply(inst, MoonDialPhaseProperty::new));

	private final Wobbler wobbler;
	private final RandomSource random = RandomSource.create();
	private final int phases;

	public MoonDialPhaseProperty(int phases) {
		super(true);
        this.wobbler = this.newWobbler(0.7F);
		this.phases = phases;
	}

	@Override
	protected float calculate(ItemStack stack, ClientLevel level, int seed, ItemOwner owner) {
		Optional<MoonPhase> moonPhase = stack.getOrDefault(TFDataComponents.MOON_DIAL_PHASE, Optional.empty());
		long gameTime = level.getGameTime();

		// MoonPhase in item data should be considered stale data if in a tagged moonless dimension, and therefore should wobble instead
        if (this.moonIsIndeterminate(stack, level)) {
            return this.indeterminateRotation(gameTime);
        }

        return moonPhase.map(MoonPhase::index).orElse(0); // Display full moon if uninitialized inside chest or item-viewer such as JEI
    }

	private boolean moonIsIndeterminate(ItemStack stack, ClientLevel level) {
		ResourceKey<DimensionType> attunedDimension = stack.getOrDefault(TFDataComponents.MOON_DIAL_DIMENSION, MoonDialItem.DEFAULT_DIMENSION);

        Optional<Registry<DimensionType>> dimensionTypes = level.registryAccess().lookup(Registries.DIMENSION_TYPE);
        if (dimensionTypes.isEmpty())
			return false; // How'd you get here?

        for (Holder<DimensionType> dimensionTypeHolder : dimensionTypes.get().getTagOrEmpty(TFDimensionTypeTags.MOON_DIAL_INDETERMINATE))
            if (attunedDimension.equals(dimensionTypeHolder.getKey()))
                return true;

        return false;
	}

	private float indeterminateRotation(long gameTime) {
		if (this.wobbler.shouldUpdate(gameTime)) {
			this.wobbler.update(gameTime, this.random.nextFloat());
		}

		return this.wobbler.rotation() * this.phases;
	}

	@Override
	public MapCodec<? extends RangeSelectItemModelProperty> type() {
		return MAP_CODEC;
	}

}

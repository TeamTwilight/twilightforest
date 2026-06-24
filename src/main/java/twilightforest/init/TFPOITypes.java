package twilightforest.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import twilightforest.TwilightForestMod;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TFPOITypes {

	public static final DeferredRegister<PoiType> POIS = DeferredRegister.create(Registries.POINT_OF_INTEREST_TYPE, TwilightForestMod.ID);

	public static final DeferredHolder<PoiType, PoiType> GHAST_TRAP = POIS.register("ghast_trap", () ->
		new PoiType(
			Stream.of(TFBlocks.GHAST_TRAP)
				.flatMap(block -> block.get().getStateDefinition().getPossibleStates().stream())
				.collect(Collectors.toUnmodifiableSet()),
			0,
			1
		)
	);
}

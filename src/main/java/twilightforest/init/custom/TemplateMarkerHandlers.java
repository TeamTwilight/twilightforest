package twilightforest.init.custom;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.PaintingVariantTags;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import twilightforest.TFRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFBlocks;
import twilightforest.loot.TFLootTables;
import twilightforest.world.components.structures.markerhandler.*;
import twilightforest.world.components.structures.util.TemplateMarkerHandlerList;

import java.util.Map;

public class TemplateMarkerHandlers {

	public static final DeferredRegister<TemplateMarkerHandlerType> TEMPLATE_MARKER_HANDLER_TYPES = DeferredRegister.create(TFRegistries.Keys.TEMPLATE_MARKER_HANDLER_TYPE, TwilightForestMod.ID);
	public static final Codec<TemplateMarkerHandlerType> TYPE_CODEC = Codec.lazyInitialized(TFRegistries.TEMPLATE_MARKER_HANDLER_TYPES::byNameCodec);
	public static final Codec<TemplateMarkerHandler> DISPATCH_CODEC = TYPE_CODEC.dispatch("type", TemplateMarkerHandler::getType, TemplateMarkerHandlerType::getCodec);
	public static final Codec<Holder<TemplateMarkerHandler>> HOLDER_CODEC = RegistryFileCodec.create(TFRegistries.Keys.TEMPLATE_MARKER_HANDLER, DISPATCH_CODEC);

	public static final DeferredHolder<TemplateMarkerHandlerType, TemplateMarkerHandlerType> BLOCK_PLACEMENT = TEMPLATE_MARKER_HANDLER_TYPES.register("block_placement", () -> () -> BlockPlaceMarkerHandler.CODEC);
	public static final DeferredHolder<TemplateMarkerHandlerType, TemplateMarkerHandlerType> HANDLER_SWITCH = TEMPLATE_MARKER_HANDLER_TYPES.register("handler_switch", () -> () -> SwitchMarkerHandler.CODEC);
	public static final DeferredHolder<TemplateMarkerHandlerType, TemplateMarkerHandlerType> ROTATION = TEMPLATE_MARKER_HANDLER_TYPES.register("rotation", () -> () -> RotationMarkerHandler.CODEC);
	public static final DeferredHolder<TemplateMarkerHandlerType, TemplateMarkerHandlerType> DRYING_RACK = TEMPLATE_MARKER_HANDLER_TYPES.register("drying_rack", () -> () -> DryingRackMarkerHandler.CODEC);
	public static final DeferredHolder<TemplateMarkerHandlerType, TemplateMarkerHandlerType> PAINTING = TEMPLATE_MARKER_HANDLER_TYPES.register("painting", () -> () -> PaintingMarkerHandler.CODEC);
	public static final DeferredHolder<TemplateMarkerHandlerType, TemplateMarkerHandlerType> LOOT = TEMPLATE_MARKER_HANDLER_TYPES.register("loot", () -> () -> LootMarkerHandler.CODEC);

	public static final ResourceKey<TemplateMarkerHandlerList> CAMP_MARKER_HANDLERS = ResourceKey.create(TFRegistries.Keys.TEMPLATE_MARKER_HANDLER_LIST, TwilightForestMod.prefix("camp_marker_handlers"));

	// TODO
	//  Lich Tower:
	//    Dangling handler
	//    List handler
	//    Mason jar handler
	//    Skull handler
	//    Candle handler
	//    Candled Skull handler
	//    Bookshelf handler
	//    Bookshelf Mimic handler
	//    Lectern handler
	//    Spawner handler

	public static void bootstrap(BootstrapContext<TemplateMarkerHandlerList> context) {
		BlockPlaceMarkerHandler campfireSeat = new BlockPlaceMarkerHandler(new WeightedStateProvider(WeightedList.<BlockState>builder()
			.add(TFBlocks.TWILIGHT_OAK_SLAB.value().defaultBlockState(), 1)
			.add(Blocks.AIR.defaultBlockState(), 3)
			.build()));

		DryingRackMarkerHandler armorRack = new DryingRackMarkerHandler(SimpleStateProvider.simple(TFBlocks.CANOPY_DRYING_RACK.value()), TFLootTables.CAMP_ARMOR_RACK);

		DryingRackMarkerHandler birchDryingRack = new DryingRackMarkerHandler(SimpleStateProvider.simple(TFBlocks.BIRCH_DRYING_RACK.value()), TFLootTables.CAMP_DRYING_RACK);

		PaintingMarkerHandler painting = new PaintingMarkerHandler(PaintingVariantTags.PLACEABLE);

		LootMarkerHandler tentPot = new LootMarkerHandler(BlockStateProvider.simple(Blocks.DECORATED_POT), TFLootTables.CAMP_POT);

		Map<String, Holder<TemplateMarkerHandler>> keyedHandlers = Map.of(
			"twilight_oak_slab", Holder.direct(campfireSeat),
			"camp_armor_rack", Holder.direct(armorRack),
			"birch_drying_rack", Holder.direct(birchDryingRack),
			"painting", Holder.direct(painting),
			"tent_pot",  Holder.direct(tentPot)
		);
		context.register(CAMP_MARKER_HANDLERS, TemplateMarkerHandlerList.of(
			new RotationMarkerHandler(Holder.direct(new SwitchMarkerHandler(keyedHandlers)))
		));
	}

}

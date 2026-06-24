package twilightforest.compat.curios;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;
import top.theillusivec4.curios.api.event.DropRulesEvent;
import top.theillusivec4.curios.api.type.capability.ICurio;
import twilightforest.client.model.TFModelLayers;
import twilightforest.compat.curios.model.CharmOfLifeNecklaceModel;
import twilightforest.compat.curios.renderer.CharmOfKeepingRenderer;
import twilightforest.compat.curios.renderer.CharmOfLifeNecklaceRenderer;
import twilightforest.compat.curios.renderer.CurioHeadRenderer;
import twilightforest.events.CharmEvents;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;
import twilightforest.network.CreateMovingCicadaSoundPacket;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.function.Predicate;

public class CuriosCompat {

	public static void registerCuriosCapabilities(RegisterCapabilitiesEvent event) {
		event.registerItem(CuriosCapability.ITEM, (stack, context) -> new ICurio() {
				@Override
				public ItemStack getStack() {
					return stack;
				}

				@Nonnull
				@Override
				public SoundInfo getEquipSound(SlotContext slotContext) {
					return new SoundInfo(SoundEvents.ARMOR_EQUIP_GENERIC.value(), 1.0F, 1.0F);
				}

				@Override
				public void onEquip(SlotContext context, ItemStack prevStack) {
					//check that we don't have a cicada already on our head before trying to start the sound
					if (!context.entity().getItemBySlot(EquipmentSlot.HEAD).is(TFBlocks.CICADA.get().asItem())) {
						if (stack.is(TFBlocks.CICADA.get().asItem()) && !context.entity().level().isClientSide()) {
							PacketDistributor.sendToPlayersTrackingEntityAndSelf(context.entity(), new CreateMovingCicadaSoundPacket(context.entity().getId()));
						}
					}
				}

				@Override
				public boolean canEquipFromUse(SlotContext slotContext) {
					return true;
				}
			},
			TFItems.CHARM_OF_KEEPING_1, TFItems.CHARM_OF_KEEPING_2, TFItems.CHARM_OF_KEEPING_3, TFItems.CHARM_OF_LIFE_1, TFItems.CHARM_OF_LIFE_2,
			TFItems.NAGA_TROPHY, TFItems.LICH_TROPHY, TFItems.MINOSHROOM_TROPHY, TFItems.HYDRA_TROPHY, TFItems.KNIGHT_PHANTOM_TROPHY,
			TFItems.UR_GHAST_TROPHY, TFItems.ALPHA_YETI_TROPHY, TFItems.SNOW_QUEEN_TROPHY, TFItems.QUEST_RAM_TROPHY,
			TFBlocks.CICADA, TFBlocks.FIREFLY, TFBlocks.MOONWORM, TFItems.SKELETON_SKULL_CANDLE, TFItems.WITHER_SKELETON_SKULL_CANDLE,
			TFItems.ZOMBIE_SKULL_CANDLE, TFItems.CREEPER_SKULL_CANDLE, TFItems.PLAYER_SKULL_CANDLE, TFItems.PIGLIN_SKULL_CANDLE
		);
	}

	//if we have any curios and die with a charm of keeping on us, keep our curios instead of dropping them
	public static void keepCurios(DropRulesEvent event) {
		if (event.getEntity() instanceof Player player) {
			CompoundTag playerData = CharmEvents.getPlayerData(player);
			if (!player.level().isClientSide() && playerData.contains(CharmEvents.CONSUMED_CHARM_TAG) && playerData.contains(CharmEvents.CHARM_INV_TAG) && !playerData.getList(CharmEvents.CHARM_INV_TAG, 10).isEmpty()) {
				//Keep all Curios items
				CuriosApi.getCuriosInventory(player).ifPresent(modifiable -> {
					for (int i = 0; i < modifiable.getSlots(); ++i) {
						int finalI = i;
						event.addOverride(stack -> stack == modifiable.getEquippedCurios().getStackInSlot(finalI), ICurio.DropRule.ALWAYS_KEEP);
					}
				});
			}
		}
	}

	public static void registerCurioLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(TFModelLayers.CHARM_OF_LIFE, CharmOfLifeNecklaceModel::create);
	}

	public static void registerCurioRenderers(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			CuriosRendererRegistry.register(TFItems.CHARM_OF_LIFE_1.get(), () -> new CharmOfLifeNecklaceRenderer(FastColor.ARGB32.colorFromFloat(1.0F, 1.0F, 0.5F, 0.5F)));
			CuriosRendererRegistry.register(TFItems.CHARM_OF_LIFE_2.get(), () -> new CharmOfLifeNecklaceRenderer(FastColor.ARGB32.colorFromFloat(1.0F, 1.0F, 0.9F, 0.0F)));
			CuriosRendererRegistry.register(TFItems.CHARM_OF_KEEPING_1.get(), CharmOfKeepingRenderer::new);
			CuriosRendererRegistry.register(TFItems.CHARM_OF_KEEPING_2.get(), CharmOfKeepingRenderer::new);
			CuriosRendererRegistry.register(TFItems.CHARM_OF_KEEPING_3.get(), CharmOfKeepingRenderer::new);

			CuriosRendererRegistry.register(TFItems.NAGA_TROPHY.get(), CurioHeadRenderer::new);
			CuriosRendererRegistry.register(TFItems.LICH_TROPHY.get(), CurioHeadRenderer::new);
			CuriosRendererRegistry.register(TFItems.MINOSHROOM_TROPHY.get(), CurioHeadRenderer::new);
			CuriosRendererRegistry.register(TFItems.HYDRA_TROPHY.get(), CurioHeadRenderer::new);
			CuriosRendererRegistry.register(TFItems.KNIGHT_PHANTOM_TROPHY.get(), CurioHeadRenderer::new);
			CuriosRendererRegistry.register(TFItems.UR_GHAST_TROPHY.get(), CurioHeadRenderer::new);
			CuriosRendererRegistry.register(TFItems.ALPHA_YETI_TROPHY.get(), CurioHeadRenderer::new);
			CuriosRendererRegistry.register(TFItems.SNOW_QUEEN_TROPHY.get(), CurioHeadRenderer::new);
			CuriosRendererRegistry.register(TFItems.QUEST_RAM_TROPHY.get(), CurioHeadRenderer::new);

			CuriosRendererRegistry.register(TFBlocks.CICADA.get().asItem(), CurioHeadRenderer::new);
			CuriosRendererRegistry.register(TFBlocks.FIREFLY.get().asItem(), CurioHeadRenderer::new);
			CuriosRendererRegistry.register(TFBlocks.MOONWORM.get().asItem(), CurioHeadRenderer::new);

			CuriosRendererRegistry.register(TFItems.CREEPER_SKULL_CANDLE.get(), CurioHeadRenderer::new);
			CuriosRendererRegistry.register(TFItems.PIGLIN_SKULL_CANDLE.get(), CurioHeadRenderer::new);
			CuriosRendererRegistry.register(TFItems.PLAYER_SKULL_CANDLE.get(), CurioHeadRenderer::new);
			CuriosRendererRegistry.register(TFItems.SKELETON_SKULL_CANDLE.get(), CurioHeadRenderer::new);
			CuriosRendererRegistry.register(TFItems.WITHER_SKELETON_SKULL_CANDLE.get(), CurioHeadRenderer::new);
			CuriosRendererRegistry.register(TFItems.ZOMBIE_SKULL_CANDLE.get(), CurioHeadRenderer::new);
		});
	}

	public static boolean isCurioEquipped(LivingEntity entity, Predicate<ItemStack> stackPredicate) {
		return CuriosApi.getCuriosInventory(entity).flatMap(handler -> handler.findFirstCurio(stackPredicate)).isPresent();
	}

	public static boolean isCurioEquippedAndVisible(LivingEntity entity, Predicate<ItemStack> stackPredicate) {
		Optional<SlotResult> slot = CuriosApi.getCuriosInventory(entity).flatMap(handler -> handler.findFirstCurio(stackPredicate));
		return slot.isPresent() && slot.get().slotContext() != null && slot.get().slotContext().visible();
	}

	public static boolean findAndConsumeCurio(Item item, Player player) {
		Optional<SlotResult> slot = CuriosApi.getCuriosInventory(player).flatMap(handler -> handler.findFirstCurio(item));
		if (slot.isPresent()) {
			CharmEvents.getPlayerData(player).put(CharmEvents.CONSUMED_CHARM_TAG, slot.get().stack().save(player.registryAccess()));
			slot.get().stack().shrink(1);
			return true;
		}
		return false;
	}
}

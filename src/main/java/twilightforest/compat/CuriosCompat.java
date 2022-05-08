package twilightforest.compat;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;
import top.theillusivec4.curios.api.event.DropRulesEvent;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.common.capability.CurioItemCapability;
import twilightforest.TFEventListener;
import twilightforest.block.TFBlocks;
import twilightforest.compat.curios.CharmOfKeepingRenderer;
import twilightforest.compat.curios.CharmOfLife1NecklaceRenderer;
import twilightforest.compat.curios.CharmOfLife2NecklaceRenderer;
import twilightforest.compat.curios.CurioHeadRenderer;
import twilightforest.item.TFItems;

import javax.annotation.Nonnull;

public class CuriosCompat extends TFCompat {

	public CuriosCompat() {
		super(CuriosApi.MODID);
	}

	@Override
	protected boolean preInit() {
		return true;
	}

	@Override
	protected void init(FMLCommonSetupEvent event) {
	}

	@Override
	protected void postInit() {
	}

	@Override
	protected void handleIMCs() {
		InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.CHARM.getMessageBuilder().build());
		InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.HEAD.getMessageBuilder().build());
	}

	@Override
	protected void initItems(RegistryEvent.Register<Item> evt) {
	}

	public static ICapabilityProvider setupCuriosCapability(ItemStack stack) {
		return CurioItemCapability.createProvider(new ICurio() {
					@Override
					public ItemStack getStack() {
						return stack;
					}

					@Nonnull
					@Override
					public SoundInfo getEquipSound(top.theillusivec4.curios.api.SlotContext slotContext) {
						return new SoundInfo(SoundEvents.ARMOR_EQUIP_GENERIC, 1.0F, 1.0F);
					}

					@Override
					public boolean canEquipFromUse(top.theillusivec4.curios.api.SlotContext slotContext) {
						return true;
					}
				});
	}

	//if we have any curios and die with a charm of keeping on us, keep our curios instead of dropping them
	public static void keepCurios(DropRulesEvent event) {
		if (event.getEntityLiving() instanceof Player player) {
			CompoundTag playerData = TFEventListener.getPlayerData(player);
			if (!player.level.isClientSide() && playerData.contains(TFEventListener.CHARM_INV_TAG) && !playerData.getList(TFEventListener.CHARM_INV_TAG, 10).isEmpty()) {
				//Keep all Curios items
				CuriosApi.getCuriosHelper().getEquippedCurios(player).ifPresent(modifiable -> {
					for (int i = 0; i < modifiable.getSlots(); ++i) {
						int finalI = i;
						event.addOverride(stack -> stack == modifiable.getStackInSlot(finalI), ICurio.DropRule.ALWAYS_KEEP);
					}
				});
			}
		}
	}

	public static void registerCurioRenderers() {
		CuriosRendererRegistry.register(TFItems.CHARM_OF_LIFE_1.get(), CharmOfLife1NecklaceRenderer::new);
		CuriosRendererRegistry.register(TFItems.CHARM_OF_LIFE_2.get(), CharmOfLife2NecklaceRenderer::new);
		CuriosRendererRegistry.register(TFItems.CHARM_OF_KEEPING_1.get(), CharmOfKeepingRenderer::new);
		CuriosRendererRegistry.register(TFItems.CHARM_OF_KEEPING_2.get(), CharmOfKeepingRenderer::new);
		CuriosRendererRegistry.register(TFItems.CHARM_OF_KEEPING_3.get(), CharmOfKeepingRenderer::new);

		CuriosRendererRegistry.register(TFBlocks.NAGA_TROPHY.get().asItem(), CurioHeadRenderer::new);
		CuriosRendererRegistry.register(TFBlocks.LICH_TROPHY.get().asItem(), CurioHeadRenderer::new);
		CuriosRendererRegistry.register(TFBlocks.MINOSHROOM_TROPHY.get().asItem(), CurioHeadRenderer::new);
		CuriosRendererRegistry.register(TFBlocks.HYDRA_TROPHY.get().asItem(), CurioHeadRenderer::new);
		CuriosRendererRegistry.register(TFBlocks.KNIGHT_PHANTOM_TROPHY.get().asItem(), CurioHeadRenderer::new);
		CuriosRendererRegistry.register(TFBlocks.UR_GHAST_TROPHY.get().asItem(), CurioHeadRenderer::new);
		CuriosRendererRegistry.register(TFBlocks.ALPHA_YETI_TROPHY.get().asItem(), CurioHeadRenderer::new);
		CuriosRendererRegistry.register(TFBlocks.SNOW_QUEEN_TROPHY.get().asItem(), CurioHeadRenderer::new);
		CuriosRendererRegistry.register(TFBlocks.QUEST_RAM_TROPHY.get().asItem(), CurioHeadRenderer::new);

		CuriosRendererRegistry.register(TFBlocks.CICADA.get().asItem(), CurioHeadRenderer::new);
		CuriosRendererRegistry.register(TFBlocks.FIREFLY.get().asItem(), CurioHeadRenderer::new);
		CuriosRendererRegistry.register(TFBlocks.MOONWORM.get().asItem(), CurioHeadRenderer::new);
	}
}

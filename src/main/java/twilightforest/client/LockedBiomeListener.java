package twilightforest.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import twilightforest.TFConfig;
import twilightforest.TwilightForestMod;
import twilightforest.block.TFBlocks;
import twilightforest.item.TFItems;
import twilightforest.world.registration.TFGenerationSettings;
import twilightforest.world.registration.biomes.BiomeKeys;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE, modid = TwilightForestMod.ID)
public class LockedBiomeListener {

	private static boolean shownToast = false;
	private static int timeUntilToast = 60;

	@SubscribeEvent
	public static void clientTick(TickEvent.WorldTickEvent event) {
		Player player = Minecraft.getInstance().player;
		if (player == null || !(player.level instanceof ClientLevel world))
			return;

		//attempt to send a biome locked toast if our player is in a locked biome, only every 5 ticks
		if(world.isClientSide && event.phase == TickEvent.Phase.END && player.tickCount % 5 == 0
				&& TFGenerationSettings.isProgressionEnforced(world)
				&& !player.isCreative() && !player.isSpectator() && !TFConfig.CLIENT_CONFIG.disableLockedBiomeToasts.get()) {
			if (!TFGenerationSettings.isBiomeSafeFor(world.getBiome(player.blockPosition()), player)) {
				timeUntilToast--;
				ItemStack item;
				if (world.getBiome(player.blockPosition()).equals(world.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).get(BiomeKeys.DARK_FOREST))) {
					item = new ItemStack(TFBlocks.LICH_TOWER_MINIATURE_STRUCTURE.get());
				} else if (world.getBiome(player.blockPosition()).equals(world.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).get(BiomeKeys.DARK_FOREST_CENTER))) {
					item = new ItemStack(TFBlocks.KNIGHT_PHANTOM_TROPHY.get().asItem());
				} else if (world.getBiome(player.blockPosition()).equals(world.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).get(BiomeKeys.SNOWY_FOREST))) {
					item = new ItemStack(TFBlocks.LICH_TOWER_MINIATURE_STRUCTURE.get());
				} else if (world.getBiome(player.blockPosition()).equals(world.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).get(BiomeKeys.GLACIER))) {
					item = new ItemStack(TFItems.ALPHA_YETI_FUR.get());
				} else if (world.getBiome(player.blockPosition()).equals(world.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).get(BiomeKeys.SWAMP))) {
					item = new ItemStack(TFBlocks.LICH_TOWER_MINIATURE_STRUCTURE.get());
				} else if (world.getBiome(player.blockPosition()).equals(world.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).get(BiomeKeys.FIRE_SWAMP))) {
					item = new ItemStack(TFItems.MEEF_STROGANOFF.get());
				} else if (world.getBiome(player.blockPosition()).equals(world.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).get(BiomeKeys.HIGHLANDS))) {
					item = new ItemStack(TFBlocks.UBEROUS_SOIL.get());
				} else if (world.getBiome(player.blockPosition()).equals(world.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).get(BiomeKeys.THORNLANDS))) {
					item = new ItemStack(TFItems.LAMP_OF_CINDERS.get());
				} else if (world.getBiome(player.blockPosition()).equals(world.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).get(BiomeKeys.FINAL_PLATEAU))) {
					item = new ItemStack(TFItems.LAMP_OF_CINDERS.get());
				} else {
					item = new ItemStack(TFBlocks.TWILIGHT_PORTAL_MINIATURE_STRUCTURE.get());
				}
				if(!shownToast && timeUntilToast <= 0) {
					Minecraft.getInstance().getToasts().addToast(new LockedBiomeToast(item));
					shownToast = true;
				}
			} else {
				if(shownToast) {
					timeUntilToast = 60;
					shownToast = false;
				}
			}
		}
	}
}

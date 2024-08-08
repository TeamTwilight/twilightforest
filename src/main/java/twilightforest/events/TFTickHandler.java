package twilightforest.events;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.commands.Commands;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import twilightforest.TwilightForestMod;
import twilightforest.block.TFPortalBlock;
import twilightforest.config.TFConfig;
import twilightforest.data.tags.ItemTagGenerator;
import twilightforest.init.TFAdvancements;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFDimension;
import twilightforest.network.MissingAdvancementToastPacket;
import twilightforest.network.StructureProtectionPacket;
import twilightforest.util.Enforcement;
import twilightforest.util.landmarks.LandmarkUtil;
import twilightforest.util.PlayerHelper;
import twilightforest.world.components.structures.util.AdvancementLockedStructure;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@EventBusSubscriber(modid = TwilightForestMod.ID)
public class TFTickHandler {

	@SubscribeEvent
	public static void playerTick(PlayerTickEvent.Post event) {
		Player eventPlayer = event.getEntity();

		if (!(eventPlayer instanceof ServerPlayer player)) return;
		if (!(player.level() instanceof ServerLevel world)) return;

		// check for portal creation, at least if it's not disabled
		if (!TFConfig.disablePortalCreation && player.tickCount % (!TFConfig.checkPortalPlacement ? 100 : 20) == 0) {
			// skip non admin players when the option is on
			if (world.getServer().getProfilePermissions(player.getGameProfile()) >= TFConfig.portalCreationPermission) {
				// reduce range to 4.0 if config is set to admins/owners only
				checkForPortalCreation(player, world, TFConfig.portalCreationPermission >= Commands.LEVEL_ADMINS ? 4.0F : 32.0F);
			}
		}

		// check the player for being in a forbidden progression area, only every 20 ticks
		if (player.tickCount % 20 == 0 && LandmarkUtil.isProgressionEnforced(world) && !player.isCreative() && !player.isSpectator()) {
			Enforcement.enforceBiomeProgression(player, world);
		}

		// check and send nearby forbidden structures, every 100 ticks or so
		if (player.tickCount % 100 == 0 && LandmarkUtil.isProgressionEnforced(world)) {
			if (player.isCreative() || player.isSpectator()) {
				sendAllClearPacket(player);
			} else {
				checkForLockedStructuresSendPacket(player, world);
			}
		}
	}

	private static void sendStructureProtectionPacket(Player player, BoundingBox sbb) {
		if (player instanceof ServerPlayer sp) {
			PacketDistributor.sendToPlayer(sp, new StructureProtectionPacket(Optional.of(sbb)));
		}
	}

	private static void sendAllClearPacket(Player player) {
		if (player instanceof ServerPlayer sp) {
			PacketDistributor.sendToPlayer(sp, new StructureProtectionPacket(Optional.empty()));
		}
	}

	@SuppressWarnings("UnusedReturnValue")
	private static boolean checkForLockedStructuresSendPacket(Player player, ServerLevel world) {
		ChunkPos chunkPlayer = player.chunkPosition();
		return LandmarkUtil.locateNearestLandmarkStart(world, chunkPlayer.x, chunkPlayer.z).map(structureStart -> {
			if (structureStart.getStructure() instanceof AdvancementLockedStructure advancementLockedStructure && !advancementLockedStructure.doesPlayerHaveRequiredAdvancements(player)) {
				sendStructureProtectionPacket(player, structureStart.getBoundingBox());
				return true;
			}

			sendAllClearPacket(player);
			return false;
		}).orElse(false);
	}

	private static void checkForPortalCreation(ServerPlayer player, Level world, float rangeToCheck) {
		if (world.dimension().location().equals(ResourceLocation.parse(TFConfig.originDimension))
			|| TFDimension.isTwilightPortalDestination(world)
			|| TFConfig.allowPortalsInOtherDimensions) {

			List<ItemEntity> itemList = world.getEntitiesOfClass(ItemEntity.class, player.getBoundingBox().inflate(rangeToCheck));
			ItemEntity qualified = null;

			for (ItemEntity entityItem : itemList) {
				if (entityItem.getItem().is(ItemTagGenerator.PORTAL_ACTIVATOR) &&
					TFBlocks.TWILIGHT_PORTAL.get().canFormPortal(world.getBlockState(entityItem.blockPosition())) &&
					Objects.equals(entityItem.getOwner(), player)) {

					qualified = entityItem;
					break;
				}
			}

			if (qualified == null) return;

			if (!player.isCreative() && !player.isSpectator() && TFConfig.getPortalLockingAdvancement(player) != null) {
				AdvancementHolder requirement = PlayerHelper.getAdvancement(player, Objects.requireNonNull(TFConfig.getPortalLockingAdvancement(player)));
				if (requirement != null && !PlayerHelper.doesPlayerHaveRequiredAdvancement(player, requirement)) {
					player.displayClientMessage(TFPortalBlock.PORTAL_UNWORTHY, true);

					if (!TFPortalBlock.isPlayerNotifiedOfRequirement(player)) {
						// .doesPlayerHaveRequiredAdvancement null-checks already, so we can skip null-checking the `requirement`
						DisplayInfo info = requirement.value().display().orElse(null);
						PacketDistributor.sendToPlayer(player, info == null ? new MissingAdvancementToastPacket(Component.translatable("twilightforest.ui.advancement.no_title"), new ItemStack(TFBlocks.TWILIGHT_PORTAL_MINIATURE_STRUCTURE.get())) : new MissingAdvancementToastPacket(info.getTitle(), info.getIcon()));

						TFPortalBlock.playerNotifiedOfRequirement(player);
					}

					return; // Item qualifies, but the player doesn't
				}
			}

			Random rand = new Random();
			for (int i = 0; i < 2; i++) {
				double vx = rand.nextGaussian() * 0.02D;
				double vy = rand.nextGaussian() * 0.02D;
				double vz = rand.nextGaussian() * 0.02D;

				world.addParticle(ParticleTypes.EFFECT, qualified.getX(), qualified.getY() + 0.2, qualified.getZ(), vx, vy, vz);
			}

			if (TFBlocks.TWILIGHT_PORTAL.get().tryToCreatePortal(world, qualified.blockPosition(), qualified, player))
				TFAdvancements.MADE_TF_PORTAL.get().trigger(player);

		}
	}

}

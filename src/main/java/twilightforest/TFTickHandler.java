package twilightforest;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fmllegacy.network.PacketDistributor;
import twilightforest.advancements.TFAdvancements;
import twilightforest.block.TFBlocks;
import twilightforest.block.TFPortalBlock;
import twilightforest.data.ItemTagGenerator;
import twilightforest.item.BrittleFlaskItem;
import twilightforest.network.MissingAdvancementToastPacket;
import twilightforest.network.StructureProtectionPacket;
import twilightforest.network.StructureProtectionClearPacket;
import twilightforest.network.TFPacketHandler;
import twilightforest.util.BoundingBoxUtils;
import twilightforest.util.PlayerHelper;
import twilightforest.util.WorldUtil;
import twilightforest.world.components.chunkgenerators.ChunkGeneratorTwilight;
import twilightforest.world.registration.TFFeature;
import twilightforest.world.registration.TFGenerationSettings;

import java.util.List;
import java.util.Random;

@Mod.EventBusSubscriber(modid = TwilightForestMod.ID)
public class TFTickHandler {

	@SubscribeEvent
	public static void playerTick(TickEvent.PlayerTickEvent event) {
		Player eventPlayer = event.player;

		if (!(eventPlayer instanceof ServerPlayer player)) return;
		if (!(player.level instanceof ServerLevel world)) return;

		// check for portal creation, at least if it's not disabled
		if (!TFConfig.COMMON_CONFIG.disablePortalCreation.get() && event.phase == TickEvent.Phase.END && player.tickCount % (TFConfig.COMMON_CONFIG.checkPortalDestination.get() ? 100 : 20) == 0) {
			// skip non admin players when the option is on
			if (TFConfig.COMMON_CONFIG.adminOnlyPortals.get()) {
				if (world.getServer().getProfilePermissions(player.getGameProfile()) != 0) {
					// reduce range to 4.0 when the option is on
					checkForPortalCreation(player, world, 4.0F);
				}
			} else {
				// normal check, no special options
				checkForPortalCreation(player, world, 32.0F);
			}
		}

		//tick every second for the advancement trigger bit of the flask
		if(event.phase == TickEvent.Phase.END && player.tickCount % 20 == 0) {
			BrittleFlaskItem.ticker();
		}

		// check the player for being in a forbidden progression area, only every 20 ticks
		if (event.phase == TickEvent.Phase.END && player.tickCount % 20 == 0 && TFGenerationSettings.isProgressionEnforced(world) && TFGenerationSettings.usesTwilightChunkGenerator(world) && !player.isCreative() && !player.isSpectator()) {
			TFGenerationSettings.enforceBiomeProgression(player, world);
		}

		// check and send nearby forbidden structures, every 100 ticks or so
		if (event.phase == TickEvent.Phase.END && player.tickCount % 100 == 0 && TFGenerationSettings.isProgressionEnforced(world)) {
			if (TFGenerationSettings.usesTwilightChunkGenerator(world)) {
				if (player.isCreative() || player.isSpectator()) {
					sendAllClearPacket(world, player);
				} else {
					checkForLockedStructuresSendPacket(player, world);
				}
			}
		}
	}

	private static void sendStructureProtectionPacket(Level world, Player player, BoundingBox sbb) {
		if (player instanceof ServerPlayer) {
			TFPacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), new StructureProtectionPacket(sbb));
		}
	}

	private static void sendAllClearPacket(Level world, Player player) {
		if (player instanceof ServerPlayer) {
			TFPacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), new StructureProtectionClearPacket());
		}
	}

	@SuppressWarnings("UnusedReturnValue")
	private static boolean checkForLockedStructuresSendPacket(Player player, Level world) {

		ChunkGeneratorTwilight chunkGenerator = WorldUtil.getChunkGenerator(world);
		if (chunkGenerator == null)
			return false;


		return TFGenerationSettings.locateTFStructureInRange((ServerLevel) world, player.blockPosition(), 100).map(structure -> {
			BoundingBox fullSBB = structure.getBoundingBox();
			Vec3i center = BoundingBoxUtils.getCenter(fullSBB);

			TFFeature nearFeature = TFFeature.getFeatureForRegionPos(center.getX(), center.getZ(), (ServerLevel) world);

			if (!nearFeature.hasProtectionAura || nearFeature.doesPlayerHaveRequiredAdvancements(player)) {
				sendAllClearPacket(world, player);
				return false;
			} else {
				sendStructureProtectionPacket(world, player, fullSBB);
				return true;
			}
		}).orElse(false);
	}

	private static final TranslatableComponent PORTAL_UNWORTHY = new TranslatableComponent(TwilightForestMod.ID + ".ui.portal.unworthy");
	private static void checkForPortalCreation(ServerPlayer player, Level world, float rangeToCheck) {
		if (world.dimension().location().equals(new ResourceLocation(TFConfig.COMMON_CONFIG.originDimension.get()))
				|| world.dimension().location().toString().equals(TFConfig.COMMON_CONFIG.DIMENSION.portalDestinationID.get())
				|| TFConfig.COMMON_CONFIG.allowPortalsInOtherDimensions.get()) {

			List<ItemEntity> itemList = world.getEntitiesOfClass(ItemEntity.class, player.getBoundingBox().inflate(rangeToCheck));
			ItemEntity qualified = null;

			for (ItemEntity entityItem : itemList) {
				if (entityItem.getItem().is(ItemTagGenerator.PORTAL_ACTIVATOR)) {
					qualified = entityItem;
					break;
				}
			}

			if (qualified == null) return;

			if (!player.isCreative() && !player.isSpectator()) {
				Advancement requirement = PlayerHelper.getAdvancement(player, TFConfig.getPortalLockingAdvancement());
				if (requirement != null && !PlayerHelper.doesPlayerHaveRequiredAdvancement(player, requirement)) {
					player.displayClientMessage(PORTAL_UNWORTHY, true);

					if (!TFPortalBlock.isPlayerNotifiedOfRequirement(player)) {
						// .doesPlayerHaveRequiredAdvancement null-checks already, so we can skip null-checking the `requirement`
						DisplayInfo info = requirement.getDisplay();
						TFPacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), info == null ? new MissingAdvancementToastPacket(new TranslatableComponent(".ui.advancement.no_title"), new ItemStack(TFBlocks.TWILIGHT_PORTAL_MINIATURE_STRUCTURE.get())) : new MissingAdvancementToastPacket(info.getTitle(), info.getIcon()));

						TFPortalBlock.playerNotifiedOfRequirement(player);
					}

					return; // Item qualifies, but the player doesn't
				}
			}

			BlockPos pos = new BlockPos(qualified.position().subtract(0, -0.1d, 0)); //TODO Quick fix, find if there's a more performant fix than this
			BlockState state = world.getBlockState(pos);
			if (TFBlocks.TWILIGHT_PORTAL.get().canFormPortal(state)) {
				Random rand = new Random();
				for (int i = 0; i < 2; i++) {
					double vx = rand.nextGaussian() * 0.02D;
					double vy = rand.nextGaussian() * 0.02D;
					double vz = rand.nextGaussian() * 0.02D;

					world.addParticle(ParticleTypes.EFFECT, qualified.getX(), qualified.getY() + 0.2, qualified.getZ(), vx, vy, vz);
				}

				if (TFBlocks.TWILIGHT_PORTAL.get().tryToCreatePortal(world, pos, qualified, player))
					TFAdvancements.MADE_TF_PORTAL.trigger(player);
			}
		}
	}

}

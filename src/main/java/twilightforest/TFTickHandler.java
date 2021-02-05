package twilightforest;

import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;
import twilightforest.advancements.TFAdvancements;
import twilightforest.block.TFBlocks;
import twilightforest.data.ItemTagGenerator;
import twilightforest.network.PacketStructureProtection;
import twilightforest.network.PacketStructureProtectionClear;
import twilightforest.network.TFPacketHandler;
import twilightforest.world.ChunkGeneratorTwilightBase;
import twilightforest.world.TFGenerationSettings;

import java.util.List;
import java.util.Random;

@Mod.EventBusSubscriber(modid = TwilightForestMod.ID)
public class TFTickHandler {

	@SubscribeEvent
	public static void playerTick(TickEvent.PlayerTickEvent event) {
		PlayerEntity player = event.player;

		if (!(player.world instanceof ServerWorld))
			return;

		ServerWorld world = (ServerWorld) player.world;

		// check for portal creation, at least if it's not disabled
		if (!world.isRemote && !TFConfig.COMMON_CONFIG.disablePortalCreation.get() && event.phase == TickEvent.Phase.END && player.ticksExisted % (TFConfig.COMMON_CONFIG.checkPortalDestination.get() ? 100 : 20) == 0) {
			// skip non admin players when the option is on
			if (TFConfig.COMMON_CONFIG.adminOnlyPortals.get()) {
				if (world.getServer().getPermissionLevel(player.getGameProfile()) != 0) {
					// reduce range to 4.0 when the option is on
					checkForPortalCreation(player, world, 4.0F);
				}
			} else {
				// normal check, no special options
				checkForPortalCreation(player, world, 32.0F);
			}
		}

		// check the player for being in a forbidden progression area, only every 20 ticks
		if (!world.isRemote && event.phase == TickEvent.Phase.END && player.ticksExisted % 20 == 0
				&& TFGenerationSettings.isProgressionEnforced(world)
				&& TFGenerationSettings.isTwilightChunk(world)
				&& !player.isCreative() && !player.isSpectator()) {

			checkBiomeForProgression(player, world);
		}

		// check and send nearby forbidden structures, every 100 ticks or so
		if (!world.isRemote && event.phase == TickEvent.Phase.END && player.ticksExisted % 100 == 0 && TFGenerationSettings.isProgressionEnforced(world)) {
			if (TFGenerationSettings.isTwilightChunk(world)) {
				if (player.isCreative() || player.isSpectator()) {
					sendAllClearPacket(world, player);
				} else {
					checkForLockedStructuresSendPacket(player, world);
				}
			}
		}
	}

	private static void sendStructureProtectionPacket(World world, PlayerEntity player, MutableBoundingBox sbb) {
		if (player instanceof ServerPlayerEntity) {
			TFPacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), new PacketStructureProtection(sbb));
		}
	}

	private static void sendAllClearPacket(World world, PlayerEntity player) {
		if (player instanceof ServerPlayerEntity) {
			TFPacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), new PacketStructureProtectionClear());
		}
	}

	@SuppressWarnings("UnusedReturnValue")
	private static boolean checkForLockedStructuresSendPacket(PlayerEntity player, World world) {

		ChunkGeneratorTwilightBase chunkGenerator = TFGenerationSettings.getChunkGenerator(world);
		if (chunkGenerator == null) return false;

		int px = MathHelper.floor(player.getPosX());
		int pz = MathHelper.floor(player.getPosZ());

//		MutableBoundingBox fullSBB = chunkGenerator.getFullSBBNear(px, pz, 100);
//		if (fullSBB != null) {
//
//			Vec3i center = StructureBoundingBoxUtils.getCenter(fullSBB);
//
//			TFFeature nearFeature = TFFeature.getFeatureForRegionPos(center.getPosX()(), center.getPosZ()(), world);
//
//			if (!nearFeature.hasProtectionAura || nearFeature.doesPlayerHaveRequiredAdvancements(player)) {
//				sendAllClearPacket(world, player);
//				return false;
//			} else {
//				sendStructureProtectionPacket(world, player, fullSBB);
//				return true;
//			}
//		}
		return false;
	}

	private static void checkForPortalCreation(PlayerEntity player, World world, float rangeToCheck) {
		if (world.getDimensionKey().getLocation().equals(new ResourceLocation(TFConfig.COMMON_CONFIG.originDimension.get()))
				|| world.getDimensionKey().getLocation().toString().equals(TFConfig.COMMON_CONFIG.DIMENSION.twilightForestID.get())
				|| TFConfig.COMMON_CONFIG.allowPortalsInOtherDimensions.get()) {

			List<ItemEntity> itemList = world.getEntitiesWithinAABB(ItemEntity.class, player.getBoundingBox().grow(rangeToCheck));

			for (ItemEntity entityItem : itemList) {
				if (ItemTagGenerator.PORTAL_ACTIVATOR.contains(entityItem.getItem().getItem())) {
					BlockPos pos = new BlockPos(entityItem.getPositionVec().subtract(0, -0.1d, 0)); //TODO Quick fix, find if there's a more performant fix than this
					BlockState state = world.getBlockState(pos);
					if (TFBlocks.twilight_portal.get().canFormPortal(state)) {
						Random rand = new Random();
						for (int i = 0; i < 2; i++) {
							double vx = rand.nextGaussian() * 0.02D;
							double vy = rand.nextGaussian() * 0.02D;
							double vz = rand.nextGaussian() * 0.02D;

							world.addParticle(ParticleTypes.EFFECT, entityItem.getPosX(), entityItem.getPosY() + 0.2, entityItem.getPosZ(), vx, vy, vz);
						}

						if (TFBlocks.twilight_portal.get().tryToCreatePortal(world, pos, entityItem, player)) {
							TFAdvancements.MADE_TF_PORTAL.trigger((ServerPlayerEntity) player);
							return;
						}
					}
				}
			}
		}
	}

	/**
	 * Check what biome the player is in, and see if current progression allows that biome.  If not, take appropriate action
	 */
	private static void checkBiomeForProgression(PlayerEntity player, World world) {
		/* FIXME
		Biome currentBiome = world.getBiome(player.getPosition());
		if (currentBiome instanceof TFBiomeBase) {
			TFBiomeBase tfBiome = (TFBiomeBase) currentBiome;
			if (!tfBiome.doesPlayerHaveRequiredAdvancements(player)) {
				tfBiome.enforceProgression(player, world);
			}
		}*/
	}
}

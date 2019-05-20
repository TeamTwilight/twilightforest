package twilightforest;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import twilightforest.advancements.TFAdvancements;
import twilightforest.biomes.TFBiomeBase;
import twilightforest.block.BlockTFPortal;
import twilightforest.block.TFBlocks;
import twilightforest.network.PacketStructureProtection;
import twilightforest.network.PacketStructureProtectionClear;
import twilightforest.network.TFPacketHandler;
import twilightforest.util.StructureBoundingBoxUtils;
import twilightforest.world.ChunkGeneratorTFBase;
import twilightforest.world.TFWorld;
import twilightforest.world.WorldProviderTwilightForest;

import java.util.List;
import java.util.Random;

@Mod.EventBusSubscriber(modid = TwilightForestMod.ID)
public class TFTickHandler {

	@SubscribeEvent
	public static void playerTick(PlayerTickEvent event) {

		EntityPlayer player = event.player;
		World world = player.world;

		// check for portal creation, at least if it's not disabled
		if (!world.isRemote && !TFConfig.disablePortalCreation && event.phase == TickEvent.Phase.END && player.ticksExisted % (TFConfig.checkPortalDestination ? 100 : 20) == 0) {
			// skip non admin players when the option is on
			if (TFConfig.adminOnlyPortals) {
				if (FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getOppedPlayers().getPermissionLevel(player.getGameProfile()) != 0) {
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
				&& world.getGameRules().getBoolean(TwilightForestMod.ENFORCED_PROGRESSION_RULE)
				&& world.provider instanceof WorldProviderTwilightForest
				&& !player.isCreative() && !player.isSpectator()) {
			checkBiomeForProgression(player, world);
		}

		// check and send nearby forbidden structures, every 100 ticks or so
		if (!world.isRemote && event.phase == TickEvent.Phase.END && player.ticksExisted % 100 == 0 && world.getGameRules().getBoolean(TwilightForestMod.ENFORCED_PROGRESSION_RULE)) {
			if (world.provider instanceof WorldProviderTwilightForest) {
				if (player.isCreative() || player.isSpectator()) {
					sendAllClearPacket(world, player);
				} else {
					checkForLockedStructuresSendPacket(player, world);
				}
			}
		}
	}

	private static void sendStructureProtectionPacket(World world, EntityPlayer player, StructureBoundingBox sbb) {
		IMessage message = new PacketStructureProtection(sbb);
		if (player instanceof EntityPlayerMP) {
			TFPacketHandler.CHANNEL.sendTo(message, (EntityPlayerMP) player);
		}
	}

	private static void sendAllClearPacket(World world, EntityPlayer player) {
		IMessage message = new PacketStructureProtectionClear();
		if (player instanceof EntityPlayerMP) {
			TFPacketHandler.CHANNEL.sendTo(message, (EntityPlayerMP) player);
		}
	}

	@SuppressWarnings({"UnusedReturnValue", "ConstantConditions"})
	private static boolean checkForLockedStructuresSendPacket(EntityPlayer player, World world) {
		IChunkGenerator uncheckedChunkGenerator = TFWorld.getChunkGenerator(world);
		if (!(uncheckedChunkGenerator instanceof ChunkGeneratorTFBase)) return false;

		ChunkGeneratorTFBase chunkGenerator = (ChunkGeneratorTFBase) uncheckedChunkGenerator;

		int px = MathHelper.floor(player.posX);
		int pz = MathHelper.floor(player.posZ);

		if (chunkGenerator != null && chunkGenerator.isBlockNearFullStructure(px, pz, 100)) {

			StructureBoundingBox fullSBB = chunkGenerator.getFullSBBNear(px, pz, 100);

			Vec3i center = StructureBoundingBoxUtils.getCenter(fullSBB);

			TFFeature nearFeature = TFFeature.getFeatureForRegion(center.getX() >> 4, center.getZ() >> 4, world);

			if (!nearFeature.hasProtectionAura || nearFeature.doesPlayerHaveRequiredAdvancements(player)) {
				sendAllClearPacket(world, player);
				return false;
			} else {
				sendStructureProtectionPacket(world, player, fullSBB);
				return true;
			}
		}
		return false;
	}

	private static void checkForPortalCreation(EntityPlayer player, World world, float rangeToCheck) {
		if (world.provider.getDimension() == TFConfig.originDimension
				|| world.provider.getDimension() == TFConfig.dimension.dimensionID
				|| TFConfig.allowPortalsInOtherDimensions) {

			List<EntityItem> itemList = world.getEntitiesWithinAABB(EntityItem.class, player.getEntityBoundingBox().grow(rangeToCheck));

			for (EntityItem entityItem : itemList) {
				if (TFConfig.portalIngredient.apply(entityItem.getItem())) {
					IBlockState state = world.getBlockState(entityItem.getPosition());
					if (state.getBlock() == Blocks.WATER || state == TFBlocks.twilight_portal.getDefaultState().withProperty(BlockTFPortal.DISALLOW_RETURN, true)) {
						Random rand = new Random();
						for (int i = 0; i < 2; i++) {
							double vx = rand.nextGaussian() * 0.02D;
							double vy = rand.nextGaussian() * 0.02D;
							double vz = rand.nextGaussian() * 0.02D;

							world.spawnParticle(EnumParticleTypes.SPELL, entityItem.posX, entityItem.posY + 0.2, entityItem.posZ, vx, vy, vz);
						}

						if (TFBlocks.twilight_portal.tryToCreatePortal(world, entityItem.getPosition(), entityItem, player)) {
							TFAdvancements.MADE_TF_PORTAL.trigger((EntityPlayerMP) player);
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
	private static void checkBiomeForProgression(EntityPlayer player, World world) {
		Biome currentBiome = world.getBiome(new BlockPos(player));

		if (currentBiome instanceof TFBiomeBase) {
			TFBiomeBase tfBiome = (TFBiomeBase) currentBiome;

			boolean dangerousBiome = !tfBiome.doesPlayerHaveRequiredAchievement(player);
			if (dangerousBiome) {
				tfBiome.enforceProgression(player, world);
			}
		}
	}
}

package twilightforest;

import java.util.List;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import twilightforest.biomes.TFBiomeBase;
import twilightforest.block.BlockTFPortal;
import twilightforest.block.TFBlocks;
import twilightforest.network.PacketStructureProtection;
import twilightforest.network.PacketStructureProtectionClear;
import twilightforest.world.WorldProviderTwilightForest;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

/**
 * This class listens for ticks in the world.  If the player is near a diamond in the water, this class attempts to open a portal.
 * 
 * @author Ben
 *
 */
public class TFTickHandler 
{
	public ResourceLocation portalItem = null;

	@SubscribeEvent
	public void playerTick(PlayerTickEvent event) {
		EntityPlayer player = event.player;
		World world = player.world;

		// check for portal creation, at least if it's not disabled
		if (!TwilightForestMod.disablePortalCreation && event.phase == TickEvent.Phase.END && !world.isRemote && world.getWorldTime() % 20 == 0) {
			// skip non admin players when the option is on
			if (TwilightForestMod.adminOnlyPortals) {
				if (FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getOppedPlayers().getEntry(player.getGameProfile()) != null) {
					// reduce range to 4.0 when the option is on
					checkForPortalCreation(player, world, 4.0F);
				}
			} else {
				// normal check, no special options
				checkForPortalCreation(player, world, 32.0F);
			}
		}

		// check the player for being in a forbidden progression area, only every 20 ticks
		if (!world.isRemote && event.phase == TickEvent.Phase.END && world.getWorldTime() % 20 == 0 && world.getGameRules().getBoolean(TwilightForestMod.ENFORCED_PROGRESSION_RULE)) {
			if (world.provider instanceof WorldProviderTwilightForest && !player.capabilities.isCreativeMode) {
				checkBiomeForProgression(player, world);
			}
		}
		
		// check and send nearby forbidden structures, every 100 ticks or so
		if (!world.isRemote && event.phase == TickEvent.Phase.END && world.getWorldTime() % 100 == 0 && world.getGameRules().getBoolean(TwilightForestMod.ENFORCED_PROGRESSION_RULE)) {
			if (world.provider instanceof WorldProviderTwilightForest) {
				if (!player.capabilities.isCreativeMode) {
					checkForLockedStructuresSendPacket(player, world);
				} else {
					sendAllClearPacket(world, player);
				}
			}
		}
	}
	
	private void sendStructureProtectionPacket(World world, EntityPlayer player, StructureBoundingBox sbb) {
		// send packet
		IMessage message = new PacketStructureProtection(sbb);
		if (player instanceof EntityPlayerMP) {
			TFPacketHandler.CHANNEL.sendTo(message, (EntityPlayerMP) player);
			//System.out.println("Sent structure protection");
		} else {
			//System.err.println("Can't sent packet to player, not an EntityPlayerMP");
		}
	}
	
	private void sendAllClearPacket(World world, EntityPlayer player) {
		IMessage message = new PacketStructureProtectionClear();
		if (player instanceof EntityPlayerMP) {
			TFPacketHandler.CHANNEL.sendTo(message, (EntityPlayerMP) player);
			//System.out.println("Sent structure all clear");
		} else {
			//System.err.println("Can't sent packet to player, not an EntityPlayerMP");
		}
	}

	private boolean checkForLockedStructuresSendPacket(EntityPlayer player, World world) {
//FIXME: AtomicBlom: Disabled for Structures
		return false;
/*
		ChunkGeneratorTwilightForest chunkProvider = ((WorldProviderTwilightForest)world.provider).getChunkProvider();
		
		int px = MathHelper.floor(player.posX);
		int py = MathHelper.floor(player.posY);
		int pz = MathHelper.floor(player.posZ);
		
		if (chunkProvider != null && chunkProvider.isBlockNearFullStructure(px, pz, 100)) {
			
			StructureBoundingBox fullSBB = chunkProvider.getFullSBBNear(px, pz, 100);
			
			TFFeature nearFeature = TFFeature.getFeatureForRegion(fullSBB.getCenter().getX() >> 4, fullSBB.getCenter().getZ() >> 4, world);
			
			if (!nearFeature.hasProtectionAura || nearFeature.doesPlayerHaveRequiredAchievement(player)) {
				sendAllClearPacket(world, player);
				return false;
			} else {
				sendStructureProtectionPacket(world, player, fullSBB);
				return true;
			}

			
		} else {
			return false;
		}
*/
	}

	private void checkForPortalCreation(EntityPlayer player, World world, float rangeToCheck) {
		if ((world.provider.getDimension() == 0 || world.provider.getDimension() == TwilightForestMod.dimensionID
				|| TwilightForestMod.allowPortalsInOtherDimensions))
		{
			List<EntityItem> itemList = world.getEntitiesWithinAABB(EntityItem.class, player.getEntityBoundingBox().expand(rangeToCheck, rangeToCheck, rangeToCheck));
			
			for (EntityItem entityItem : itemList)
			{
				if (portalItem.equals(entityItem.getEntityItem().getItem().getRegistryName()) && world.isMaterialInBB(entityItem.getEntityBoundingBox(), Material.WATER))
				{
					// make sparkles in the area
					Random rand = new Random();
					for (int k = 0; k < 2; k++)
					{
						double d = rand.nextGaussian() * 0.02D;
						double d1 = rand.nextGaussian() * 0.02D;
						double d2 = rand.nextGaussian() * 0.02D;
	
						world.spawnParticle(EnumParticleTypes.SPELL, entityItem.posX, entityItem.posY + 0.2, entityItem.posZ, d, d1, d2);
					}
	
					// try to make a portal
					if (((BlockTFPortal)TFBlocks.portal).tryToCreatePortal(world, new BlockPos(entityItem))) {
						player.addStat(TFAchievementPage.twilightPortal);
					}
				}
			}
		}
	}

	/**
	 * Check what biome the player is in, and see if current progression allows that biome.  If not, take appropriate action
	 */
	private void checkBiomeForProgression(EntityPlayer player, World world) {
		Biome currentBiome = world.getBiome(new BlockPos(player));
		
		if (currentBiome instanceof TFBiomeBase) {
			TFBiomeBase tfBiome = (TFBiomeBase)currentBiome;
			
			boolean dangerousBiome = !tfBiome.doesPlayerHaveRequiredAchievement(player);
			if (dangerousBiome) {
				tfBiome.enforceProgession(player, world);
			}
		}
	}


}

package twilightforest.events;

import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.util.BlockSnapshot;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import twilightforest.TwilightForestMod;
import twilightforest.data.tags.BlockTagGenerator;
import twilightforest.entity.boss.Hydra;
import twilightforest.entity.monster.Kobold;
import twilightforest.init.TFStructures;
import twilightforest.network.AreaProtectionPacket;
import twilightforest.util.landmarks.LandmarkUtil;
import twilightforest.util.landmarks.LegacyLandmarkPlacements;
import twilightforest.util.WorldUtil;
import twilightforest.world.components.structures.TFStructureComponent;
import twilightforest.world.components.structures.util.ProgressionStructure;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * A class to store events relating to progression
 */
@EventBusSubscriber(modid = TwilightForestMod.ID)
public class ProgressionEvents {
	/**
	 * Check if the player is trying to break a block in a structure that's considered unbreakable for progression reasons
	 * FIXME If there is a way to check on the client, it would be ideal to prevent the block-breaking in the first place
	 */
	@SubscribeEvent
	public static void breakBlock(BlockEvent.BreakEvent event) {
		Player player = event.getPlayer();

		if (!(event.getLevel() instanceof ServerLevel level) || level.isClientSide()) return;

		BlockPos pos = event.getPos();
		if (isBlockProtectedFromBreaking(level, pos) && isAreaProtected(level, player, pos)) {
			event.setCanceled(true);
		}
	}

	/**
	 * Check if the player is trying to place a block in a structure that's considered inaccessible for progression reasons
	 * FIXME If there is a way to check on the client, it would be ideal to prevent the block placement in the first place.
	 *  Currently makes a desync from server that the item appeared consumed to the placer's client, despite it being unconsumed on serverside
	 */
	@SubscribeEvent
	public static void placeBlock(BlockEvent.EntityPlaceEvent event) {
		Entity entity = event.getEntity();

		if (!(event.getLevel() instanceof ServerLevel level) || !(entity instanceof Player player)) return;

		BlockPos pos = event.getPos();
		if (isBlockProtectedFromBreaking(level, pos) && isAreaProtected(level, player, pos)) {
			event.setCanceled(true);
			player.inventoryMenu.sendAllDataToRemote();
		}
	}

	/**
	 * Check if the player is trying to break a multi-block that intersects a structure that's considered inaccessible for progression reasons
	 * FIXME If there is a way to check on the client, it would be ideal to prevent the block placement in the first place.
	 *  Currently makes a desync from server that the item appeared consumed to the placer's client, despite it being unconsumed on serverside
	 */
	@SubscribeEvent
	public static void placeMultiBlock(BlockEvent.EntityMultiPlaceEvent event) {
		Entity entity = event.getEntity();

		if (!(event.getLevel() instanceof ServerLevel level) || !(entity instanceof Player player)) return;

		for (BlockSnapshot snapshot : event.getReplacedBlockSnapshots()) {
			BlockPos pos = snapshot.getPos();

			if (isBlockProtectedFromBreaking(level, pos) && isAreaProtected(level, player, pos)) {
				event.setCanceled(true);
				player.inventoryMenu.sendAllDataToRemote();
			}
		}
	}

	/**
	 * Stop the player from interacting with blocks that could produce treasure or open doors in a protected area
	 */
	@SubscribeEvent
	public static void onPlayerRightClick(PlayerInteractEvent.RightClickBlock event) {
		Player player = event.getEntity();
		Level level = player.level();

		if (!level.isClientSide() && level instanceof ServerLevel serverLevel && isBlockProtectedFromInteraction(level, event.getPos()) && isAreaProtected(serverLevel, player, event.getPos())) {
			event.setUseBlock(TriState.FALSE);
		}
	}

	private static boolean isBlockProtectedFromInteraction(BlockGetter level, BlockPos pos) {
		return level.getBlockState(pos).is(BlockTagGenerator.STRUCTURE_BANNED_INTERACTIONS);
	}

	private static boolean isBlockProtectedFromBreaking(BlockGetter level, BlockPos pos) {
		return !level.getBlockState(pos).is(BlockTagGenerator.PROGRESSION_ALLOW_BREAKING);
	}

	/**
	 * Return if the area at the coordinates is considered protected for that player.
	 * Currently, if we return true, we also send the area protection packet here.
	 */
	private static boolean isAreaProtected(ServerLevel level, Player player, BlockPos pos) {
		if (player.getAbilities().instabuild || player.isSpectator() ||
			!LandmarkUtil.isProgressionEnforced(level) || player instanceof FakePlayer) {
			return false;
		}

		Optional<StructureStart> struct = LandmarkUtil.locateNearestLandmarkStart(level, SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ()));
		if (struct.isPresent()) {
			StructureStart structureStart = struct.get();
			if (structureStart.getPieces().stream().anyMatch(structurePiece -> structurePiece.getBoundingBox().isInside(pos) && (!(structurePiece instanceof TFStructureComponent tfStructureComponent) || tfStructureComponent.isComponentProtected())) && structureStart.getStructure() instanceof ProgressionStructure structureHints) {
				if (!structureHints.doesPlayerHaveRequiredAdvancements(player)/* && chunkGenerator.isBlockProtected(pos)*/) {
					// send protection packet
					List<BoundingBox> boxes = new ArrayList<>();
					structureStart.getPieces().forEach(piece -> {
						if (piece.getBoundingBox().isInside(pos))
							boxes.add(piece.getBoundingBox());
					});

					sendAreaProtectionPacket(level, pos, boxes);

					// send a hint monster?
					structureHints.trySpawnHintMonster(level, player, pos);

					return true;
				}
			}
		}
		return false;
	}

	private static void sendAreaProtectionPacket(ServerLevel level, BlockPos pos, List<BoundingBox> sbb) {
		PacketDistributor.sendToPlayersNear(level, null, pos.getX(), pos.getY(), pos.getZ(), 64, new AreaProtectionPacket(sbb, pos));
	}

	@SubscribeEvent
	public static void livingAttack(LivingIncomingDamageEvent event) {
		LivingEntity living = event.getEntity();
		// cancel attacks in protected areas
		if (living.level() instanceof ServerLevel serverLevel && living instanceof Enemy && event.getSource().getEntity() instanceof Player && !(living instanceof Kobold)
			&& isAreaProtected(serverLevel, (Player) event.getSource().getEntity(), new BlockPos(living.blockPosition()))) {

			event.setCanceled(true);
		}
	}
}

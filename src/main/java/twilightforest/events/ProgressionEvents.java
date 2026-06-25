package twilightforest.events;

import com.mojang.datafixers.util.Pair;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SpellParticleOption;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.Permission;
import net.minecraft.server.permissions.PermissionLevel;
import net.minecraft.util.TriState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.BlockSnapshot;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.level.block.BreakBlockEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import twilightforest.block.TFPortalBlock;
import twilightforest.config.TFConfig;
import twilightforest.entity.monster.Kobold;
import twilightforest.init.TFAdvancements;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFDimension;
import twilightforest.network.AreaProtectionPacket;
import twilightforest.network.MissingAdvancementToastPacket;
import twilightforest.network.StructureProtectionPacket;
import twilightforest.tags.TFBlockTags;
import twilightforest.tags.TFItemTags;
import twilightforest.util.Enforcement;
import twilightforest.util.PlayerHelper;
import twilightforest.util.landmarks.LandmarkUtil;
import twilightforest.world.components.structures.util.AdvancementLockedStructure;
import twilightforest.world.components.structures.util.ProgressionPiece;
import twilightforest.world.components.structures.util.ProgressionStructure;

import java.util.*;

/**
 * A class to store events relating to progression
 */
@Component
public class ProgressionEvents {

	@PostConstruct
	private void setup() {
		NeoForge.EVENT_BUS.addListener(this::preventLockedAreaBlockBreaking);
		NeoForge.EVENT_BUS.addListener(this::preventLockedAreaBlockPlacing);
		NeoForge.EVENT_BUS.addListener(this::preventLockedAreaBlockInteracting);
		NeoForge.EVENT_BUS.addListener(this::preventLockedAreaMultiblocks);
		NeoForge.EVENT_BUS.addListener(this::preventLockedAreaEntityDamage);
		NeoForge.EVENT_BUS.addListener(this::performProtectionAndPortalChecks);
	}

	/**
	 * Check if the player is trying to break a block in a structure that's considered unbreakable for progression reasons
	 */
	private void preventLockedAreaBlockBreaking(BreakBlockEvent event) {
		if (!(event.getLevel() instanceof ServerLevel level) || event.isCanceled()) return;

		BlockPos pos = event.getPos();
		if (isBlockProtectedFromBreaking(level, pos) && isAreaProtected(level, event.getPlayer(), pos)) {
			event.setCanceled(true);
		}
	}

	/**
	 * Check if the player is trying to place a block in a structure that's considered inaccessible for progression reasons
	 */
	private void preventLockedAreaBlockPlacing(PlayerInteractEvent.RightClickBlock event) {
		if (!(event.getLevel() instanceof ServerLevel level) || event.isCanceled()) return;

		BlockPos pos = event.getPos();
		if (isAreaProtected(level, event.getEntity(), pos)) {
			event.setCanceled(true);
			event.getEntity().inventoryMenu.sendAllDataToRemote();
		}
	}

	/**
	 * Check if the player is trying to break a multi-block that intersects a structure that's considered inaccessible for progression reasons
	 */
	private void preventLockedAreaMultiblocks(BlockEvent.EntityMultiPlaceEvent event) {
		Entity entity = event.getEntity();

		if (!(event.getLevel() instanceof ServerLevel level) || !(entity instanceof Player player) || event.isCanceled()) return;

		for (BlockSnapshot snapshot : event.getReplacedBlockSnapshots()) {
			BlockPos pos = snapshot.getPos();

			if (isBlockProtectedFromBreaking(level, pos) && isAreaProtected(level, player, pos)) {
				event.setCanceled(true);
				player.inventoryMenu.sendAllDataToRemote();
				break;
			}
		}
	}

	/**
	 * Stop the player from interacting with blocks that could produce treasure or open doors in a protected area
	 */
	private void preventLockedAreaBlockInteracting(PlayerInteractEvent.RightClickBlock event) {
		Player player = event.getEntity();
		Level level = player.level();

		if (!level.isClientSide() && level instanceof ServerLevel serverLevel && isBlockProtectedFromInteraction(level, event.getPos()) && isAreaProtected(serverLevel, player, event.getPos())) {
			event.setUseBlock(TriState.FALSE);
		}
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
			if (structureStart.getPieces().stream().anyMatch(structurePiece -> structurePiece.getBoundingBox().isInside(pos) && isPieceProtected(structurePiece)) && structureStart.getStructure() instanceof ProgressionStructure structureHints) {
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

	//TODO make ignored entities into a tag
	private void preventLockedAreaEntityDamage(LivingIncomingDamageEvent event) {
		LivingEntity living = event.getEntity();
		// cancel attacks in protected areas
		if (living.level() instanceof ServerLevel serverLevel && living instanceof Enemy && event.getSource().getEntity() instanceof Player && !(living instanceof Kobold)
			&& isAreaProtected(serverLevel, (Player) event.getSource().getEntity(), new BlockPos(living.blockPosition()))) {

			event.setCanceled(true);
		}
	}

	private void performProtectionAndPortalChecks(PlayerTickEvent.Post event) {
		Player eventPlayer = event.getEntity();

		if (!(eventPlayer instanceof ServerPlayer player)) return;
		ServerLevel level = player.level();

		// check for portal creation, at least if it's not disabled
		if (!TFConfig.disablePortalCreation && player.tickCount % (!TFConfig.checkPortalPlacement ? 100 : 20) == 0) {
			// skip non admin players when the option is on
			if (level.getServer().getProfilePermissions(player.nameAndId()).level().isEqualOrHigherThan(TFConfig.portalCreationPermission)) {
				// reduce range to 4.0 if config is set to admins/owners only
				checkForPortalCreation(player, level, TFConfig.portalCreationPermission.isEqualOrHigherThan(PermissionLevel.ADMINS)  ? 4.0F : 32.0F);
			}
		}

		// check the player for being in a forbidden progression area, only every 20 ticks
		if (player.tickCount % 20 == 0 && LandmarkUtil.isProgressionEnforced(level) && !player.isCreative() && !player.isSpectator()) {
			Enforcement.enforceBiomeProgression(player, level);
		}

		// check and send nearby forbidden structures, every 100 ticks or so
		if (player.tickCount % 100 == 0 && LandmarkUtil.isProgressionEnforced(level)) {
			if (player.isCreative() || player.isSpectator()) {
				sendAllClearPacket(player);
			} else {
				checkForLockedStructuresSendPacket(player, level);
			}
		}
	}

	@SuppressWarnings("UnusedReturnValue")
	private static boolean checkForLockedStructuresSendPacket(Player player, ServerLevel world) {
		ChunkPos chunkPlayer = player.chunkPosition();
		return LandmarkUtil.locateNearestLandmarkStart(world, chunkPlayer.x(), chunkPlayer.z()).map(structureStart -> {
			if (structureStart.getStructure() instanceof AdvancementLockedStructure advancementLockedStructure && !advancementLockedStructure.doesPlayerHaveRequiredAdvancements(player)) {
				List<Pair<BoundingBox, Boolean>> boundingBoxesData = structureStart.getPieces().stream()
					.map(piece -> Pair.of(isPieceProtected(piece) ? piece.getBoundingBox().inflatedBy(4) : piece.getBoundingBox(), isPieceProtected(piece)))
					.toList();

				sendStructureProtectionPacket(player, boundingBoxesData);
				return true;
			}

			sendAllClearPacket(player);
			return false;
		}).orElse(false);
	}

	private static void checkForPortalCreation(ServerPlayer player, ServerLevel level, float rangeToCheck) {
		if (level.dimension().identifier().equals(Identifier.parse(TFConfig.originDimension))
			|| TFDimension.isTwilightPortalDestination(level)
			|| TFConfig.allowPortalsInOtherDimensions) {

			List<ItemEntity> itemList = level.getEntitiesOfClass(ItemEntity.class, player.getBoundingBox().inflate(rangeToCheck));
			ItemEntity qualified = null;

			for (ItemEntity entityItem : itemList) {
				if (entityItem.getItem().is(TFItemTags.PORTAL_ACTIVATOR) &&
				TFBlocks.TWILIGHT_PORTAL.get().canFormPortal(level.getBlockState(entityItem.blockPosition())) &&
				Objects.equals(entityItem.getOwner(), player)) {

					qualified = entityItem;
					break;
				}
			}

			if (qualified == null) return;

			if (!player.isCreative() && !player.isSpectator() && TFConfig.getPortalLockingAdvancement(player) != null) {
				AdvancementHolder requirement = PlayerHelper.getAdvancement(player, Objects.requireNonNull(TFConfig.getPortalLockingAdvancement(player)));
				if (requirement != null && !PlayerHelper.doesPlayerHaveRequiredAdvancement(player, requirement)) {
					player.sendOverlayMessage(TFPortalBlock.PORTAL_UNWORTHY);

					if (!TFPortalBlock.isPlayerNotifiedOfRequirement(player)) {
						// .doesPlayerHaveRequiredAdvancement null-checks already, so we can skip null-checking the `requirement`
						DisplayInfo info = requirement.value().display().orElse(null);
						PacketDistributor.sendToPlayer(player, info == null ? new MissingAdvancementToastPacket(net.minecraft.network.chat.Component.translatable("twilightforest.ui.advancement.no_title"), new ItemStack(TFBlocks.TWILIGHT_PORTAL_MINIATURE_STRUCTURE.get())) : new MissingAdvancementToastPacket(info.getTitle(), new ItemStack(info.getIcon().item(), info.getIcon().count())));

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

				level.addParticle(SpellParticleOption.create(ParticleTypes.EFFECT, -1, 1.0F), qualified.getX(), qualified.getY() + 0.2, qualified.getZ(), vx, vy, vz);
			}

			if (TFBlocks.TWILIGHT_PORTAL.get().tryToCreatePortal(level, qualified.blockPosition(), qualified, player))
				TFAdvancements.MADE_TF_PORTAL.get().trigger(player);

		}
	}

	private static boolean isPieceProtected(StructurePiece piece) {
		return !(piece instanceof ProgressionPiece progressionPiece) || progressionPiece.isComponentProtected();
	}

	private static boolean isBlockProtectedFromInteraction(BlockGetter level, BlockPos pos) {
		return level.getBlockState(pos).is(TFBlockTags.STRUCTURE_BANNED_INTERACTIONS);
	}

	private static boolean isBlockProtectedFromBreaking(BlockGetter level, BlockPos pos) {
		return !level.getBlockState(pos).is(TFBlockTags.PROGRESSION_ALLOW_BREAKING);
	}

	private static void sendAreaProtectionPacket(ServerLevel level, BlockPos pos, List<BoundingBox> sbb) {
		PacketDistributor.sendToPlayersNear(level, null, pos.getX(), pos.getY(), pos.getZ(), 64, new AreaProtectionPacket(sbb, pos));
	}

	private static void sendStructureProtectionPacket(Player player, List<Pair<BoundingBox, Boolean>> sbbData) {
		if (player instanceof ServerPlayer sp) {
			PacketDistributor.sendToPlayer(sp, new StructureProtectionPacket(Optional.of(sbbData)));
		}
	}

	private static void sendAllClearPacket(Player player) {
		if (player instanceof ServerPlayer sp) {
			PacketDistributor.sendToPlayer(sp, new StructureProtectionPacket(Optional.empty()));
		}
	}
}

package twilightforest.events;

import net.minecraftforge.eventbus.api.Event;
import twilightforest.forge.attachment.Attachments;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.level.BlockEvent;
import org.jetbrains.annotations.Nullable;
import twilightforest.forge.beans.Component;
import twilightforest.forge.beans.PostConstruct;
import twilightforest.block.GiantBlock;
import twilightforest.components.entity.GiantPickaxeMiningAttachment;
import twilightforest.data.tags.BlockTagGenerator;
import twilightforest.init.TFDataAttachments;
import twilightforest.init.TFItems;
import twilightforest.item.*;
import twilightforest.loot.modifiers.GiantToolGroupingModifier;

import java.util.List;

@Component
public class ToolEvents {

	private static final int KNIGHTMETAL_BONUS_DAMAGE = 2;
	private static final int MINOTAUR_AXE_BONUS_DAMAGE = 7;

	@PostConstruct
	private void setup() {
		MinecraftForge.EVENT_BUS.addListener(this::onEnderBowHit);
		// PORTAGE : gestionnaire « fieryToolSetFire » supprime — l'evenement NeoForge
		// correspondant n'existe pas chez Forge (voir PORTAGE-twilightforest-crochets.txt).
		// PORTAGE : gestionnaire « doKnightmetalToolLogic » supprime — l'evenement NeoForge
		// correspondant n'existe pas chez Forge (voir PORTAGE-twilightforest-crochets.txt).
		// PORTAGE : gestionnaire « addExtraAxeChargingDamage » supprime — l'evenement NeoForge
		// correspondant n'existe pas chez Forge (voir PORTAGE-twilightforest-crochets.txt).
		MinecraftForge.EVENT_BUS.addListener(this::damageNonMazebreakerToolsMore);
		MinecraftForge.EVENT_BUS.addListener(this::preventFatigueWithPocketWatch);
		MinecraftForge.EVENT_BUS.addListener(this::handleGiantPickaxeMining);
		MinecraftForge.EVENT_BUS.addListener(this::refreshOreMagnetCache);
	}

	private void onEnderBowHit(ProjectileImpactEvent evt) {
		Projectile arrow = evt.getProjectile();
		if (arrow.getOwner() instanceof Player player
			&& evt.getRayTraceResult() instanceof EntityHitResult result
			&& result.getEntity() instanceof LivingEntity living
			&& arrow.getOwner() != result.getEntity() && !result.getEntity().getType().is(Tags.EntityTypes.BOSSES)) {

			if (arrow.getPersistentData().contains(EnderBowItem.KEY)) {
				double sourceX = player.getX(), sourceY = player.getY(), sourceZ = player.getZ();
				float sourceYaw = player.getYRot(), sourcePitch = player.getXRot();
				@Nullable Entity playerVehicle = player.getVehicle();

				player.setYRot(living.getYRot());
				player.teleportTo(living.getX(), living.getY(), living.getZ());
				player.invulnerableTime = 40;
				player.level().broadcastEntityEvent(player, (byte) 46);
				if (living.isPassenger() && living.getVehicle() != null) {
					player.startRiding(living.getVehicle(), true);
					living.stopRiding();
				}
				player.playSound(SoundEvents.CHORUS_FRUIT_TELEPORT, 1.0F, 1.0F);

				living.setYRot(sourceYaw);
				living.setXRot(sourcePitch);
				living.teleportTo(sourceX, sourceY, sourceZ);
				living.level().broadcastEntityEvent(player, (byte) 46);
				if (playerVehicle != null) {
					living.startRiding(playerVehicle, true);
					player.stopRiding();
				}
				living.playSound(SoundEvents.CHORUS_FRUIT_TELEPORT, 1.0F, 1.0F);
			}
		}
	}

	// PORTAGE : gestionnaire supprime. Il reposait sur le pipeline de degats de
	// NeoForge (DamageContainer), que Forge n'a pas. Voir PORTAGE-twilightforest-crochets.txt.

	// PORTAGE : gestionnaire supprime. Il reposait sur le pipeline de degats de
	// NeoForge (DamageContainer), que Forge n'a pas. Voir PORTAGE-twilightforest-crochets.txt.

	// PORTAGE : gestionnaire supprime. Il reposait sur le pipeline de degats de
	// NeoForge (DamageContainer), que Forge n'a pas. Voir PORTAGE-twilightforest-crochets.txt.

	private void damageNonMazebreakerToolsMore(BlockEvent.BreakEvent event) {
		ItemStack stack = event.getPlayer().getMainHandItem();
		if (event.getState().is(BlockTagGenerator.MAZEBREAKER_ACCELERATED)) {
			if (stack.isDamageableItem() && !(stack.getItem() instanceof MazebreakerPickItem)) {
				stack.hurtAndBreak(16, event.getPlayer(), EquipmentSlot.MAINHAND);
			}
		}
	}

	private void preventFatigueWithPocketWatch(MobEffectEvent.Applicable event) {
		// PORTAGE : NeoForge a une enumeration dediee (APPLY / DO_NOT_APPLY) et un
		// « getApplicationResult ». Forge s'en remet au resultat generique de ses evenements :
		// DENY empeche l'effet, DEFAULT laisse le jeu decider.
		if (event.getResult() != Event.Result.DENY && event.getEffectInstance().is(MobEffects.DIG_SLOWDOWN) && event.getEntity().isHolding(TFItems.POCKET_WATCH.get())) {
			event.setResult(Event.Result.DENY);
		}
	}

	private void handleGiantPickaxeMining(BlockEvent.BreakEvent event) {
		BlockPos pos = event.getPos();
		BlockState state = event.getState();

		if (event.getPlayer() instanceof ServerPlayer player && canHarvestWithGiantPick(player, state, pos)) {
			var attachment = Attachments.get(player, TFDataAttachments.GIANT_PICKAXE_MINING);

			if (shouldBreakGiantBlock(player, attachment)) {
				attachment.setBreaking(true); // Tell the capability that a block breaking loop is happening, so it knows to fail the if check above. Otherwise, this would go on forever

				LootParams.Builder builder = new LootParams.Builder(player.serverLevel())
					.withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
					.withParameter(LootContextParams.BLOCK_STATE, state)
					.withOptionalParameter(LootContextParams.THIS_ENTITY, player)
					.withParameter(LootContextParams.TOOL, player.getMainHandItem());

				List<ItemStack> drops = state.getDrops(builder);

				if (!drops.isEmpty() && drops.getFirst().getItem() instanceof BlockItem block) {
					boolean allTheSame = GiantToolGroupingModifier.CONVERSIONS.containsKey(block.getBlock()); //check if the block drops can be converted instead of the block itself so things like stone can make giant cobble
					if (allTheSame) {
						for (BlockPos offsetPos : GiantBlock.getVolume(pos)) {
							if (!player.level().getBlockState(offsetPos).is(state.getBlock())) {
								allTheSame = false;
								break; //end early: we have determined we arent getting a giant block from this. No need to keep checking positions
							}
						}
					}
					attachment.setGiantBlockConversion(allTheSame ? 64 : 0); // NO IN-BETWEEN! Either the whole 64 get converted, or none do
				}
				event.setCanceled(true); // We cancel this event, since we want to break the block we're looking at first
				player.level().levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, pos, Block.getId(state));
				player.gameMode.destroyBlock(pos); // Break the block we broke, for real this time

				// Break all the other blocks, if they're the same type
				for (BlockPos offsetPos : GiantBlock.getVolume(pos)) {
					if (!offsetPos.equals(pos) && player.level().getBlockState(offsetPos).is(state.getBlock())) {
						BlockPos newPos = new BlockPos(offsetPos); // This feels dumb, but without it, the client thinks the last block in the iterator is broken too
						player.level().levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, newPos, Block.getId(player.level().getBlockState(newPos)));
						player.gameMode.destroyBlock(newPos);
					}
				}
				attachment.setBreaking(false); // Tell the capability that the loop is over, and all is good in the world
			}
		}
	}

	// PORTAGE : NeoForge passe le niveau et la position ; Forge attend a la place le resultat
	// par defaut du test, que le joueur sait donner lui-meme.
	private static boolean canHarvestWithGiantPick(Player player, BlockState state, BlockPos pos) {
		return player.getMainHandItem().getItem() instanceof GiantPickItem && ForgeEventFactory.doPlayerHarvestCheck(player, state, player.hasCorrectToolForDrops(state));
	}

	private static boolean shouldBreakGiantBlock(Player player, GiantPickaxeMiningAttachment attachment) {
		return attachment.getMining() == player.level().getGameTime() && !attachment.getBreaking();
	}

	private void refreshOreMagnetCache(TagsUpdatedEvent event) {
		OreMagnetItem.MAGNET_ORE_TO_BLOCK_REPLACEMENTS.clear();
		OreMagnetItem.TREE_ORE_TO_BLOCK_REPLACEMENTS.clear();

		//collect all tags
		for (TagKey<Block> tag : BuiltInRegistries.BLOCK.getTagNames().filter(location -> location.location().getNamespace().equals("c")).toList()) {
			//check if the tag is a valid ore tag
			if (tag.location().getPath().contains("ores_in_ground/")) {
				//grab the part after the slash for use later
				String oreground = tag.location().getPath().substring(15);
				//check if a tag for ore grounds matches up with our ores in ground tag
				if (BuiltInRegistries.BLOCK.getTagNames().filter(location -> location.location().getNamespace().equals("c")).anyMatch(blockTagKey -> blockTagKey.location().getPath().equals("ore_bearing_ground/" + oreground))) {
					//add each ground type to each ore
					BuiltInRegistries.BLOCK.getTag(TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("c", "ore_bearing_ground/" + oreground))).get().forEach(ground ->
						BuiltInRegistries.BLOCK.getTag(tag).get().forEach(ore -> {
							//exclude ignored ores
							if (!ore.value().defaultBlockState().is(BlockTagGenerator.ORE_MAGNET_IGNORE)) {
								OreMagnetItem.MAGNET_ORE_TO_BLOCK_REPLACEMENTS.put(ore.value(), ground.value());
							}
							if (!ore.value().defaultBlockState().is(BlockTagGenerator.MINING_CORE_EXCLUDED)) {
								OreMagnetItem.TREE_ORE_TO_BLOCK_REPLACEMENTS.put(ore.value(), ground.value());
							}
						}));
				}
			}
		}

		//Gonna need to special case this one as it isn't covered by tags.
		//Ancient debris isn't exactly an ore, so it makes sense that the tag doesn't include it
		if (!Blocks.ANCIENT_DEBRIS.defaultBlockState().is(BlockTagGenerator.ORE_MAGNET_IGNORE) && !OreMagnetItem.MAGNET_ORE_TO_BLOCK_REPLACEMENTS.containsKey(Blocks.ANCIENT_DEBRIS)) {
			OreMagnetItem.MAGNET_ORE_TO_BLOCK_REPLACEMENTS.put(Blocks.ANCIENT_DEBRIS, Blocks.NETHERRACK);
		}

		if (!Blocks.ANCIENT_DEBRIS.defaultBlockState().is(BlockTagGenerator.MINING_CORE_EXCLUDED) && !OreMagnetItem.TREE_ORE_TO_BLOCK_REPLACEMENTS.containsKey(Blocks.ANCIENT_DEBRIS)) {
			OreMagnetItem.TREE_ORE_TO_BLOCK_REPLACEMENTS.put(Blocks.ANCIENT_DEBRIS, Blocks.NETHERRACK);
		}
	}
}

package twilightforest.events;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NonTameRandomTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import twilightforest.TwilightForestMod;
import twilightforest.block.TomeSpawnerBlock;
import twilightforest.entity.monster.DeathTome;
import twilightforest.entity.passive.Bighorn;
import twilightforest.entity.passive.DwarfRabbit;
import twilightforest.entity.passive.Squirrel;
import twilightforest.entity.passive.TinyBird;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFEntities;
import twilightforest.network.CreateMovingCicadaSoundPacket;

@Mod.EventBusSubscriber(modid = TwilightForestMod.ID)
public class MiscEvents {

	@SubscribeEvent
	public static void addPrey(EntityJoinLevelEvent event) {
		if (event.getEntity() instanceof Mob mob) {
			EntityType<?> type = mob.getType();
			if (type == EntityType.CAT) {
				mob.targetSelector.addGoal(1, new NonTameRandomTargetGoal<>((TamableAnimal) mob, DwarfRabbit.class, true, null));
				mob.targetSelector.addGoal(1, new NonTameRandomTargetGoal<>((TamableAnimal) mob, Squirrel.class, true, null));
				mob.targetSelector.addGoal(1, new NonTameRandomTargetGoal<>((TamableAnimal) mob, TinyBird.class, true, null));
			} else if (type == EntityType.OCELOT) {
				mob.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(mob, DwarfRabbit.class, true));
				mob.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(mob, Squirrel.class, true));
				mob.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(mob, TinyBird.class, true));
			} else if (type == EntityType.FOX) {
				mob.targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(mob, DwarfRabbit.class, true));
				mob.targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(mob, Squirrel.class, true));
			} else if (type == EntityType.WOLF) {
				mob.targetSelector.addGoal(7, new NonTameRandomTargetGoal<>((TamableAnimal) mob, DwarfRabbit.class, true, null));
				mob.targetSelector.addGoal(7, new NonTameRandomTargetGoal<>((TamableAnimal) mob, Squirrel.class, true, null));
				mob.targetSelector.addGoal(7, new NonTameRandomTargetGoal<>((TamableAnimal) mob, Bighorn.class, true, null));
			}
		}
	}

	@SubscribeEvent
	public static void armorChanged(LivingEquipmentChangeEvent event) {
		LivingEntity living = event.getEntity();

		// from what I can see, vanilla doesnt have a hook for this in the item class. So this will have to do.
		// we only have to check equipping, when its unequipped the sound instance handles the rest

		//if we have a cicada in our curios slot, dont try to run this
		if (ModList.get().isLoaded("curios")) { //FIXME: When curios gets updated, uncomment this
			//if (CuriosCompat.isCurioEquipped(living, stack -> stack.is(TFBlocks.CICADA.asItem()))) return;
		}

		if (living != null && !living.level().isClientSide() && event.getSlot() == EquipmentSlot.HEAD && event.getTo().is(TFBlocks.CICADA.asItem())) {
			PacketDistributor.TRACKING_ENTITY_AND_SELF.with(living).send(new CreateMovingCicadaSoundPacket(living.getId()));
		}
	}

	@SubscribeEvent
	public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
		Player player = event.getEntity();
		ItemStack stack = player.getItemInHand(event.getHand());

        if (!(stack.getItem() instanceof SpawnEggItem spawnEggItem) || spawnEggItem.getType(stack.getTag()) != TFEntities.DEATH_TOME.get())
            return;

        BlockPos pos = event.getPos();
		Level level = event.getLevel();
		BlockState state = level.getBlockState(pos);

        if (state.getBlock() instanceof LecternBlock && !state.getValue(BlockStateProperties.HAS_BOOK)) {
            event.setCanceled(true);
            level.playSound(null, pos, SoundEvents.BOOK_PUT, SoundSource.BLOCKS, 1.0F, 1.0F);

            if (level instanceof ServerLevel serverLevel) {
                DeathTome tome = TFEntities.DEATH_TOME.get().spawn(serverLevel, stack, player, pos.below(), MobSpawnType.SPAWN_EGG, true, false);
                if (tome != null) {
                    if (!player.getAbilities().instabuild) stack.shrink(1);
                    serverLevel.gameEvent(player, GameEvent.ENTITY_PLACE, pos);
                    tome.setOnLectern(true);
                }
            }
        } else if (state.is(TFBlocks.DEATH_TOME_SPAWNER) && state.getValue(TomeSpawnerBlock.SPAWNER)) {
			int bookCount = state.getValue(TomeSpawnerBlock.BOOK_STAGES);
			if (bookCount < TomeSpawnerBlock.MAX_STAGES) {
				level.setBlockAndUpdate(pos, state.setValue(TomeSpawnerBlock.BOOK_STAGES, bookCount + 1));

				event.setCanceled(true);
			}
		} else if (state.is(TFBlocks.EMPTY_CANOPY_BOOKSHELF)) {
			BlockState newState = TFBlocks.DEATH_TOME_SPAWNER.get().defaultBlockState().setValue(TomeSpawnerBlock.SPAWNER, true).setValue(TomeSpawnerBlock.BOOK_STAGES, 1);
			level.setBlockAndUpdate(pos, newState);

			event.setCanceled(true);
		}
    }
}

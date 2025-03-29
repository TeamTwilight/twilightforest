package twilightforest.entity.boss;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import twilightforest.config.TFConfig;
import twilightforest.init.TFSounds;
import twilightforest.loot.TFLootTables;
import twilightforest.network.ParticlePacket;

import java.util.List;

public interface IBossLootBuffer {
	int CONTAINER_SIZE = 27;

	default ItemStack getItem(int slot) {
		return this.getItemStacks().get(slot);
	}

	default void setItem(int slot, ItemStack stack) {
		this.getItemStacks().set(slot, stack);
		if (!stack.isEmpty() && stack.getCount() > stack.getMaxStackSize()) {
			stack.setCount(stack.getMaxStackSize());
		}
	}

	default void addDeathItemsSaveData(CompoundTag tag, RegistryAccess registryAccess) {
		ContainerHelper.saveAllItems(tag, this.getItemStacks(), registryAccess);
	}

	default void readDeathItemsSaveData(CompoundTag tag, RegistryAccess registryAccess) {
		ContainerHelper.loadAllItems(tag, this.getItemStacks(), registryAccess);
	}

	static <T extends LivingEntity & IBossLootBuffer> void saveDropsIntoBoss(T boss, LootParams params, ServerLevel serverLevel) {
		if (TFConfig.bossDropChests) {
			LootTable table = serverLevel.getServer().reloadableRegistries().getLootTable(boss.getLootTable());
			ObjectArrayList<ItemStack> stacks = table.getRandomItems(params);
			boss.fill(boss, params, table);

			//If our loot stack size is bigger than the inventory, drop everything else outside it. Don't want to lose any loot now do we?
			if (stacks.size() > CONTAINER_SIZE) {
				for (ItemStack stack : stacks.subList(28, stacks.size())) {
					ItemEntity item = new ItemEntity(serverLevel, boss.getX(), boss.getY(), boss.getZ(), stack);
					item.setExtendedLifetime();
					item.setNoPickUpDelay();
					serverLevel.addFreshEntity(item);
				}
			}
		}
	}

	static <T extends LivingEntity & IBossLootBuffer> void depositDropsIntoChest(T boss, BlockState chest, BlockPos pos, ServerLevel serverLevel) {
		if (TFConfig.bossDropChests && !boss.getItemStacks().isEmpty()) {
			if (!tryDeposit(boss, chest, pos, serverLevel)) {
				BlockPos.MutableBlockPos chestPos = pos.mutable();
				for (int y = pos.getY(); y < serverLevel.getMaxBuildHeight(); y++) {
					chestPos.setY(y);
					if (tryDeposit(boss, chest, chestPos, serverLevel)) return;
				}
			} else return;

			for (int i = 0; i < CONTAINER_SIZE; i++) {
				Block.popResource(serverLevel, pos, boss.getItem(i));
			}
			celebrateAt(boss, pos.getCenter(), serverLevel);
		}
	}

	static <T extends LivingEntity & IBossLootBuffer> boolean tryDeposit(T boss, BlockState chest, BlockPos pos, ServerLevel serverLevel) {
		if ((serverLevel.getBlockState(pos).is(chest.getBlock()) ||
			((serverLevel.getBlockState(pos).canBeReplaced() || serverLevel.getBlockState(pos).getPistonPushReaction() != PushReaction.BLOCK) && serverLevel.getBlockEntity(pos) == null && serverLevel.setBlock(pos, chest, TFLootTables.DEFAULT_PLACE_FLAG))) &&
			serverLevel.getBlockEntity(pos) instanceof Container container) {

			for (int i = 0; i < CONTAINER_SIZE && i < container.getContainerSize(); i++) {
				container.setItem(i, boss.getItem(i));
			}
			celebrateAt(boss, pos.getCenter(), serverLevel);
			return true;
		}
		return false;
	}

	static <T extends LivingEntity & IBossLootBuffer> void celebrateAt(T boss, Vec3 vec3, ServerLevel serverLevel) {
		serverLevel.playSound(null, vec3.x, vec3.y, vec3.z, TFSounds.BOSS_CHEST_APPEAR.get(), boss.getSoundSource(), 128.0F, (boss.getRandom().nextFloat() - boss.getRandom().nextFloat()) * 0.175F + 0.5F);

		ParticlePacket particlePacket = new ParticlePacket();
		for (int i = 0; i < 40; i++) {
			double x = (boss.getRandom().nextDouble() - 0.5D) * 0.075D * i;
			double y = (boss.getRandom().nextDouble() - 0.5D) * 0.075D * i;
			double z = (boss.getRandom().nextDouble() - 0.5D) * 0.075D * i;
			particlePacket.queueParticle(ParticleTypes.POOF, false, vec3.add(x, y, z), Vec3.ZERO);
		}
		PacketDistributor.sendToPlayersTrackingEntity(boss, particlePacket);
	}

	default <T extends LivingEntity & IBossLootBuffer> void fill(T boss, LootParams context, LootTable table) {
		ObjectArrayList<ItemStack> items = table.getRandomItems(context);
		RandomSource randomsource = boss.getRandom();
		List<Integer> list = this.getAvailableSlots(randomsource);
		table.shuffleAndSplitItems(items, list.size(), randomsource);

		for (ItemStack itemstack : items) {
			if (!list.isEmpty()) {
				this.setItem(list.removeLast(), itemstack.isEmpty() ? ItemStack.EMPTY : itemstack);
			}
		}
	}

	default List<Integer> getAvailableSlots(RandomSource random) {
		ObjectArrayList<Integer> arrayList = new ObjectArrayList<>();
		for (int i = 0; i < CONTAINER_SIZE; ++i) arrayList.add(i);
		Util.shuffle(arrayList, random);
		return arrayList;
	}

	NonNullList<ItemStack> getItemStacks();
}

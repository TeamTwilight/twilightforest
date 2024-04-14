package twilightforest.entity.boss;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.Vec3;
import twilightforest.config.TFConfig;
import twilightforest.init.TFSounds;
import twilightforest.loot.TFLootTables;

import java.util.List;

public interface IBossLootBuffer {
	int CONTAINER_SIZE = 27;

	default ItemStack getItem(int slot) {
		return this.getItemStacks().get(slot);
	}

	default void setItem(int slot, ItemStack stack) {
		this.getItemStacks().set(slot, stack);
		if (!stack.isEmpty() && stack.getCount() > 64) {
			stack.setCount(64);
		}
	}

	default void addDeathItemsSaveData(CompoundTag tag) {
		ContainerHelper.saveAllItems(tag, this.getItemStacks());
	}

	default void readDeathItemsSaveData(CompoundTag tag) {
		ContainerHelper.loadAllItems(tag, this.getItemStacks());
	}

	static <T extends LivingEntity & IBossLootBuffer> void saveDropsIntoBoss(T boss, LootParams params, ServerLevel serverLevel) {
		if (TFConfig.bossDropChests) {
			LootTable table = serverLevel.getServer().getLootData().getLootTable(boss.getLootTable());
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
		if (TFConfig.bossDropChests && (serverLevel.setBlock(pos, chest, TFLootTables.DEFAULT_PLACE_FLAG) || serverLevel.getBlockState(pos).is(chest.getBlock())) && serverLevel.getBlockEntity(pos) instanceof Container container) {
			for (int i = 0; i < CONTAINER_SIZE && i < container.getContainerSize(); i++) {
				container.setItem(i, boss.getItem(i));
			}
			Vec3 vec3 = Vec3.atCenterOf(pos);
			serverLevel.playSound(null, vec3.x, vec3.y, vec3.z, TFSounds.BOSS_CHEST_APPEAR.get(), boss.getSoundSource(), 128.0F, (boss.getRandom().nextFloat() - boss.getRandom().nextFloat()) * 0.175F + 0.5F);
		}
	}

	default <T extends LivingEntity & IBossLootBuffer> void fill(T boss, LootParams context, LootTable table) {
		ObjectArrayList<ItemStack> items = table.getRandomItems(context);
		RandomSource randomsource = boss.getRandom();
		List<Integer> list = this.getAvailableSlots(randomsource);
		table.shuffleAndSplitItems(items, list.size(), randomsource);

		for (ItemStack itemstack : items) {
			if (list.isEmpty()) {
				return;
			}

			if (itemstack.isEmpty()) {
				this.setItem(list.remove(list.size() - 1), ItemStack.EMPTY);
			} else {
				this.setItem(list.remove(list.size() - 1), itemstack);
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

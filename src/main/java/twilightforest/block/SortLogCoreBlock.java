package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.network.PacketDistributor;
import twilightforest.config.TFConfig;
import twilightforest.data.tags.EntityTagGenerator;
import twilightforest.init.TFParticleType;
import twilightforest.network.ParticlePacket;

import java.util.*;

public class SortLogCoreBlock extends SpecialMagicLogBlock {

	// No capability cache: the previous one lived on this (shared) Block instance, was keyed by the mutable BlockPos
	// from BlockPos.betweenClosed (so keys changed after insertion and it grew forever) and ignored the level.

	public SortLogCoreBlock(Properties properties) {
		super(properties);
	}

	@Override
	public boolean doesCoreFunction() {
		return !TFConfig.disableSortingCore;
	}

	@Override
	void performTreeEffect(ServerLevel level, BlockPos pos, RandomSource rand) {
		Map<List<IItemHandler>, Vec3> inputMap = new HashMap<>();
		Map<IItemHandler, Vec3> outputMap = new HashMap<>();

		// Iterate the block entities of the loaded chunks in range instead of calling getBlockEntity on every position of
		// a (2r+1)^3 cube (~36k lookups per second at the default range, which could also load chunks at the edge).
		int range = TFConfig.sortingCoreRange;
		for (int cx = SectionPos.blockToSectionCoord(pos.getX() - range); cx <= SectionPos.blockToSectionCoord(pos.getX() + range); cx++) {
			for (int cz = SectionPos.blockToSectionCoord(pos.getZ() - range); cz <= SectionPos.blockToSectionCoord(pos.getZ() + range); cz++) {
				LevelChunk chunk = level.getChunkSource().getChunkNow(cx, cz);
				if (chunk == null)
					continue;
				for (BlockPos blockPos : chunk.getBlockEntities().keySet()) {
					if (blockPos.equals(pos) || Math.abs(blockPos.getX() - pos.getX()) > range || Math.abs(blockPos.getY() - pos.getY()) > range || Math.abs(blockPos.getZ() - pos.getZ()) > range)
						continue;
					// Put it in the input if its within 2 blocks
					if (Math.abs(blockPos.getX() - pos.getX()) <= 2 && Math.abs(blockPos.getY() - pos.getY()) <= 2 && Math.abs(blockPos.getZ() - pos.getZ()) <= 2) {
						List<IItemHandler> handlers = new ArrayList<>();
						for (Direction side : Direction.values()) {
							IItemHandler handler = level.getCapability(Capabilities.ItemHandler.BLOCK, blockPos, side);
							if (handler != null) handlers.add(handler);
						}
						if (!handlers.isEmpty()) {
							inputMap.put(handlers, Vec3.upFromBottomCenterOf(blockPos, 1.9D));
						}
					} else { // Output if its outside that range
						for (Direction side : Direction.values()) {
							IItemHandler handler = level.getCapability(Capabilities.ItemHandler.BLOCK, blockPos, side);
							if (handler != null) outputMap.put(handler, Vec3.upFromBottomCenterOf(blockPos, 1.9D));
						}
					}
				}
			}
		}

		List<Entity> alreadyUsedForInput = new ArrayList<>(); // Keep track of entities we already have for inputs, so we can skip over them when looking for outputs

		level.getEntities((Entity) null, new AABB(pos).inflate(2), entity -> entity.isAlive() && entity.getType().is(EntityTagGenerator.SORTABLE_ENTITIES)).forEach(entity -> {
			List<IItemHandler> handlers = new ArrayList<>();
			for (Direction side : Direction.values()) {
				IItemHandler handler = entity.getCapability(Capabilities.ItemHandler.ENTITY_AUTOMATION, side);
				if (handler != null) handlers.add(handler);
			}
			if (!handlers.isEmpty()) {
				inputMap.put(handlers, entity.position().add(0D, entity.getBbHeight() + 0.9D, 0D));
				alreadyUsedForInput.add(entity);
			}
		});

		if (inputMap.isEmpty()) return; // No input

		level.getEntities((Entity) null, new AABB(pos).inflate(16), entity -> entity.isAlive() && !alreadyUsedForInput.contains(entity) && entity.getType().is(EntityTagGenerator.SORTABLE_ENTITIES)).forEach(entity -> {
			for (Direction side : Direction.values()) {
				IItemHandler handler = entity.getCapability(Capabilities.ItemHandler.ENTITY_AUTOMATION, side);
				if (handler != null) outputMap.put(handler, entity.position().add(0D, entity.getBbHeight() + 0.9D, 0D));
			}
		});

		if (outputMap.isEmpty()) return; // No output

		for (Map.Entry<List<IItemHandler>, Vec3> inputHandlers : inputMap.entrySet()) {
			boolean transferred = false;
			for (IItemHandler inputIItemHandler : inputHandlers.getKey()) {
				for (int i = 0; i < inputIItemHandler.getSlots(); i++) {
					ItemStack inputStack = inputIItemHandler.extractItem(i, 1, true);
					if (!inputStack.isEmpty()) {
						// (handler, count) pairs: a Map keyed by count dropped outputs that held the same amount
						List<Map.Entry<IItemHandler, Integer>> outputsByCount = new ArrayList<>();

						for (IItemHandler outputIItemHandler : outputMap.keySet()) {
							int count = 0;
							for (int j = 0; j < outputIItemHandler.getSlots(); j++) {
								ItemStack stack = outputIItemHandler.getStackInSlot(j);
								if (stack.is(inputStack.getItem())) count += stack.getCount();
							}
							if (count > 0) outputsByCount.add(Map.entry(outputIItemHandler, count));
						}
						outputsByCount.sort(Map.Entry.<IItemHandler, Integer>comparingByValue().reversed());

						for (Map.Entry<IItemHandler, Integer> output : outputsByCount) {
							IItemHandler outputIItemHandler = output.getKey();
							int firstProperStack = -1;
							for (int j = 0; j < outputIItemHandler.getSlots(); j++) {
								if (outputIItemHandler.isItemValid(j, inputStack)) {
									ItemStack outputStack = outputIItemHandler.getStackInSlot(j);

									if (firstProperStack == -1 && outputStack.isEmpty()) {
										firstProperStack = j; //We reference the index of the first empty slot, in case there is no stacks that aren't at max size
									} else if (ItemStack.isSameItemSameComponents(inputStack, outputStack)
										&& outputStack.getCount() < outputStack.getMaxStackSize()
										&& outputStack.getCount() < outputIItemHandler.getSlotLimit(j)) {
										firstProperStack = j;
										break;
									}
								}
							}
							if (firstProperStack != -1) { // If there weren't any non-full stacks, we transfer to an empty space instead
								ItemStack newStack = inputIItemHandler.extractItem(i, 1, false);
								if (!newStack.isEmpty() && outputIItemHandler.insertItem(firstProperStack, newStack, true).isEmpty()) {
									outputIItemHandler.insertItem(firstProperStack, newStack, false);
									transferred = true;

									Vec3 xyz = outputMap.get(outputIItemHandler);
									Vec3 diff = inputHandlers.getValue().subtract(xyz);

									ParticlePacket particlePacket = new ParticlePacket();
									double x = diff.x - 0.25D + rand.nextDouble() * 0.5D;
									double y = diff.y - 1.75D + rand.nextDouble() * 0.5D;
									double z = diff.z - 0.25D + rand.nextDouble() * 0.5D;
									particlePacket.queueParticle(TFParticleType.SORTING_PARTICLE.get(), false, xyz, new Vec3(x, y, z).scale(1D / diff.length()));
									PacketDistributor.sendToPlayersNear(level, null, xyz.x(), xyz.y(), xyz.z(), 64.0D, particlePacket);
									break;
								}
							}
						}
					}
					if (transferred) break;// If we transferred the item from this Entry already, we break, since all IItemHandlers in one entry come from the same source
				}
				if (transferred) break; // Again, since we only transfer once per source, break
			}
		}
	}
}

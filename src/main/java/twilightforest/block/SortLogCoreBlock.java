package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandlerUtil;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jspecify.annotations.Nullable;
import twilightforest.config.TFConfig;
import twilightforest.init.TFParticleType;
import twilightforest.network.ParticlePacket;
import twilightforest.tags.TFEntityTypeTags;
import twilightforest.util.BlockCapabilityDirectionalCache;
import twilightforest.util.WorldUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SortLogCoreBlock extends SpecialMagicLogBlock {

	private final BlockCapabilityDirectionalCache<ResourceHandler<ItemResource>> capabilityCache = new BlockCapabilityDirectionalCache<>(Capabilities.Item.BLOCK);

	public SortLogCoreBlock(Properties properties) {
		super(properties);
	}

	@Override
	public boolean doesCoreFunction() {
		return !TFConfig.disableSortingCore;
	}

	@Override
	void performTreeEffect(ServerLevel level, BlockPos pos, RandomSource rand) {
		Map<List<ResourceHandler<ItemResource>>, Vec3> inputMap = new HashMap<>();
		Map<ResourceHandler<ItemResource>, Vec3> outputMap = new HashMap<>();

		for (BlockEntity blockEntity : WorldUtil.getLoadedBlockEntitiesInRange(level, pos, TFConfig.sortingCoreRange)) {
			BlockPos blockPos = blockEntity.getBlockPos();
			if (!blockPos.equals(pos)) {
				// Put it in the input if its within 2 blocks
				if (Math.abs(blockPos.getX() - pos.getX()) <= 2 && Math.abs(blockPos.getY() - pos.getY()) <= 2 && Math.abs(blockPos.getZ() - pos.getZ()) <= 2) {
					List<ResourceHandler<ItemResource>> handlers = new ArrayList<>();
					for (Direction side : Direction.values()) {
						ResourceHandler<ItemResource> handler = this.getItemHandler(level, blockEntity, side);
						if (handler != null) handlers.add(handler);
					}
					if (!handlers.isEmpty()) {
						inputMap.put(handlers, Vec3.upFromBottomCenterOf(blockPos, 1.9D));
					}
				} else { // Output if its outside that range
					for (Direction side : Direction.values()) {
						ResourceHandler<ItemResource> handler = this.getItemHandler(level, blockEntity, side);
						if (handler != null) outputMap.put(handler, Vec3.upFromBottomCenterOf(blockPos, 1.9D));
					}
				}
			}
		}

		List<Entity> alreadyUsedForInput = new ArrayList<>(); // Keep track of entities we already have for inputs, so we can skip over them when looking for outputs

		level.getEntities((Entity) null, new AABB(pos).inflate(2), entity -> entity.isAlive() && entity.is(TFEntityTypeTags.SORTABLE_ENTITIES)).forEach(entity -> {
			List<ResourceHandler<ItemResource>> handlers = new ArrayList<>();
			for (Direction side : Direction.values()) {
				ResourceHandler<ItemResource> handler = entity.getCapability(Capabilities.Item.ENTITY_AUTOMATION, side);
				if (handler != null) handlers.add(handler);
			}
			if (!handlers.isEmpty()) {
				inputMap.put(handlers, entity.position().add(0D, entity.getBbHeight() + 0.9D, 0D));
				alreadyUsedForInput.add(entity);
			}
		});

		if (inputMap.isEmpty()) return; // No input

		level.getEntities((Entity) null, new AABB(pos).inflate(16), entity -> entity.isAlive() && !alreadyUsedForInput.contains(entity) && entity.is(TFEntityTypeTags.SORTABLE_ENTITIES)).forEach(entity -> {
			for (Direction side : Direction.values()) {
				ResourceHandler<ItemResource> handler = entity.getCapability(Capabilities.Item.ENTITY_AUTOMATION, side);
				if (handler != null) outputMap.put(handler, entity.position().add(0D, entity.getBbHeight() + 0.9D, 0D));
			}
		});

		if (outputMap.isEmpty()) return; // No output

		for (Map.Entry<List<ResourceHandler<ItemResource>>, Vec3> inputHandlers : inputMap.entrySet()) {
			boolean transferred = false;
			for (ResourceHandler<ItemResource> inputIItemHandler : inputHandlers.getKey()) {
				for (int i = 0; i < inputIItemHandler.size(); i++) {
					ItemResource itemResource = inputIItemHandler.getResource(i);
					if (itemResource.isEmpty()) continue;
					try (Transaction tx = Transaction.openRoot()) {
						if (inputIItemHandler.extract(i, itemResource, 1, tx) == 0) continue;
						Map<ResourceHandler<ItemResource>, Integer> outputCounts = new HashMap<>();

						for (ResourceHandler<ItemResource> outputHandler : outputMap.keySet()) {
							int count = 0;
							for (int j = 0; j < outputHandler.size(); j++) {
								if (itemResource.equals(outputHandler.getResource(j))) count += outputHandler.getAmountAsInt(j);
							}
							if (count > 0) outputCounts.put(outputHandler, count);
						}

						List<ResourceHandler<ItemResource>> sortedOutputs = outputCounts.entrySet().stream()
							.sorted(Map.Entry.<ResourceHandler<ItemResource>, Integer>comparingByValue().reversed())
							.map(Map.Entry::getKey)
							.toList();

						for (ResourceHandler<ItemResource> outputHandler : sortedOutputs) {
							if (ResourceHandlerUtil.insertStacking(outputHandler, itemResource, 1, tx) == 1) {
								tx.commit();
								transferred = true;

								Vec3 xyz = outputMap.get(outputHandler);
								Vec3 diff = inputHandlers.getValue().subtract(xyz);

								ParticlePacket particlePacket = new ParticlePacket();
								double x = diff.x - 0.25D + rand.nextDouble() * 0.5D;
								double y = diff.y - 1.75D + rand.nextDouble() * 0.5D;
								double z = diff.z - 0.25D + rand.nextDouble() * 0.5D;
								particlePacket.queueParticle(TFParticleType.SORTING_PARTICLE.get(), false, false, xyz, new Vec3(x, y, z).scale(1D / diff.length()));
								PacketDistributor.sendToPlayersNear(level, null, xyz.x(), xyz.y(), xyz.z(), 64.0D, particlePacket);
								break;
							}
						}
					}
					if (transferred) break;// If we transferred the item from this Entry already, we break, since all IItemHandlers in one entry come from the same source
				}
				if (transferred) break; // Again, since we only transfer once per source, break
			}
		}
	}

	@Nullable
	ResourceHandler<ItemResource> getItemHandler(ServerLevel level, BlockEntity blockEntity, Direction side) {
		return this.capabilityCache.get(level, blockEntity, side);
	}

	public void clearCapabilityCache(ServerLevel level) {
		this.capabilityCache.clear(level);
	}
}

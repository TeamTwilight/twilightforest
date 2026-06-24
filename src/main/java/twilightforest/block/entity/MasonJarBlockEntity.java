package twilightforest.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ReloadableServerRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.world.AuxiliaryLightManager;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import twilightforest.init.TFBlockEntities;
import twilightforest.network.SetMasonJarItemPacket;

import java.util.List;
import java.util.Optional;

import static net.minecraft.world.level.block.entity.DecoratedPotBlockEntity.WobbleStyle;

public class MasonJarBlockEntity extends JarBlockEntity {
	public static final String TAG_ANGLE = "rotation";

	protected final MasonJarItemStackHandler item;
	protected int itemRotation = 0;

	public MasonJarBlockEntity(BlockPos pos, BlockState state) {
		super(TFBlockEntities.MASON_JAR.get(), pos, state);
		this.item = new MasonJarItemStackHandler(this);
	}

	public MasonJarItemStackHandler getItemHandler() {
		return this.item;
	}

	@Override
	protected void saveAdditional(ValueOutput output) {
		super.saveAdditional(output);
		this.item.serialize(output);
		output.putInt(TAG_ANGLE, this.itemRotation);
	}

	@Override
	protected void loadAdditional(ValueInput input) {
		super.loadAdditional(input);
		this.item.deserialize(input);
		this.itemRotation = input.getIntOr(TAG_ANGLE, 0);
	}

	public boolean fillFromLootTable(ResourceKey<LootTable> lootTableKey, long seed, ServerLevel level) {
		MinecraftServer currentServer = ServerLifecycleHooks.getCurrentServer();
		return this.fillFromLootTable(lootTableKey, seed, level, currentServer.reloadableRegistries());
	}

	public boolean fillFromLootTable(ResourceKey<LootTable> lootTableKey, long seed, ServerLevel serverLevel, ReloadableServerRegistries.Holder holder) {
		LootTable lootTable = holder.getLootTable(lootTableKey);

		if (lootTable == LootTable.EMPTY) return false;

		LootParams params = new LootParams.Builder(serverLevel).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(this.getBlockPos())).create(LootContextParamSets.CHEST);
		lootTable.getRandomItems(new LootContext.Builder(params).withOptionalRandomSeed(seed).create(Optional.of(lootTableKey.identifier())), this::acceptLootTable);
		return true;
	}

	private void acceptLootTable(ItemStack stack) {
		MasonJarItemStackHandler jarInv = this.getItemHandler();
		if (jarInv.isEmpty()) {
			jarInv.setItem(stack);
		} else {
			ItemStack contained = jarInv.peekItem();
			// Merge stack in if there's already an item inside
			if (ItemStack.isSameItemSameComponents(contained, stack)) {
				contained.setCount(Math.min(contained.getCount() + stack.getCount(), contained.getMaxStackSize()));
			}
		}
	}

	public void setFromItem(ItemStack stack) {
		this.applyComponentsFromItemStack(stack);
	}

	@Override
	protected void collectImplicitComponents(DataComponentMap.Builder builder) {
		super.collectImplicitComponents(builder);
		builder.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(List.of(this.item.getItem())));
	}

	@Override
	protected void applyImplicitComponents(DataComponentGetter components) {
		super.applyImplicitComponents(components);
		this.item.setItem(components.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY).copyOne());
	}

	@Override
	public void removeComponentsFromTag(ValueOutput output) {
		super.removeComponentsFromTag(output);
		output.discard(ItemStacksResourceHandler.VALUE_IO_KEY);
	}

	@Override
	@SuppressWarnings("deprecation")
	public void setChanged() {
		super.setChanged();
		if (this.level != null) {
			BlockPos pos = this.getBlockPos();
			AuxiliaryLightManager lightManager = this.level.getAuxLightManager(pos);
			if (lightManager != null) {
				lightManager.setLightAt(pos, this.item.getItem().getItem() instanceof BlockItem blockItem ? blockItem.getBlock().defaultBlockState().getLightEmission() : 0);
			}
			this.level.getLightEngine().checkBlock(pos);
		}
		if (this.level instanceof ServerLevel serverLevel) {
			PacketDistributor.sendToPlayersTrackingChunk(serverLevel, ChunkPos.containing(this.getBlockPos()), new SetMasonJarItemPacket(this.getBlockPos(), this.item.getItem(), this.itemRotation));
		}
	}

	public int getItemRotation() {
		return this.itemRotation;
	}

	public void setItemRotation(int itemRotation) {
		this.itemRotation = itemRotation;
	}

	public static class MasonJarItemStackHandler extends ItemStacksResourceHandler {
		protected final MasonJarBlockEntity jarEntity;

		public MasonJarItemStackHandler(MasonJarBlockEntity jarEntity) {
			super(1);
			this.jarEntity = jarEntity;
		}

		// Used for simple checks of what the one item is, without going through all the hoops. Used by the renderer and when saving contents to item
		public ItemStack getItem() {
			return this.stacks.getFirst().copy();
		}

		// Peeks at the stored item, without cloning it
		private ItemStack peekItem() {
			return this.stacks.getFirst();
		}

		// Used when syncing to client and when placing a jar that already has stored items
		public void setItem(ItemStack itemStack) {
			this.stacks.set(0, itemStack);
		}

		@Override
		public boolean isValid(int index, ItemResource resource) {
			return resource.toStack().canFitInsideContainerItems();
		}

		@Override
		public int extract(int index, ItemResource resource, int amount, TransactionContext transaction) {
			int extracted = super.extract(index, resource, amount, transaction);
			if (extracted > 0) {
				this.jarEntity.wobble(WobbleStyle.NEGATIVE);
				this.jarEntity.setChanged();
			}
			return extracted;
		}

		@Override
		public int insert(int index, ItemResource resource, int amount, TransactionContext transaction) {
			int inserted = super.insert(index, resource, amount, transaction);
			if (inserted > 0) {
				this.jarEntity.wobble(WobbleStyle.POSITIVE);
				this.jarEntity.setChanged();
			}
			return inserted;
		}

		public boolean isEmpty() {
			return this.stacks.getFirst().isEmpty();
		}
	}
}

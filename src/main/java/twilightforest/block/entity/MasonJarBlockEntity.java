package twilightforest.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.world.AuxiliaryLightManager;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.network.PacketDistributor;
import twilightforest.init.TFBlockEntities;
import twilightforest.network.SetMasonJarItemPacket;

import java.util.List;

import static net.minecraft.world.level.block.entity.DecoratedPotBlockEntity.WobbleStyle;

public class MasonJarBlockEntity extends JarBlockEntity {
	public static final String TAG_ITEM = "item";

	protected final MasonJarItemStackHandler item;

	public MasonJarBlockEntity(BlockPos pos, BlockState state) {
		super(TFBlockEntities.MASON_JAR.get(), pos, state);
		this.item = new MasonJarItemStackHandler(this);
	}

	public MasonJarItemStackHandler getItemHandler() {
		return this.item;
	}

	@Override
	protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.saveAdditional(tag, registries);
		tag.put(TAG_ITEM, this.item.serializeNBT(registries));
	}

	@Override
	protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.loadAdditional(tag, registries);
		this.item.deserializeNBT(registries, tag.getCompound(TAG_ITEM));
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
	protected void applyImplicitComponents(BlockEntity.DataComponentInput input) {
		super.applyImplicitComponents(input);
		this.item.setItem(input.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY).copyOne());
	}

	@Override
	public void removeComponentsFromTag(CompoundTag tag) {
		super.removeComponentsFromTag(tag);
		tag.remove(TAG_ITEM);
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
			PacketDistributor.sendToPlayersTrackingChunk(serverLevel, new ChunkPos(this.getBlockPos()), new SetMasonJarItemPacket(this.getBlockPos(), this.item.getItem()));
		}
	}

	public static class MasonJarItemStackHandler extends ItemStackHandler {
		protected final MasonJarBlockEntity jarEntity;

		public MasonJarItemStackHandler(MasonJarBlockEntity jarEntity) {
			super(1);
			this.jarEntity = jarEntity;
		}

		// Used for simple checks of what the one item is, without going through all the hoops. Used by the renderer and when saving contents to item
		public ItemStack getItem() {
			return this.stacks.getFirst().copy();
		}

		// Used when syncing to client and when placing a jar that already has stored items
		public void setItem(ItemStack itemStack) {
			this.stacks.set(0, itemStack);
		}

		@Override
		public boolean isItemValid(int slot, ItemStack stack) {
			return stack.getItem().canFitInsideContainerItems();
		}

		@Override
		public ItemStack extractItem(int slot, int amount, boolean simulate) {
			if (simulate) return super.extractItem(slot, amount, true);
			ItemStack extractedStack = super.extractItem(slot, amount, false);
			if (!extractedStack.isEmpty()) {
				this.jarEntity.wobble(WobbleStyle.NEGATIVE);
				this.jarEntity.setChanged();
			}
			return extractedStack;
		}

		@Override
		public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
			if (simulate) return super.insertItem(slot, stack, true);
			ItemStack inserted = stack.copy();
			ItemStack returned = super.insertItem(slot, stack, false);
			if (!ItemStack.isSameItemSameComponents(inserted, returned) || inserted.getCount() != returned.getCount()) {
				this.jarEntity.wobble(WobbleStyle.POSITIVE);
				this.jarEntity.setChanged();
			}
			return returned;
		}
	}
}

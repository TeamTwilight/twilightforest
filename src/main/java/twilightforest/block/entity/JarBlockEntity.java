package twilightforest.block.entity;

import com.mojang.serialization.Codec;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import twilightforest.TwilightForestMod;
import twilightforest.block.JarBlock;
import twilightforest.components.item.JarLid;
import twilightforest.init.TFBlockEntities;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFDataComponents;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static net.minecraft.world.level.block.entity.DecoratedPotBlockEntity.WobbleStyle;

public class JarBlockEntity extends BlockEntity {
	public static final Codec<Item> ITEM_CODEC = BuiltInRegistries.ITEM.byNameCodec();
	public static final List<Item> REGISTERED_LOG_LIDS = new ArrayList<>();
	public static final String TAG_LID = "lid";
	public static final ResourceLocation JAR_LID = TwilightForestMod.prefix("jar_lid");
	public static final int EVENT_POT_WOBBLES = 1;

	@NotNull
	public Item lid = TFBlocks.TWILIGHT_OAK_LOG.asItem();
	public long wobbleStartedAtTick;
	@Nullable
	public WobbleStyle lastWobbleStyle;

	public JarBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
		super(type, pos, blockState);
		if (blockState.getBlock() instanceof JarBlock jarBlock) this.lid = jarBlock.getDefaultLid();
	}

	public JarBlockEntity(BlockPos pos, BlockState state) {
		this(TFBlockEntities.JAR.get(), pos, state);
	}

	@Override
	protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.saveAdditional(tag, registries);
		tag.put(TAG_LID, ITEM_CODEC.encodeStart(NbtOps.INSTANCE, this.lid).getOrThrow());
	}

	@Override
	protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.loadAdditional(tag, registries);
		this.lid = tag.contains(TAG_LID) ? ITEM_CODEC.parse(NbtOps.INSTANCE, tag.get(TAG_LID)).result().orElse(TFBlocks.TWILIGHT_OAK_LOG.asItem()) : TFBlocks.TWILIGHT_OAK_LOG.asItem();
	}

	public ItemStack getJarAsItem() {
		return Util.make(this.getBlockState().getBlock().asItem().getDefaultInstance(), jar -> jar.applyComponents(this.collectComponents()));
	}

	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
		return this.saveCustomOnly(registries);
	}

	@Override
	protected void collectImplicitComponents(DataComponentMap.Builder builder) {
		super.collectImplicitComponents(builder);
		builder.set(TFDataComponents.JAR_LID, new JarLid(this.lid));
	}

	@Override
	protected void applyImplicitComponents(DataComponentInput input) {
		super.applyImplicitComponents(input);
		this.lid = input.getOrDefault(TFDataComponents.JAR_LID, new JarLid(TFBlocks.TWILIGHT_OAK_LOG.asItem())).lid();
	}

	@Override
	@SuppressWarnings("deprecation")
	public void removeComponentsFromTag(CompoundTag tag) {
		super.removeComponentsFromTag(tag);
		tag.remove(TAG_LID);
	}

	public void wobble(WobbleStyle style) {
		if (this.level != null && !this.level.isClientSide()) {
			this.level.blockEvent(this.getBlockPos(), this.getBlockState().getBlock(), EVENT_POT_WOBBLES, style.ordinal());
		}
	}

	@Override
	public boolean triggerEvent(int id, int type) {
		if (this.level != null && id == EVENT_POT_WOBBLES && type >= 0 && type < WobbleStyle.values().length) {
			this.wobbleStartedAtTick = this.level.getGameTime();
			this.lastWobbleStyle = WobbleStyle.values()[type];
			return true;
		} else {
			return super.triggerEvent(id, type);
		}
	}
}

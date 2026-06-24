package twilightforest.block.entity;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.block.JarBlock;
import twilightforest.components.item.JarLid;
import twilightforest.init.TFBlockEntities;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFDataComponents;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BooleanSupplier;

import static net.minecraft.world.level.block.entity.DecoratedPotBlockEntity.WobbleStyle;

public class JarBlockEntity extends BlockEntity {
	public static final Codec<Item> ITEM_CODEC = BuiltInRegistries.ITEM.byNameCodec();
	public static final Map<Item, BooleanSupplier> REGISTERED_LOG_LIDS = new HashMap<>();
	public static final String TAG_LID = "lid";
	public static final Identifier JAR_LID = TwilightForestMod.prefix("jar_lid");
	public static final int EVENT_POT_WOBBLES = 1;

	public static void addLid(Item item, BooleanSupplier supplier) {
		REGISTERED_LOG_LIDS.put(item, supplier);
	}

	public static void addLid(Item item) {
		addLid(item, () -> true);
	}

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
	protected void saveAdditional(ValueOutput output) {
		super.saveAdditional(output);
		output.store(TAG_LID, ITEM_CODEC, this.lid);
	}

	@Override
	protected void loadAdditional(ValueInput input) {
		super.loadAdditional(input);
		this.lid = input.read(TAG_LID, ITEM_CODEC).orElse(TFBlocks.TWILIGHT_OAK_LOG.asItem());
	}

	public ItemStack getJarAsItem() {
		return Util.make(this.getBlockState().getBlock().asItem().getDefaultInstance(), jar -> jar.applyComponents(this.collectComponents()));
	}

	@Override
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
	protected void applyImplicitComponents(DataComponentGetter components) {
		super.applyImplicitComponents(components);
		this.lid = components.getOrDefault(TFDataComponents.JAR_LID, new JarLid(TFBlocks.TWILIGHT_OAK_LOG.asItem())).lid();
	}

	@Override
	public void removeComponentsFromTag(ValueOutput output) {
		super.removeComponentsFromTag(output);
		output.discard(TAG_LID);
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

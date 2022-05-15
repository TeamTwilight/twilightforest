package twilightforest.block;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.ModList;
import twilightforest.TFSounds;
import twilightforest.TwilightForestMod;
import twilightforest.block.entity.CicadaBlockEntity;
import twilightforest.block.entity.TFBlockEntities;
import twilightforest.compat.TFCompat;

import javax.annotation.Nullable;
import java.util.List;

public class CicadaBlock extends CritterBlock {
	private static final MutableComponent TOOLTIP = new TranslatableComponent("block.twilightforest.cicada.desc").withStyle(TwilightForestMod.getRarity().color).withStyle(ChatFormatting.ITALIC);

	protected CicadaBlock(Properties properties) {
		super(properties);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new CicadaBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, TFBlockEntities.CICADA.get(), CicadaBlockEntity::tick);
	}

	@Override
	public ItemStack getSquishResult() {
		return new ItemStack(Items.GRAY_DYE, 1);
	}

	@Override
	public void destroy(LevelAccessor level, BlockPos pos, BlockState state) {
		super.destroy(level, pos, state);
		if(level.isClientSide()) Minecraft.getInstance().getSoundManager().stop(TFSounds.CICADA.getLocation(), SoundSource.NEUTRAL);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, BlockGetter getter, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, getter, tooltip, flag);

		if (ModList.get().isLoaded(TFCompat.IE_ID)) {
			tooltip.add(TOOLTIP);
		}
	}
}

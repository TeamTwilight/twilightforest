package twilightforest.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.ResourceLocation;
import static twilightforest.TwilightForestMod.prefix;

public class ItemTFExperiment115 extends BlockItem {
	public static final ResourceLocation THINK = prefix("think");
	public static final ResourceLocation FULL = prefix("full");

	public ItemTFExperiment115(Block block, Properties props) {
		super(block, props);
	}

	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		PlayerEntity player = context.getPlayer();
		if(!player.isSneaking()) {
			ActionResultType actionresulttype = this.tryPlace(new BlockItemUseContext(context));
			return !actionresulttype.isSuccessOrConsume() && this.isFood() ? this.onItemRightClick(context.getWorld(), context.getPlayer(), context.getHand()).getType() : actionresulttype;
		}
		return ActionResultType.PASS;
	}
}

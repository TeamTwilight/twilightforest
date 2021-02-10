package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import twilightforest.inventory.ContainerTFUncrafting;

import javax.annotation.Nullable;

public class BlockTFUncraftingTable extends Block {

	protected BlockTFUncraftingTable() {
		super(Properties.create(Material.WOOD).hardnessAndResistance(2.5F).sound(SoundType.WOOD));
	}

	@Override
	@Deprecated
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		if (!world.isRemote) {
			player.openContainer(state.getContainer(world, pos));
			player.addStat(Stats.INTERACT_WITH_CRAFTING_TABLE);
		}
		return ActionResultType.SUCCESS;
	}

	@Nullable
	@Override
	public INamedContainerProvider getContainer(BlockState state, World world, BlockPos pos) {
		return new SimpleNamedContainerProvider((id, inv, player) -> new ContainerTFUncrafting(id, inv, player.world, IWorldPosCallable.of(world, pos)),
						new TranslationTextComponent(getTranslationKey()));
	}
}

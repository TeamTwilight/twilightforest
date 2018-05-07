package twilightforest.block;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.client.ModelRegisterCallback;
import twilightforest.client.ModelUtils;
import twilightforest.item.TFItems;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Locale;
import java.util.Random;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class BlockTFAuroraSlab extends BlockSlab implements ModelRegisterCallback {

	private static final PropertyEnum<AuroraSlabVariant> VARIANT = PropertyEnum.create("variant", AuroraSlabVariant.class);

	private final boolean isDouble;

	public BlockTFAuroraSlab(boolean isDouble) {
		super(Material.PACKED_ICE);
		this.isDouble = isDouble;
		this.setCreativeTab(TFItems.creativeTab);
		this.setHardness(2.0F);
		this.setResistance(10.0F);
		this.setLightOpacity(isDouble ? 255 : 0);

		IBlockState state = this.blockState.getBaseState().withProperty(VARIANT, AuroraSlabVariant.AURORA);

		if (!this.isDouble()) state = state.withProperty(HALF, EnumBlockHalf.BOTTOM);

		this.setDefaultState(state);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return this.isDouble() ? new BlockStateContainer(this, VARIANT) : new BlockStateContainer(this, VARIANT, HALF);
	}

	@Override
	public String getUnlocalizedName(int meta) {
		return super.getUnlocalizedName();
	}

	@Override
	public boolean isDouble() {
		return isDouble;
	}

	@Override
	public IProperty<?> getVariantProperty() {
		return VARIANT;
	}

	@Override
	public Comparable<?> getTypeForItem(ItemStack stack) {
		return AuroraSlabVariant.AURORA;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(TFBlocks.aurora_slab);
	}

	@Override
	protected ItemStack getSilkTouchDrop(IBlockState state) {
		return new ItemStack(Item.getItemFromBlock(TFBlocks.aurora_slab), 2, 0);
	}

	@Override
	@Deprecated
	public IBlockState getStateFromMeta(int meta) {
		return this.isDouble() ? this.getDefaultState() : this.getDefaultState().withProperty(HALF, EnumBlockHalf.values()[meta % EnumBlockHalf.values().length]);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(HALF).ordinal();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModel() {
		if (this.isDouble())
			ModelLoader.setCustomStateMapper(this, new StateMap.Builder().ignore(VARIANT).ignore(HALF).build());
		else {
			ModelLoader.setCustomStateMapper(this, new StateMap.Builder().ignore(VARIANT).build());
			ModelUtils.registerToState(this, 0, getDefaultState());
		}
	}

	private enum AuroraSlabVariant implements IStringSerializable {
		AURORA;

		@Override
		public String getName() {
			return name().toLowerCase(Locale.ROOT);
		}
	}
}

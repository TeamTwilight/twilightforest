package twilightforest.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidBase;
import twilightforest.TwilightForestMod;
import twilightforest.block.enums.FireJetVariant;
import twilightforest.client.ModelRegisterCallback;
import twilightforest.client.ModelUtils;
import twilightforest.item.TFItems;
import twilightforest.tileentity.TileEntityTFFlameJet;
import twilightforest.tileentity.TileEntityTFPoppingJet;
import twilightforest.tileentity.TileEntityTFSmoker;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockTFFireJet extends Block implements ModelRegisterCallback {

	public static final PropertyEnum<FireJetVariant> VARIANT = PropertyEnum.create("variant", FireJetVariant.class);

	protected BlockTFFireJet() {
		super(Material.ROCK);
		this.setHardness(1.5F);
		//this.setResistance(10.0F);
		this.setSoundType(SoundType.WOOD);
		this.setCreativeTab(TFItems.creativeTab);
		this.setTickRandomly(true);
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, VARIANT);
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		final FireJetVariant[] values = FireJetVariant.values();
		final FireJetVariant variant = values[meta % values.length];

		return getDefaultState().withProperty(VARIANT, variant);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{

		final FireJetVariant value = state.getValue(VARIANT);

		return value.ordinal();
	}

	@Override
	public int damageDropped(IBlockState state) {
		switch (state.getValue(VARIANT)) {
			case ENCASED_SMOKER_ON: state = state.withProperty(VARIANT, FireJetVariant.ENCASED_SMOKER_OFF); break;
			case ENCASED_JET_POPPING: state = state.withProperty(VARIANT, FireJetVariant.ENCASED_JET_IDLE); break;
			case ENCASED_JET_FLAME: state = state.withProperty(VARIANT, FireJetVariant.ENCASED_JET_IDLE); break;
			case JET_POPPING: state = state.withProperty(VARIANT, FireJetVariant.JET_IDLE); break;
			case JET_FLAME: state = state.withProperty(VARIANT, FireJetVariant.JET_IDLE); break;
		}

		return getMetaFromState(state);
	}

    @Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos)
    {
    	if (world.getBlockState(pos) == state)
    	{
			switch (state.getValue(VARIANT)) {
				case JET_FLAME:
				case ENCASED_JET_FLAME: return 15;
				case SMOKER:
				default: return 0;
			}
    	}
    	else
    	{
    		return 0;
    	}
    }
    
    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random random)
    {
    	// idle jets may turn active
    	if (!world.isRemote && state.getValue(VARIANT) == FireJetVariant.JET_IDLE) {
    		BlockPos lavaPos = findLavaAround(world, pos.down());
    		
    		if (isLava(world, lavaPos))
    		{
    			// deplete lava reservoir
    			world.setBlockState(lavaPos, Blocks.AIR.getDefaultState(), 2);
    			// change jet state
    			world.setBlockState(pos, state.withProperty(VARIANT, FireJetVariant.JET_POPPING), 0);
    		}
    		else
    		{
    			//System.out.println("Can't fire jet because reservoir is empty.  Block  meta is " + world.getBlockMetadata(lavaPos.posX, lavaPos.posY, lavaPos.posZ));
    		}
    	}
	}
    
    @Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block myBlockID)
    {
        if (!world.isRemote)
        {
			FireJetVariant variant = state.getValue(VARIANT);
			boolean powered = world.isBlockIndirectlyGettingPowered(pos) > 0;
			
        	if (variant == FireJetVariant.ENCASED_SMOKER_OFF && powered)
        	{
    			world.setBlockState(pos, state.withProperty(VARIANT, FireJetVariant.ENCASED_SMOKER_ON), 3);
                world.playSound(null, pos, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 0.3F, 0.6F);
        	}
        	
        	if (variant == FireJetVariant.ENCASED_SMOKER_ON && !powered)
        	{
				world.setBlockState(pos, state.withProperty(VARIANT, FireJetVariant.ENCASED_SMOKER_OFF), 3);
				world.playSound(null, pos, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 0.3F, 0.6F);
        	}
        	
        	if (variant == FireJetVariant.ENCASED_JET_IDLE && powered)
        	{
				world.setBlockState(pos, state.withProperty(VARIANT, FireJetVariant.ENCASED_JET_POPPING), 3);
				world.playSound(null, pos, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 0.3F, 0.6F);
        	}

        }
    }

    /**
     * Find a full block of lava near the designated block.  This is intentionally not really that reliable.
     */
    private BlockPos findLavaAround(World world, BlockPos pos) {
		if (isLava(world, pos)) {
			return pos;
		}

		// try three random spots nearby
		for (int i = 0; i < 3; i++) {
			BlockPos randPos = pos.add(world.rand.nextInt(3) - 1, 0, world.rand.nextInt(3) - 1);
			if (isLava(world, randPos)) {
				return randPos;
			}
		}
		
		// finally, give up
		return pos;
	}
    
    private boolean isLava(World world, BlockPos pos)
    {
		IBlockState state = world.getBlockState(pos);
		Block b = state.getBlock();
		IProperty<Integer> levelProp = b instanceof BlockLiquid
				? BlockLiquid.LEVEL
				: b instanceof BlockFluidBase
					? BlockFluidBase.LEVEL
					: null;
    	return state.getMaterial() == Material.LAVA
				&& (levelProp == null || state.getValue(levelProp) == 0);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
		FireJetVariant variant = state.getValue(VARIANT);
		
		return variant == FireJetVariant.SMOKER
				|| variant == FireJetVariant.JET_POPPING
				|| variant == FireJetVariant.JET_FLAME
				|| variant == FireJetVariant.ENCASED_SMOKER_ON
				|| variant == FireJetVariant.ENCASED_JET_POPPING
				|| variant == FireJetVariant.ENCASED_JET_FLAME;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
		FireJetVariant variant = state.getValue(VARIANT);
		
    	if (variant == FireJetVariant.SMOKER || variant == FireJetVariant.ENCASED_SMOKER_ON)
    	{
    		return new TileEntityTFSmoker();
    	}
    	else if (variant == FireJetVariant.JET_POPPING)
    	{
    		return new TileEntityTFPoppingJet(FireJetVariant.JET_FLAME);
    	}
    	else if (variant == FireJetVariant.JET_FLAME)
    	{
    		return new TileEntityTFFlameJet(FireJetVariant.JET_IDLE);
    	}
    	else if (variant == FireJetVariant.ENCASED_JET_POPPING)
    	{
    		return new TileEntityTFPoppingJet(FireJetVariant.ENCASED_JET_FLAME);
    	}
    	else if (variant == FireJetVariant.ENCASED_JET_FLAME)
    	{
    		return new TileEntityTFFlameJet(FireJetVariant.ENCASED_JET_IDLE);
    	}
    	else
    	{
    		return null;
    	}
    }

    @Override
	public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List)
    {
        par3List.add(new ItemStack(par1, 1, FireJetVariant.SMOKER.ordinal()));
        par3List.add(new ItemStack(par1, 1, FireJetVariant.JET_IDLE.ordinal()));
        par3List.add(new ItemStack(par1, 1, FireJetVariant.ENCASED_SMOKER_OFF.ordinal()));
        par3List.add(new ItemStack(par1, 1, FireJetVariant.ENCASED_JET_IDLE.ordinal()));
    }

    @SideOnly(Side.CLIENT)
    @Override
	public void registerModel() {
    	FireJetVariant[] variants = { FireJetVariant.SMOKER, FireJetVariant.JET_IDLE, FireJetVariant.ENCASED_SMOKER_OFF, FireJetVariant.ENCASED_JET_IDLE };
    	for (int i = 0; i < variants.length; i++) {
			ModelUtils.registerToState(this, variants[i].ordinal(), getDefaultState().withProperty(VARIANT, variants[i]));
		}
	}

}

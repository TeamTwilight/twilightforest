package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.enums.HedgeVariant;
import twilightforest.client.ModelRegisterCallback;
import twilightforest.client.ModelUtils;
import twilightforest.item.TFItems;

import java.util.List;
import java.util.Random;

public class BlockTFHedge extends Block implements ModelRegisterCallback {
	public static final PropertyEnum<HedgeVariant> VARIANT = PropertyEnum.create("variant", HedgeVariant.class);
	private static final AxisAlignedBB HEDGE_BB = new AxisAlignedBB(0, 0, 0, 1, 0.9375, 1);

	private final int damageDone;

	protected BlockTFHedge() {
		super(Material.CACTUS);
		this.damageDone = 3;
		this.setSoundType(SoundType.PLANT);
		this.setHardness(2F);
		this.setResistance(10F);
		this.setCreativeTab(TFItems.creativeTab);
	}

	@Override
	@Deprecated
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		return blockAccess.getBlockState(pos.offset(side)).getBlock() != this && super.shouldSideBeRendered(blockState, blockAccess, pos, side);
	}

	@Override
	@Deprecated
	public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		if (state.getValue(VARIANT) == HedgeVariant.HEDGE) {
			return HEDGE_BB;
		} else {
			return FULL_BLOCK_AABB;
		}
	}

	@Override
	@Deprecated
	public boolean isOpaqueCube(IBlockState state) {
		return true;
	}

	@Override
	public int damageDropped(IBlockState state) {
		if (state.getValue(VARIANT) == HedgeVariant.DARKWOOD_LEAVES) {
			// Darkwood sapling
			return 3;
		} else {
			return getMetaFromState(state);
		}
	}

	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
		if (state.getValue(VARIANT) == HedgeVariant.HEDGE && shouldDamage(entity)) {
			entity.attackEntityFrom(DamageSource.CACTUS, damageDone);
		}
	}

	@Override
	public void onEntityWalk(World world, BlockPos pos, Entity entity) {
		if (world.getBlockState(pos).getValue(VARIANT) == HedgeVariant.HEDGE && shouldDamage(entity)) {
			entity.attackEntityFrom(DamageSource.CACTUS, damageDone);
		}
	}

	@Override
	public void onBlockClicked(World world, BlockPos pos, EntityPlayer entityplayer) {
		if (!world.isRemote && world.getBlockState(pos).getValue(VARIANT) == HedgeVariant.HEDGE) {
			world.scheduleUpdate(pos, this, 10);
		}
	}

	@Override
	public void harvestBlock(World world, EntityPlayer entityplayer, BlockPos pos, IBlockState state, TileEntity te, ItemStack stack) {
		super.harvestBlock(world, entityplayer, pos, state, te, stack);
		if (state.getValue(VARIANT) == HedgeVariant.HEDGE) {
			entityplayer.attackEntityFrom(DamageSource.CACTUS, damageDone);
		}
	}


	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random random) {
		double range = 4.0; // do we need to get this with a better method than hardcoding it?

		// find players within harvest range
		List<EntityPlayer> nearbyPlayers = world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(pos, pos.add(1, 1, 1)).grow(range, range, range));

		// are they swinging?
		for (EntityPlayer player : nearbyPlayers) {
			if (player.isSwingInProgress) {
				// are they pointing at this block?
				RayTraceResult mop = getPlayerPointVec(world, player, range);

				if (mop != null && mop.getBlockPos() != null
						&& world.getBlockState(mop.getBlockPos()).getBlock() == this) {
					// prick them!  prick them hard!
					player.attackEntityFrom(DamageSource.CACTUS, damageDone);

					// trigger this again!
					world.scheduleUpdate(pos, this, 10);
				}
			}
		}
	}


	/**
	 * [VanillaCopy] Exact copy of Entity.rayTrace
	 * todo 1.9 update it
	 */
	private RayTraceResult getPlayerPointVec(World world, EntityPlayer player, double range) {
		Vec3d position = new Vec3d(player.posX, player.posY + player.getEyeHeight(), player.posZ);
		Vec3d look = player.getLook(1.0F);
		Vec3d dest = position.addVector(look.x * range, look.y * range, look.z * range);
		return world.rayTraceBlocks(position, dest);
	}

	private boolean shouldDamage(Entity entity) {
		return !(entity instanceof EntitySpider) && !(entity instanceof EntityItem) && !entity.doesEntityNotTriggerPressurePlate();
	}

	@Override
	public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing side) {
		IBlockState state = world.getBlockState(pos);
		return state.getValue(VARIANT) == HedgeVariant.DARKWOOD_LEAVES ? 1 : 0;
	}

	@Override
	public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing side) {
		return 0;
	}

	@Override
	public int quantityDropped(Random par1Random) {
		return par1Random.nextInt(40) == 0 ? 1 : 0;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random par2Random, int par3) {
		if (state.getValue(VARIANT) == HedgeVariant.DARKWOOD_LEAVES) {
			return Item.getItemFromBlock(TFBlocks.twilight_sapling);
		} else {
			return null;
		}
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(this, 1, getMetaFromState(state));
	}

	@Override
	public void dropBlockAsItemWithChance(World par1World, BlockPos pos, IBlockState state, float par6, int fortune) {
		if (!par1World.isRemote && state.getValue(VARIANT) == HedgeVariant.DARKWOOD_LEAVES) {
			if (par1World.rand.nextInt(40) == 0) {
				this.dropBlockAsItem(par1World, pos, state, fortune);
			}
		}
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, VARIANT);
	}

	@Override
	@Deprecated
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(VARIANT, HedgeVariant.values()[meta % HedgeVariant.values().length]);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(VARIANT).ordinal();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModel() {
		ModelUtils.registerToStateSingleVariant(this, VARIANT);
	}

}


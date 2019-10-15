package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.enums.HedgeVariant;
import twilightforest.client.ModelRegisterCallback;
import twilightforest.client.ModelUtils;
import twilightforest.item.TFItems;
import twilightforest.util.EntityUtil;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockTFHedge extends Block implements ModelRegisterCallback {

	public static final IProperty<HedgeVariant> VARIANT = PropertyEnum.create("variant", HedgeVariant.class);
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

	@Nullable
	@Override
	public PathNodeType getAiPathNodeType(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable EntityLiving entity) {
		return entity != null && state.getValue(VARIANT) == HedgeVariant.HEDGE && shouldDamage(entity) ? PathNodeType.DAMAGE_CACTUS : null;
	}

	@Override
	public void onEntityCollision(World world, BlockPos pos, IBlockState state, Entity entity) {
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
	public void onBlockClicked(World world, BlockPos pos, EntityPlayer player) {
		if (!world.isRemote && world.getBlockState(pos).getValue(VARIANT) == HedgeVariant.HEDGE) {
			world.scheduleUpdate(pos, this, 10);
		}
	}

	@Override
	public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack) {
		super.harvestBlock(world, player, pos, state, te, stack);
		if (state.getValue(VARIANT) == HedgeVariant.HEDGE) {
			player.attackEntityFrom(DamageSource.CACTUS, damageDone);
		}
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random random) {
		// find players within range
		List<EntityPlayer> nearbyPlayers = world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(pos).grow(8.0));

		for (EntityPlayer player : nearbyPlayers) {
			// are they swinging?
			if (player.isSwingInProgress) {
				RayTraceResult ray = EntityUtil.rayTrace(player);
				// are they pointing at this block?
				if (ray != null && ray.typeOfHit == RayTraceResult.Type.BLOCK && pos.equals(ray.getBlockPos())) {
					// prick them!  prick them hard!
					player.attackEntityFrom(DamageSource.CACTUS, damageDone);

					// trigger this again!
					world.scheduleUpdate(pos, this, 10);
				}
			}
		}
	}

	private boolean shouldDamage(Entity entity) {
		return !(entity instanceof EntitySpider || entity instanceof EntityItem || entity.doesEntityNotTriggerPressurePlate());
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
	public int quantityDropped(Random random) {
		return random.nextInt(40) == 0 ? 1 : 0;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random random, int fortune) {
		if (state.getValue(VARIANT) == HedgeVariant.DARKWOOD_LEAVES) {
			return Item.getItemFromBlock(TFBlocks.twilight_sapling);
		} else {
			return Items.AIR;
		}
	}

	@Override
	public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
		return new ItemStack(this, 1, getMetaFromState(state));
	}

	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		if (state.getValue(VARIANT) == HedgeVariant.DARKWOOD_LEAVES) {
			Random rand = world instanceof World ? ((World)world).rand : RANDOM;
			if (rand.nextInt(40) == 0) {
				Item item = this.getItemDropped(state, rand, fortune);
				drops.add(new ItemStack(item, 1, this.damageDropped(state)));
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

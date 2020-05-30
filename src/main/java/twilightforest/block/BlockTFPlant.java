package twilightforest.block;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.PlantType;
import twilightforest.client.particle.ParticleLeaf;
import twilightforest.enums.PlantVariant;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockTFPlant extends BushBlock implements IShearable {

	public final PlantVariant plantVariant;

	protected BlockTFPlant(PlantVariant plant, Block.Properties props) {
		super(props);
		plantVariant = plant;
	}

	//TODO: Neither methods exist
//	@Override
//	public void onBlockAdded(World world, BlockPos pos, BlockState state) {
//		world.scheduleUpdate(pos, this, world.rand.nextInt(50) + 20);
//	}

	@Override
	public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos) {
		BlockState soil = world.getBlockState(pos.down());

		//TODO: Verify if this logic actually works
		/*
			Comment from superclass:
			Forge: This function is called during world gen and placement, before this block is set, so if we are not 'here' then assume it's the pre-check.
			Therefore, we just take the OR of all the conditions below as the most general "can block stay" check
		*/
		if (state.getBlock() != this) {
			return BlockTFPlant.canPlaceRootAt(world, pos)
					|| soil.getBlock().canSustainPlant(soil, world, pos.down(), Direction.UP, this)
					|| soil.isSideSolidFullSquare(world, pos, Direction.UP)
					|| ((world.getLight(pos) >= 3 || world.canBlockSeeSky(pos)) && soil.getBlock().canSustainPlant(soil, world, pos.down(), Direction.UP, this));
		} else {
			switch (plantVariant) {
				case TORCHBERRY:
				case ROOT_STRAND:
					return BlockTFPlant.canPlaceRootAt(world, pos);
				case FORESTGRASS:
				case DEADBUSH:
					return soil.getBlock().canSustainPlant(soil, world, pos.down(), Direction.UP, this);
				case FALLEN_LEAVES:
				case MUSHGLOOM:
				case MOSSPATCH:
					return soil.isSideSolidFullSquare(world, pos, Direction.UP);
				default:
					return (world.getLight(pos) >= 3 || world.canBlockSeeSky(pos)) && soil.getBlock().canSustainPlant(soil, world, pos.down(), Direction.UP, this);
			}
		}
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, IBlockReader access, BlockPos pos, ISelectionContext context) {
		long seed = pos.getX() * 3129871 ^ pos.getY() * 116129781L ^ pos.getZ();
		seed = seed * seed * 42317861L + seed * 11L;

		if (plantVariant == PlantVariant.MOSSPATCH) {
			int xOff0 = (int) (seed >> 12 & 3L);
			int xOff1 = (int) (seed >> 15 & 3L);
			int zOff0 = (int) (seed >> 18 & 3L);
			int zOff1 = (int) (seed >> 21 & 3L);

			boolean xConnect0 = access.getBlockState(pos.east()).getBlock() == this && access.getBlockState(pos.east()).getBlock() == TFBlocks.moss_patch.get();
			boolean xConnect1 = access.getBlockState(pos.west()).getBlock() == this && access.getBlockState(pos.west()).getBlock() == TFBlocks.moss_patch.get();
			boolean zConnect0 = access.getBlockState(pos.south()).getBlock() == this && access.getBlockState(pos.north()).getBlock() == TFBlocks.moss_patch.get();
			boolean zConnect1 = access.getBlockState(pos.north()).getBlock() == this && access.getBlockState(pos.south()).getBlock() == TFBlocks.moss_patch.get();

			return VoxelShapes.create(new AxisAlignedBB(xConnect1 ? 0F : (1F + xOff1) / 16F, 0.0F, zConnect1 ? 0F : (1F + zOff1) / 16F,
					xConnect0 ? 1F : (15F - xOff0) / 16F, 1F / 16F, zConnect0 ? 1F : (15F - zOff0) / 16F));

		} else if (plantVariant == PlantVariant.CLOVERPATCH) {
			int xOff0 = (int) (seed >> 12 & 3L);
			int xOff1 = (int) (seed >> 15 & 3L);
			int zOff0 = (int) (seed >> 18 & 3L);
			int zOff1 = (int) (seed >> 21 & 3L);

			int yOff0 = (int) (seed >> 24 & 1L);
			int yOff1 = (int) (seed >> 27 & 1L);

			boolean xConnect0 = access.getBlockState(pos.east()).getBlock() == this && access.getBlockState(pos.east()).getBlock() == TFBlocks.clover_patch.get();
			boolean xConnect1 = access.getBlockState(pos.west()).getBlock() == this && access.getBlockState(pos.west()).getBlock() == TFBlocks.clover_patch.get();
			boolean zConnect0 = access.getBlockState(pos.south()).getBlock() == this && access.getBlockState(pos.north()).getBlock() == TFBlocks.clover_patch.get();
			boolean zConnect1 = access.getBlockState(pos.north()).getBlock() == this && access.getBlockState(pos.south()).getBlock() == TFBlocks.clover_patch.get();

			return VoxelShapes.create(new AxisAlignedBB(xConnect1 ? 0F : (1F + xOff1) / 16F, 0.0F, zConnect1 ? 0F : (1F + zOff1) / 16F,
					xConnect0 ? 1F : (15F - xOff0) / 16F, (1F + yOff0 + yOff1) / 16F, zConnect0 ? 1F : (15F - zOff0) / 16F));
		} else if (plantVariant == PlantVariant.MAYAPPLE) {
			return VoxelShapes.create(new AxisAlignedBB(4F / 16F, 0, 4F / 16F, 13F / 16F, 6F / 16F, 13F / 16F));
		} else if (plantVariant == PlantVariant.FALLEN_LEAVES) {
			return VoxelShapes.create(new AxisAlignedBB(0F, 0F, 0F, 1F, 1F / 16F, 1F));
		} else {
			return VoxelShapes.fullCube();
		}
	}

	public static boolean canPlaceRootAt(IWorldReader world, BlockPos pos) {
		BlockState state = world.getBlockState(pos.up());
		if (state.getMaterial() == Material.EARTH || state.getMaterial() == Material.ORGANIC) {
			// can always hang below dirt blocks
			return true;
		} else {
			return (state.getBlock() == TFBlocks.root_strand.get()
					|| state == TFBlocks.root.get().getDefaultState());
		}
	}

	@Override
	public OffsetType getOffsetType() {
		return OffsetType.NONE;
	}


//    /** todo thaumcraft
//     * Drops the block items with a specified chance of dropping the specified items
//     */
//    public void dropBlockAsItemWithChance(World var1, int var2, int var3, int var4, int var5, float var6, int var7)
//    {
//        super.dropBlockAsItemWithChance(var1, var2, var3, var4, var5, var6, var7);
//
//        if (!var1.isRemote && var5 == 1)
//        {
//            this.dropBlockAsItem_do(var1, var2, var3, var4, new ItemStack(mod_ThaumCraft.itemComponents, 1, 2));
//        }
//
//        if (!var1.isRemote && var5 == 3)
//        {
//            this.dropBlockAsItem_do(var1, var2, var3, var4, new ItemStack(mod_ThaumCraft.itemPlants, 1, 3));
//        }
//
//        if (!var1.isRemote && (var5 == 2 || var5 == 4) && var1.rand.nextInt(10) == 0)
//        {
//            this.dropBlockAsItem_do(var1, var2, var3, var4, new ItemStack(mod_ThaumCraft.itemArtifactTainted, 1, 0));
//        }
//    }
//    

	//TODO: Move to block loot table
//	@Override
//	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, BlockState state, int fortune) {
//		List<ItemStack> ret = NonNullList.create();
//
//		switch (state.getValue(VARIANT)) {
//			case TORCHBERRY:
//				ret.add(new ItemStack(TFItems.torchberries));
//				break;
//			case FALLEN_LEAVES:
//			case MOSSPATCH:
//			case MAYAPPLE:
//			case CLOVERPATCH:
//			case FIDDLEHEAD:
//			case FORESTGRASS:
//			case DEADBUSH:
//			case ROOT_STRAND:
//				break;
//			default:
//				ret.add(new ItemStack(this, 1, damageDropped(state)));
//				break;
//		}
//
//		return ret;
//	}

	@Override
	public void harvestBlock(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity te, ItemStack stack) {
		// do not call normal harvest if the player is shearing
		if (world.isRemote || stack.getItem() != Items.SHEARS) {
			super.harvestBlock(world, player, pos, state, te, stack);
		}
	}

	@Override
	public PlantType getPlantType(IBlockReader world, BlockPos pos) {
		BlockState blockState = world.getBlockState(pos);
		if (blockState.getBlock() == this) {
			switch (plantVariant) {
				case MOSSPATCH:
				case MUSHGLOOM:
					return PlantType.Cave;
				default:
					return PlantType.Plains;
			}
		}
		return PlantType.Plains;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState state, World world, BlockPos pos, Random random) {
		super.animateTick(state, world, pos, random);

		if (state.getBlock() == TFBlocks.moss_patch.get() && random.nextInt(10) == 0) {
			world.addParticle(ParticleTypes.MYCELIUM, pos.getX() + random.nextFloat(), pos.getY() + 0.1F, pos.getZ() + random.nextFloat(), 0.0D, 0.0D, 0.0D);
		} else if (state.getBlock() == TFBlocks.fallen_leaves.get() && random.nextInt(50) == 0) {
			float dist = 10F;
			if (!world.canBlockSeeSky(pos)) {
				for (int y = 0; y <= dist; y++)
					if (world.getBlockState(pos.up(y)).getMaterial() == Material.LEAVES) {
						dist = y;
						break;
					}
				if (dist > 10F)
					return;
			}
			//Particle leaf = TFParticleFactory.createParticle(TFParticleType.FALLEN_LEAF, world, pos.getX() + random.nextFloat(), pos.getY() + dist - 0.25F, pos.getZ() + random.nextFloat(), 0.0D, 0.0D, 0.0D);
			Particle leaf = new ParticleLeaf(world, pos.getX() + random.nextFloat(), pos.getY() + dist - 0.25F, pos.getZ() + random.nextFloat(), 0.0D, 0.0D, 0.0D);
			int color = Minecraft.getInstance().getBlockColors().getColor(Blocks.OAK_LEAVES.getDefaultState(), world, pos, 0);
			leaf.setColor(

					MathHelper.clamp(((color >> 16) & 0xFF) + RANDOM.nextInt(0x22) - 0x11, 0x00, 0xFF) / 255F,

					MathHelper.clamp(((color >> 8) & 0xFF) + RANDOM.nextInt(0x22) - 0x11, 0x00, 0xFF) / 255F,

					MathHelper.clamp((color & 0xFF) + RANDOM.nextInt(0x22) - 0x11, 0x00, 0xFF) / 255F

			);
			Minecraft.getInstance().particles.addEffect(leaf);
		}

	}

	@Override
	@Deprecated
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entityIn) {
		super.onEntityCollision(state, world, pos, entityIn);
		if (world.isRemote && state.getBlock() == TFBlocks.fallen_leaves.get() && entityIn instanceof LivingEntity && (entityIn.getMotion().getX() != 0 || entityIn.getMotion().getZ() != 0) && RANDOM.nextBoolean()) {
			int color = Minecraft.getInstance().getBlockColors().getColor(Blocks.OAK_LEAVES.getDefaultState(), world, pos, 0);
			Particle leaf = new ParticleLeaf(world,

					pos.getX() + world.rand.nextFloat(),

					pos.getY(),

					pos.getZ() + world.rand.nextFloat(),

					(world.rand.nextFloat() * -0.5F) * entityIn.getMotion().getX(),

					world.rand.nextFloat() * 0.5F + 0.25F,

					(world.rand.nextFloat() * -0.5F) * entityIn.getMotion().getZ()

			);
			leaf.setColor(

					MathHelper.clamp(((color >> 16) & 0xFF) + RANDOM.nextInt(0x22) - 0x11, 0x00, 0xFF) / 255F,

					MathHelper.clamp(((color >> 8) & 0xFF) + RANDOM.nextInt(0x22) - 0x11, 0x00, 0xFF) / 255F,

					MathHelper.clamp((color & 0xFF) + RANDOM.nextInt(0x22) - 0x11, 0x00, 0xFF) / 255F

			);
			Minecraft.getInstance().particles.addEffect(leaf);
		}
	}

	//TODO: Move to client
//	@OnlyIn(Dist.CLIENT)
//	@Override
//	public BlockRenderLayer getRenderLayer() {
//		return BlockRenderLayer.CUTOUT;
//	}
}

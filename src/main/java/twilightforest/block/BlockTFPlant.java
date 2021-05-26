package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.BushBlock;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.PlantType;
import net.minecraftforge.fml.network.PacketDistributor;
import twilightforest.client.particle.LeafParticleData;
import twilightforest.enums.PlantVariant;
import twilightforest.network.PacketSpawnFallenLeafFrom;
import twilightforest.network.TFPacketHandler;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockTFPlant extends BushBlock {
	private static final VoxelShape MAYAPPLE_SHAPE = makeCuboidShape(4, 0, 4, 13, 6, 13);
	private static final VoxelShape FALLEN_LEAVES_SHAPE = makeCuboidShape(0, 0, 0, 16, 1, 16);

	public final PlantVariant plantVariant;

	protected BlockTFPlant(PlantVariant plant, Block.Properties props) {
		super(props);
		plantVariant = plant;
	}

	@Override
	public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos) {
		BlockState soil = world.getBlockState(pos.down());

		switch (plantVariant) {
		case TORCHBERRY:
		case ROOT_STRAND:
			return BlockTFPlant.canPlaceRootAt(world, pos);
		case FALLEN_LEAVES:
		case MUSHGLOOM:
		case MOSSPATCH:
			return soil.isSolidSide(world, pos, Direction.UP);
		default:
			return (world.getLight(pos) >= 3 || world.canBlockSeeSky(pos)) && soil.canSustainPlant(world, pos.down(), Direction.UP, this);
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
			return MAYAPPLE_SHAPE;
		} else if (plantVariant == PlantVariant.FALLEN_LEAVES) {
			return FALLEN_LEAVES_SHAPE;
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
	public PlantType getPlantType(IBlockReader world, BlockPos pos) {
		BlockState blockState = world.getBlockState(pos);
		if (blockState.getBlock() == this) {
			switch (plantVariant) {
				case MOSSPATCH:
				case MUSHGLOOM:
					return PlantType.CAVE;
				default:
					return PlantType.PLAINS;
			}
		}
		return PlantType.PLAINS;
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

			int color = Minecraft.getInstance().getBlockColors().getColor(Blocks.OAK_LEAVES.getDefaultState(), world, pos, 0);
			int r = MathHelper.clamp(((color >> 16) & 0xFF) + RANDOM.nextInt(0x22) - 0x11, 0x00, 0xFF);
			int g = MathHelper.clamp(((color >> 8) & 0xFF) + RANDOM.nextInt(0x22) - 0x11, 0x00, 0xFF);
			int b = MathHelper.clamp((color & 0xFF) + RANDOM.nextInt(0x22) - 0x11, 0x00, 0xFF);
			world.addParticle(new LeafParticleData(r, g, b), pos.getX() + random.nextFloat(), pos.getY() + dist - 0.25F, pos.getZ() + random.nextFloat(), 0.0D, 0.0D, 0.0D);
		}
	}

	@Override
	@Deprecated
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entityIn) {
		super.onEntityCollision(state, world, pos, entityIn);
		if (state.getBlock() == TFBlocks.fallen_leaves.get() && entityIn instanceof LivingEntity && (entityIn.getMotion().getX() != 0 || entityIn.getMotion().getZ() != 0) && RANDOM.nextBoolean()) {
			if(world.isRemote) {
				int color = Minecraft.getInstance().getBlockColors().getColor(Blocks.OAK_LEAVES.getDefaultState(), world, pos, 0);
				int r = MathHelper.clamp(((color >> 16) & 0xFF) + RANDOM.nextInt(0x22) - 0x11, 0x00, 0xFF);
				int g = MathHelper.clamp(((color >> 8) & 0xFF) + RANDOM.nextInt(0x22) - 0x11, 0x00, 0xFF);
				int b = MathHelper.clamp((color & 0xFF) + RANDOM.nextInt(0x22) - 0x11, 0x00, 0xFF);
					world.addParticle(new LeafParticleData(r, g, b),
							pos.getX() + world.rand.nextFloat(),
							pos.getY(),
							pos.getZ() + world.rand.nextFloat(),

							(world.rand.nextFloat() * -0.5F) * entityIn.getMotion().getX(),
							world.rand.nextFloat() * 0.5F + 0.25F,
							(world.rand.nextFloat() * -0.5F) * entityIn.getMotion().getZ()
				);
			} else if (world instanceof ServerWorld)
				TFPacketHandler.CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> entityIn), new PacketSpawnFallenLeafFrom(pos, entityIn.getMotion()));
		}
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		if(stack.getItem() == TFBlocks.clover_patch.get().asItem()) {
			tooltip.add(new TranslationTextComponent("twilightforest.misc.nyi"));
		} else if (stack.getItem() == TFBlocks.moss_patch.get().asItem()) {
			tooltip.add(new TranslationTextComponent("twilightforest.misc.wip0"));
			tooltip.add(new TranslationTextComponent("twilightforest.misc.wip1"));
		}
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}
}

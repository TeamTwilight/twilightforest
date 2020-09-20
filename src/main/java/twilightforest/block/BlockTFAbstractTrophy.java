package twilightforest.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.pathfinding.PathType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.TFSounds;
import twilightforest.enums.BossVariant;
import twilightforest.tileentity.TileEntityTFTrophy;

//[VanillaCopy] of AbstractSkullBlock except uses Variants instead of ISkullType and adds Sounds when clicked
public abstract class BlockTFAbstractTrophy extends ContainerBlock {

	private final BossVariant variant;
	
	protected BlockTFAbstractTrophy(BossVariant variant, AbstractBlock.Properties builder) {
		super(builder);
		this.variant = variant;
	}
	
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand handIn, BlockRayTraceResult hit) {
		TileEntity te = worldIn.getTileEntity(pos);
		if (te instanceof TileEntityTFTrophy) {
			SoundEvent sound = null;
			float volume = 1.0F;
			float pitch = 1.0F;
			switch (variant) {
				case NAGA:
					sound = TFSounds.NAGA_RATTLE;
					volume = 1.25F;
					pitch = 1.2F;
					break;
				case LICH:
					sound = TFSounds.LICH_AMBIENT;
					volume = 0.35F;
					pitch = 1.1F;
					break;
				case HYDRA:
					sound = TFSounds.HYDRA_GROWL;
					pitch = 1.2F;
					break;
				case UR_GHAST:
					sound = TFSounds.URGHAST_AMBIENT;
					pitch = 0.8F;
					break;
				case SNOW_QUEEN:
					sound = TFSounds.SNOW_QUEEN_AMBIENT;
					break;
				case KNIGHT_PHANTOM:
					sound = TFSounds.WRAITH;
					pitch = 1.1F;
					break;
				case MINOSHROOM:
					sound = TFSounds.MINOSHROOM_AMBIENT;
					volume = 0.5F;
					pitch = 0.7F;
					break;
				case QUEST_RAM:
					sound = TFSounds.QUEST_RAM_AMBIENT;
					pitch = 0.7F;
					break;
			default:
				break;
			}
			if (sound != null)
				worldIn.playSound(playerIn, pos, sound, SoundCategory.BLOCKS, volume, pitch);
		}
		return ActionResultType.SUCCESS;
	}
	
	@Override
	public TileEntity createNewTileEntity(IBlockReader worldIn) {
	      return new TileEntityTFTrophy();
	   }

	   @OnlyIn(Dist.CLIENT)
	   public BossVariant getVariant() {
	      return this.variant;
	   }

	   public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
	      return false;
	   }

}

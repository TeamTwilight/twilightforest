package twilightforest.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.TwilightForestMod;
import twilightforest.block.BlockTFTrophy;
import twilightforest.block.TFBlocks;
import twilightforest.enums.BossVariant;
import twilightforest.client.renderer.tileentity.TileEntityTFTrophyRenderer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Locale;

public class ItemTFTrophy extends ItemTF {

	public ItemTFTrophy() {
		this.setCreativeTab(TFItems.creativeTab);
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {
		if (isInCreativeTab(tab)) {
			for (BossVariant v : BossVariant.values()) {
				if (v != BossVariant.ALPHA_YETI) {
					list.add(new ItemStack(this, 1, v.ordinal()));
				}
			}
		}
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		return I18n.translateToLocalFormatted(this.getTranslationKey(stack), I18n.translateToLocal("entity.twilightforest." + BossVariant.values()[stack.getMetadata() % BossVariant.values().length].getName().toLowerCase(Locale.ROOT) + ".name"));
	}

	@Nonnull
	@Override
	public EnumRarity getRarity(ItemStack stack) {
		return TwilightForestMod.getRarity();
	}

	// [VanillaCopy] ItemSkull, with own block and no player heads
	@Nonnull
	@Override
	public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (facing == EnumFacing.DOWN) {
			return EnumActionResult.FAIL;
		} else {
			if (worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos)) {
				facing = EnumFacing.UP;
				pos = pos.down();
			}
			IBlockState iblockstate = worldIn.getBlockState(pos);
			Block block = iblockstate.getBlock();
			boolean flag = block.isReplaceable(worldIn, pos);

			if (!flag) {
				if (!worldIn.getBlockState(pos).getMaterial().isSolid() && !worldIn.isSideSolid(pos, facing, true)) {
					return EnumActionResult.FAIL;
				}

				pos = pos.offset(facing);
			}

			ItemStack itemstack = playerIn.getHeldItem(hand);

			if (playerIn.canPlayerEdit(pos, facing, itemstack) && TFBlocks.trophy.canPlaceBlockAt(worldIn, pos)) {
				if (worldIn.isRemote) {
					return EnumActionResult.SUCCESS;
				} else {
					worldIn.setBlockState(pos, TFBlocks.trophy.getDefaultState().withProperty(BlockTFTrophy.FACING, facing), 11);
					int i = 0;

					if (facing == EnumFacing.UP) {
						i = MathHelper.floor((double) (playerIn.rotationYaw * 16.0F / 360.0F) + 0.5D) & 15;
					}

					TileEntity tileentity = worldIn.getTileEntity(pos);

					if (tileentity instanceof TileEntitySkull) {
						TileEntitySkull tileentityskull = (TileEntitySkull) tileentity;

						tileentityskull.setType(itemstack.getMetadata());

						tileentityskull.setSkullRotation(i);
					}

					if (playerIn instanceof EntityPlayerMP) {
						CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)playerIn, pos, itemstack);
					}

					itemstack.shrink(1);
					return EnumActionResult.SUCCESS;
				}
			} else {
				return EnumActionResult.FAIL;
			}
		}
	}

	@Nonnull
	@Override
	public String getTranslationKey(ItemStack stack) {
		//int meta = MathHelper.clamp(stack.getItemDamage(), 0, BossVariant.values().length);
		return "item.twilightforest.tf_trophy.name"; //super.getUnlocalizedName() + "." + BossVariant.values()[meta].getName();
	}

	@Override
	public boolean isValidArmor(ItemStack stack, EntityEquipmentSlot armorType, Entity entity) {
		return armorType == EntityEquipmentSlot.HEAD;
	}

	@Override
	@Nullable
	public EntityEquipmentSlot getEquipmentSlot(ItemStack stack) {
		return EntityEquipmentSlot.HEAD;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModel() {

		ModelResourceLocation itemModelLocation = new ModelResourceLocation(TwilightForestMod.ID + ":trophy_tesr", "inventory");

		TileEntityTFTrophyRenderer tesr = new TileEntityTFTrophyRenderer(itemModelLocation);
		//TileEntityTFTrophyRenderer tesrMinor = new TileEntityTFTrophyRenderer() { @Override protected String getModelRSL() { return TwilightForestMod.ID + ":trophy_minor"; } };

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTFTrophyRenderer.DummyTile.class, tesr);

		for (BossVariant variant : BossVariant.values()) {
			if (variant != BossVariant.ALPHA_YETI) {
				ModelLoader.setCustomModelResourceLocation(this, variant.ordinal(), itemModelLocation);
				ForgeHooksClient.registerTESRItemStack(this, variant.ordinal(), TileEntityTFTrophyRenderer.DummyTile.class);
			}
		}

		ModelBakery.registerItemVariants(this,
				new ModelResourceLocation(new ResourceLocation(TwilightForestMod.ID, "trophy"), "inventory"),
				new ModelResourceLocation(new ResourceLocation(TwilightForestMod.ID, "trophy_minor"), "inventory"),
				new ModelResourceLocation(new ResourceLocation(TwilightForestMod.ID, "trophy_quest"), "inventory")
		);
	}
}

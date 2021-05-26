package twilightforest.compat.ie;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.shader.IShaderItem;
import blusunrize.immersiveengineering.api.shader.ShaderCase;
import blusunrize.immersiveengineering.api.shader.ShaderLayer;
import blusunrize.immersiveengineering.api.shader.ShaderRegistry;
import blusunrize.immersiveengineering.api.shader.impl.ShaderCaseItem;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.blocks.IEBlocks;
import blusunrize.immersiveengineering.common.blocks.cloth.ShaderBannerStandingBlock;
import blusunrize.immersiveengineering.common.blocks.cloth.ShaderBannerTileEntity;
import blusunrize.immersiveengineering.common.blocks.cloth.ShaderBannerWallBlock;
import blusunrize.immersiveengineering.common.items.ShaderItem;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.block.BannerBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.WallBannerBlock;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.*;
import net.minecraft.tileentity.BannerTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.ItemHandlerHelper;
import twilightforest.TwilightForestMod;
import twilightforest.item.TFItems;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;

// TODO Move to shader capability, where we can then just turn item Trophies into the shaders
public class ItemTFShader extends Item implements IShaderItem {

    public ItemTFShader() {
        super(TFItems.defaultBuilder().maxStackSize(1));
    }

    static final String TAG_SHADER = "shader_name";

    @Override
    public ShaderCase getShaderCase(ItemStack shader, ItemStack tool, ResourceLocation shaderType) {
        return ShaderRegistry.getShader(getShaderName(shader), shaderType);
    }

    @Override
    public ResourceLocation getShaderName(ItemStack stack) {
        return getShaderType(stack);
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        ResourceLocation rawShaderName = this.getShaderName(stack);
        String unlocalizedShaderName = ("item." + rawShaderName.getNamespace() + ".shader.name." + rawShaderName.getPath().replace(' ', '_').toLowerCase(Locale.ROOT));
        String localizedShaderName = new TranslationTextComponent(unlocalizedShaderName).getString();

        if (unlocalizedShaderName.equals(localizedShaderName))
            return new TranslationTextComponent(this.getTranslationKey(stack), rawShaderName.getPath()).appendString(" *TRANSLATION FAILURE*").mergeStyle(TextFormatting.DARK_RED); // Translation failure
        else
            return new TranslationTextComponent(this.getTranslationKey(stack), localizedShaderName);
    }

    //Copy of ShaderItem.onItemUse so we can apply to banners
    @Override
    public ActionResultType onItemUse(ItemUseContext ctx) {
        World world = ctx.getWorld();
        BlockPos pos = ctx.getPos();
        ResourceLocation name = getShaderName(ctx.getItem());
        if(ShaderRegistry.shaderRegistry.containsKey(name))
        {
            BlockState blockState = world.getBlockState(pos);
            TileEntity tile = world.getTileEntity(pos);
            if(tile instanceof BannerTileEntity)
            {
                ShaderCase sCase = ShaderRegistry.shaderRegistry.get(name).getCase(new ResourceLocation("immersiveengineering", "banner"));
                if(sCase!=null)
                {
                    boolean wall = blockState.getBlock() instanceof WallBannerBlock;

                    if(wall)
                        world.setBlockState(pos, IEBlocks.Cloth.shaderBannerWall.getDefaultState()
                                .with(ShaderBannerWallBlock.FACING, blockState.get(WallBannerBlock.HORIZONTAL_FACING)));
                    else
                        world.setBlockState(pos, IEBlocks.Cloth.shaderBanner.getDefaultState()
                                .with(ShaderBannerStandingBlock.ROTATION, blockState.get(BannerBlock.ROTATION)));
                    tile = world.getTileEntity(pos);
                    if(tile instanceof ShaderBannerTileEntity)
                    {
                        ((ShaderBannerTileEntity)tile).shader.setShaderItem(ItemHandlerHelper.copyStackWithSize(ctx.getItem(), 1));
                        tile.markDirty();
                        return ActionResultType.SUCCESS;
                    }
                }
            }
            else if(tile instanceof ShaderBannerTileEntity)
            {
                ((ShaderBannerTileEntity)tile).shader.setShaderItem(ItemHandlerHelper.copyStackWithSize(ctx.getItem(), 1));
                tile.markDirty();
                return ActionResultType.SUCCESS;
            }

        }
        return ActionResultType.FAIL;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> list, ITooltipFlag flag) {
        list.add(new TranslationTextComponent(Lib.DESC_INFO + "shader.level").append(ClientUtils.applyFormat(new TranslationTextComponent(Lib.DESC_INFO + "shader.rarity." + this.getRarity(stack).name().toLowerCase(Locale.US)), getRarity(stack).color)));
        if(ShaderRegistry.shaderRegistry.containsKey(getShaderName(stack))) {
            if (!Screen.hasShiftDown())
                list.add(new TranslationTextComponent(Lib.DESC_INFO + "shader.applyTo").appendString(" ").append(new TranslationTextComponent(Lib.DESC_INFO + "holdShift")));
            else {
                list.add(new TranslationTextComponent(Lib.DESC_INFO + "shader.applyTo"));

                for (ShaderCase sCase : ShaderRegistry.shaderRegistry.get(getShaderName(stack)).getCases())
                    if (!(sCase instanceof ShaderCaseItem))
                        list.add(ClientUtils.applyFormat(new TranslationTextComponent(Lib.DESC_INFO + "shader." + sCase.getShaderType()), TextFormatting.DARK_GRAY));
            }
        }
    }

    @Override
    public Rarity getRarity(ItemStack stack) {
        return ShaderRegistry.shaderRegistry.containsKey(getShaderName(stack)) ? ShaderRegistry.shaderRegistry.get(getShaderName(stack)).getRarity() : Rarity.COMMON;
    }

    public static ResourceLocation getShaderType(ItemStack stack) {
        return ItemNBTHelper.hasKey(stack, TAG_SHADER) ? new ResourceLocation(ItemNBTHelper.getString(stack, TAG_SHADER)) : TwilightForestMod.prefix("twilight");
    }

    public static int getShaderColors(ItemStack stack, int pass) {
        ResourceLocation name = getShaderType(stack);
        if(ShaderRegistry.shaderRegistry.containsKey(name)) {
            ShaderCase sCase = ShaderRegistry.shaderRegistry.get(name).getCase(new ResourceLocation("immersiveengineering", "item"));
            if(sCase!=null) {
                ShaderLayer[] layers = sCase.getLayers();
                if(pass < layers.length && layers[pass] != null) {
                    return Utils.intFromRGBA(layers[pass].getColor());
                }
                return 0xffffffff;
            }
        }
        return 0xffffffff;
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (group == TFItems.creativeTab) {
            for (ShaderRegistry.ShaderRegistryEntry entry : IEShaderRegister.getAllTwilightShaders()) {
                ItemStack stack = new ItemStack(this);
                ItemNBTHelper.putString(stack, TAG_SHADER, entry.getName().toString());
                items.add(stack);
            }
        }
    }
}

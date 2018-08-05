package twilightforest.compat.ie;

import blusunrize.immersiveengineering.api.shader.ShaderRegistry;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.TwilightForestMod;
import twilightforest.client.ModelRegisterCallback;
import twilightforest.client.model.item.ShaderGrabbagStackRenderer;
import twilightforest.item.TFItems;

import javax.annotation.Nullable;
import java.util.List;

public class ItemTFShaderGrabbag extends Item implements ModelRegisterCallback {
    public ItemTFShaderGrabbag() {
        this.setHasSubtypes(true);
        this.setCreativeTab(TFItems.creativeTab);
    }

    private static final String TAG_SHADER = "shader_rarity";
    public static final ItemTFShaderGrabbag shader_bag = new ItemTFShaderGrabbag();

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (tab == TFItems.creativeTab) {
            for(int i = ShaderRegistry.sortedRarityMap.size() - 1; i >= 0; i--) {
                ItemStack stack = new ItemStack(this, 1, 0);

                NBTTagCompound compound = new NBTTagCompound();
                compound.setString(TAG_SHADER, ShaderRegistry.sortedRarityMap.get(i).toString());

                stack.setTagCompound(compound);

                items.add(stack);
            }
        }
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        NBTTagCompound compound = stack.getTagCompound();

        if (compound == null) {
            stack.setTagCompound(new NBTTagCompound());
            compound = stack.getTagCompound();
        }

        String rarityString = compound.getString(TAG_SHADER);

        for(EnumRarity rarity : EnumRarity.values())
            if(rarity.toString().equalsIgnoreCase(rarityString))
                return rarity;

        return EnumRarity.COMMON;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        String name = this.getRarity(stack).rarityName;

        return I18n.translateToLocalFormatted(this.getUnlocalizedName(stack), name);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);

        if (!worldIn.isRemote) {
            EnumRarity rarity = stack.getRarity();

            if (rarity == TwilightForestMod.getRarity()) {
                List<ShaderRegistry.ShaderRegistryEntry> list = IEShaderRegister.getAllNonbossShaders();

                return randomShader(list.get(playerIn.getRNG().nextInt(list.size())).name, stack, playerIn);
            } else if (ShaderRegistry.totalWeight.containsKey(rarity)) {
                return randomShader(ShaderRegistry.getRandomShader(playerIn.getName(), playerIn.getRNG(), rarity, true), stack, playerIn);
            }
        }

        return new ActionResult<>(EnumActionResult.PASS, stack);
    }

    private static ActionResult<ItemStack> randomShader(@Nullable String shader, ItemStack stack, EntityPlayer playerIn) {
        if ( shader == null || shader.isEmpty() )
            return new ActionResult<>(EnumActionResult.FAIL, stack);

        ItemStack shaderItem = new ItemStack(ItemTFShader.shader);
        NBTTagCompound compound = shaderItem.getTagCompound();

        if (compound == null) {
            shaderItem.setTagCompound(new NBTTagCompound());
            compound = shaderItem.getTagCompound();
        }

        compound.setString(ItemTFShader.TAG_SHADER, shader);

        stack.shrink(1);

        if (stack.getCount() <= 0)
            return new ActionResult<>(EnumActionResult.SUCCESS, shaderItem);

        if (!playerIn.inventory.addItemStackToInventory(shaderItem))
            playerIn.dropItem(shaderItem, false, true);

        return new ActionResult<>(EnumActionResult.PASS, stack);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(TwilightForestMod.ID + ":grabbag_tesr", "inventory"));

        ShaderGrabbagStackRenderer tesr = new ShaderGrabbagStackRenderer();

        ClientRegistry.bindTileEntitySpecialRenderer(ShaderGrabbagStackRenderer.DummyTile.class, tesr);
        ClientEventHandler.dummyModel = tesr.baked;

        ForgeHooksClient.registerTESRItemStack(this, 0, ShaderGrabbagStackRenderer.DummyTile.class);
    }

    // Register subscriber with IE, so if IE isn't installed, this shouldn't exist in bus... right?
    @Mod.EventBusSubscriber(value = Side.CLIENT)
    private static final class ClientEventHandler {
        static ShaderGrabbagStackRenderer.BakedModel dummyModel;

        @Optional.Method(modid = "immersiveengineering")
        @SubscribeEvent
        public static void onModelBake(ModelBakeEvent event) {
            TwilightForestMod.LOGGER.debug("Registering fancy item model for TF Shader Grabbag!");

            event.getModelRegistry().putObject(new ModelResourceLocation(TwilightForestMod.ID + ":grabbag_tesr", "inventory"), dummyModel);
        }
    }
}

package twilightforest.compat.ie;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.shader.IShaderItem;
import blusunrize.immersiveengineering.api.shader.ShaderCase;
import blusunrize.immersiveengineering.api.shader.ShaderCaseItem;
import blusunrize.immersiveengineering.api.shader.ShaderRegistry;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.client.ModelRegisterCallback;
import twilightforest.item.TFItems;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;

// TODO Move to shader capability, where we can then just turn item Trophies into the shaders
@Optional.Interface(modid = "immersiveengineering", iface = "blusunrize.immersiveengineering.api.shader.IShaderItem")
public class ItemTFShader extends Item implements IShaderItem, ModelRegisterCallback {
    public ItemTFShader() {
        this.setHasSubtypes(true);
        this.setCreativeTab(TFItems.creativeTab);
    }

    static final String TAG_SHADER = "shader_type";

    public static final ItemTFShader shader = new ItemTFShader();

    @Optional.Method(modid = "immersiveengineering")
    @Override
    public ShaderCase getShaderCase(ItemStack shader, ItemStack tool, String shaderType) {
        return ShaderRegistry.getShader(getShaderType(shader), shaderType);
    }

    @Optional.Method(modid = "immersiveengineering")
    @Override
    public String getShaderName(ItemStack stack) {
        return getShaderType(stack);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        String rawShaderName = this.getShaderName(stack);
        String unlocalizedShaderName = "twilightforest.shader." + rawShaderName.replace(' ', '_').toLowerCase(Locale.ROOT);
        String localizedShaderName = I18n.translateToLocal(unlocalizedShaderName);

        if (unlocalizedShaderName.equals(localizedShaderName))
            return I18n.translateToLocalFormatted(this.getTranslationKey(stack), rawShaderName); // Translation failure
        else
            return I18n.translateToLocalFormatted(this.getTranslationKey(stack), localizedShaderName);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World world, List<String> list, ITooltipFlag flag) {
        list.add(I18n.translateToLocalFormatted("Level: " + this.getRarity(stack).color + this.getRarity(stack).rarityName));

        if(!GuiScreen.isShiftKeyDown())
            list.add(I18n.translateToLocalFormatted(Lib.DESC_INFO + "shader.applyTo") + " " + I18n.translateToLocalFormatted(Lib.DESC_INFO + "holdShift"));
        else {
            list.add(I18n.translateToLocalFormatted(Lib.DESC_INFO + "shader.applyTo"));

            for (ShaderCase sCase : ShaderRegistry.shaderRegistry.get(getShaderName(stack)).getCases())
                if (!(sCase instanceof ShaderCaseItem))
                    list.add(TextFormatting.DARK_GRAY + " " + I18n.translateToLocalFormatted(Lib.DESC_INFO + "shader." + sCase.getShaderType()));
        }
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return ShaderRegistry.shaderRegistry.get(getShaderType(stack)).getRarity();
    }

    @Nonnull
    private static String getShaderType(ItemStack stack) {
        NBTTagCompound compound = stack.getTagCompound();

        if (compound != null) {
            String string = stack.getTagCompound().getString(TAG_SHADER);

            if (!string.isEmpty() && ShaderRegistry.shaderRegistry.containsKey(string))
                return string;
        }

        return "Twilight";
    }

    public static int getShaderColors(ItemStack stack, int layer) {
        ShaderRegistry.ShaderRegistryEntry entry = ShaderRegistry.shaderRegistry.get(getShaderType(stack));

        if (entry != null) {
            ShaderCase shaderCase = entry.getCase("immersiveengineering:item");

            if (shaderCase != null) {
                ShaderCase.ShaderLayer[] layers = shaderCase.getLayers();

                return layers[layer % layers.length].getColour();
            }
        }

        return 0xFF_FF_FF_FF;
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (tab == TFItems.creativeTab) {
            for (ShaderRegistry.ShaderRegistryEntry entry : IEShaderRegister.getAllTwilightShaders()) {
                NBTTagCompound compound = new NBTTagCompound();

                ItemStack stack = new ItemStack(this, 1, 0);
                compound.setString(TAG_SHADER, entry.getName());
                stack.setTagCompound(compound);

                items.add(stack);
            }
        }
    }
}

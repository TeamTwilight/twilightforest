package twilightforest.compat.ie;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.shader.ShaderRegistry;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.item.TFItems;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;

public class ItemTFShaderGrabbag extends Item {

    @Nonnull
    private final Rarity rarity;

    public ItemTFShaderGrabbag(Rarity r) {
        super(TFItems.defaultBuilder());
        setRegistryName(TwilightForestMod.ID, "shader_bag_" + r.name().toLowerCase(Locale.US).replace(':', '_'));
        this.rarity = r;
    }

    @Override
    public Rarity getRarity(ItemStack stack) {
        return rarity;
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        return (new TranslationTextComponent("item.twilightforest.shader_bag", new TranslationTextComponent(Lib.DESC_INFO + "shader.rarity." + rarity.name().toLowerCase(Locale.US))));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);

        if (!world.isRemote) {
            if(rarity == TwilightForestMod.getRarity()) {
                List<ShaderRegistry.ShaderRegistryEntry> list = IEShaderRegister.getAllNonbossShaders();
                return randomShader(list.get(player.getRNG().nextInt(list.size())).name, stack, player);
            } else {
                return randomShader(ShaderRegistry.getRandomShader(player.getUniqueID(), player.getRNG(), rarity, true), stack, player);
            }
        }
        return new ActionResult<>(ActionResultType.PASS, stack);
    }

    private static ActionResult<ItemStack> randomShader(@Nullable ResourceLocation shader, ItemStack stack, PlayerEntity playerIn) {
        if ( shader == null || shader.getPath().isEmpty() )
            return new ActionResult<>(ActionResultType.FAIL, stack);

        ItemStack shaderItem = new ItemStack(ForgeRegistries.ITEMS.getValue(TwilightForestMod.prefix("shader")));
        ItemNBTHelper.putString(shaderItem, ItemTFShader.TAG_SHADER, shader.toString());

        stack.shrink(1);

        if (stack.getCount() <= 0)
            return new ActionResult<>(ActionResultType.SUCCESS, shaderItem);

        if (!playerIn.inventory.addItemStackToInventory(shaderItem))
            playerIn.dropItem(shaderItem, false, true);

        return new ActionResult<>(ActionResultType.PASS, stack);
    }
}

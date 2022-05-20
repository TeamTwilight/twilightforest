package twilightforest.compat.ie;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.shader.ShaderRegistry;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.item.TFItems;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;

import static twilightforest.TFConstants.MOD_ID;

public class TFShaderGrabbagItem extends Item {

    @Nonnull
    private final Rarity rarity;

    public TFShaderGrabbagItem(Rarity r) {
        super(TFItems.defaultBuilder());
        setRegistryName(MOD_ID, "shader_bag_" + r.name().toLowerCase(Locale.US).replace(':', '_'));
        this.rarity = r;
    }

    @Override
    public Rarity getRarity(ItemStack stack) {
        return rarity;
    }

    @Override
    public Component getName(ItemStack stack) {
        return (new TranslatableComponent("item.twilightforest.shader_bag", new TranslatableComponent(Lib.DESC_INFO + "shader.rarity." + rarity.name().toLowerCase(Locale.US))));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!world.isClientSide) {
            if(rarity == TwilightForestMod.getRarity()) {
                List<ShaderRegistry.ShaderRegistryEntry> list = IEShaderRegister.getAllNonbossShaders();
                return randomShader(list.get(player.getRandom().nextInt(list.size())).name, stack, player);
            } else {
                return randomShader(ShaderRegistry.getRandomShader(player.getUUID(), player.getRandom(), rarity, true), stack, player);
            }
        }
        return new InteractionResultHolder<>(InteractionResult.PASS, stack);
    }

    private static InteractionResultHolder<ItemStack> randomShader(@Nullable ResourceLocation shader, ItemStack stack, Player playerIn) {
        if ( shader == null || shader.getPath().isEmpty() )
            return new InteractionResultHolder<>(InteractionResult.FAIL, stack);

        ItemStack shaderItem = new ItemStack(ForgeRegistries.ITEMS.getValue(TwilightForestMod.prefix("shader")));
        ItemNBTHelper.putString(shaderItem, TFShaderItem.TAG_SHADER, shader.toString());

        stack.shrink(1);

        if (stack.getCount() <= 0)
            return new InteractionResultHolder<>(InteractionResult.SUCCESS, shaderItem);

        if (!playerIn.getInventory().add(shaderItem))
            playerIn.drop(shaderItem, false, true);

        return new InteractionResultHolder<>(InteractionResult.PASS, stack);
    }
}

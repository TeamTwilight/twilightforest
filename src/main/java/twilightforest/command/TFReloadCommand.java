package twilightforest.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.ModList;
import twilightforest.client.shader.ShaderManager;
import twilightforest.compat.ie.IEShaderRegister;

//FIXME this command was sitting in the proxy, so I moved it over here.
//However, if you run it, the game closes because ShaderManager isnt registering properly
public class TFReloadCommand {

    public static LiteralArgumentBuilder<CommandSource> register() {
        return Commands.literal("tfreload").executes(TFReloadCommand::reload);
    }

    private static int reload(CommandContext<CommandSource> ctx) {
        ctx.getSource().sendFeedback(new TranslationTextComponent("commands.tfreload.reload"), true);
        ShaderManager.getShaderReloadListener().onResourceManagerReload(Minecraft.getInstance().getResourceManager());
        if (ModList.get().isLoaded("immersiveengineering"))
            IEShaderRegister.initShaders();
        return Command.SINGLE_SUCCESS;
    }
}

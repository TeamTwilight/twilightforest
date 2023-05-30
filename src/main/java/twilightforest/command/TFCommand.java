package twilightforest.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class TFCommand {
    public static final SimpleCommandExceptionType NOT_IN_TF = new SimpleCommandExceptionType(Component.translatable("commands.tffeature.not_in_twilight_forest").withStyle(ChatFormatting.RED));

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("twilightforest")
                .executes(TFCommand::run)
                .then(CenterCommand.register())
                .then(ConquerCommand.register())
                .then(GenerateBookCommand.register())
                .then(InfoCommand.register())
                .then(MapBiomesCommand.register())
                .then(ShieldCommand.register());
        LiteralCommandNode<CommandSourceStack> node = dispatcher.register(builder);
        dispatcher.register(Commands.literal("tf").executes(TFCommand::run).redirect(node));
        dispatcher.register(Commands.literal("tffeature").executes(TFCommand::run).redirect(node));
    }

    private static int run(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        throw new SimpleCommandExceptionType(Component.translatable("commands.tffeature.usage", ctx.getInput())).create();
    }
}

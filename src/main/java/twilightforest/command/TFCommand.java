package twilightforest.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TranslationTextComponent;

public class TFCommand {
    public static final SimpleCommandExceptionType NOT_IN_TF = new SimpleCommandExceptionType(new TranslationTextComponent("commands.tffeature.not_in_twilight_forest"));

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> builder = Commands.literal("twilightforest")
                .then(CenterCommand.register())
                .then(ConquerCommand.register())
                .then(InfoCommand.register())
                .then(ShieldCommand.register());
        LiteralCommandNode<CommandSource> node = dispatcher.register(builder);
        dispatcher.register(Commands.literal("tf").redirect(node));
        dispatcher.register(Commands.literal("tffeature").redirect(node));
    }
}

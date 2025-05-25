package twilightforest.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.ServerLevel;

@tamaized.beanification.Component
public class GetSeedAndTeleportCommand {
	public LiteralArgumentBuilder<CommandSourceStack> register(boolean notIntegratedServer) {
		return Commands.literal("seed_and_tp").requires((stack) -> !notIntegratedServer || stack.hasPermission(2)).executes(this::run);
	}

	private int run(CommandContext<CommandSourceStack> ctx) {
		CommandSourceStack source = ctx.getSource();
		ServerLevel level = source.getLevel();
		BlockPos pos = BlockPos.containing(source.getPosition());
		long seed = level.getSeed();
		source.sendSuccess(() -> Component.translatable("commands.seed.success", ComponentUtils.copyOnClickText(String.valueOf(seed))), false);
		source.sendSuccess(() -> Component.translatable("commands.tffeature.coords", ComponentUtils.copyOnClickText(pos.toShortString())), false);
		String tp = "/execute in " + level.dimension().location() + " run tp " + pos.getX() + " " + pos.getY() + " " + pos.getZ();
		String block = "Seed: `" + seed + "`" + System.lineSeparator() + "Tp: `" + tp + "`";
		source.sendSuccess(() -> copyOnClickText(Component.translatable("commands.tffeature.tp"), tp).append(" ").append(copyOnClickText(Component.translatable("commands.tffeature.seed_and_tp"), block)), false);
		return (int) seed;
	}

	public static MutableComponent copyOnClickText(MutableComponent component, String text) {
		return ComponentUtils.wrapInSquareBrackets(component.withStyle((style) -> style.withColor(ChatFormatting.GREEN).withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, text)).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("chat.copy.click"))).withInsertion(text)));
	}
}

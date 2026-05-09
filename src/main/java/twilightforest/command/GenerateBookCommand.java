package twilightforest.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceKeyArgument;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.neoforged.neoforge.common.util.FakePlayer;
import org.jetbrains.annotations.Nullable;
import twilightforest.world.components.structures.util.StructureHints;

@tamaized.beanification.Component
public class GenerateBookCommand {

	private final SimpleCommandExceptionType ERROR_NOT_RUN_BY_PLAYER = new SimpleCommandExceptionType(Component.translatable("commands.tffeature.not_player"));

	public LiteralArgumentBuilder<CommandSourceStack> register() {
		return Commands.literal("genbook")
			.executes(context -> generateBook(context.getSource(), null))
			.requires(Commands.hasPermission(Commands.LEVEL_ADMINS))
			.then(Commands.argument("structure", ResourceKeyArgument.key(Registries.STRUCTURE))
				.executes(context -> generateBook(context.getSource(), ResourceKeyArgument.getStructure(context, "structure"))));
	}

	private int generateBook(CommandSourceStack source, @Nullable Holder.Reference<Structure> structureKey) throws CommandSyntaxException {
		if (!(source.getEntity() instanceof Player player) || player instanceof FakePlayer) throw ERROR_NOT_RUN_BY_PLAYER.create();
		if (structureKey == null) {
			for (Structure structure : source.getLevel().registryAccess().lookupOrThrow(Registries.STRUCTURE).stream().toList()) {
				if (structure instanceof StructureHints hint) {
					ItemStack book = hint.createHintBook(source.registryAccess());
					if (!book.isEmpty()) {
						if (!player.addItem(book)) {
							player.drop(book, true);
						}
					}
				}
			}
		} else {
			if (source.getLevel().registryAccess().lookupOrThrow(Registries.STRUCTURE).get(structureKey.key()).orElseThrow() instanceof StructureHints hint) {
				ItemStack book = hint.createHintBook(source.registryAccess());
				if (!book.isEmpty()) {
					if (!player.addItem(book)) {
						player.drop(book, true);
					}
				}
			} else {
				ItemStack book = StructureHints.HintConfig.defaultBook();
				if (!player.addItem(book)) {
					player.drop(book, true);
				}
			}
		}
		return Command.SINGLE_SUCCESS;
	}
}

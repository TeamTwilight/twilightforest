package twilightforest.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import tamaized.beanification.Autowired;

@tamaized.beanification.Component
public class TFCommand {

	@Autowired
	private CenterCommand centerCommand;

	@Autowired
	private ConquerCommand conquerCommand;

	@Autowired
	private GenerateBookCommand generateBookCommand;

	@Autowired
	private InfoCommand infoCommand;

	@Autowired
	private MapBiomesCommand mapBiomesCommand;

	@Autowired
	private MapLocatorCommand mapLocatorCommand;

	@Autowired
	private ShieldCommand shieldCommand;

	@Autowired
	private SinisterSpawnerCommand spawnerCommand;

	@Autowired
	private DisplayPiecesCommand displayPiecesCommand;

	@Autowired
	private CountLootCommand countLootCommand;

	@Autowired
	private CountTemplateCommand countTemplateCommand;

	@Autowired
	private StructureDistanceCommand structureDistanceCommand;

	@Autowired
	private ClearDisplayCommand clearDisplayCommand;

	@Autowired
	private TravellersGearCommand travellersGearCommand;

	@Autowired
	private TFTeleportCommand tfTeleportCommand;

	public void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext buildContext) {
		LiteralArgumentBuilder<CommandSourceStack> structureBranch = Commands.literal("structure_util")
			.then(displayPiecesCommand.register())
			.then(clearDisplayCommand.register())
			.then(countLootCommand.register())
			.then(countTemplateCommand.register())
			.then(structureDistanceCommand.register());

		LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("twilightforest")
			.executes(this::run)
			.then(tfTeleportCommand.register())
			.then(centerCommand.register())
			.then(mapLocatorCommand.register())
			.then(conquerCommand.register())
			.then(generateBookCommand.register())
			.then(infoCommand.register())
			.then(mapBiomesCommand.register())
			.then(shieldCommand.register())
			.then(spawnerCommand.register(buildContext))
			.then(travellersGearCommand.register())
			.then(structureBranch);
		LiteralCommandNode<CommandSourceStack> node = dispatcher.register(builder);
		dispatcher.register(Commands.literal("tf").executes(this::run).redirect(node));
		dispatcher.register(Commands.literal("tffeature").executes(this::run).redirect(node));
	}

	private int run(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
		throw new SimpleCommandExceptionType(Component.translatable("commands.tffeature.usage", ctx.getInput())).create();
	}
}

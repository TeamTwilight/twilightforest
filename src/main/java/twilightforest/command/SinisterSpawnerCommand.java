package twilightforest.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.ParticleArgument;
import net.minecraft.commands.arguments.ResourceOrIdArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.LootTable;
import twilightforest.block.entity.spawner.SinisterSpawnerBlockEntity;

@tamaized.beanification.Component
public class SinisterSpawnerCommand {
	public static final SuggestionProvider<CommandSourceStack> SUGGEST_LOOT_TABLE = (context, builder) -> {
		var registryAccess = context.getSource().getServer().registryAccess();

		var lootTables = registryAccess.lookupOrThrow(Registries.LOOT_TABLE).listElements().map(holder -> holder.key().identifier());

		return SharedSuggestionProvider.suggestResource(lootTables, builder);
	};

	public LiteralArgumentBuilder<CommandSourceStack> register(CommandBuildContext buildContext) {
		return Commands.literal("sinister_spawner")
			.requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
			.then(Commands.literal("add_particle").then(Commands.argument("particle", ParticleArgument.particle(buildContext)).then(Commands.argument("pos", BlockPosArgument.blockPos()).executes(this::addParticle))))
			.then(Commands.literal("remove_particle").then(Commands.argument("particle", ParticleArgument.particle(buildContext)).then(Commands.argument("pos", BlockPosArgument.blockPos()).executes(this::removeParticle))))
			.then(Commands.literal("set_loot").then(Commands.argument("loot", ResourceOrIdArgument.lootTable(buildContext)).suggests(SUGGEST_LOOT_TABLE).then(Commands.argument("pos", BlockPosArgument.blockPos()).executes(this::setLootTable))))
			.then(Commands.literal("clear_loot").then(Commands.argument("pos", BlockPosArgument.blockPos()).executes(this::clearLootTable)))
			;
	}

	private int addParticle(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		ParticleOptions options = ParticleArgument.getParticle(context, "particle");
		BlockPos pos = BlockPosArgument.getLoadedBlockPos(context, "pos");

		if (context.getSource().getLevel().getBlockEntity(pos) instanceof SinisterSpawnerBlockEntity entity) {
			if (entity.addParticle(options, true))
				return 1;
		}

		return 0;
	}

	private int removeParticle(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		ParticleOptions options = ParticleArgument.getParticle(context, "particle");
		BlockPos pos = BlockPosArgument.getLoadedBlockPos(context, "pos");

		if (context.getSource().getLevel().getBlockEntity(pos) instanceof SinisterSpawnerBlockEntity entity) {
			if (entity.removeParticle(options, true))
				return 1;
		}

		return 0;
	}

	private int setLootTable(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		BlockPos pos = BlockPosArgument.getLoadedBlockPos(context, "pos");
		Holder<LootTable> loot = ResourceOrIdArgument.getLootTable(context, "loot");

		if (context.getSource().getLevel().getBlockEntity(pos) instanceof SinisterSpawnerBlockEntity entity)
			if (entity.setLootTable(loot.getKey()))
				return 1;

		return 0;
	}

	private int clearLootTable(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		BlockPos pos = BlockPosArgument.getLoadedBlockPos(context, "pos");

		if (context.getSource().getLevel().getBlockEntity(pos) instanceof SinisterSpawnerBlockEntity entity)
			if (entity.setLootTable(null))
				return 1;

		return 0;
	}
}

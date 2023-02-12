package twilightforest.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;
import twilightforest.util.LegacyLandmarkPlacements;
import twilightforest.util.WorldUtil;
import twilightforest.world.components.chunkgenerators.ChunkGeneratorTwilight;
import twilightforest.world.components.structures.start.TFStructureStart;
import twilightforest.init.TFLandmark;
import twilightforest.world.registration.TFGenerationSettings;

import java.util.List;

public class InfoCommand {
	public static LiteralArgumentBuilder<CommandSourceStack> register() {
		return Commands.literal("info").requires(cs -> cs.hasPermission(2)).executes(InfoCommand::run);
	}

	private static int run(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
		CommandSourceStack source = ctx.getSource();

		if (!TFGenerationSettings.usesTwilightChunkGenerator(source.getLevel())) {
			throw TFCommand.NOT_IN_TF.create();
		}

		BlockPos pos = new BlockPos(source.getPosition());

		// nearest feature
		BlockPos cc = LegacyLandmarkPlacements.getNearestCenterXZ(pos.getX() >> 4, pos.getZ() >> 4);
		TFLandmark closestFeature = LegacyLandmarkPlacements.pickLandmarkAtBlock(cc.getX(), cc.getZ(), source.getLevel());
		source.sendSuccess(Component.translatable("This command is still WIP, some things may still be broken.").withStyle(ChatFormatting.RED, ChatFormatting.BOLD), false);
		String structurename = Component.translatable("structure." + closestFeature.name).getString();
		source.sendSuccess(Component.translatable("commands.tffeature.nearest", structurename), false);

		// are you in a structure?
		ChunkGeneratorTwilight chunkGenerator = WorldUtil.getChunkGenerator(source.getLevel());
		if (chunkGenerator != null) {
			TFGenerationSettings.locateTFStructureInRange(source.getLevel(), closestFeature, pos, 0).map(s -> (TFStructureStart) s).ifPresent(structure -> {
				source.sendSuccess(Component.translatable("commands.tffeature.structure.inside").withStyle(ChatFormatting.BOLD, ChatFormatting.GREEN), false);
				source.sendSuccess(Component.translatable("commands.tffeature.structure.conquer.status", structure.isConquered()).
						withStyle(ChatFormatting.BOLD, structure.isConquered() ? ChatFormatting.GREEN : ChatFormatting.RED), false);

				// what is the spawn list
				List<MobSpawnSettings.SpawnerData> spawnList = ChunkGeneratorTwilight.gatherPotentialSpawns(source.getLevel().structureManager(), MobCategory.MONSTER, pos);
				source.sendSuccess(Component.translatable("commands.tffeature.structure.spawn_list").withStyle(ChatFormatting.UNDERLINE), false);
				if (spawnList != null)
					for (MobSpawnSettings.SpawnerData entry : spawnList)
						source.sendSuccess(Component.translatable("commands.tffeature.structure.spawn_info", entry.type.getDescription().getString(), entry.getWeight().asInt()), false);
			});
		} else {
			source.sendSuccess(Component.translatable("commands.tffeature.structure.outside").withStyle(ChatFormatting.BOLD, ChatFormatting.RED), false);
		}

		return Command.SINGLE_SUCCESS;
	}

}

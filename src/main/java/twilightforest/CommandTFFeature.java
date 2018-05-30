package twilightforest;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import twilightforest.world.ChunkGeneratorTwilightForest;
import twilightforest.world.TFWorld;
import twilightforest.world.WorldProviderTwilightForest;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class CommandTFFeature extends CommandBase {

	@Override
	public String getName() {
		return "tffeature";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "commands.tffeature.usage";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
		return args.length == 1 ? getListOfStringsMatchingLastWord(args, "info", "reactivate", "conquer", "center") : Collections.emptyList();
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length > 0) {
			if (args[0].equalsIgnoreCase("info")) {
				// info on current feature

				EntityPlayerMP player = getCommandSenderAsPlayer(sender);

				int dx = MathHelper.floor(player.posX);
				int dy = MathHelper.floor(player.posY);
				int dz = MathHelper.floor(player.posZ);

				if (!(player.world.provider instanceof WorldProviderTwilightForest)) {
					throw new CommandException("commands.tffeature.not_in_twilight_forest");
				} else {
					// nearest feature
					TFFeature nearbyFeature = TFFeature.getFeatureAt(dx, dz, player.world);

					sender.sendMessage(new TextComponentTranslation("commands.tffeature.nearest", nearbyFeature.name));

					// are you in a structure?

					ChunkGeneratorTwilightForest chunkProvider = (ChunkGeneratorTwilightForest) TFWorld.getChunkGenerator(player.world);

					final BlockPos pos = new BlockPos(dx, dy, dz);

					if (chunkProvider.isBlockInStructureBB(pos)) {
						sender.sendMessage(new TextComponentTranslation("commands.tffeature.structure.inside"));

						sender.sendMessage(new TextComponentTranslation("commands.tffeature.structure.conquer.status", chunkProvider.isStructureConquered(pos)));
						// are you in a room?

						// what is the spawn list
//						List<SpawnListEntry> spawnList = chunkProvider.getPossibleCreatures(EnumCreatureType.monster, dx, dy, dz);
//						sender.sendMessage(new TextComponentTranslation("Spawn list for the area is:"));
//						for (SpawnListEntry entry : spawnList) {
//							sender.sendMessage(new TextComponentTranslation(entry.toString()));
//						}


					} else {
						sender.sendMessage(new TextComponentTranslation("commands.tffeature.structure.outside"));
					}
				}
			} else if (args[0].equalsIgnoreCase("reactivate")) {
				changeStructureActivity(sender, false);
			} else if (args[0].equalsIgnoreCase("conquer")) {
				changeStructureActivity(sender, true);
			} else if (args[0].equalsIgnoreCase("center")) {
				EntityPlayerMP player = getCommandSenderAsPlayer(sender);

				int dx = MathHelper.floor(player.posX);
				//int dy = MathHelper.floor(player.posY);
				int dz = MathHelper.floor(player.posZ);

				BlockPos cc = TFFeature.getNearestCenterXYZ(dx >> 4, dz >> 4, player.world);

				boolean fc = TFFeature.isInFeatureChunk(player.world, dx, dz);
				sender.sendMessage(new TextComponentTranslation("commands.tffeature.center", cc));
				sender.sendMessage(new TextComponentTranslation("commands.tffeature.chunk", fc));
			} else {
				throw new WrongUsageException("commands.tffeature.usage");
			}

		} else {
			throw new WrongUsageException("commands.tffeature.usage");
		}

	}

	private void changeStructureActivity(ICommandSender sender, boolean flag) throws CommandException {
		// activate current feature
		EntityPlayerMP player = getCommandSenderAsPlayer(sender);

		int dx = MathHelper.floor(player.posX);
		int dy = MathHelper.floor(player.posY);
		int dz = MathHelper.floor(player.posZ);

		if (!(player.world.provider instanceof WorldProviderTwilightForest)) {
			throw new CommandException("commands.tffeature.not_in_twilight_forest");
		} else {
			// are you in a structure?
			ChunkGeneratorTwilightForest chunkProvider = (ChunkGeneratorTwilightForest) TFWorld.getChunkGenerator(player.world);

			final BlockPos pos = new BlockPos(dx, dy, dz);
			if (chunkProvider.isBlockInStructureBB(pos)) {
				sender.sendMessage(new TextComponentTranslation("commands.tffeature.structure.conquer.update", chunkProvider.isStructureConquered(pos), flag));

				chunkProvider.setStructureConquered(dx, dy, dz, flag);
			} else {
				throw new CommandException("commands.tffeature.structure.required");
			}
		}
	}

}

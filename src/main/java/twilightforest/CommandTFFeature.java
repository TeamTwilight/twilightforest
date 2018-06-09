package twilightforest;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import twilightforest.client.shader.ShaderHelper;
import twilightforest.world.ChunkGeneratorTwilightForest;
import twilightforest.world.TFWorld;
import twilightforest.world.WorldProviderTwilightForest;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class CommandTFFeature extends CommandBase {
	private static final List<String> aliases = ImmutableList.of("tffeature", "twilightforest", "tf");

	@Override
	public List<String> getAliases() {
		return aliases;
	}

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
		if (args.length == 1)
			return getListOfStringsMatchingLastWord(args, EnumActions.ACTION_LIST);

		if (args.length > 1) {
			try {
				String[] argsMoved = Arrays.copyOfRange(args, 1, args.length);

				return EnumActions.valueOf(args[0].toUpperCase(Locale.ROOT)).tabCompletion(server, sender, argsMoved, targetPos);
			} catch (Throwable e) {
				return Collections.emptyList();
			}
		}

		return Collections.emptyList();
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length > 0) {
			try {
				EnumActions.valueOf(args[0].toUpperCase(Locale.ROOT)).execute(server, sender, args);
			} catch (IllegalArgumentException e) {
				throw new WrongUsageException("commands.tffeature.usage");
			}
		} else {
			throw new CommandException("commands.tffeature.usage");
		}
	}

	private static void changeStructureActivity(ICommandSender sender, boolean flag) throws CommandException {
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

	private enum EnumActions {
		CENTER {
			@Override
			protected void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
				EntityPlayerMP player = getCommandSenderAsPlayer(sender);

				int dx = MathHelper.floor(player.posX);
				//int dy = MathHelper.floor(player.posY);
				int dz = MathHelper.floor(player.posZ);

				BlockPos cc = TFFeature.getNearestCenterXYZ(dx >> 4, dz >> 4, player.world);

				boolean fc = TFFeature.isInFeatureChunk(player.world, dx, dz);
				sender.sendMessage(new TextComponentTranslation("commands.tffeature.center", cc));
				sender.sendMessage(new TextComponentTranslation("commands.tffeature.chunk", fc));
			}
		},
		CONQUER {
			@Override
			protected void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
				changeStructureActivity(sender, true);
			}
		},
		INFO {
			// info on current feature

			@Override
			protected void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
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
			}
		},
		LOCATE {
			private final String[] STRUCTURE_LIST = {
					TFFeature.hill1.name,
					TFFeature.hill2.name,
					TFFeature.hill3.name,
					TFFeature.hedgeMaze.name,
					TFFeature.nagaCourtyard.name,
					TFFeature.lichTower.name,
					TFFeature.iceTower.name,
					TFFeature.questGrove.name,
					TFFeature.hydraLair.name,
					TFFeature.labyrinth.name,
					TFFeature.darkTower.name,
					TFFeature.tfStronghold.name,
					TFFeature.yetiCave.name,
					TFFeature.trollCave.name,
					TFFeature.finalCastle.name,
					TFFeature.mushroomTower.name
			};

			@Override
			protected void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
				if (args.length != 2)
					throw new WrongUsageException("commands.tffeature.locate.usage");

				if (!(getCommandSenderAsPlayer(sender).world.provider instanceof WorldProviderTwilightForest))
					throw new CommandException("commands.tffeature.not_in_twilight_forest");

				String s = args[1];
				BlockPos blockpos = sender.getEntityWorld().findNearestStructure(s, sender.getPosition(), false);

				if (blockpos != null) {
					sender.sendMessage(new TextComponentTranslation("commands.locate.success", s, blockpos.getX(), blockpos.getZ()));
				} else {
					blockpos = sender.getEntityWorld().findNearestStructure("twilightforest:" + s, sender.getPosition(), false);

					if (blockpos != null)
						sender.sendMessage(new TextComponentTranslation("commands.locate.success", s, blockpos.getX(), blockpos.getZ()));
					else throw new CommandException("commands.locate.failure", s);
				}
			}

			@Override
			protected List<String> tabCompletion(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
				return args.length == 1 ? getListOfStringsMatchingLastWord(args, STRUCTURE_LIST) : Collections.emptyList();
			}
		},
		REACTIVATE {
			@Override
			protected void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
				changeStructureActivity(sender, false);
			}
		};/*, RELOADSHADERS {
			@Override
			protected void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
				// Reload our shaders without reloading all of the game resources
				if(FMLCommonHandler.instance().getEffectiveSide().isClient()) {
					TwilightForestMod.LOGGER.info("Reloaded");
					ShaderHelper.getShaderReloadListener().onResourceManagerReload(Minecraft.getMinecraft().getResourceManager());
				}
			}
		};//*/

		private final static String[] ACTION_LIST;
		//private final static String actionListInOneLine;

		static {
			int length = EnumActions.values().length;

			String[] list = new String[length];
			//StringBuilder oneLine = new StringBuilder("<");

			for (EnumActions action : EnumActions.values()) {
				list[action.ordinal()] = action.toString().toLowerCase(Locale.ROOT);
				//oneLine.append(action.toString().toLowerCase(Locale.ROOT)).append(action.ordinal() < length - 1 ? "|" : ">");
			}

			ACTION_LIST = list;
			//actionListInOneLine = oneLine.toString();
		}

		protected abstract void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException;

		protected List<String> tabCompletion(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
			return Collections.emptyList();
		}
	}
}

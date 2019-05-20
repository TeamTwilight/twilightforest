package twilightforest;

import com.google.common.collect.ImmutableList;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import twilightforest.capabilities.CapabilityList;
import twilightforest.capabilities.shield.IShieldCapability;
import twilightforest.world.ChunkGeneratorTFBase;
import twilightforest.world.TFWorld;
import twilightforest.world.WorldProviderTwilightForest;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class CommandTF extends CommandBase {
	private static final List<String> aliases = ImmutableList.of("tffeature", "twilightforest", "tf");

	@Override
	public List<String> getAliases() {
		return aliases;
	}

	@Override
	public String getName() {
		return "tffeature"; // This has become more of a general-purpose command now, but keeping this for legacy
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
			ChunkGeneratorTFBase chunkGenerator = (ChunkGeneratorTFBase) TFWorld.getChunkGenerator(player.world);

			final BlockPos pos = new BlockPos(dx, dy, dz);
			if (chunkGenerator.isBlockInStructureBB(pos)) {
				sender.sendMessage(new TextComponentTranslation("commands.tffeature.structure.conquer.update", chunkGenerator.isStructureConquered(pos), flag));

				chunkGenerator.setStructureConquered(dx, dy, dz, flag);
			} else {
				throw new CommandException("commands.tffeature.structure.required");
			}
		}
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		try {
			return EnumActions.valueOf(args[0].toUpperCase(Locale.ROOT)).isUsernameIndex(args, index);
		} catch (Throwable t) {
			// Blah blah
		}

		return false;
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
					ChunkGeneratorTFBase chunkGenerator = (ChunkGeneratorTFBase) TFWorld.getChunkGenerator(player.world);

					final BlockPos pos = new BlockPos(dx, dy, dz);

					if (chunkGenerator.isBlockInStructureBB(pos)) {
						sender.sendMessage(new TextComponentTranslation("commands.tffeature.structure.inside"));

						sender.sendMessage(new TextComponentTranslation("commands.tffeature.structure.conquer.status", chunkGenerator.isStructureConquered(pos)));
						// are you in a room?

						// what is the spawn list
//						List<SpawnListEntry> spawnList = chunkGenerator.getPossibleCreatures(EnumCreatureType.monster, dx, dy, dz);
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
					TFFeature.SMALL_HILL.name,
					TFFeature.MEDIUM_HILL.name,
					TFFeature.LARGE_HILL.name,
					TFFeature.HEDGE_MAZE.name,
					TFFeature.NAGA_COURTYARD.name,
					TFFeature.LICH_TOWER.name,
					TFFeature.ICE_TOWER.name,
					TFFeature.QUEST_GROVE.name,
					TFFeature.HYDRA_LAIR.name,
					TFFeature.LABYRINTH.name,
					TFFeature.DARK_TOWER.name,
					TFFeature.KNIGHT_STRONGHOLD.name,
					TFFeature.YETI_CAVE.name,
					TFFeature.TROLL_CAVE.name,
					TFFeature.FINAL_CASTLE.name,
					TFFeature.MUSHROOM_TOWER.name
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
		},
		SHIELD {
			@Override
			protected boolean isUsernameIndex(String[] args, int index) {
				return index == 1;
			}

			@Override
			protected void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
				Entity entity = getEntity(server, sender, args[1]);

				if (args.length >= 4 && entity.hasCapability(CapabilityList.SHIELDS, null)) {
					// If no boolean param is around, then default val is true
					boolean temp = args.length < 5 || Boolean.valueOf(args[4]);

					IShieldCapability cap = entity.getCapability(CapabilityList.SHIELDS, null);

					if (cap != null) {
						if ("set".equals(args[2].toLowerCase(Locale.ROOT))) {
							cap.setShields(Integer.valueOf(args[3]), temp);
						} else if ("add".equals(args[2].toLowerCase(Locale.ROOT))) {
							cap.addShields(Integer.valueOf(args[3]), temp);
						}
					}
				}
			}
		};

		private static final String[] ACTION_LIST;

		static {
			int length = EnumActions.values().length;

			String[] list = new String[length];

			for (EnumActions action : EnumActions.values())
				list[action.ordinal()] = action.toString().toLowerCase(Locale.ROOT);

			ACTION_LIST = list;
		}

		protected abstract void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException;

		protected List<String> tabCompletion(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
			return Collections.emptyList();
		}

		protected boolean isUsernameIndex(String[] args, int index) {
			return false;
		}
	}
}

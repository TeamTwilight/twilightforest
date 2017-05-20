package twilightforest;

import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;

import javax.annotation.Nullable;

public class CommandTFProgress extends CommandBase {
	
	private String[] bosses = {"none", "naga", "lich", "mooshroom", "hydra", "knights", "urghast", "yeti", "snowqueen", "giants", "final"};

	@Override
	public String getName() {
		return "tfprogress";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "tfprogress <player> <boss>";
	}

    @Override
	public int getRequiredPermissionLevel()
    {
        return 2;
    }
	
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
    	if (args.length < 2) {
    		throw new WrongUsageException("tfprogress <player> <boss>", new Object[0]);
    	} else {
    		EntityPlayerMP player = getPlayer(server, sender, args[0]);
    		int bossIndex = getBossIndex(args[1]);
    		
            notifyCommandListener(sender, this, "Setting player %s progress to past boss %s.", new Object[] {player.getName(), bosses[bossIndex]});

            
            setProgress (player, bossIndex);
    	}
    }

    private void setProgress(EntityPlayerMP player, int bossIndex) {
    	
    	// give previous achievements, if necessary
    	if (bossIndex > 0) {
    		for (int i = 0; i < bossIndex; i++) {
    			setProgress(player, i);
    		}
    	}
    	
    	// give achievements
    	switch (bossIndex) {
    	case 0:
    	default :
    		break;
    	case 1:
    		player.addStat(TFAchievementPage.twilightPortal);
    		player.addStat(TFAchievementPage.twilightArrival);
    		player.addStat(TFAchievementPage.twilightHunter);
    		player.addStat(TFAchievementPage.twilightKillNaga);
    		player.addStat(TFAchievementPage.twilightProgressNaga);
    		break;
    	case 2:
    		player.addStat(TFAchievementPage.twilightKillLich);
    		player.addStat(TFAchievementPage.twilightProgressLich);
    		break;
    	case 3:
    		player.addStat(TFAchievementPage.twilightProgressLabyrinth);
    		break;
    	case 4:
    		player.addStat(TFAchievementPage.twilightKillHydra);
    		player.addStat(TFAchievementPage.twilightProgressHydra);
    		break;
    	case 5:
    		player.addStat(TFAchievementPage.twilightProgressTrophyPedestal);
    		player.addStat(TFAchievementPage.twilightProgressKnights);
    		break;
    	case 6:
    		player.addStat(TFAchievementPage.twilightProgressUrghast);
    		break;
    	case 7:
    		player.addStat(TFAchievementPage.twilightProgressYeti);
    		break;
    	case 8:
    		player.addStat(TFAchievementPage.twilightProgressGlacier);
    		break;
    	case 9:
    		player.addStat(TFAchievementPage.twilightProgressTroll);
    		break;
    	case 10:
    		player.addStat(TFAchievementPage.twilightProgressThorns);
    		player.addStat(TFAchievementPage.twilightProgressCastle);
    		break;
    	}
    	
    	
	}

	private int getBossIndex(String string) {
		for (int i = 0; i < bosses.length; i++) {
			if (bosses[i].equalsIgnoreCase(string)) {
				return i;
			}
		}

		return 0;
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
		return args.length == 1 ? getListOfStringsMatchingLastWord(args, this.getListOfPlayers()) : (args.length == 2 ? getListOfStringsMatchingLastWord(args, bosses) : null);
	}

    private String[] getListOfPlayers()
    {
        return FMLCommonHandler.instance().getMinecraftServerInstance().getOnlinePlayerNames();
    }

    @Override
	public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_)
    {
        return p_82358_2_ == 0;
    }
}

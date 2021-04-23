package twilightforest.client;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.world.server.ServerWorld;
import twilightforest.ASMHooks;
import twilightforest.TFCommonProxy;

import javax.annotation.Nullable;

public class TFClientProxy extends TFCommonProxy {

	private boolean isDangerOverlayShown;

//	public static MusicTicker.MusicType TFMUSICTYPE;

	@Nullable
	public static Iterable<Entity> getEntityListForASM() {
		return ASMHooks.world instanceof ServerWorld ? ((ServerWorld) ASMHooks.world).func_241136_z_() : ASMHooks.world instanceof ClientWorld ? ((ClientWorld) ASMHooks.world).getAllEntities() : null;
	}

	@Override
	public void init() {

//		MinecraftForge.EVENT_BUS.register(new LoadingScreenListener());

//		TFMUSICTYPE = EnumHelperClient.addMusicType("TFMUSIC", TFSounds.MUSIC, 1200, 12000);

		//ShaderManager.initShaders();

//		ClientCommandHandler.instance.registerCommand(new CommandBase() {
//			@Override
//			public String getName() {
//				return "tfreload";
//			}
//
//			@Override
//			public String getUsage(ICommandSender sender) {
//				return "commands.tffeature.reload";
//			}
//
//			@Override
//			public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
//				if(FMLCommonHandler.instance().getEffectiveSide().isClient()) {
//					Minecraft.getInstance().player.sendMessage(new StringTextComponent("Reloading Twilight Forest Shaders!"));
//					twilightforest.client.shader.ShaderManager.getShaderReloadListener().onResourceManagerReload(net.minecraft.client.Minecraft.getInstance().getResourceManager());
//					if (TFCompat.IMMERSIVEENGINEERING.isActivated())
//						twilightforest.compat.ie.IEShaderRegister.initShaders();
//				}
//			}
//		});
	}

//	public boolean isDangerOverlayShown() {
//		return isDangerOverlayShown;
//	}
//
//	public void setDangerOverlayShown(boolean isDangerOverlayShown) {
//		this.isDangerOverlayShown = isDangerOverlayShown;
//	}

}

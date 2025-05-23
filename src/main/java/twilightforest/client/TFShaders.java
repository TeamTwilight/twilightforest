package twilightforest.client;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.renderer.ShaderDefines;
import net.minecraft.client.renderer.ShaderProgram;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import twilightforest.TwilightForestMod;

public class TFShaders {

	public static ShaderProgram RED_THREAD;
	public static ShaderProgram AURORA;

	public static void registerShaders(RegisterShadersEvent event) {
		RED_THREAD = new ShaderProgram(TwilightForestMod.prefix("core/red_thread/red_thread"), DefaultVertexFormat.BLOCK, ShaderDefines.EMPTY);
		event.registerShader(RED_THREAD);
		AURORA = new ShaderProgram(TwilightForestMod.prefix("core/aurora/aurora"), DefaultVertexFormat.POSITION_COLOR, ShaderDefines.EMPTY);
		event.registerShader(AURORA);
	}
}

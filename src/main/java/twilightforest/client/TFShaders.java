package twilightforest.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.IEventBus;

import java.io.IOException;
import java.util.function.Consumer;

import static twilightforest.TFConstants.MOD_ID;

public class TFShaders {

	public static ShaderInstance RED_THREAD;

	public static void init(IEventBus bus) {
		bus.addListener((Consumer<RegisterShadersEvent>) event -> {
			try {
				event.registerShader(new ShaderInstance(event.getResourceManager(), new ResourceLocation(MOD_ID, "red_thread/red_thread"), DefaultVertexFormat.
						BLOCK), shader -> RED_THREAD = shader);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

}

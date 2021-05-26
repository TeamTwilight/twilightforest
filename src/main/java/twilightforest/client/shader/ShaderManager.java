/*
 * Original class written by Vazkii for Botania.
 * Copied from TTFTCUTS' ShadowsOfPhysis
 */

// TEMA: this is the main shader stuff, where the programs are loaded and compiled for the card.
// other relevant files are the shader in /assets/physis/shader/, and the tesr in /client/render/tile/
// they have other comments like this in.

package twilightforest.client.shader;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.IResourceManager;
import net.minecraft.resources.SimpleReloadableResourceManager;
import net.minecraftforge.resource.ISelectiveResourceReloadListener;
import net.minecraftforge.resource.VanillaResourceType;
import org.lwjgl.opengl.*;
import twilightforest.TFConfig;
import twilightforest.TwilightForestMod;
import twilightforest.client.TFClientEvents;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.function.IntConsumer;

public final class ShaderManager {

    private static ISelectiveResourceReloadListener shaderReloadListener;

    private static final int VERT =  ARBVertexShader.GL_VERTEX_SHADER_ARB;
    private static final int FRAG =  ARBFragmentShader.GL_FRAGMENT_SHADER_ARB;

    private static final String PREFIX = "/assets/twilightforest/shaders/";

    @SuppressWarnings({"WeakerAccess", "unused"})
    public static int
            enderPortalShader,
            twilightSkyShader,
            fireflyShader,
            auroraShader,
            carminiteShader,
            towerDeviceShader,
            yellowCircuitShader,
            bloomShader,
            starburstShader,
            shieldShader,
            outlineShader;

    @SuppressWarnings("WeakerAccess")
    public static final class Uniforms {

        public static final ShaderUniform TIME       = ShaderUniform.create("time"      , () -> TFClientEvents.time + Minecraft.getInstance().getRenderPartialTicks());
        public static final ShaderUniform YAW        = ShaderUniform.create("yaw"       , () ->  (Minecraft.getInstance().player.rotationYaw   * 2.0f * TFClientEvents.PI) / 360.0f);
        public static final ShaderUniform PITCH      = ShaderUniform.create("pitch"     , () -> -(Minecraft.getInstance().player.rotationPitch * 2.0f * TFClientEvents.PI) / 360.0f);
        public static final ShaderUniform RESOLUTION = ShaderUniform.create("resolution", () -> Minecraft.getInstance().getMainWindow().getWidth(), () -> Minecraft.getInstance().getMainWindow().getHeight());
        public static final ShaderUniform ZERO       = ShaderUniform.create("zero"      , 0);
        public static final ShaderUniform ONE        = ShaderUniform.create("one"       , 1);
        public static final ShaderUniform TWO        = ShaderUniform.create("two"       , 2);

        public static final ShaderUniform[] STAR_UNIFORMS = { TIME, YAW, PITCH, RESOLUTION, ZERO, TWO };
        public static final ShaderUniform[] TIME_UNIFORM  = { TIME };
    }

    public static void initShaders() {
        IResourceManager iManager;

        if ((iManager = Minecraft.getInstance().getResourceManager()) instanceof SimpleReloadableResourceManager) {
            ((SimpleReloadableResourceManager) iManager).addReloadListener(shaderReloadListener = (manager, predicate) -> {
                if (predicate.test(VanillaResourceType.SHADERS)) reloadShaders();
            });
        }
    }

    public static ISelectiveResourceReloadListener getShaderReloadListener() {
        return shaderReloadListener;
    }

    private static void reloadShaders() {
        //deleteProgram(enderPortalShader);
        deleteProgram(twilightSkyShader);
        deleteProgram(fireflyShader);
        deleteProgram(auroraShader);
        deleteProgram(carminiteShader);
        deleteProgram(towerDeviceShader);
        deleteProgram(yellowCircuitShader);
        //deleteProgram(bloomShader);
        deleteProgram(starburstShader);
        //deleteProgram(outlineShader);

        initShaderList();
    }

    private static void deleteProgram(int id) {
        if (id != 0)  ARBShaderObjects.glDeleteObjectARB(id);
    }

    private static void initShaderList() {
        //enderPortalShader      = createProgram("standard.vert", "ender.frag");
        twilightSkyShader      = createProgram("standard_texcoord.vert" , "twilight_sky.frag");
        fireflyShader          = createProgram("standard_texcoord2.vert", "firefly.frag");
        auroraShader           = createProgram("standard_texcoord2.vert", "aurora.frag");
        carminiteShader        = createProgram("camera_fixed.vert"      , "spiral.frag");
        towerDeviceShader      = createProgram("camera_fixed.vert"      , "pulsing.frag");
        yellowCircuitShader    = createProgram("standard_texcoord2.vert", "pulsing_yellow.frag");
        //bloomShader            = createProgram("standard.vert", "bloom.frag");
        starburstShader        = createProgram("standard_texcoord2.vert", "starburst.frag");
        shieldShader           = createProgram("standard_texcoord2.vert", "shield.frag");
        //outlineShader          = createProgram("outline.vert", "outline.frag");
    }

    public static void useShader(int shader, @Nullable IntConsumer callback) {
        if(!useShaders())
            return;

        ARBShaderObjects.glUseProgramObjectARB(shader);

        if(shader != 0 && callback != null) callback.accept(shader);
    }

    public static void useShader(int shader, ShaderUniform uniform) {
        if(!useShaders())
            return;

        ARBShaderObjects.glUseProgramObjectARB(shader);

        if(shader != 0) uniform.assignUniform(shader);
    }

    public static void useShader(int shader, ShaderUniform... uniforms) {
        if(!useShaders())
            return;

        ARBShaderObjects.glUseProgramObjectARB(shader);

        if(shader != 0) {
            for (ShaderUniform uniform : uniforms) {
                uniform.assignUniform(shader);
            }
        }
    }

    @SuppressWarnings("WeakerAccess")
    public static void useShader(int shader) {
        if(!useShaders())
            return;

        ARBShaderObjects.glUseProgramObjectARB(shader);
    }

    public static void releaseShader() {
        useShader(0);
    }

    @SuppressWarnings("WeakerAccess")
    public static boolean useShaders() {
        return TFConfig.COMMON_CONFIG.PERFORMANCE.shadersSupported;
    }

    // Most of the code taken from the LWJGL wiki
    // http://lwjgl.org/wiki/index.php?title=GLSL_Shaders_with_LWJGL

    @SuppressWarnings("SameParameterValue")
    private static int createProgram(String vert, String frag) {

        int vertId = 0, fragId = 0, program;
        if(vert != null)
            vertId = createShader(PREFIX+vert, VERT);
        if(frag != null)
            fragId = createShader(PREFIX+frag, FRAG);

        program = ARBShaderObjects.glCreateProgramObjectARB();
        if(program == 0)
            return 0;
        if(vert != null)
            ARBShaderObjects.glAttachObjectARB(program, vertId);
        if(frag != null)
            ARBShaderObjects.glAttachObjectARB(program, fragId);

        ARBShaderObjects.glLinkProgramARB(program);
        if (ARBShaderObjects.glGetObjectParameteriARB(program, ARBShaderObjects.GL_OBJECT_LINK_STATUS_ARB) == GL11.GL_FALSE) {
            TwilightForestMod.LOGGER.error("Failed to create shader! (LINK) {} {}", vert, frag);
            TwilightForestMod.LOGGER.error(getInfoLog(program));
            return 0;
        }

        ARBShaderObjects.glValidateProgramARB(program);
        if (ARBShaderObjects.glGetObjectParameteriARB(program, ARBShaderObjects.GL_OBJECT_VALIDATE_STATUS_ARB) == GL11.GL_FALSE) {
            TwilightForestMod.LOGGER.error("Failed to create shader! (VALIDATE) {} {}", vert, frag);
            TwilightForestMod.LOGGER.error(getInfoLog(program));
            return 0;
        }

        return program;
    }

    private static int createShader(String filename, int shaderType){
        int shader = 0;
        try {
            shader = ARBShaderObjects.glCreateShaderObjectARB(shaderType);

            if(shader == 0)
                return 0;

             ARBShaderObjects.glShaderSourceARB(shader, readFileAsString(filename));
             ARBShaderObjects.glCompileShaderARB(shader);

            if (ARBShaderObjects.glGetObjectParameteriARB(shader,  ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB) == GL11.GL_FALSE) {
                TwilightForestMod.LOGGER.error("Failed to create shader! (COMPILE) {}", filename);
                throw new RuntimeException("Error creating shader: " + getInfoLog(shader));
            }

            return shader;

        } catch (Exception e) {
            ARBShaderObjects.glDeleteObjectARB(shader);
            e.printStackTrace();
            return -1;
        }
    }

    // See ShaderLoader.loadShader for buffer management
    private static String readFileAsString(String path) throws Exception {
        StringBuilder source = new StringBuilder();
        InputStream in = ShaderManager.class.getResourceAsStream(path);
        Exception exception = null;
        BufferedReader reader;
        if(in==null)
            return "";

        try {
            reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));

            Exception innerExc = null;
            try {
                String line;
                while((line = reader.readLine())!=null)
                    source.append(line).append('\n');
            } catch(Exception exc) {
                exception = exc;
            } finally {
                try {
                    reader.close();
                } catch(Exception exc) {
                    if(innerExc==null)
                        innerExc = exc;
                    else exc.printStackTrace();
                }
            }

            if(innerExc!=null)
                throw innerExc;
        } catch(Exception exc) {
            exception = exc;
        } finally {
            try {
                in.close();
            } catch(Exception exc) {
                if(exception==null)
                    exception = exc;
                else exc.printStackTrace();
            }

            if(exception!=null)
                throw exception;
        }
        return source.toString();
    }

    private static String getInfoLog(int program) {
        return  ARBShaderObjects.glGetInfoLogARB(program,  ARBShaderObjects.glGetObjectParameteriARB(program, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB));
    }


    static void glUniform2i(int location, int v0, int v1) {
        ARBShaderObjects.glUniform2iARB(location, v0, v1);
    }

    static void glUniform1f(int location, float v0) {
        ARBShaderObjects.glUniform1fARB(location, v0);
    }

    static void glUniform2f(int location, float v0, float v1) {
        ARBShaderObjects.glUniform2fARB(location, v0, v1);
    }
}

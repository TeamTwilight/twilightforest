/**
 * Original class written by Vazkii for Botania.
 * Copied from TTFTCUTS' ShadowsOfPhysis
 */

// TEMA: this is the main shader stuff, where the programs are loaded and compiled for the card.
// other relevant files are the shader in /assets/physis/shader/, and the tesr in /client/render/tile/
// they have other comments like this in.

package twilightforest.client.shader;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.function.IntConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;

import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import org.lwjgl.opengl.*;
import twilightforest.TFConfig;
import twilightforest.TwilightForestMod;
import twilightforest.client.TFClientEvents;

import javax.annotation.Nullable;

public final class ShaderHelper {
    private static IResourceManagerReloadListener shaderReloadListener;

    private static final int VERT = ARBVertexShader.GL_VERTEX_SHADER_ARB;
    private static final int FRAG = ARBFragmentShader.GL_FRAGMENT_SHADER_ARB;
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
            bloomShader;

    @SuppressWarnings("WeakerAccess") public static final ShaderUniformFloat TIME       = new ShaderUniformFloat("time"      , () -> TFClientEvents.time + Minecraft.getMinecraft().getRenderPartialTicks());
    @SuppressWarnings("WeakerAccess") public static final ShaderUniformFloat YAW        = new ShaderUniformFloat("yaw"       , () -> (Minecraft.getMinecraft().player.rotationYaw * 2.0f * TFClientEvents.PI) / 360.0f);
    @SuppressWarnings("WeakerAccess") public static final ShaderUniformFloat PITCH      = new ShaderUniformFloat("pitch"     , () -> -(Minecraft.getMinecraft().player.rotationPitch * 2.0f * TFClientEvents.PI) / 360.0f);
    @SuppressWarnings("WeakerAccess") public static final ShaderUniformInt2  RESOLUTION = new ShaderUniformInt2 ("resolution", () -> Minecraft.getMinecraft().displayWidth, () -> Minecraft.getMinecraft().displayHeight);
    @SuppressWarnings("WeakerAccess") public static final ShaderUniformInt   ZERO       = new ShaderUniformInt  ("zero"      , () -> 0);
    @SuppressWarnings("WeakerAccess") public static final ShaderUniformInt   ONE        = new ShaderUniformInt  ("one"       , () -> 1);
    @SuppressWarnings("WeakerAccess") public static final ShaderUniformInt   TWO        = new ShaderUniformInt  ("two"       , () -> 2);

    public static final ShaderUniform[] STAR_UNIFORMS = { TIME, YAW, PITCH, RESOLUTION, ZERO, TWO };
    public static final ShaderUniform[] TIME_UNIFORM  = { TIME };

    //@SuppressWarnings("WeakerAccess")
    //public static Framebuffer bloomFbo;

    public static void initShaders() {
        IResourceManager iManager;

        if ((iManager = Minecraft.getMinecraft().getResourceManager()) instanceof SimpleReloadableResourceManager) {
            ((SimpleReloadableResourceManager) iManager).registerReloadListener(shaderReloadListener = (manager -> {
                //deleteShader(enderPortalShader);
                deleteShader(twilightSkyShader);
                deleteShader(fireflyShader);
                deleteShader(auroraShader);
                deleteShader(carminiteShader);
                deleteShader(towerDeviceShader);
                deleteShader(yellowCircuitShader);
                //deleteShader(bloomShader);

                initShaderList();
            }));
        }

        //bloomFbo = new Framebuffer(MINECRAFT.displayWidth, MINECRAFT.displayHeight, true);
    }

    public static IResourceManagerReloadListener getShaderReloadListener() {
        return shaderReloadListener;
    }

    private static void deleteShader(int id) {
        if (id != 0) ARBShaderObjects.glDeleteObjectARB(id);
    }

    private static void initShaderList() {
        //enderPortalShader      = createProgram("standard.vert", "ender.frag");
        twilightSkyShader      = createProgram("standard_texcoord.vert" , "twilight_sky.frag");
        fireflyShader          = createProgram("standard_texcoord2.vert", "firefly.frag");
        auroraShader           = createProgram("standard_texcoord2.vert", "aurora.frag");
        carminiteShader        = createProgram("camera_fixed.vert"      , "spiral.frag");
        towerDeviceShader      = createProgram("standard_texcoord2.vert", "pulsing.frag");
        yellowCircuitShader    = createProgram("standard_texcoord2.vert", "pulsing_yellow.frag");
        //bloomShader            = createProgram("standard.vert", "bloom.frag");
    }

    public static void useShader(int shader, @Nullable IntConsumer callback) {
        if(!useShaders())
            return;

        ARBShaderObjects.glUseProgramObjectARB(shader);

        if(shader != 0 && callback != null) callback.accept(shader);
    }

    @SuppressWarnings("WeakerAccess")
    public static void useShader(int shader) {
        useShader(shader, null);
    }

    public static void releaseShader() {
        useShader(0);
    }

    @SuppressWarnings("WeakerAccess")
    public static boolean useShaders() {
        return TFConfig.performance.shadersSupported && OpenGlHelper.shadersSupported;
    }

    // Most of the code taken from the LWJGL wiki
    // http://lwjgl.org/wiki/index.php?title=GLSL_Shaders_with_LWJGL

    @SuppressWarnings("SameParameterValue")
    private static int createProgram(String vert, String frag) {
        int program = ARBShaderObjects.glCreateProgramObjectARB();

        if(program == 0)
            return 0;

        int vertId = createShader(PREFIX + vert, VERT);
        ARBShaderObjects.glAttachObjectARB(program, vertId);

        int fragId = createShader(PREFIX + frag, FRAG);
        ARBShaderObjects.glAttachObjectARB(program, fragId);

        ARBShaderObjects.glLinkProgramARB(program);
        if(ARBShaderObjects.glGetObjectParameteriARB(program, ARBShaderObjects.GL_OBJECT_LINK_STATUS_ARB) == GL11.GL_FALSE) {
            TwilightForestMod.LOGGER.error("Failed to create shader! 1 " + vert + " " + frag);
            TwilightForestMod.LOGGER.error(getLogInfo(program));
            return 0;
        }

        ARBShaderObjects.glValidateProgramARB(program);
        if (ARBShaderObjects.glGetObjectParameteriARB(program, ARBShaderObjects.GL_OBJECT_VALIDATE_STATUS_ARB) == GL11.GL_FALSE) {
            TwilightForestMod.LOGGER.error("Failed to create shader! 2 " + vert + " " + frag);
            TwilightForestMod.LOGGER.error(getLogInfo(program));
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

            if (ARBShaderObjects.glGetObjectParameteriARB(shader, ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB) == GL11.GL_FALSE) {
                TwilightForestMod.LOGGER.error("Failed to create shader! 3 " + filename);
                throw new RuntimeException("Error creating shader: " + getLogInfo(shader));
            }

            return shader;
        }
        catch(Exception e) {
            ARBShaderObjects.glDeleteObjectARB(shader);
            e.printStackTrace();
            return -1;
        }
    }

    private static String getLogInfo(int obj) {
        return ARBShaderObjects.glGetInfoLogARB(obj, ARBShaderObjects.glGetObjectParameteriARB(obj, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB));
    }

    private static String readFileAsString(String filename) throws Exception {
        StringBuilder source = new StringBuilder();
        InputStream in = ShaderHelper.class.getResourceAsStream(filename);
        Exception exception = null;
        BufferedReader reader;

        if(in == null)
            return "";

        try {
            reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));

            Exception innerExc = null;
            try {
                String line;
                while((line = reader.readLine()) != null)
                    source.append(line).append('\n');
            } catch(Exception exc) {
                exception = exc;
            } finally {
                try {
                    reader.close();
                } catch(Exception exc) {
                    innerExc = exc;
                }
            }

            if(innerExc != null)
                throw innerExc;
        } catch(Exception exc) {
            exception = exc;
        } finally {
            try {
                in.close();
            } catch(Exception exc) {
                if(exception == null)
                    exception = exc;
                else exc.printStackTrace();
            }

            if(exception != null)
                throw exception;
        }

        return source.toString();
    }
}
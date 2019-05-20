/*
 * Original class written by Vazkii for Botania.
 * Copied from TTFTCUTS' ShadowsOfPhysis
 */

// TEMA: this is the main shader stuff, where the programs are loaded and compiled for the card.
// other relevant files are the shader in /assets/physis/shader/, and the tesr in /client/render/tile/
// they have other comments like this in.

package twilightforest.client.shader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.function.IntConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraftforge.client.resource.ISelectiveResourceReloadListener;
import net.minecraftforge.client.resource.VanillaResourceType;

import org.apache.commons.io.IOUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import twilightforest.TFConfig;
import twilightforest.TwilightForestMod;
import twilightforest.client.TFClientEvents;

import javax.annotation.Nullable;

public final class ShaderManager {

    private static ISelectiveResourceReloadListener shaderReloadListener;

    private static final int VERT = OpenGlHelper.arbShaders ? ARBVertexShader.GL_VERTEX_SHADER_ARB : GL20.GL_VERTEX_SHADER;
    private static final int FRAG = OpenGlHelper.arbShaders ? ARBFragmentShader.GL_FRAGMENT_SHADER_ARB : GL20.GL_FRAGMENT_SHADER;

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

        public static final ShaderUniform TIME       = ShaderUniform.create("time"      , () -> TFClientEvents.time + Minecraft.getMinecraft().getRenderPartialTicks());
        public static final ShaderUniform YAW        = ShaderUniform.create("yaw"       , () ->  (Minecraft.getMinecraft().player.rotationYaw   * 2.0f * TFClientEvents.PI) / 360.0f);
        public static final ShaderUniform PITCH      = ShaderUniform.create("pitch"     , () -> -(Minecraft.getMinecraft().player.rotationPitch * 2.0f * TFClientEvents.PI) / 360.0f);
        public static final ShaderUniform RESOLUTION = ShaderUniform.create("resolution", () -> Minecraft.getMinecraft().displayWidth, () -> Minecraft.getMinecraft().displayHeight);
        public static final ShaderUniform ZERO       = ShaderUniform.create("zero"      , 0);
        public static final ShaderUniform ONE        = ShaderUniform.create("one"       , 1);
        public static final ShaderUniform TWO        = ShaderUniform.create("two"       , 2);

        public static final ShaderUniform[] STAR_UNIFORMS = { TIME, YAW, PITCH, RESOLUTION, ZERO, TWO };
        public static final ShaderUniform[] TIME_UNIFORM  = { TIME };
    }

    //@SuppressWarnings("WeakerAccess")
    //public static Framebuffer bloomFbo;

    public static void initShaders() {
        IResourceManager iManager;

        if ((iManager = Minecraft.getMinecraft().getResourceManager()) instanceof SimpleReloadableResourceManager) {
            ((SimpleReloadableResourceManager) iManager).registerReloadListener(shaderReloadListener = (manager, predicate) -> {
                if (predicate.test(VanillaResourceType.SHADERS)) reloadShaders();
            });
        }

        //bloomFbo = new Framebuffer(MINECRAFT.displayWidth, MINECRAFT.displayHeight, true);
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
        if (id != 0) OpenGlHelper.glDeleteProgram(id);
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

        OpenGlHelper.glUseProgram(shader);

        if(shader != 0 && callback != null) callback.accept(shader);
    }

    public static void useShader(int shader, ShaderUniform uniform) {
        if(!useShaders())
            return;

        OpenGlHelper.glUseProgram(shader);

        if(shader != 0) uniform.assignUniform(shader);
    }

    public static void useShader(int shader, ShaderUniform... uniforms) {
        if(!useShaders())
            return;

        OpenGlHelper.glUseProgram(shader);

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

        OpenGlHelper.glUseProgram(shader);
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
        int program = OpenGlHelper.glCreateProgram();

        if(program == 0)
            return 0;

        int vertId = createShader(PREFIX + vert, VERT);
        OpenGlHelper.glAttachShader(program, vertId);

        int fragId = createShader(PREFIX + frag, FRAG);
        OpenGlHelper.glAttachShader(program, fragId);

        OpenGlHelper.glLinkProgram(program);
        if (OpenGlHelper.glGetProgrami(program, OpenGlHelper.GL_LINK_STATUS) == GL11.GL_FALSE) {
            TwilightForestMod.LOGGER.error("Failed to create shader! (LINK) {} {}", vert, frag);
            TwilightForestMod.LOGGER.error(getProgramInfoLog(program));
            return 0;
        }

        glValidateProgram(program);
        if (OpenGlHelper.glGetProgrami(program, VALIDATE_STATUS) == GL11.GL_FALSE) {
            TwilightForestMod.LOGGER.error("Failed to create shader! (VALIDATE) {} {}", vert, frag);
            TwilightForestMod.LOGGER.error(getProgramInfoLog(program));
            return 0;
        }

        return program;
    }

    private static int createShader(String filename, int shaderType){
        int shader = 0;
        try {
            shader = OpenGlHelper.glCreateShader(shaderType);

            if(shader == 0)
                return 0;

            OpenGlHelper.glShaderSource(shader, readFile(filename));
            OpenGlHelper.glCompileShader(shader);

            if (OpenGlHelper.glGetShaderi(shader, OpenGlHelper.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
                TwilightForestMod.LOGGER.error("Failed to create shader! (COMPILE) {}", filename);
                throw new RuntimeException("Error creating shader: " + getShaderInfoLog(shader));
            }

            return shader;

        } catch (Exception e) {
            OpenGlHelper.glDeleteShader(shader);
            e.printStackTrace();
            return -1;
        }
    }

    // See ShaderLoader.loadShader for buffer management
    private static ByteBuffer readFile(String path) throws IOException {
        try (InputStream in = ShaderManager.class.getResourceAsStream(path)) {
            byte[] bytes = IOUtils.toByteArray(in);
            return (ByteBuffer) BufferUtils.createByteBuffer(bytes.length).put(bytes).position(0);
        }
    }

    private static String getShaderInfoLog(int shader) {
        return OpenGlHelper.glGetShaderInfoLog(shader, OpenGlHelper.glGetShaderi(shader, INFO_LOG_LENGTH));
    }

    private static String getProgramInfoLog(int program) {
        return OpenGlHelper.glGetProgramInfoLog(program, OpenGlHelper.glGetProgrami(program, INFO_LOG_LENGTH));
    }

    /* Assorted constants/functions missing from OpenGlHelper */

    private static final int INFO_LOG_LENGTH
            = OpenGlHelper.arbShaders
            ? ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB
            : GL20.GL_INFO_LOG_LENGTH;

    private static final int VALIDATE_STATUS
            = OpenGlHelper.arbShaders
            ? ARBShaderObjects.GL_OBJECT_VALIDATE_STATUS_ARB
            : GL20.GL_VALIDATE_STATUS;

    private static void glValidateProgram(int program) {
        if (OpenGlHelper.arbShaders) {
            ARBShaderObjects.glValidateProgramARB(program);
        } else {
            GL20.glValidateProgram(program);
        }
    }

    static void glUniform2i(int location, int v0, int v1) {
        if (OpenGlHelper.arbShaders) {
            ARBShaderObjects.glUniform2iARB(location, v0, v1);
        } else {
            GL20.glUniform2i(location, v0, v1);
        }
    }

    static void glUniform1f(int location, float v0) {
        if (OpenGlHelper.arbShaders) {
            ARBShaderObjects.glUniform1fARB(location, v0);
        } else {
            GL20.glUniform1f(location, v0);
        }
    }

    static void glUniform2f(int location, float v0, float v1) {
        if (OpenGlHelper.arbShaders) {
            ARBShaderObjects.glUniform2fARB(location, v0, v1);
        } else {
            GL20.glUniform2f(location, v0, v1);
        }
    }
}

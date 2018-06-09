package twilightforest.client.shader;

import org.lwjgl.opengl.ARBShaderObjects;
import java.util.function.Supplier;

public class ShaderUniformFloat extends ShaderUniform {
    private final Supplier<Float> supplier;

    @SuppressWarnings("WeakerAccess")
    public ShaderUniformFloat(String name, Supplier<Float> supplier) {
        super(name);
        this.supplier = supplier;
    }

    public final void assignUniform(int shader) {
        ARBShaderObjects.glUniform1fARB(ARBShaderObjects.glGetUniformLocationARB(shader, name), supplier.get());
    }
}
package twilightforest.client.shader;

import org.lwjgl.opengl.ARBShaderObjects;

import java.util.function.Supplier;

public class ShaderUniformFloat2 extends ShaderUniform {
    private final Supplier<Float> supplier0;
    private final Supplier<Float> supplier1;

    @SuppressWarnings("WeakerAccess")
    public ShaderUniformFloat2(String name, Supplier<Float> supplier0, Supplier<Float> supplier1) {
        super(name);
        this.supplier0 = supplier0;
        this.supplier1 = supplier1;
    }

    @Override
    public final void assignUniform(int shader) {
        ARBShaderObjects.glUniform2fARB(ARBShaderObjects.glGetUniformLocationARB(shader, name), supplier0.get(), supplier1.get());
    }
}
package twilightforest.client.shader;

import org.lwjgl.opengl.ARBShaderObjects;

import java.util.function.IntSupplier;

public class ShaderUniformInt2 extends ShaderUniform {
    private final IntSupplier supplier0;
    private final IntSupplier supplier1;

    @SuppressWarnings("WeakerAccess")
    public ShaderUniformInt2(String name, IntSupplier supplier0, IntSupplier supplier1) {
        super(name);
        this.supplier0 = supplier0;
        this.supplier1 = supplier1;
    }

    @Override
    public final void assignUniform(int shader) {
        ARBShaderObjects.glUniform2iARB(ARBShaderObjects.glGetUniformLocationARB(shader, name), supplier0.getAsInt(), supplier1.getAsInt());
    }
}
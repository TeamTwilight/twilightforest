package twilightforest.client.shader;

import org.lwjgl.opengl.ARBShaderObjects;
import twilightforest.client.TFClientEvents;

import java.util.function.IntSupplier;
import java.util.function.Supplier;

public class ShaderUniformInt extends ShaderUniform {
    private final IntSupplier supplier;

    @SuppressWarnings("WeakerAccess")
    public ShaderUniformInt(String name, IntSupplier supplier) {
        super(name);
        this.supplier = supplier;
    }

    public final void assignUniform(int shader) {
        ARBShaderObjects.glUniform1iARB(ARBShaderObjects.glGetUniformLocationARB(shader, name), supplier.getAsInt());
    }
}
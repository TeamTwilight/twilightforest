package twilightforest.client.shader;

public abstract class ShaderUniform {
    protected final String name;

    @SuppressWarnings("WeakerAccess")
    public ShaderUniform(String name) {
        this.name = name;
    }

    public abstract void assignUniform(int shader);
}

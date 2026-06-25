package twilightforest.asm;

import net.neoforged.neoforgespi.transformation.ProcessorName;
import net.neoforged.neoforgespi.transformation.SimpleClassProcessor;
import net.neoforged.neoforgespi.transformation.SimpleTransformationContext;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.Locale;
import java.util.Set;

/**
 * Base class for transformers that target a single method in a class.
 * Adapts the old ITransformer&lt;MethodNode&gt; pattern to the new SimpleClassProcessor API.
 */
public abstract class SimpleMethodTransformer extends SimpleClassProcessor {
    protected final String targetClassName;
    protected final String targetMethodName;
    protected final String targetMethodDesc;
    private final Set<Target> targets;

    public SimpleMethodTransformer(String className, String methodName, String methodDesc) {
        this.targetClassName = className;
        this.targetMethodName = methodName;
        this.targetMethodDesc = methodDesc;
        this.targets = Set.of(new Target(className));
    }

    @Override
	public ProcessorName name() {
		var owner = targetClassName.toLowerCase(Locale.ROOT).replace('.', '_');
		var method = targetMethodName.toLowerCase(Locale.ROOT).replace('$', '_');
		// Include descriptor to differentiate overloaded methods (e.g. getRenderer(Entity) vs getRenderer(EntityRenderState))
		var desc = targetMethodDesc.replace('/', '_').replace('(', '_').replace(')', '_').replace(";", "_").replace(".", "_").replace("$", "_").replace("[", "arr_").toLowerCase(Locale.ROOT);
		return new ProcessorName("twilightforest", owner + "." + method + desc);
	}

    @Override
    public Set<Target> targets() {
        return targets;
    }

    @Override
    public void transform(ClassNode classNode, SimpleTransformationContext context) {
        for (MethodNode method : classNode.methods) {
            if (method.name.equals(targetMethodName) && method.desc.equals(targetMethodDesc)) {
                transform(classNode, method, context);
                return;
            }
        }
    }

    protected abstract void transform(ClassNode classNode, MethodNode method, SimpleTransformationContext context);
}

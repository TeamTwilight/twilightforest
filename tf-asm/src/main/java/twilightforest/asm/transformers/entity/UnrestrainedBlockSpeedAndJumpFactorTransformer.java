package twilightforest.asm.transformers.entity;

import net.neoforged.neoforgespi.transformation.ProcessorName;
import net.neoforged.neoforgespi.transformation.SimpleClassProcessor;
import net.neoforged.neoforgespi.transformation.SimpleTransformationContext;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import twilightforest.asm.ASMUtil;

import java.util.Set;

/**
 * {@link twilightforest.asmhooks.EntityHooks#resetFactorWithUnrestrained}
 */
public class UnrestrainedBlockSpeedAndJumpFactorTransformer extends SimpleClassProcessor {

	private final Set<Target> targets;
	private final ProcessorName name;

	public UnrestrainedBlockSpeedAndJumpFactorTransformer() {
		this.targets = Set.of(new Target("net.minecraft.world.entity.Entity"));
		this.name = new ProcessorName("twilightforest", "entity");
	}

	@Override
	public ProcessorName name() {
		return this.name;
	}

	@Override
	public Set<Target> targets() {
		return this.targets;
	}

	@Override
	public void transform(ClassNode classNode, SimpleTransformationContext context) {
		for (MethodNode method : classNode.methods) {
			if (("getBlockJumpFactor".equals(method.name) && "()F".equals(method.desc)) ||
				("getBlockSpeedFactor".equals(method.name) && "()F".equals(method.desc))) {
				transformMethod(method);
			}
		}
	}

	private void transformMethod(MethodNode method) {
		ASMUtil.findInstructions(
			method,
			Opcodes.FRETURN
		).forEach(target -> method.instructions.insertBefore(
			target,
			ASMUtil.listOf(
				new VarInsnNode(Opcodes.ALOAD, 0),
				new MethodInsnNode(
					Opcodes.INVOKESTATIC,
					"twilightforest/asmhooks/EntityHooks",
					"resetFactorWithUnrestrained",
					"(FLnet/minecraft/world/entity/Entity;)F"
				)
			)
		));
	}
}

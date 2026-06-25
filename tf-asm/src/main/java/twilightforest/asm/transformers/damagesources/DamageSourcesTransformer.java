package twilightforest.asm.transformers.damagesources;

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
 * {@link twilightforest.asmhooks.DamageSourceHooks#getCustomDamageSource}
 */
public class DamageSourcesTransformer extends SimpleClassProcessor {

	private final Set<Target> targets;
	private final ProcessorName name;

	public DamageSourcesTransformer() {
		this.targets = Set.of(new Target("net.minecraft.world.damagesource.DamageSources"));
		this.name = new ProcessorName("twilightforest", "damagesources");
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
			if (("mobAttack".equals(method.name) && "(Lnet/minecraft/world/entity/LivingEntity;)Lnet/minecraft/world/damagesource/DamageSource;".equals(method.desc)) ||
				("playerAttack".equals(method.name) && "(Lnet/minecraft/world/entity/player/Player;)Lnet/minecraft/world/damagesource/DamageSource;".equals(method.desc))) {
				transformMethod(method);
			}
		}
	}

	private void transformMethod(MethodNode method) {
		ASMUtil.findInstructions(
			method,
			Opcodes.ARETURN
		).forEach(target -> method.instructions.insertBefore(target, ASMUtil.listOf(
			// First in stack should be a DamageSource
			new VarInsnNode(Opcodes.ALOAD, 1), // Also add the parameter, a LivingEntity
			new MethodInsnNode(
				Opcodes.INVOKESTATIC,
				"twilightforest/asmhooks/DamageSourceHooks",
				"getCustomDamageSource",
				"(Lnet/minecraft/world/damagesource/DamageSource;Lnet/minecraft/world/entity/LivingEntity;)Lnet/minecraft/world/damagesource/DamageSource;"
			)
		)));
	}
}

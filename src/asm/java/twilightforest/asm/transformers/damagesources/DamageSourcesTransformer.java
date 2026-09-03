package twilightforest.asm.transformers.damagesources;

import net.neoforged.neoforgespi.transformation.ProcessorName;
import net.neoforged.neoforgespi.transformation.SimpleMethodProcessor;
import net.neoforged.neoforgespi.transformation.SimpleTransformationContext;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import twilightforest.asm.ASMUtil;

import java.util.Set;

/**
 * {@link twilightforest.asmhooks.DamageSourceHooks#getCustomDamageSource}
 */
public class DamageSourcesTransformer extends SimpleMethodProcessor {

	@Override
	public ProcessorName name() {
		return ASMUtil.named("damage_sources");
	}

	@Override
	public void transform(MethodNode node, SimpleTransformationContext context) {
		ASMUtil.findInstructions(
			node,
			Opcodes.ARETURN
		).forEach(target -> node.instructions.insertBefore(target, ASMUtil.listOf(
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

	@Override
	public Set<Target> targets() {
		return Set.of(new Target(
			"net.minecraft.world.damagesource.DamageSources",
			"mobAttack",
			"(Lnet/minecraft/world/entity/LivingEntity;)Lnet/minecraft/world/damagesource/DamageSource;"
		), new Target(
			"net.minecraft.world.damagesource.DamageSources",
			"playerAttack",
			"(Lnet/minecraft/world/entity/player/Player;)Lnet/minecraft/world/damagesource/DamageSource;"
		));
	}
}

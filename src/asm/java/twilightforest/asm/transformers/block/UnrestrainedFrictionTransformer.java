package twilightforest.asm.transformers.block;

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
 * {@link twilightforest.asmhooks.BlockHooks#resetBlockFrictionWithUnrestrained}
 */
public class UnrestrainedFrictionTransformer extends SimpleMethodProcessor {

	@Override
	public ProcessorName name() {
		return ASMUtil.named("unrestrained_friction");
	}

	@Override
	public void transform(MethodNode node, SimpleTransformationContext context) {
		ASMUtil.findInstructions(
			node,
			Opcodes.FRETURN
		).forEach(target -> node.instructions.insertBefore(
			target,
			ASMUtil.listOf(
				new VarInsnNode(Opcodes.ALOAD, 4),
				new MethodInsnNode(
					Opcodes.INVOKESTATIC,
					"twilightforest/asmhooks/BlockHooks",
					"resetBlockFrictionWithUnrestrained",
					"(FLnet/minecraft/world/entity/Entity;)F"
				)
			)
		));
	}

	@Override
	public Set<Target> targets() {
		return Set.of(new Target(
			"net.neoforged.neoforge.common.extensions.IBlockExtension",
			"getFriction",
			"(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/Entity;)F"
		));
	}
}
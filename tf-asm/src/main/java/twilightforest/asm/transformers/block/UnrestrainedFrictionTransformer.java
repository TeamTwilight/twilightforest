package twilightforest.asm.transformers.block;

import net.neoforged.neoforgespi.transformation.SimpleTransformationContext;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import twilightforest.asm.ASMUtil;
import twilightforest.asm.SimpleMethodTransformer;

/**
 * {@link twilightforest.asmhooks.BlockHooks#resetBlockFrictionWithUnrestrained}
 */
public class UnrestrainedFrictionTransformer extends SimpleMethodTransformer {

	public UnrestrainedFrictionTransformer() {
		super(
			"net.neoforged.neoforge.common.extensions.IBlockExtension",
			"getFriction",
			"(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/Entity;)F"
		);
	}

	@Override
	protected void transform(ClassNode classNode, MethodNode method, SimpleTransformationContext context) {
		ASMUtil.findInstructions(
			method,
			Opcodes.FRETURN
		).forEach(target -> method.instructions.insertBefore(
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
}

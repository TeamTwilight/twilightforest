package twilightforest.asm.transformers.snow;

import net.neoforged.neoforgespi.transformation.SimpleTransformationContext;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import twilightforest.asm.ASMUtil;
import twilightforest.asm.SimpleMethodTransformer;

/**
 * {@link twilightforest.asmhooks.BlockHooks#keepSnowyStateForSnowloggableBlocks}
 */
public class KeepGrassSnowyForSnowloggableBlocksTransformer extends SimpleMethodTransformer {

	public KeepGrassSnowyForSnowloggableBlocksTransformer() {
		super(
			"net.minecraft.world.level.block.SnowyDirtBlock",
			"isSnowySetting",
			"(Lnet/minecraft/world/level/block/state/BlockState;)Z"
		);
	}

	@Override
	protected void transform(ClassNode classNode, MethodNode method, SimpleTransformationContext context) {
		ASMUtil.findInstructions(
			method,
			Opcodes.IRETURN
		).forEach(target -> method.instructions.insertBefore(
			target,
			ASMUtil.listOf(
				new VarInsnNode(Opcodes.ALOAD, 0),
				new MethodInsnNode(
					Opcodes.INVOKESTATIC,
					"twilightforest/asmhooks/BlockHooks",
					"keepSnowyStateForSnowloggableBlocks",
					"(ZLnet/minecraft/world/level/block/state/BlockState;)Z"
				)
			)
		));
	}

}

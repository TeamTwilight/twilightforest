package twilightforest.asm.transformers.player;

import net.neoforged.neoforgespi.transformation.SimpleTransformationContext;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import twilightforest.asm.ASMUtil;
import twilightforest.asm.SimpleMethodTransformer;

/**
 * {@link twilightforest.asmhooks.PlayerHooks#straightAheadNullify}
 * {@link twilightforest.asmhooks.PlayerHooks#straightAheadRestore}
 */
public class GetFieldOfViewModifierTransformer extends SimpleMethodTransformer {

	public GetFieldOfViewModifierTransformer() {
		super(
			"net.minecraft.client.player.AbstractClientPlayer",
			"getFieldOfViewModifier",
			"()F"
		);
	}

	@Override
	protected void transform(ClassNode classNode, MethodNode method, SimpleTransformationContext context) {
		ASMUtil.findInstructions(method, Opcodes.FCONST_1).findFirst().ifPresent(target -> {
			method.instructions.insertBefore(target, ASMUtil.listOf(
				new VarInsnNode(Opcodes.ALOAD, 0),
				new MethodInsnNode(Opcodes.INVOKESTATIC,
					"twilightforest/asmhooks/PlayerHooks",
					"straightAheadNullify",
					"(Lnet/minecraft/client/player/AbstractClientPlayer;)V",
					false)
			));

			ASMUtil.findInstructions(method, Opcodes.FRETURN).forEach(insn ->
				method.instructions.insertBefore(insn, ASMUtil.listOf(
					new VarInsnNode(Opcodes.ALOAD, 0),
					new MethodInsnNode(Opcodes.INVOKESTATIC,
						"twilightforest/asmhooks/PlayerHooks",
						"straightAheadRestore",
						"(Lnet/minecraft/client/player/AbstractClientPlayer;)V",
						false)
				))
			);
		});
	}

}

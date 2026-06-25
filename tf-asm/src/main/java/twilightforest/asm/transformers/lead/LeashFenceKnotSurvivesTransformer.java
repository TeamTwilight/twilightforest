package twilightforest.asm.transformers.lead;

import net.neoforged.neoforgespi.transformation.SimpleTransformationContext;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import twilightforest.asm.ASMUtil;
import twilightforest.asm.SimpleMethodTransformer;

/**
 * {@link twilightforest.asmhooks.BlockHooks#leashFenceKnotSurvives}
 */
public class LeashFenceKnotSurvivesTransformer extends SimpleMethodTransformer {

	public LeashFenceKnotSurvivesTransformer() {
		super(
			"net.minecraft.world.entity.decoration.LeashFenceKnotEntity",
			"survives",
			"()Z"
		);
	}

	@Override
	protected void transform(ClassNode classNode, MethodNode method, SimpleTransformationContext context) {
		ASMUtil.findInstructions(
			method,
			Opcodes.IRETURN
		).findFirst().ifPresent(target -> method.instructions.insertBefore(
			target,
			ASMUtil.listOf(
				new VarInsnNode(Opcodes.ALOAD, 0),
				new MethodInsnNode(
					Opcodes.INVOKESTATIC,
					"twilightforest/asmhooks/BlockHooks",
					"leashFenceKnotSurvives",
					"(ZLnet/minecraft/world/entity/decoration/LeashFenceKnotEntity;)Z"
				)
			)
		));
	}

}

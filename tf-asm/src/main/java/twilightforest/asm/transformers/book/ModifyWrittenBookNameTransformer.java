package twilightforest.asm.transformers.book;

import net.neoforged.neoforgespi.transformation.SimpleTransformationContext;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import twilightforest.asm.ASMUtil;
import twilightforest.asm.SimpleMethodTransformer;

/**
 * {@link twilightforest.asmhooks.ItemHooks#modifyWrittenBookName}
 */
public class ModifyWrittenBookNameTransformer extends SimpleMethodTransformer {

	public ModifyWrittenBookNameTransformer() {
		super(
			"net.minecraft.world.item.WrittenBookItem",
			"getName",
			"(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/network/chat/Component;"
		);
	}

	@Override
	protected void transform(ClassNode classNode, MethodNode method, SimpleTransformationContext context) {
		ASMUtil.findInstructions(method, Opcodes.ARETURN)
			.findFirst()
			.ifPresent(target -> method.instructions.insertBefore(
				target,
				ASMUtil.listOf(
					new VarInsnNode(Opcodes.ALOAD, 1),
					new MethodInsnNode(
						Opcodes.INVOKESTATIC,
						"twilightforest/asmhooks/ItemHooks",
						"modifyWrittenBookName",
						"(Lnet/minecraft/network/chat/Component;Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/network/chat/Component;"
					)
				)
			));
	}

}

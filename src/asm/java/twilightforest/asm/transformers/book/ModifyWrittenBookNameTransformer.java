package twilightforest.asm.transformers.book;

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
 * {@link twilightforest.asmhooks.ItemHooks#modifyWrittenBookName}
 */
public class ModifyWrittenBookNameTransformer extends SimpleMethodProcessor {

	@Override
	public ProcessorName name() {
		return ASMUtil.named("modify_written_book_name");
	}

	@Override
	public void transform(MethodNode node, SimpleTransformationContext context) {
		ASMUtil.findInstructions(node, Opcodes.ARETURN)
			.findFirst()
			.ifPresent(target -> node.instructions.insertBefore(
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

	@Override
	public Set<Target> targets() {
		return Set.of(new Target(
			"net.minecraft.world.item.WrittenBookItem",
			"getName",
			"(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/network/chat/Component;"
		));
	}

}

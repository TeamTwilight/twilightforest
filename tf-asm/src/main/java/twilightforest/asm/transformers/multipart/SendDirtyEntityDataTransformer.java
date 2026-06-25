package twilightforest.asm.transformers.multipart;

import net.neoforged.neoforgespi.transformation.SimpleTransformationContext;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import twilightforest.asm.SimpleMethodTransformer;

/**
 * {@link twilightforest.asmhooks.MultipartHooks#sendDirtyEntityData}
 */
public class SendDirtyEntityDataTransformer extends SimpleMethodTransformer {

	public SendDirtyEntityDataTransformer() {
		super(
			"net.minecraft.server.level.ServerEntity",
			"sendDirtyEntityData",
			"()V"
		);
	}

	@Override
	protected void transform(ClassNode classNode, MethodNode method, SimpleTransformationContext context) {
		// Insert at the beginning of sendDirtyEntityData():
		//   ALOAD 0
		//   GETFIELD net/minecraft/server/level/ServerEntity.entity:Lnet/minecraft/world/entity/Entity;
		//   INVOKESTATIC twilightforest/asmhooks/MultipartHooks.sendDirtyEntityData(Lnet/minecraft/world/entity/Entity;)V
		// We don't use the return value; just call it for its side effect (sending the multipart packet).
		// This is safer than inserting before getEntityData() because it runs regardless of code path.
		InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInject.add(new FieldInsnNode(
			Opcodes.GETFIELD,
			"net/minecraft/server/level/ServerEntity",
			"entity",
			"Lnet/minecraft/world/entity/Entity;"
		));
		toInject.add(new MethodInsnNode(
			Opcodes.INVOKESTATIC,
			"twilightforest/asmhooks/MultipartHooks",
			"sendDirtyEntityData",
			"(Lnet/minecraft/world/entity/Entity;)V",
			false
		));
		method.instructions.insert(toInject);
	}

}

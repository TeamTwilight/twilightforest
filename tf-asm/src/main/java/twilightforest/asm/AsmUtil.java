package twilightforest.asm;

import org.objectweb.asm.tree.*;

import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class AsmUtil {

	private AsmUtil() {

	}

	public static Stream<AbstractInsnNode> streamInstructions(MethodNode node) {
		return StreamSupport.stream(node.instructions.spliterator(), false);
	}

	/**
	 * If you are looking for a #findFirst then use Stream#findFirst, you dummy
	 */
	public static <T extends AbstractInsnNode> Optional<T> findLast(Stream<T> stream) {
		return stream.reduce((a, b) -> b);
	}

	public static Stream<AbstractInsnNode> findInstructions(MethodNode node, int opcode) {
		return streamInstructions(node).filter(instruction -> instruction.getOpcode() == opcode);
	}

	public static Stream<MethodInsnNode> findMethodInstructions(MethodNode node, int opcode, String owner, String name, String descriptor) {
		return streamInstructions(node).filter(instruction -> instruction instanceof MethodInsnNode i &&
			i.getOpcode() == opcode &&
			i.owner.equals(owner) &&
			i.name.equals(name) &&
			i.desc.equals(descriptor)
		).map(MethodInsnNode.class::cast);
	}

	public static Stream<FieldInsnNode> findFieldInstructions(MethodNode node, int opcode, String owner, String name) {
		return streamInstructions(node).filter(instruction -> instruction instanceof FieldInsnNode i &&
			i.getOpcode() == opcode &&
			i.owner.equals(owner) &&
			i.name.equals(name)
		).map(FieldInsnNode.class::cast);
	}

	public static Stream<VarInsnNode> findVarInstructions(MethodNode node, int opcode, int index) {
		return streamInstructions(node).filter(instruction -> instruction instanceof VarInsnNode i &&
			i.getOpcode() == opcode &&
			i.var == index
		).map(VarInsnNode.class::cast);
	}

}

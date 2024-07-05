package twilightforest.asm;

import org.objectweb.asm.tree.*;

import java.util.Optional;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ASMUtil {

	private ASMUtil() {

	}

	public static Stream<AbstractInsnNode> streamInstructions(MethodNode node) {
		return StreamSupport.stream(node.instructions.spliterator(), false);
	}

	public static Stream<AbstractInsnNode> streamInstructions(MethodNode node, AbstractInsnNode index) {
		return StreamSupport.stream(Spliterators.spliteratorUnknownSize(node.instructions.iterator(node.instructions.indexOf(index)), 0), false);
	}

	/**
	 * If you are looking for a #findFirst then use Stream#findFirst, you dummy
	 */
	public static <T extends AbstractInsnNode> Optional<T> findLast(Stream<T> stream) {
		return stream.reduce((a, b) -> b);
	}

	public static Stream<AbstractInsnNode> findInstructions(Stream<AbstractInsnNode> stream, int opcode) {
		return stream.filter(instruction -> instruction.getOpcode() == opcode);
	}

	public static Stream<AbstractInsnNode> findInstructions(MethodNode node, int opcode) {
		return findInstructions(streamInstructions(node), opcode);
	}

	public static Stream<AbstractInsnNode> findInstructions(MethodNode node, AbstractInsnNode startIndex, int opcode) {
		return findInstructions(streamInstructions(node, startIndex), opcode);
	}

	public static Stream<MethodInsnNode> findMethodInstructions(Stream<AbstractInsnNode> stream, int opcode, String owner, String name, String descriptor) {
		return stream.filter(instruction -> instruction instanceof MethodInsnNode i &&
			i.getOpcode() == opcode &&
			i.owner.equals(owner) &&
			i.name.equals(name) &&
			i.desc.equals(descriptor)
		).map(MethodInsnNode.class::cast);
	}

	public static Stream<MethodInsnNode> findMethodInstructions(MethodNode node, int opcode, String owner, String name, String descriptor) {
		return findMethodInstructions(streamInstructions(node), opcode, owner, name, descriptor);
	}

	public static Stream<MethodInsnNode> findMethodInstructions(MethodNode node, AbstractInsnNode startIndex, int opcode, String owner, String name, String descriptor) {
		return findMethodInstructions(streamInstructions(node, startIndex), opcode, owner, name, descriptor);
	}

	public static Stream<FieldInsnNode> findFieldInstructions(Stream<AbstractInsnNode> stream, int opcode, String owner, String name) {
		return stream.filter(instruction -> instruction instanceof FieldInsnNode i &&
			i.getOpcode() == opcode &&
			i.owner.equals(owner) &&
			i.name.equals(name)
		).map(FieldInsnNode.class::cast);
	}

	public static Stream<FieldInsnNode> findFieldInstructions(MethodNode node, int opcode, String owner, String name) {
		return findFieldInstructions(streamInstructions(node), opcode, owner, name);
	}

	public static Stream<FieldInsnNode> findFieldInstructions(MethodNode node, AbstractInsnNode startIndex, int opcode, String owner, String name) {
		return findFieldInstructions(streamInstructions(node, startIndex), opcode, owner, name);
	}

	public static Stream<VarInsnNode> findVarInstructions(Stream<AbstractInsnNode> stream, int opcode, int index) {
		return stream.filter(instruction -> instruction instanceof VarInsnNode i &&
			i.getOpcode() == opcode &&
			i.var == index
		).map(VarInsnNode.class::cast);
	}

	public static Stream<VarInsnNode> findVarInstructions(MethodNode node, int opcode, int index) {
		return findVarInstructions(streamInstructions(node), opcode, index);
	}

	public static Stream<VarInsnNode> findVarInstructions(MethodNode node, AbstractInsnNode startIndex, int opcode, int index) {
		return findVarInstructions(streamInstructions(node, startIndex), opcode, index);
	}

}

package twilightforest.asm.transformers.bossbar;

import net.neoforged.neoforgespi.transformation.ProcessorName;
import net.neoforged.neoforgespi.transformation.SimpleMethodProcessor;
import net.neoforged.neoforgespi.transformation.SimpleTransformationContext;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import twilightforest.asm.ASMUtil;

import java.util.Set;

/**
 * Applies <a href="https://github.com/neoforged/NeoForge/pull/3414">NeoForge's 26.2+ fix</a> that wasn't pulled into 26.1.2.
 * </br>
 * This should be safe even with their fix, but remove this when either: A. We move to 26.2+ or B. Neo applies the fix to 26.1.2
 */
public class BossHealthOverlayNeoForgeFixTmpTransformer extends SimpleMethodProcessor {

	@Override
	public ProcessorName name() {
		return ASMUtil.named("boss_health_overlay_neo_forge_fix_tmp");
	}

	@Override
	public void transform(MethodNode node, SimpleTransformationContext context) {
		// Remove all usages of getIncrement, replaced with a 0
		ASMUtil.findMethodInstructions(
			node,
			Opcodes.INVOKEVIRTUAL,
			"net/neoforged/neoforge/client/event/CustomizeGuiOverlayEvent$BossEventProgress",
			"getIncrement",
			"()I"
		).forEach(target -> node.instructions.insert(target, ASMUtil.listOf(
			new InsnNode(Opcodes.POP),
			new InsnNode(Opcodes.ICONST_0)
		)));

		// This should then invoke getIncrement outside the if statement and apply it
		ASMUtil.findLast(ASMUtil.findVarInstructions(
			node,
			Opcodes.ILOAD,
			4
		)).ifPresent(target -> node.instructions.insertBefore(target, ASMUtil.listOf(
			new VarInsnNode(Opcodes.ILOAD, 4),
			new VarInsnNode(Opcodes.ALOAD, 8),
			new MethodInsnNode(
				Opcodes.INVOKEVIRTUAL,
				"net/neoforged/neoforge/client/event/CustomizeGuiOverlayEvent$BossEventProgress",
				"getIncrement",
				"()I"
			),
			new InsnNode(Opcodes.IADD),
			new VarInsnNode(Opcodes.ISTORE, 4)
		)));
	}

	@Override
	public Set<Target> targets() {
		return Set.of(new Target(
			"net.minecraft.client.gui.components.BossHealthOverlay",
			"extractRenderState",
			"(Lnet/minecraft/client/gui/GuiGraphicsExtractor;)V"
		));
	}

}

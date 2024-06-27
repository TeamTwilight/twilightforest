package twilightforest.asm.transformers.book;

import cpw.mods.modlauncher.api.ITransformer;
import cpw.mods.modlauncher.api.ITransformerVotingContext;
import cpw.mods.modlauncher.api.TargetType;
import cpw.mods.modlauncher.api.TransformerVoteResult;
import net.neoforged.coremod.api.ASMAPI;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import twilightforest.asm.AsmUtil;

import java.util.Set;

/**
 * {@link twilightforest.ASMHooks#modifyWrittenBookName}
 */
public class ModifyWrittenBookNameTransformer implements ITransformer<MethodNode> {

	@Override
	public @NotNull MethodNode transform(MethodNode node, ITransformerVotingContext context) {
		AsmUtil.findInstructions(node, Opcodes.ARETURN)
			.findFirst()
			.ifPresent(target -> node.instructions.insertBefore(
				target,
				ASMAPI.listOf(
					new VarInsnNode(Opcodes.ALOAD, 1),
					new MethodInsnNode(
						Opcodes.INVOKESTATIC,
						"twilightforest/ASMHooks",
						"modifyWrittenBookName",
						"(Lnet/minecraft/network/chat/Component;Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/network/chat/Component;"
					)
				)
			));
		return node;
	}

	@Override
	public @NotNull TransformerVoteResult castVote(ITransformerVotingContext context) {
		return TransformerVoteResult.YES;
	}

	@Override
	public @NotNull Set<Target<MethodNode>> targets() {
		return Set.of(Target.targetMethod(
			"net.minecraft.world.item.WrittenBookItem",
			"getName",
			"(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/network/chat/Component;"
		));
	}

	@Override
	public @NotNull TargetType<MethodNode> getTargetType() {
		return TargetType.METHOD;
	}

}

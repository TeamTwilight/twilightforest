package twilightforest.asm.transformers.armor;

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
import twilightforest.asm.ASMUtil;

import java.util.Set;

/**
 * {@link twilightforest.asmhooks.ArmorHooks#cancelArmorRendering}
 */
public class CancelElytraRenderingTransformer implements ITransformer<MethodNode> {

	@Override
	public MethodNode transform(MethodNode node, ITransformerVotingContext context) {
		ASMUtil.findInstructions(
				node,
				Opcodes.IRETURN
		).forEach(target -> node.instructions.insertBefore(
				target,
				ASMAPI.listOf(
					new VarInsnNode(Opcodes.ALOAD, 1),
					new MethodInsnNode(
						Opcodes.INVOKESTATIC,
						"twilightforest/asmhooks/ArmorHooks",
						"cancelArmorRendering",
						"(ZLnet/minecraft/world/item/ItemStack;)Z"
					)
				)
			));
		return node;
	}

	@Override
	public TransformerVoteResult castVote(ITransformerVotingContext context) {
		return TransformerVoteResult.YES;
	}

	@Override
	public Set<Target<MethodNode>> targets() {
		return Set.of(Target.targetMethod(
			"net.minecraft.client.renderer.entity.layers.ElytraLayer",
			"shouldRender",
			"(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;)Z"
		));
	}

	@Override
	public TargetType<MethodNode> getTargetType() {
		return TargetType.METHOD;
	}

}

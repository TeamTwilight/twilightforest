package twilightforest.asm.transformers.map;

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
 * {@link twilightforest.asmhooks.MapHooks#updateMapsInGoggles}
 */
public class UpdateMapsInGogglesTransformer implements ITransformer<MethodNode> {

	@Override
	public @NotNull MethodNode transform(MethodNode node, ITransformerVotingContext context) {
		ASMUtil.findMethodInstructions(
			node,
			Opcodes.INVOKEVIRTUAL,
			"net/minecraft/world/entity/player/Inventory",
			"contains",
			"(Ljava/util/function/Predicate;)Z"
		).forEach(target -> node.instructions.insert(
			target,
			ASMAPI.listOf(
				new VarInsnNode(Opcodes.ALOAD, 2),
				new VarInsnNode(Opcodes.ALOAD, 1),
				new MethodInsnNode(
					Opcodes.INVOKESTATIC,
					"twilightforest/asmhooks/MapHooks",
					"updateMapsInGoggles",
					"(ZLnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/player/Player;)Z"
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
			"net.minecraft.world.level.saveddata.maps.MapItemSavedData",
			"tickCarriedBy",
			"(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;)V"
		));
	}

	@Override
	public @NotNull TargetType<MethodNode> getTargetType() {
		return TargetType.METHOD;
	}

}

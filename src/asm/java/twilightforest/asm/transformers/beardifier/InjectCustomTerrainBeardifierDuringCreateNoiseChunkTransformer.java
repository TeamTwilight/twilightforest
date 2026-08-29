package twilightforest.asm.transformers.beardifier;

import net.neoforged.neoforgespi.transformation.ProcessorName;
import net.neoforged.neoforgespi.transformation.SimpleMethodProcessor;
import net.neoforged.neoforgespi.transformation.SimpleTransformationContext;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import twilightforest.asm.ASMUtil;

import java.util.Set;

/**
 * {@link twilightforest.asmhooks.WorldgenHooks#gatherCustomTerrain}
 */
public class InjectCustomTerrainBeardifierDuringCreateNoiseChunkTransformer extends SimpleMethodProcessor {

	@Override
	public ProcessorName name() {
		return ASMUtil.named("initialize_custom_beardifier_fields_during_create_noise_chunk");
	}

	@Override
	public void transform(MethodNode node, SimpleTransformationContext context) {
		ASMUtil.findMethodInstructions(
			node,
			Opcodes.INVOKESTATIC,
			"net/minecraft/world/level/levelgen/Beardifier",
			"forStructuresInChunk",
			"(Lnet/minecraft/world/level/StructureManager;Lnet/minecraft/world/level/ChunkPos;)Lnet/minecraft/world/level/levelgen/Beardifier;"
		).forEach(target -> node.instructions.insert(
			target,
			ASMUtil.listOf(
				new VarInsnNode(Opcodes.ALOAD, 2), // StructureManager from params
				new VarInsnNode(Opcodes.ALOAD, 1), // Chunk from params
				new MethodInsnNode(
					Opcodes.INVOKESTATIC,
					"twilightforest/asmhooks/WorldgenHooks",
					"gatherCustomTerrain",
					"(Lnet/minecraft/world/level/levelgen/Beardifier;Lnet/minecraft/world/level/StructureManager;Lnet/minecraft/world/level/chunk/ChunkAccess;)Lnet/minecraft/world/level/levelgen/DensityFunctions$BeardifierOrMarker;"
				)
			)
		));
	}

	@Override
	public Set<Target> targets() {
		return Set.of(new Target(
			"net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator",
			"createNoiseChunk",
			"(Lnet/minecraft/world/level/chunk/ChunkAccess;Lnet/minecraft/world/level/StructureManager;Lnet/minecraft/world/level/levelgen/blending/Blender;Lnet/minecraft/world/level/levelgen/RandomState;)Lnet/minecraft/world/level/levelgen/NoiseChunk;"
		));
	}

}

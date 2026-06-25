package twilightforest.asm.transformers.beardifier;

import net.neoforged.neoforgespi.transformation.SimpleTransformationContext;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import twilightforest.asm.ASMUtil;
import twilightforest.asm.SimpleMethodTransformer;

/**
 * After {@link Beardifier#forStructuresInChunk} returns:
 * 1. If result is Beardifier.EMPTY: create a NEW Beardifier instance (instead of modifying shared static instance)
 * 2. Call gatherCustomTerrain() to get our custom density list
 * 3. Set the twilightforest_customStructureDensities field on the Beardifier
 */
public class InitializeCustomBeardifierFieldsDuringCreateNoiseChunkTransformer extends SimpleMethodTransformer {

	public InitializeCustomBeardifierFieldsDuringCreateNoiseChunkTransformer() {
		super(
			"net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator",
			"createNoiseChunk",
			"(Lnet/minecraft/world/level/chunk/ChunkAccess;Lnet/minecraft/world/level/StructureManager;Lnet/minecraft/world/level/levelgen/blending/Blender;Lnet/minecraft/world/level/levelgen/RandomState;)Lnet/minecraft/world/level/levelgen/NoiseChunk;"
		);
	}

	@Override
	protected void transform(ClassNode classNode, MethodNode method, SimpleTransformationContext context) {
		ASMUtil.findMethodInstructions(
			method,
			Opcodes.INVOKESTATIC,
			"net/minecraft/world/level/levelgen/Beardifier",
			"forStructuresInChunk",
			"(Lnet/minecraft/world/level/StructureManager;Lnet/minecraft/world/level/ChunkPos;)Lnet/minecraft/world/level/levelgen/Beardifier;"
		).forEach(target -> {
			InsnList insert = new InsnList();

			LabelNode notEmptyLabel = new LabelNode();
			LabelNode afterSetLabel = new LabelNode();

			// Stack: [beardifier]
			insert.add(new InsnNode(Opcodes.DUP));
			// Stack: [beardifier, beardifier]
			insert.add(new FieldInsnNode(
				Opcodes.GETSTATIC,
				"net/minecraft/world/level/levelgen/Beardifier",
				"EMPTY",
				"Lnet/minecraft/world/level/levelgen/Beardifier;"
			));
			// Stack: [beardifier, beardifier, EMPTY]
			insert.add(new JumpInsnNode(Opcodes.IF_ACMPNE, notEmptyLabel));

			// IS EMPTY - replace with new Beardifier instance
			// Pop the original EMPTY
			insert.add(new InsnNode(Opcodes.POP));
			// Stack: []
			insert.add(new TypeInsnNode(Opcodes.NEW, "net/minecraft/world/level/levelgen/Beardifier"));
			// Stack: [newBeard]
			insert.add(new InsnNode(Opcodes.DUP));
			// Stack: [newBeard, newBeard]
			insert.add(new MethodInsnNode(
				Opcodes.INVOKESTATIC,
				"java/util/List",
				"of",
				"()Ljava/util/List;",
				true
			));
			// Stack: [newBeard, newBeard, emptyPieces]
			insert.add(new MethodInsnNode(
				Opcodes.INVOKESTATIC,
				"java/util/List",
				"of",
				"()Ljava/util/List;",
				true
			));
			// Stack: [newBeard, newBeard, emptyPieces, emptyJunctions]
			insert.add(new InsnNode(Opcodes.ACONST_NULL));
			// Stack: [newBeard, newBeard, emptyPieces, emptyJunctions, nullAffectedBox]
			insert.add(new MethodInsnNode(
				Opcodes.INVOKESPECIAL,
				"net/minecraft/world/level/levelgen/Beardifier",
				"<init>",
				"(Ljava/util/List;Ljava/util/List;Lnet/minecraft/world/level/levelgen/structure/BoundingBox;)V",
				false
			));
			// Stack: [newBeard]
			insert.add(new JumpInsnNode(Opcodes.GOTO, afterSetLabel));

			// NOT EMPTY - just use original
			insert.add(notEmptyLabel);
			// Stack: [beardifier] (fall through)

			insert.add(afterSetLabel);
			// Stack: [finalBeardifier]

			// Now set the custom densities field
			insert.add(new InsnNode(Opcodes.DUP));
			// Stack: [finalBeardifier, finalBeardifier]
			insert.add(new VarInsnNode(Opcodes.ALOAD, 2)); // StructureManager (param 1)
			insert.add(new VarInsnNode(Opcodes.ALOAD, 1)); // ChunkAccess (param 0)
			insert.add(new MethodInsnNode(
				Opcodes.INVOKEVIRTUAL,
				"net/minecraft/world/level/chunk/ChunkAccess",
				"getPos",
				"()Lnet/minecraft/world/level/ChunkPos;",
				false
			));
			// Stack: [finalBeardifier, finalBeardifier, structureManager, chunkPos]
			insert.add(new MethodInsnNode(
				Opcodes.INVOKESTATIC,
				"twilightforest/asmhooks/WorldgenHooks",
				"gatherCustomTerrain",
				"(Lnet/minecraft/world/level/StructureManager;Lnet/minecraft/world/level/ChunkPos;)Lit/unimi/dsi/fastutil/objects/ObjectList;",
				false
			));
			// Stack: [finalBeardifier, customDensities]
			insert.add(new FieldInsnNode(
				Opcodes.PUTFIELD,
				"net/minecraft/world/level/levelgen/Beardifier",
				"twilightforest_customStructureDensities",
				"Lit/unimi/dsi/fastutil/objects/ObjectList;"
			));
			// Stack: [finalBeardifier] - ready for original code to use

			method.instructions.insert(target, insert);
		});
	}

}

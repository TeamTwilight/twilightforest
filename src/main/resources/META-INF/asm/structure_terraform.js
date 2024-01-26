// noinspection ES6ConvertVarToLetConst

var ASM = Java.type('net.neoforged.coremod.api.ASMAPI');
var Opcodes = Java.type('org.objectweb.asm.Opcodes');

var FieldNode = Java.type('org.objectweb.asm.tree.FieldNode');

var FieldInsnNode = Java.type('org.objectweb.asm.tree.FieldInsnNode');
var InsnNode = Java.type('org.objectweb.asm.tree.InsnNode');
var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');

// noinspection JSUnusedGlobalSymbols
function initializeCoreMod() {
    return {
        'collect': {
            'target': {
                'type': 'CLASS',
                'name': 'net.minecraft.world.level.levelgen.Beardifier'
            },
            'transformer': function (/*org.objectweb.asm.tree.ClassNode*/ classNode) {
                // Nothing fancy at all here. Only giving this Beardifier class a new field.
                classNode.fields.add(new FieldNode(
                    Opcodes.ACC_PRIVATE,
                    "customStructureDensities",
                    "Lit/unimi/dsi/fastutil/objects/ObjectListIterator;",
                    "Lit/unimi/dsi/fastutil/objects/ObjectListIterator<net.minecraft.world.level.levelgen.DensityFunction>;", // Not necessary at all but apparently useful for language-level inferences. Neat!
                    null
                ));
                return classNode;
            }
        },
        'attach': {
            'target': {
               'type': 'METHOD',
               'class': 'net.minecraft.world.level.levelgen.Beardifier',
               'methodName': 'forStructuresInChunk',
               'methodDesc': '(Lnet/minecraft/world/level/StructureManager;Lnet/minecraft/world/level/ChunkPos;)Lnet/minecraft/world/level/levelgen/Beardifier;'
            },
            'transformer': function (/*org.objectweb.asm.tree.MethodNode*/ methodNode) {
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;

                /* Projected bytecode diff:

                Original instruction seq of Beardifier setup:
                    INVOKESPECIAL net/minecraft/world/level/levelgen/Beardifier.<init> (Lit/unimi/dsi/fastutil/objects/ObjectListIterator;Lit/unimi/dsi/fastutil/objects/ObjectListIterator;)V
                    // stack: [ Beardifier ]
                    ARETURN

                Modified instruction seq of Beardifier setup:
                    INVOKESPECIAL net/minecraft/world/level/levelgen/Beardifier.<init> (Lit/unimi/dsi/fastutil/objects/ObjectListIterator;Lit/unimi/dsi/fastutil/objects/ObjectListIterator;)V
                    // stack: [ Beardifier ]

                    // ---- Entered injection
                    DUP // duplicate the Beardifier
                    // stack: [ Beardifier, Beardifier ]
                    ALOAD 0 // Load StructureManager param
                    ALOAD 1 // Load ChunkPos param
                    // stack: [ Beardifier, Beardifier, StructureManager, ChunkPos ]
                    INVOKESTATIC ASMHooks.gatherCustomTerrain
                    // stack: [ Beardifier, Beardifier, ObjectListIterator ]
                    PUTFIELD customStructureDensities
                    // stack: [ Beardifier ]
                    // ---- Exited injection

                    // stack: [ Beardifier ]
                    ARETURN
                 */

                instructions.insertBefore(
                    ASM.findFirstInstruction(methodNode, Opcodes.ARETURN),
                    ASM.listOf(
                        // stack: [ Beardifier ]
                        new InsnNode(Opcodes.DUP),
                        // stack: [ Beardifier, Beardifier ]
                        new VarInsnNode(Opcodes.ALOAD, 0), // StructureManager from params
                        new VarInsnNode(Opcodes.ALOAD, 1), // ChunkPos from params
                        // stack: [ Beardifier, Beardifier, StructureManager, ChunkPos ]
                        new MethodInsnNode(
                            Opcodes.INVOKESTATIC,
                            'twilightforest/ASMHooks',
                            'gatherCustomTerrain',
                            '(Lnet/minecraft/world/level/StructureManager;Lnet/minecraft/world/level/ChunkPos;)Lit/unimi/dsi/fastutil/objects/ObjectListIterator;',
                            false
                        ),
                        // stack: [ Beardifier, Beardifier, ObjectListIterator ]
                        new FieldInsnNode(
                            Opcodes.PUTFIELD,
                            'net/minecraft/world/level/levelgen/Beardifier',
                            'customStructureDensities',
                            'Lit/unimi/dsi/fastutil/objects/ObjectListIterator;'
                        )
                        // stack: [ Beardifier ]
                    )
                );
                return methodNode;
            }
        },
        'recompute': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.world.level.levelgen.Beardifier',
                'methodName': 'compute',
                'methodDesc': '(Lnet/minecraft/world/level/levelgen/DensityFunction$FunctionContext;)D'
            },
            'transformer': function (/*org.objectweb.asm.tree.MethodNode*/ methodNode) {
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;

                /* Projected bytecode diff:

                Original instruction seq:
                    DLOAD 5
                    // stack: [ double ]
                    DRETURN

                Modified instruction seq:
                    DLOAD 5
                    // stack: [ double ]

                    // ---- Entered injection
                    ALOAD 1
                    ALOAD 0
                    // stack: [ double, DensityFunction.FunctionContext, Beardifier ]
                    GETFIELD customStructureDensities
                    // stack: [ double, DensityFunction.FunctionContext, ObjectListIterator ]
                    INVOKESTATIC ASMHooks.getCustomDensity
                    // stack: [ double, double ]
                    DADD
                    // stack: [ double ]
                    // ---- Exited injection

                    // stack: [ double ]
                    DRETURN
                 */

                instructions.insertBefore(
                    ASM.findFirstInstruction(methodNode, Opcodes.DRETURN),
                    ASM.listOf(
                        // stack: [ double ]
                        new VarInsnNode(Opcodes.ALOAD, 1), // DensityFunction$FunctionContext from params
                        new VarInsnNode(Opcodes.ALOAD, 0), // Beardifier.this
                        // stack: [ double, DensityFunction.FunctionContext, Beardifier ]
                        new FieldInsnNode(
                            Opcodes.GETFIELD,
                            'net/minecraft/world/level/levelgen/Beardifier',
                            'customStructureDensities',
                            'Lit/unimi/dsi/fastutil/objects/ObjectListIterator;'
                        ),
                        // stack: [ double, DensityFunction.FunctionContext, ObjectListIterator ]
                        new MethodInsnNode(
                            Opcodes.INVOKESTATIC,
                            'twilightforest/ASMHooks',
                            'getCustomDensity',
                            '(DLnet/minecraft/world/level/levelgen/DensityFunction$FunctionContext;Lit/unimi/dsi/fastutil/objects/ObjectListIterator;)D',
                            false
                        )
                        // stack: [ double ]
                    )
                );
                return methodNode;
            }
        }
    }
}

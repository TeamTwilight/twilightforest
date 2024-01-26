// noinspection ES6ConvertVarToLetConst

var ASM = Java.type('net.neoforged.coremod.api.ASMAPI');
var Opcodes = Java.type('org.objectweb.asm.Opcodes');

var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');

// noinspection JSUnusedGlobalSymbols
function initializeCoreMod() {
    return {
        'conquered': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.world.level.levelgen.structure.StructureStart',
                'methodName': 'loadStaticStart',
                'methodDesc': '(Lnet/minecraft/world/level/levelgen/structure/pieces/StructurePieceSerializationContext;Lnet/minecraft/nbt/CompoundTag;J)Lnet/minecraft/world/level/levelgen/structure/StructureStart;'
            },
            'transformer': function (/*org.objectweb.asm.tree.MethodNode*/ methodNode) {
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                instructions.insert(
                    ASM.findFirstMethodCall(
                        methodNode,
                        ASM.MethodType.SPECIAL,
                        'net/minecraft/world/level/levelgen/structure/StructureStart',
                        '<init>',
                        '(Lnet/minecraft/world/level/levelgen/structure/Structure;Lnet/minecraft/world/level/ChunkPos;ILnet/minecraft/world/level/levelgen/structure/pieces/PiecesContainer;)V'
                        ),
                    ASM.listOf(
                        new VarInsnNode(Opcodes.ALOAD, 10),
                        new VarInsnNode(Opcodes.ALOAD, 1),
                        new MethodInsnNode(
                            Opcodes.INVOKESTATIC,
                            'twilightforest/ASMHooks',
                            'conquered',
                            '(Lnet/minecraft/world/level/levelgen/structure/StructureStart;Lnet/minecraft/world/level/levelgen/structure/pieces/PiecesContainer;Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/world/level/levelgen/structure/StructureStart;',
                            false
                            )
                        )
                    );
                return methodNode;
            }
        }
    }
}


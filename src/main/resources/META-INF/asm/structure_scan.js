// noinspection ES6ConvertVarToLetConst

var ASM = Java.type('net.neoforged.coremod.api.ASMAPI');
var Opcodes = Java.type('org.objectweb.asm.Opcodes');

var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');

// noinspection JSUnusedGlobalSymbols
function initializeCoreMod() {
    ASM.loadFile('META-INF/asm/util/util.js');
    return {
        'structure_scan': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.world.level.chunk.ChunkGenerator',
                'methodName': 'findNearestMapStructure',
                'methodDesc': '(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/HolderSet;Lnet/minecraft/core/BlockPos;IZ)Lcom/mojang/datafixers/util/Pair;'
            },
            'transformer': function (/*org.objectweb.asm.tree.MethodNode*/ methodNode) {
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                instructions.insertBefore(
                    findLastInstruction(methodNode, Opcodes.ARETURN),
                    ASM.listOf(
                        new VarInsnNode(Opcodes.ALOAD, 1), // ServerLevel from params
                        new VarInsnNode(Opcodes.ALOAD, 2), // HolderSet from params
                        new VarInsnNode(Opcodes.ALOAD, 3), // BlockPos from params
                        new VarInsnNode(Opcodes.ILOAD, 4), // int from params
                        new VarInsnNode(Opcodes.ILOAD, 5), // boolean from params
                        new MethodInsnNode(
                            Opcodes.INVOKESTATIC,
                            'twilightforest/ASMHooks',
                            'findNearestMapLandmark',
                            '(Lcom/mojang/datafixers/util/Pair;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/HolderSet;Lnet/minecraft/core/BlockPos;IZ)Lcom/mojang/datafixers/util/Pair;',
                            false
                        )
                    )
                );
                return methodNode;
            }
        }
    }
}

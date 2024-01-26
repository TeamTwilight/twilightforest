// noinspection ES6ConvertVarToLetConst

var ASM = Java.type('net.neoforged.coremod.api.ASMAPI');
var Opcodes = Java.type('org.objectweb.asm.Opcodes');

var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');

// noinspection JSUnusedGlobalSymbols
function initializeCoreMod() {
    return {
        'chunkstatus': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.world.level.chunk.ChunkStatus',
                'methodName': 'getStatusList',
                'methodDesc': '()Ljava/util/List;'
            },
            'transformer': function (/*org.objectweb.asm.tree.MethodNode*/ methodNode) {
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                instructions.insert(
                    ASM.listOf(
                        new MethodInsnNode(
                            Opcodes.INVOKESTATIC,
                            'twilightforest/ASMHooks',
                            'assertChunkBlanketing',
                            '()V',
                            false
                        )
                    )
                );
                return methodNode;
            }
        }
    }
}

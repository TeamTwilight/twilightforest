// noinspection ES6ConvertVarToLetConst

var ASM = Java.type('net.neoforged.coremod.api.ASMAPI');
var Opcodes = Java.type('org.objectweb.asm.Opcodes');

var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode')

// noinspection JSUnusedGlobalSymbols
function initializeCoreMod() {
    ASM.loadFile('META-INF/asm/util/util.js');
    return {
        'render': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.client.renderer.LevelRenderer',
                'methodName': 'renderLevel',
                'methodDesc': '(Lnet/minecraft/client/DeltaTracker;ZLnet/minecraft/client/Camera;Lnet/minecraft/client/renderer/GameRenderer;Lnet/minecraft/client/renderer/LightTexture;Lorg/joml/Matrix4f;Lorg/joml/Matrix4f;)V'
            },
            'transformer': function (/*org.objectweb.asm.tree.MethodNode*/ methodNode) {
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                instructions.insert(
                    findLastMethodInstruction(methodNode, Opcodes.INVOKEVIRTUAL, 'net/minecraft/client/multiplayer/ClientLevel', 'entitiesForRendering', '()Ljava/lang/Iterable;'),
                    ASM.listOf(
                        new MethodInsnNode(
                            Opcodes.INVOKESTATIC,
                            'twilightforest/ASMHooks',
                            'renderMultiparts',
                            '(Ljava/lang/Iterable;)Ljava/lang/Iterable;',
                            false
                        )
                    )
                );
                return methodNode;
            }
        }
    }
}

// noinspection ES6ConvertVarToLetConst

var ASM = Java.type('net.neoforged.coremod.api.ASMAPI');
var Opcodes = Java.type('org.objectweb.asm.Opcodes');

var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');

// noinspection JSUnusedGlobalSymbols
function initializeCoreMod() {
    return {
        'shroom': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.world.level.block.MushroomBlock',
                'methodName': 'canSurvive',
                'methodDesc': '(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;)Z'
            },
            'transformer': function (/*org.objectweb.asm.tree.MethodNode*/ methodNode) {
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                instructions.insert(
                    ASM.findFirstMethodCall(
                        methodNode,
                        ASM.MethodType.INTERFACE,
                        'net/minecraft/world/level/LevelReader',
                        'getRawBrightness',
                        '(Lnet/minecraft/core/BlockPos;I)I'
                    ),
                    ASM.listOf(
                        new VarInsnNode(Opcodes.ALOAD, 2),
                        new VarInsnNode(Opcodes.ALOAD, 3),
                        new MethodInsnNode(
                            Opcodes.INVOKESTATIC,
                            'twilightforest/ASMHooks',
                            'shroom',
                            '(ILnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;)I',
                            false
                        )
                    )
                );
                return methodNode;
            }
        }
    }
}


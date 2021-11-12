// noinspection ES6ConvertVarToLetConst

var ASM = Java.type('net.minecraftforge.coremod.api.ASMAPI');
var Opcodes = Java.type('org.objectweb.asm.Opcodes');

var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');

// noinspection JSUnusedGlobalSymbols
function initializeCoreMod() {
    return {
        'conquered': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.world.level.levelgen.feature.StructureFeature',
                'methodName': ASM.mapMethod('m_160447_'), // loadStaticStart
                'methodDesc': '(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/nbt/CompoundTag;J)Lnet/minecraft/world/level/levelgen/structure/StructureStart;'
            },
            'transformer': function (/*org.objectweb.asm.tree.MethodNode*/ methodNode) {
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                instructions.insert(
                    ASM.findFirstMethodCall(
                        methodNode,
                        ASM.MethodType.VIRTUAL,
                        'net/minecraft/world/level/levelgen/feature/StructureFeature',
                        ASM.mapMethod('m_160451_'), // createStart
                        '(Lnet/minecraft/world/level/ChunkPos;IJ)Lnet/minecraft/world/level/levelgen/structure/StructureStart;'
                        ),
                    ASM.listOf(
                        new VarInsnNode(Opcodes.ALOAD, 1),
                        new MethodInsnNode(
                            Opcodes.INVOKESTATIC,
                            'twilightforest/ASMHooks',
                            'conquered',
                            '(Lnet/minecraft/world/level/levelgen/structure/StructureStart;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/world/level/levelgen/structure/StructureStart;',
                            false
                            )
                        )
                    );
                return methodNode;
            }
        }
    }
}


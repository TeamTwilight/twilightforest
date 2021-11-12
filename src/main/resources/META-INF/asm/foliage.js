// noinspection ES6ConvertVarToLetConst

var ASM = Java.type('net.minecraftforge.coremod.api.ASMAPI');
var Opcodes = Java.type('org.objectweb.asm.Opcodes');

var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');

// noinspection JSUnusedGlobalSymbols
function initializeCoreMod() {
    return {
        'dragons': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.client.renderer.BiomeColors',
                'methodName': ASM.mapMethod('m_108807_'),
                'methodDesc': '(Lnet/minecraft/world/level/biome/Biome;DD)I'
            },
            'transformer': function (/*org.objectweb.asm.tree.MethodNode*/ methodNode) {
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                instructions.insertBefore(
                    ASM.findFirstInstruction(methodNode, Opcodes.IRETURN),
                    ASM.listOf(
                        new VarInsnNode(Opcodes.ALOAD, 0),
                        new VarInsnNode(Opcodes.DLOAD, 1),
                        new VarInsnNode(Opcodes.DLOAD, 3),
                        new MethodInsnNode(
                            Opcodes.INVOKESTATIC,
                            'twilightforest/ASMHooks',
                            'foliage',
                            '(ILnet/minecraft/world/level/biome/Biome;DD)I',
                            false
                            )
                        )
                    );
                return methodNode;
            }
        }
    }
}

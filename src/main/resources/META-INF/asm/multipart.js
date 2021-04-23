function initializeCoreMod() {
    return {
        'hitbox': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.world.World',
                'methodName': Java.type("net.minecraftforge.coremod.api.ASMAPI").mapMethod('func_175674_a'),
                'methodDesc': '(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/AxisAlignedBB;Ljava/util/function/Predicate;)Ljava/util/List;'
            },
            'transformer': function (methodNode) {
                if (methodNode instanceof org.objectweb.asm.tree.MethodNode) {
                    var ASM = Java.type('net.minecraftforge.coremod.api.ASMAPI');
                    var Opcodes = Java.type('org.objectweb.asm.Opcodes');
                    methodNode.instructions.insertBefore(
                        ASM.findFirstInstruction(methodNode, Opcodes.ARETURN),
                        ASM.listOf(
                            new org.objectweb.asm.tree.VarInsnNode(Opcodes.ALOAD, 0),
                            new org.objectweb.asm.tree.VarInsnNode(Opcodes.ALOAD, 1),
                            new org.objectweb.asm.tree.VarInsnNode(Opcodes.ALOAD, 2),
                            new org.objectweb.asm.tree.VarInsnNode(Opcodes.ALOAD, 3),
                            new org.objectweb.asm.tree.MethodInsnNode(
                                Opcodes.INVOKESTATIC,
                                'twilightforest/ASMHooks',
                                'multipartHitbox',
                                '(Ljava/util/List;Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/AxisAlignedBB;Ljava/util/function/Predicate;)Ljava/util/List;',
                                false
                                )
                            )
                        );
                }
                return methodNode;
            }
        },
        'sync': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.world.TrackedEntity',
                'methodName': Java.type("net.minecraftforge.coremod.api.ASMAPI").mapMethod('func_219457_c'),
                'methodDesc': '()V'
            },
            'transformer': function (methodNode) {
                if (methodNode instanceof org.objectweb.asm.tree.MethodNode) {
                    var ASM = Java.type('net.minecraftforge.coremod.api.ASMAPI');
                    var Opcodes = Java.type('org.objectweb.asm.Opcodes');
                    methodNode.instructions.insert(
                        ASM.findFirstInstruction(methodNode, Opcodes.GETFIELD),
                        ASM.listOf(
                            new org.objectweb.asm.tree.MethodInsnNode(
                                Opcodes.INVOKESTATIC,
                                'twilightforest/ASMHooks',
                                'updateMultiparts',
                                '(Lnet/minecraft/entity/Entity;)Lnet/minecraft/entity/Entity;',
                                false
                                )
                            )
                        );
                }
                return methodNode;
            }
        },
        'renderer': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.client.renderer.entity.EntityRendererManager',
                'methodName': Java.type("net.minecraftforge.coremod.api.ASMAPI").mapMethod('func_78713_a'),
                'methodDesc': '(Lnet/minecraft/entity/Entity;)Lnet/minecraft/client/renderer/entity/EntityRenderer;'
            },
            'transformer': function (methodNode) {
                if (methodNode instanceof org.objectweb.asm.tree.MethodNode) {
                    var ASM = Java.type('net.minecraftforge.coremod.api.ASMAPI');
                    var Opcodes = Java.type('org.objectweb.asm.Opcodes');
                    var lastInstruction = null;
                    for (var index = methodNode.instructions.size() - 1; index > 0; index--) {
                        var node = methodNode.instructions.get(index);
                        if (lastInstruction == null &&

                            node instanceof org.objectweb.asm.tree.InsnNode &&

                            node.getOpcode() === Opcodes.ARETURN

                        )
                            lastInstruction = node;

                    }
                    methodNode.instructions.insertBefore(
                        lastInstruction,
                        ASM.listOf(
                            new org.objectweb.asm.tree.VarInsnNode(Opcodes.ALOAD, 1),
                            new org.objectweb.asm.tree.VarInsnNode(Opcodes.ALOAD, 0),
                            new org.objectweb.asm.tree.MethodInsnNode(
                                Opcodes.INVOKESTATIC,
                                'twilightforest/ASMHooks',
                                'getMultipartRenderer',
                                '(Lnet/minecraft/client/renderer/entity/EntityRenderer;Lnet/minecraft/entity/Entity;Lnet/minecraft/client/renderer/entity/EntityRendererManager;)Lnet/minecraft/client/renderer/entity/EntityRenderer;',
                                false
                                )
                            )
                        );
                }
                return methodNode;
            }
        },
        'render': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.client.renderer.WorldRenderer',
                'methodName': Java.type("net.minecraftforge.coremod.api.ASMAPI").mapMethod('func_228426_a_'),
                'methodDesc': '(Lcom/mojang/blaze3d/matrix/MatrixStack;FJZLnet/minecraft/client/renderer/ActiveRenderInfo;Lnet/minecraft/client/renderer/GameRenderer;Lnet/minecraft/client/renderer/LightTexture;Lnet/minecraft/util/math/vector/Matrix4f;)V'
            },
            'transformer': function (methodNode) {
                if (methodNode instanceof org.objectweb.asm.tree.MethodNode) {
                    var ASM = Java.type('net.minecraftforge.coremod.api.ASMAPI');
                    var Opcodes = Java.type('org.objectweb.asm.Opcodes');
                    var lastInstruction = null;
                    for (var index = methodNode.instructions.size() - 1; index > 0; index--) {
                        var node = methodNode.instructions.get(index);
                        if (lastInstruction == null &&

                            node instanceof org.objectweb.asm.tree.MethodInsnNode &&

                            node.getOpcode() === Opcodes.INVOKEVIRTUAL &&

                            node.owner.equals('net/minecraft/client/world/ClientWorld') &&

                            node.name.equals(ASM.mapMethod('func_217416_b')) &&

                            node.desc.equals('()Ljava/lang/Iterable;')

                        )
                            lastInstruction = node;

                    }
                    methodNode.instructions.insert(
                        lastInstruction,
                        ASM.listOf(
                            new org.objectweb.asm.tree.MethodInsnNode(
                                Opcodes.INVOKESTATIC,
                                'twilightforest/ASMHooks',
                                'renderMutiparts',
                                '(Ljava/lang/Iterable;)Ljava/lang/Iterable;',
                                false
                                )
                            )
                        );
                }
                return methodNode;
            }
        }
    }
}

// noinspection ES6ConvertVarToLetConst

var FieldInsnNode = Java.type('org.objectweb.asm.tree.FieldInsnNode');
var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');

function findAllInstructions(/*org.objectweb.asm.tree.MethodNode*/ methodNode, /* org.objectweb.asm.Opcodes */ opcode) {
    var arr = [];
    methodNode.instructions.forEach(/*org.objectweb.asm.tree.AbstractInsnNode*/ node => {
        if (node.getOpcode() === opcode)
            arr.push(node);
    })
    return arr;
}

function findAllMethodInstructions(/*org.objectweb.asm.tree.MethodNode*/ methodNode, /* org.objectweb.asm.Opcodes */ opcode, owner, name, desc) {
    var arr = [];
    methodNode.instructions.forEach(/*org.objectweb.asm.tree.MethodInsnNode*/ node => { // It's not really a MethodInsnNode at this point, but we do this for autocomplete
        if (node instanceof MethodInsnNode && node.getOpcode() === opcode && node.owner === owner && node.name === name && node.desc === desc)
            arr.push(node);
    })
    return arr;
}

function findFirstVarInstruction(/*org.objectweb.asm.tree.MethodNode*/ methodNode, /* org.objectweb.asm.Opcodes */ opcode, varIndex) {
    for (var i = 0; i < methodNode.instructions.size() - 1; i++) {
        var /*org.objectweb.asm.tree.VarInsnNode*/ node = methodNode.instructions.get(i); // It's not really a VarInsnNode at this point, but we do this for autocomplete
        if (node instanceof VarInsnNode && node.getOpcode() === opcode && node.var === varIndex)
            return node;
    }
    return null;
}

function findLastInstruction(/*org.objectweb.asm.tree.MethodNode*/ methodNode, /* org.objectweb.asm.Opcodes */ opcode) {
    for (var i = methodNode.instructions.size() - 1; i > 0; i--) {
        var /*org.objectweb.asm.tree.AbstractInsnNode*/ node = methodNode.instructions.get(i);
        if (node.getOpcode() === opcode)
            return node;
    }
    return null;
}

function findLastFieldInstruction(/*org.objectweb.asm.tree.MethodNode*/ methodNode, /* org.objectweb.asm.Opcodes */ opcode, owner, name) {
    for (var i = methodNode.instructions.size() - 1; i > 0; i--) {
        var /*org.objectweb.asm.tree.FieldInsnNode*/ node = methodNode.instructions.get(i); // It's not really a FieldInsnNode at this point, but we do this for autocomplete
        if (node instanceof FieldInsnNode && node.getOpcode() === opcode && node.owner === owner && node.name === name)
            return node;
    }
    return null;
}

function findLastMethodInstruction(/*org.objectweb.asm.tree.MethodNode*/ methodNode, /* org.objectweb.asm.Opcodes */ opcode, owner, name, desc) {
    for (var i = methodNode.instructions.size() - 1; i > 0; i--) {
        var /*org.objectweb.asm.tree.MethodInsnNode*/ node = methodNode.instructions.get(i); // It's not really a MethodInsnNode at this point, but we do this for autocomplete
        if (node instanceof MethodInsnNode && node.getOpcode() === opcode && node.owner === owner && node.name === name && node.desc === desc)
            return node;
    }
    return null;
}

function findLastVarInstruction(/*org.objectweb.asm.tree.MethodNode*/ methodNode, /* org.objectweb.asm.Opcodes */ opcode, varIndex) {
    for (var i = methodNode.instructions.size() - 1; i > 0; i--) {
        var /*org.objectweb.asm.tree.VarInsnNode*/ node = methodNode.instructions.get(i); // It's not really a VarInsnNode at this point, but we do this for autocomplete
        if (node instanceof VarInsnNode && node.getOpcode() === opcode && node.var === varIndex)
            return node;
    }
    return null;
}
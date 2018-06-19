#version 120

varying float scale;
varying vec2 texCoord0;
varying vec4 worldPos;
varying vec4 center;
varying vec4 position;

void main() {
    // gl_ModelViewMatrix  * gl_ProjectionMatrix != gl_ModelViewProjectionMatrix
    // gl_ProjectionMatrix * gl_ModelViewMatrix  == gl_ModelViewProjectionMatrix
    // P * MV = MVP
    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;

    worldPos = gl_Vertex;
    scale = gl_NormalScale;

    center = (gl_ModelViewProjectionMatrix * vec4(vec3(0), 1));
    position = gl_ModelViewProjectionMatrix * gl_Vertex;

    texCoord0 = vec2(gl_MultiTexCoord0);
}
#version 120

varying vec3 position;
varying vec3 worldPos;
varying vec2 texCoord0;
varying float scale;
varying vec4 colorIn;

void main() {
	gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
	position = (gl_ModelViewMatrix * gl_Vertex).xyz;

	worldPos = gl_Vertex.xyz;
    scale = gl_NormalScale;

	texCoord0 = vec2(gl_MultiTexCoord0);

	colorIn = gl_Color;
}
#version 120

// From TTFTCUTS, thanks TTFTCUTS!

varying vec3 position;
varying vec2 texCoord0;

void main() {
	gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
	position = (gl_ModelViewMatrix * gl_Vertex).xyz;

	texCoord0 = vec2(gl_MultiTexCoord0);
}
#version 120

 varying vec3 position;
 varying vec2 texCoord0;
 varying vec4 colorIn;

 void main() {
 	gl_Position = gl_ModelViewProjectionMatrix * (gl_Vertex + vec4((gl_Normal * 0.1), 0));
 	position = (gl_ModelViewMatrix * gl_Vertex).xyz;

 	texCoord0 = vec2(gl_MultiTexCoord0);

 	colorIn = gl_Color;
 }
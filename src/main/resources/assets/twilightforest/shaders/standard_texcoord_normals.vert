#version 120

// From TTFTCUTS, thanks TTFTCUTS!

varying vec3 position;
varying vec2 texCoord0;
varying vec2 texCoord1;

varying vec3 normals;
varying vec3 camNorms;

void main() {
	gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
	position = (gl_ModelViewMatrix * gl_Vertex).xyz;

	texCoord0 = vec2(gl_MultiTexCoord0);
	texCoord1 = vec2(gl_MultiTexCoord1);

    normals = gl_Normal;
	camNorms = gl_NormalMatrix * gl_Normal;

	// to make a color from the normals do:
	// gl_FragColor = vec4(abs(normal), 1) * tex;
}
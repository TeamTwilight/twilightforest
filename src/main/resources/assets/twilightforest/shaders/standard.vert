#version 120

// From TTFTCUTS, thanks TTFTCUTS!

varying vec3 position;

void main()
{
	gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
	position = (gl_ModelViewMatrix * gl_Vertex).xyz;
}
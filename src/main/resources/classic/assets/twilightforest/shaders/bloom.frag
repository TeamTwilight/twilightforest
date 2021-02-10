#version 120
//#version 330 core
//layout (location = 0) out vec4 fragOut;
//layout (location = 1) out vec4 brightOut;

uniform sampler2D texture;
uniform int time;

uniform float yaw;
uniform float pitch;
uniform ivec2 resolution;

varying vec3 position;
void main() {
    //fragColorOut = vec4(1, 0.5, 0, 1);
    //brightColorOut = vec4(0, 0.5, 1, 1);
}
#version 120

uniform sampler2D zero;
//uniform sampler2D one;
uniform int time;

uniform float yaw;
uniform float pitch;

varying vec2 texCoord0;
//varying vec2 texCoord1;
varying vec3 position;

varying vec3 normals;
varying vec3 camNorms;

// https://gist.github.com/antoineMoPa/dea8e2f8495f6e5edcf724569ba5feae

void main() {
    vec4 tex = texture2D(zero, vec2(texCoord0));
    //vec4 light = texture2D(one, vec2(texCoord1));

    //gl_FragColor = vec4(abs(normal) * 2 - 1, tex.a);

    gl_FragColor = vec4(abs(camNorms), 1) * tex;
}
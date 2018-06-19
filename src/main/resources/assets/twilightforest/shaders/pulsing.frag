#version 120

#define M_PI 3.1415926535897932384626433832795

uniform sampler2D zero;
uniform float time;

uniform float yaw;
uniform float pitch;

varying float scale;
varying vec2 texCoord0;
varying vec3 worldPos;

void main() {
    vec4 tex = texture2D(zero, vec2(texCoord0));

    gl_FragColor = vec4( clamp( sin( length( worldPos.xyz * scale ) * 100 - time/3 ) * 0.339 + 0.592, 0.392, 0.729) * tex.r, 0, 0, tex.a );
}
#version 120

uniform sampler2D zero;
uniform float time;

uniform float yaw;
uniform float pitch;

varying float scale;
varying vec2 texCoord0;
varying vec3 worldPos;

void main() {
    vec4 tex = texture2D(zero, vec2(texCoord0));
    float timing = mod(length(worldPos.xyz) * 5 - time/50, 1);

    if (timing <= 0.25) {
        gl_FragColor = vec4( 0.392, 0.501, 0.0039, tex.a );
    } else if (timing <= 0.50) {
        gl_FragColor = vec4( 0.615, 0.784, 0.0078, tex.a );
    } else if (timing <= 0.75) {
        gl_FragColor = vec4( 0.729, 0.933, 0.0078, tex.a );
    } else {
        gl_FragColor = vec4( 0.615, 0.784, 0.0078, tex.a );
    }
}
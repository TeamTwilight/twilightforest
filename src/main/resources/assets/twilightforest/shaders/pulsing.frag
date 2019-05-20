#version 120

#define M_PI 3.1415926535897932384626433832795

uniform sampler2D zero;
uniform float time;
uniform ivec2 resolution;

uniform float ringCount = 3;
uniform float ringThickness = 0.3679;

varying float scale;
varying vec2 texCoord0;
varying vec4 worldPos;

varying vec4 center;
varying vec4 position;

void main() {
    // Do NOT move these to the vertex shader! The interpolation of vertex->fragment will cause visual shearing
    vec4 centerCoord_frag = (center / center.w) - (position / position.w);
    float posZ = (0.55 + center.w * 0.45);//sqrt(abs(center_z_frag.z - position_frag.z)) * 5;
    vec2 pos = vec2(centerCoord_frag.x * (float(resolution.x)/float(resolution.y)), centerCoord_frag.y) * posZ * posZ * 0.9;
    // Begin actual shader code

    vec4 tex = texture2D(zero, vec2(texCoord0));

    // 3D effect variation
    //float dist = length(worldPos.xyz * scale) * 2;
    // 2D effect variation
    float dist = length(pos) * 2;

    float wave = floor(fract(ringCount * dist * dist - time / 30) + ringThickness);

    gl_FragColor = vec4( max(wave - dist, 0) * 0.592 + 0.339 * tex.r, 0, 0, tex.a );
}
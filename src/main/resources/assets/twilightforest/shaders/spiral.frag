#version 120

uniform sampler2D zero;
uniform float time;

uniform float yaw;
uniform float pitch;
uniform ivec2 resolution;

varying float scale;
varying vec2 texCoord0;
varying vec4 worldPos;
varying vec4 center;
varying vec4 position;

// Thanks Amadornes! :3

// Utils
vec2 convolve(vec2 pos, float off, float strength) {
	float amt = length(pos) * strength + off;
	float arc = atan(pos.x, pos.y);
	return vec2(cos(arc + amt), sin(arc + amt)) * length(pos);
}

float getPerceptualBrightness(vec3 c) {
    return sqrt(0.241 * c.r * c.r + 0.691 * c.g * c.g + 0.068 * c.b * c.b);
}

void main() {
    // Do NOT move these to the vertex shader! The interpolation of vertex->fragment will cause visual shearing
    float dist = center.w;
    vec4 center_frag = center / center.w;
    //vec4 center_z_frag = center_z / center_z.w;
    vec4 position_frag = position / position.w;
    vec4 centerCoord_frag = center_frag - position_frag;

    vec4 tex = texture2D(zero, vec2(texCoord0));
    float gray = getPerceptualBrightness(tex.xyz);
    float posZ = (0.55 + dist * 0.45);//sqrt(abs(center_z_frag.z - position_frag.z)) * 5;

    vec2 pos = vec2(centerCoord_frag.x * (float(resolution.x)/float(resolution.y)), centerCoord_frag.y) * posZ * posZ * 0.9;
    vec2 realPos = convolve(pos, -time/10.0, 65.0 / posZ);

    if(tex.a != 0.0 && abs(realPos.x) < abs(realPos.y) && realPos.y <= 0.0) {
        float col = max(0.0, 1.5 + 6 * realPos.y);
        float col2 = max(0.0, 1.5 + 8 * realPos.y) - 0.5;

        gl_FragColor = vec4(max(col, 0.2862), col2, col2, /*max(0.0, 1.5 + 8 * realPos.y) * */tex.a * gray);
    } else {
        gl_FragColor = vec4(0.2862, 0, 0, tex.a * gray);
    }

    //gl_FragColor = vec4(realPos * 0.5 + 0.5, 0, 1);
}
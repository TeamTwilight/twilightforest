#version 120

uniform sampler2D zero;
uniform float time;

uniform float yaw;
uniform float pitch;
uniform ivec2 resolution;

varying vec2 texCoord0;
varying vec4 worldPos;
varying vec4 position;
varying vec4 center;
varying vec4 centerCoord;
varying float scale;

// Thanks Amadornes! :3

// Utils
vec2 convolve(vec2 pos, float off, float strength) {
	float amt = length(pos) * strength + off;
	float arc = atan(pos.x, pos.y);
	return vec2(cos(arc + amt), sin(arc + amt)) * length(pos);
}

vec3 hsv2rgb(vec3 c) {
    vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);
    vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);
    return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);
}

void main() {
    // Do NOT move these to the vertex shader! The interpolation of vert->Frag will cause visual shearing
    vec4 center_frag = center / center.w;
    vec4 position_frag = position / position.w;
    vec4 centerCoord_frag = center_frag - position_frag;

    vec4 tex = texture2D(zero, vec2(texCoord0));

    ////tex.a = 1;

    ////float posZ = sqrt(abs((center - position).z)) * 10;

    //vec2 pos = (position - center).xy / 2;
    //vec2 realPos = convolve(pos, -time/10.0, 75.0);

    //if(abs(realPos.x) < abs(realPos.y) && realPos.y <= 0.0 && realPos.y >= -0.2) {
    //	gl_FragColor = vec4(vec3(1.0), 1.0);
    //} else {
    //	gl_FragColor = tex;
    //}

    // fixme debug
    //gl_FragColor = tex;

    float posZ = 1;//sqrt(abs(center.z));

    //float len = length(vec2(position.y, 1));
    float len = length(centerCoord_frag.w);

    gl_FragColor = vec4(0);//vec4(vec3(len), 1.0);
    if ( mod(len, 0.05) < 0.01 && mod(len, 0.05) > 0 ) { gl_FragColor = vec4(hsv2rgb(vec3(len * 4, 1, 1)), 1.0); }

    if ( mod(centerCoord_frag.x, 0.05) < 0.005 && mod(centerCoord_frag.x, 0.05) > 0 ) { gl_FragColor = vec4(1.0, 1.0, 1.0, 1.0); }
    if ( mod(centerCoord_frag.y, 0.05) < 0.005 && mod(centerCoord_frag.y, 0.05) > 0 ) { gl_FragColor = vec4(1.0, 1.0, 1.0, 1.0); }

    //gl_FragColor = vec4(vec3(position.xyz), 1.0);

    //gl_FragColor = vec4(hsv2rgb(vec3(position.w - mod(position.w, 0.05), 1, 1)), 1.0);
}
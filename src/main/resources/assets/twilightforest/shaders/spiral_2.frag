//precision highp float;

#version 120

uniform sampler2D zero;
uniform float time;

uniform float yaw;
uniform float pitch;
uniform ivec2 resolution;

varying vec4 center;
varying vec2 texCoord0;
varying vec3 worldPos;
varying vec4 position;

varying vec3 centerCoord;

// Thanks Amadornes! :3

// Utils
vec2 convolve(vec2 pos, float off, float strength) {
	float amt = -length(pos) * strength + off;
	float arc = atan(pos.x, pos.y);
	return vec2(cos(arc + amt), sin(arc + amt)) * length(pos);
}

void main() {
    vec4 tex = texture2D(zero, vec2(texCoord0));

    //tex.a = 1;

    //float posZ = sqrt(abs(centerCoord.z)) * 10;

    //vec2 pos = centerCoord.xy;
    //vec2 realPos = convolve(pos * posZ, -time/10.0, 50.0);

    //if(abs(realPos.x) < abs(realPos.y) && realPos.y <= 0.0 && realPos.y >= -0.2) {
    //	gl_FragColor = vec4(vec3(1.0), tex.a);
    //} else {
    //	gl_FragColor = vec4(vec3(0.0), tex.a);
    //}

    // fixme debug
    gl_FragColor.xyz = centerCoord;
    gl_FragColor.a = 1.0;

    float posZ = 1;//sqrt(abs(center.z));

    if (length(centerCoord.xy) < 0.1  * posZ) { gl_FragColor = vec4(vec3(0.5), 1.0); }
    if (length(centerCoord.xy) < 0.05 * posZ) { gl_FragColor = vec4(1.0); }

    gl_FragColor = vec4(position.xy, 1, 1);
}
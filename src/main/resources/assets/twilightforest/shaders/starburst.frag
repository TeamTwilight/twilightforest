#version 120

#define M_PI 3.1415926535897932384626433832795

uniform sampler2D texture;
uniform float time;
uniform vec3 position;

varying vec2 texCoord0;

varying vec3 worldPos;

varying vec4 colorIn;

float roundEither(float valueIn) {
    float valueOut = valueIn + 0.5;

    return valueOut - mod(valueOut, 1.0);
}

void main( void ) {
    vec4 tex = texture2D(texture, vec2(texCoord0));

	float ray = atan( (worldPos.y)/(worldPos.x) ) / M_PI * 2.0;

	float burst = roundEither(mod(ray + time/100.0, 0.5)*2.0);

	gl_FragColor = vec4(colorIn.xyz * 0.6666 + 0.3333, burst*tex.r);
}
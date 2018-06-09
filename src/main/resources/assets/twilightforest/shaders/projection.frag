#version 120

// TODO Code needs integrating. Originally written in shadertoy.

// Rounds the value to either 0 or 1. 0.5 is the threshold.
vec4 roundEither(vec4 valueIn) {
    vec4 valueOut = valueIn + 0.5;

    return valueOut - mod(valueOut, 1.0);
}

void main( in vec2 fragCoord ) {
    // Ticker used for progression.
    // TODO Change to a uniform for time progression
    float tick = mod(iTime, 2.0) / 2.0;

    // Transform screen position into percent
    vec2 uv = fragCoord.xy / iResolution.xy;

    // Sampler, Texture in
    vec4 texVal = texture( iChannel0, uv );

    // tests the distance from a source
    float distanceFrom = distance(uv*vec2(2,1), vec2(1,0));

    gl_FragColor = roundEither(texVal - (distanceFrom - (tick * 2.0)));
}
#version 330
#extension GL_ARB_separate_shader_objects : require

#include <minecraft:dynamictransforms.glsl>
#include <minecraft:fog.glsl>
#include <minecraft:globals.glsl>

////////////////// K.jpg's Smooth Re-oriented 8-Point BCC Noise //////////////////
////////////////////// a.k.a. OpenSimplex2, Smooth Version ///////////////////////
///////////// Modified to produce a Gabor noise like output instead. /////////////
//////////////////// Output: vec4(dF/dx, dF/dy, dF/dz, value) ////////////////////

// Borrowed from Stefan Gustavson's noise code
vec4 permute(vec4 t) {
    return t * (t * 34.0 + 133.0);
}

// BCC lattice split up into 2 cube lattices
vec2 simplexGaborNoisePart(vec3 X, vec3 dir) {
    vec3 b = floor(X);
    vec4 i4 = vec4(X - b, 2.5);

    // Pick between each pair of oppposite corners in the cube.
    vec3 v1 = b + floor(dot(i4, vec4(.25)));
    vec3 v2 = b + vec3(1, 0, 0) + vec3(-1, 1, 1) * floor(dot(i4, vec4(-.25, .25, .25, .35)));
    vec3 v3 = b + vec3(0, 1, 0) + vec3(1, -1, 1) * floor(dot(i4, vec4(.25, -.25, .25, .35)));
    vec3 v4 = b + vec3(0, 0, 1) + vec3(1, 1, -1) * floor(dot(i4, vec4(.25, .25, -.25, .35)));

    // Gradient hashes for the four vertices in this half-lattice.
    vec4 hashes = permute(mod(vec4(v1.x, v2.x, v3.x, v4.x), 289.0));
    hashes = permute(mod(hashes + vec4(v1.y, v2.y, v3.y, v4.y), 289.0));
    hashes = mod(permute(mod(hashes + vec4(v1.z, v2.z, v3.z, v4.z), 289.0)), 48.0);
    vec4 sineOffsets = hashes / 48.0 * 3.14159265 * 4.0;

    // Gradient extrapolations are replaced with sin(dot(dX, inputVector) + pseudorandomOffset)
    vec3 d1 = X - v1; vec3 d2 = X - v2; vec3 d3 = X - v3; vec3 d4 = X - v4;
    vec4 a = max(0.75 - vec4(dot(d1, d1), dot(d2, d2), dot(d3, d3), dot(d4, d4)), 0.0);
    vec4 aa = a * a; vec4 aaa = aa * a;
    vec4 extrapolations = vec4(dot(d1, dir), dot(d2, dir), dot(d3, dir), dot(d4, dir)) + sineOffsets;
    extrapolations = sin(extrapolations);

    // Return (kernels^3) * sinusoids, and just (kernels^3), so we can average them later
    return vec2(dot(aaa, extrapolations), dot(aaa, vec4(1.0)));
}

// Gives X and Y a triangular alignment, and lets Z move up the main diagonal.
// Might be good for terrain, or a time varying X/Y plane. Z repeats.
float simplexGaborNoise_XYBeforeZ(vec3 X, vec3 dir) {

    // Not a skew transform.
    mat3 orthonormalMap = mat3(
        0.788675134594813, -0.211324865405187, -0.577350269189626,
        -0.211324865405187, 0.788675134594813, -0.577350269189626,
        0.577350269189626, 0.577350269189626, 0.577350269189626);

    X = orthonormalMap * X;
    dir = orthonormalMap * dir;
    vec2 both = simplexGaborNoisePart(X, dir) + simplexGaborNoisePart(X + 144.5, dir);
    return both.x / both.y;
}

//////////////////////////////// End noise code ////////////////////////////////

layout(std140) uniform TwilightForestAurora {
    int SeedContext;
    vec3 PositionContext;
};

layout(location = 0) out vec4 fragColor;

layout(location = 0) in vec4 pixelPos;
layout(location = 1) in vec4 vertexColor;

const int STEPS = 16;
const float NOISE_SCALE = 512.0;
const float AURORA_LENGTH = 0.85;
const float HAZE = 0.035;

float genNoise(vec2 pos, float speed) {
    pos += PositionContext.xz * 0.75 + vec2(SeedContext / 360, SeedContext % 360);
    return simplexGaborNoise_XYBeforeZ(vec3(pos / NOISE_SCALE, GameTime * speed), vec3(3.14159265 * NOISE_SCALE / 300.0, 0.0, 0.0));
}

float fixNoise(float noise) {
    return max(0.0, 0.5 - 2.5 * abs(noise));
}

float fixNoiseIntegral(float noise) {
    float c = clamp(noise, -0.2, 0.2);
    return 0.05 + 0.5 * c - 1.25 * c * abs(c);
}

// https://michaelwalczyk.com/blog-ray-marching.html
float rayMarch(vec2 pos, float footprint) {
    float tailBlur = smoothstep(0.15, 0.5, AURORA_LENGTH * length(pos) / (float(STEPS) * NOISE_SCALE));
    float n0 = genNoise(pos, 22.5);
    float noise = mix(fixNoise(n0), HAZE, smoothstep(0.05, 0.2, footprint));
    for (int i = 1; i < STEPS; ++i) {
        float average = HAZE;
        if (tailBlur < 1.0) {
            float n1 = genNoise(pos * (1.0 + float(i) / float(STEPS) * AURORA_LENGTH), 22.5);
            float dn = n1 - n0;
            float exact = abs(dn) > 0.0001 ? (fixNoiseIntegral(n1) - fixNoiseIntegral(n0)) / dn : fixNoise((n0 + n1) * 0.5);
            average = mix(exact, HAZE, tailBlur);
            n0 = n1;
        }
        noise += average * 0.65 * (1.0 - (float(i) - 0.5) / float(STEPS));
    }
    return noise;
}

void main() {
    float footprint = length(fwidth(pixelPos.xz)) / NOISE_SCALE;
    float fogFade = 1.0 - smoothstep(FogRenderDistanceEnd * 2.5, FogRenderDistanceEnd * 5.0, length(pixelPos.xz));
    if (fogFade <= 0.0)
        discard;

    float noise = rayMarch(pixelPos.xz, footprint);
    float colorNoise = (genNoise(pixelPos.xz, 720.0) + 1.0) * 0.25;
    vec3 color = vec3(0.0, 0.5 + colorNoise, 1.0 - colorNoise);
    fragColor = vec4(vertexColor.rgb * ColorModulator.rgb * color, vertexColor.a * ColorModulator.a * noise * fogFade);
}

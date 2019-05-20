#version 120

uniform sampler2D zero;

varying vec2 texCoord0;
varying vec4 colorIn;

void main() {
    gl_FragColor = vec4(1);//colorIn;
}
#version 420

layout(location = 0) in vec2 vertex_position;

uniform mat4 trs;
uniform mat4 proj;

void main() {
  gl_Position = proj*trs*vec4(vertex_position, 0.0, 1.0);
}
#version 420

layout(location = 0) in vec2 vertex_position;

uniform mat4 proj;

void main() {
  gl_Position = proj*vec4(vertex_position, 1f, 1f);
}
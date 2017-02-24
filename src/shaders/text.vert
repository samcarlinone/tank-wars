#version 420

layout(location = 0) in vec2 vertex_position;
layout(location = 1) in vec3 tex_position;

out vec3 tex_pos;

uniform mat4 proj;

void main() {
  tex_pos = tex_position;
  gl_Position = proj*vec4(vertex_position, 1.0, 1.0);
}
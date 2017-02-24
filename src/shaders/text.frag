#version 420

in vec3 tex_pos;
out vec4 fragColor;

uniform sampler2DArray texture0;

void main() {
    fragColor = texture2DArray(texture0, tex_pos);
    //fragColor = vec4(tex_pos.z/26f, 0f, 0f, 1.0f);
}
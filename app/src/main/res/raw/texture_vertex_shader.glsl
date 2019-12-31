
uniform mat4 u_Matrix;

attribute vec4 a_Position;
//纹理坐标  两个分量 ST
attribute vec2 a_TextureCoordinates;
//插值
varying vec2 v_TextureCoordinates;

void main() {

    v_TextureCoordinates = a_TextureCoordinates;
    gl_Position = u_Matrix * a_Position;

}

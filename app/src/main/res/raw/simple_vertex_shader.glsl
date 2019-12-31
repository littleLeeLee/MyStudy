
//定义一个代表 4*4 的矩阵
uniform mat4 u_Matrix;

attribute vec4 a_Position ;
attribute vec4 a_Color;

//混合颜色 渐变 两个点之间的颜色渐变
varying vec4 v_Color;

void main() {
    v_Color = a_Color;
    gl_Position = u_Matrix * a_Position;
    gl_PointSize  = 15.0;

}

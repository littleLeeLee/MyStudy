
attribute vec4 a_Position ;
attribute vec4 a_Color;
//混合颜色 渐变 两个点之间的颜色渐变
varying vec4 v_Color;

void main() {
    v_Color = a_Color;
    gl_Position = a_Position;
    gl_PointSize  = 10.0;

}

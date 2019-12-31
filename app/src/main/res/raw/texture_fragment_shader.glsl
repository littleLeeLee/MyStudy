
precision mediump float;
//实际的纹理数据  sampler2D二维纹理数据数组
uniform sampler2D u_TextureUnit;
//每个片段都会调用
varying vec2 v_TextureCoordinates;

void main() {
    //通过计算特定坐标处的颜色值，结果赋值给gl_fragment
    gl_FragColor = texture2D(u_TextureUnit,v_TextureCoordinates);

}

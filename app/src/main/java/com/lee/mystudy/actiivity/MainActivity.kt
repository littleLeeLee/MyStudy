package com.lee.mystudy.actiivity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jaredrummler.android.widget.AnimatedSvgView
import com.lee.mystudy.R
import com.lee.mystudy.adapter.MainAdapter
import com.lee.mystudy.jnitest.MyJniLibs
import com.lee.mystudy.util.LogUtil
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var dataList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        LogUtil.d(MyJniLibs.loadString())
        checkPer()
        dataList.add("camera_by_surfaceView")
        dataList.add("camera_by_glSurfaceView")
        dataList.add("camera_by_glSurfaceView_black")
        dataList.add("myOpenGL")
        dataList.add("camera")
        dataList.add("camera")
        setDataView()
        val svgView = findViewById<AnimatedSvgView>(R.id.animated_svg_view)
        svgView.setOnStateChangeListener {
        //    LogUtil.d("state:$it")

            if(it == 3){
                svgView.postDelayed( {
                    svgView.start()
                },500)
            }
        }
        svgView.start()
    }

    private fun testSpeed() {
        /*var path = "/sdcard/facepic/1563243566797.jpg"

        val options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.ARGB_8888

        val bitmap = BitmapFactory.decodeFile(path, options)

        val argb = IntArray(480 * 640)
        //扫描所有的像素点
        bitmap.getPixels(argb, 0, 480, 0, 0, 480, 640)
        LogUtil.d("argb:::"+argb.size)*/

      /*  var k = 2
        LogUtil.d("kkk:"+k)
        for (a in 0 until 1024 * 1024 * 1024) {

            k += a

        }
        LogUtil.d("kkk:"+k)*/

        MyJniLibs.rgb2yuv()


    }

    @SuppressLint("CheckResult")
    private fun checkPer() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            //没有权限，申请权限
            val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
            //申请权限，其中RC_PERMISSION是权限申请码，用来标志权限申请的
            ActivityCompat.requestPermissions(this@MainActivity, permissions, 1024)
        }else {
            //拥有权限
        }

    }

    @SuppressLint("WrongConstant")
    private fun setDataView() {

        val mainAdapter = MainAdapter(this, dataList)
        mainAdapter.setOnItemClickListener(object: MainAdapter.onItemClickListener {
            override fun onItemClick(view: View, position: Int) {
         //       ToastUtils.showShort(""+position)
                when(position){

                    0->{

                        CameraActivitySurface.start(this@MainActivity)

                    }

                    1->{

                        CameraActivityGLRgb.start(this@MainActivity)

                    }

                    2->{

                        CameraActivityGLBlack.start(this@MainActivity)

                    }
                    3->{
                      //  testSpeed()
                      /*  var cpuTestThread = CPUTestThread()
                        for (a in 0 until 1500) {
                            Thread(cpuTestThread).start()

                        }*/
                        val intent = Intent(this@MainActivity, MyOpenGLActivity::class.java)
                        startActivity(intent);
                    }

                }

            }
        })
        ry_main.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        ry_main.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        ry_main.adapter = mainAdapter



    }


    inner class CPUTestThread : Runnable{
        override fun run() {

            var busyTime = 10
            var idleTime = busyTime
            var startTime = 0L
            while (true) {

                busyTime++
                if(busyTime ==Integer.MAX_VALUE-10){
                    busyTime = 10
                }
                try {
                    Thread.sleep(10)
                } catch ( e :InterruptedException) {

                }
            }



        }

    }
}

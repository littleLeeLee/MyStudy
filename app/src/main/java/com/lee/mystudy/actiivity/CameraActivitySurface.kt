package com.lee.mystudy.actiivity

import android.content.Context
import android.content.Intent
import android.hardware.Camera
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.lee.mystudy.R
import com.lee.mystudy.util.LogUtil
import kotlinx.android.synthetic.main.activity_cam_surface.*

class CameraActivitySurface  : AppCompatActivity() ,View.OnClickListener{

    val ORI_LEFT = 90
    val ORI_RIGHT = 270
    val ORI_TOP = 0
    val ORI_BOTTOM = 180

    var ORI = 90

    override fun onClick(v: View?) {

        when(v!!.id){

            R.id.ori_left->{
                ORI = ORI_LEFT
                switchOri(ORI)
            }

            R.id.ori_right->{
                ORI = ORI_RIGHT
                switchOri(ORI)
            }

            R.id.ori_top->{
                ORI = ORI_TOP
                switchOri(ORI_TOP)
            }

            R.id.ori_bottom->{
                ORI = ORI_BOTTOM
                switchOri(ORI_BOTTOM)
            }

        }



    }

    private fun switchOri(ori: Int) {

        if(camera != null){
            camera!!.stopPreview()
            camera!!.setDisplayOrientation(ori)
            camera!!.startPreview()
        }



    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cam_surface)
        ori_top.setOnClickListener(this)
        ori_bottom.setOnClickListener(this)
        ori_left.setOnClickListener(this)
        ori_right.setOnClickListener(this)
        initData()

    }
    private  var camera : Camera?=null
    private fun initData() {

        sur_preview.setOnClickListener {

            camera?.autoFocus { success, camera ->  }
        }


            sur_preview.holder.addCallback(object: SurfaceHolder.Callback {
                override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {


                }

                override fun surfaceDestroyed(holder: SurfaceHolder?) {

                }

                override fun surfaceCreated(holder: SurfaceHolder?) {
                    LogUtil.d("surfaceCreated")
                   openCamera(holder!!)
                }
            })

    }

    private fun openCamera(holder: SurfaceHolder) {

        camera = Camera.open(0)
        if(camera != null) {

            camera!!.setPreviewDisplay(holder)

            camera!!.setPreviewCallback(object: Camera.PreviewCallback {
                override fun onPreviewFrame(data: ByteArray?, camera: Camera?) {



                }
            })
         //   camera?.autoFocus { success, camera ->  }
            camera!!.startPreview()
        }

    }

    companion object{

        fun start(context :Context){

            context.startActivity(Intent(context,CameraActivitySurface::class.java))

        }

    }


}
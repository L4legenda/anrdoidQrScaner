package ru.l4legenda.qrscaner

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.SparseArray
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.util.isNotEmpty
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import java.lang.Exception

class readerQr : AppCompatActivity() {

    private val requestCodeCameraPermission = 1001

    private lateinit var cameraSource: CameraSource

    private lateinit var detector: BarcodeDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reader_qr)

        if(ContextCompat.checkSelfPermission(
                this@readerQr,
                android.Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED){
            askForCameraPermission()
        }else{
            setupControls()
        }
    }


    private fun setupControls(){
        detector = BarcodeDetector.Builder(this@readerQr).build()
        cameraSource = CameraSource.Builder(this@readerQr, detector)
            .setAutoFocusEnabled(true)
            .build()
        val cameraSurfaceView = findViewById<SurfaceView>(R.id.cameraSurfaceView)
        cameraSurfaceView.holder.addCallback(surfaceCallBack)
        detector.setProcessor(processor)
    }

    private fun askForCameraPermission(){

        ActivityCompat.requestPermissions(
            this@readerQr,
            arrayOf(android.Manifest.permission.CAMERA),
            requestCodeCameraPermission
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == requestCodeCameraPermission && grantResults.isNotEmpty()){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                setupControls()
            }else{
                Toast.makeText(applicationContext, "Permission Denied", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private val surfaceCallBack = object : SurfaceHolder.Callback{
        override fun surfaceCreated(holder: SurfaceHolder) {
            try {
                if (ActivityCompat.checkSelfPermission(
                        this@readerQr,
                        android.Manifest.permission.CAMERA
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    Toast.makeText(applicationContext, "Нет доступа", Toast.LENGTH_SHORT).show()
                    return
                }
                cameraSource.start(holder)
            }catch (exeption: Exception){
                Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        }

        override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {

        }

        override fun surfaceDestroyed(holder: SurfaceHolder) {
            cameraSource.stop()
        }

    }


    private val processor = object : Detector.Processor<Barcode>{
        override fun release() {

        }

        override fun receiveDetections(p0: Detector.Detections<Barcode>?) {
            val textScanResult = findViewById<TextView>(R.id.textScaner)
            if(p0 != null && p0.detectedItems.isNotEmpty()){
                val qrCodes: SparseArray<Barcode> = p0.detectedItems
                val code = qrCodes.valueAt(0)
                textScanResult.text = code.displayValue
            }else{
                textScanResult.text = ""
            }
        }
    }
}
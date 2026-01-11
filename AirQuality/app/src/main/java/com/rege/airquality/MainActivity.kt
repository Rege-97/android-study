package com.rege.airquality

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.rege.airquality.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    // 권한 요청 시 구분용으로 사용하는 코드
    private val PERMISSIONS_REQUEST_CODE = 100

    // 앱에서 반드시 필요한 위치 권한들
    val REQUIRED_PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,   // 정확한 위치(GPS)
        Manifest.permission.ACCESS_COARSE_LOCATION  // 대략적인 위치
    )

    // 설정 화면을 열었다가 돌아왔을 때 결과를 받기 위한 런처
    lateinit var getGPSPermissionLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 앱 실행 시 권한 + GPS 상태 체크 시작
        checkAllPermissions()
    }

    /**
     * GPS 켜짐 여부 + 앱 권한 여부를 한 번에 검사하는 함수
     */
    private fun checkAllPermissions() {
        if (!isLocationServiceAvailable()) {    // GPS 또는 네트워크 위치 서비스가 꺼져 있으면
            showDialogForLocationServiceSetting()    // 설정 화면으로 유도하는 다이얼로그 표시
        } else {
            isRunTimePermissionGranted()     // GPS가 켜져 있으면 앱 권한 확인
        }
    }

    /**
     * GPS 또는 네트워크 위치 서비스가 켜져 있는지 확인
     */
    private fun isLocationServiceAvailable(): Boolean {
        // 시스템에서 위치 관리자 객체 가져오기
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        // GPS 또는 네트워크 위치 중 하나라도 켜져 있으면 true
        return (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    /**
     * 앱에 위치 권한이 있는지 런타임에서 확인
     */
    private fun isRunTimePermissionGranted() {
        // 정확한 위치 권한 상태 확인
        val hasFineLocationPermission = ContextCompat.checkSelfPermission(
            this@MainActivity,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        // 대략적인 위치 권한 상태 확인
        val hasCoarseLocationPermission = ContextCompat.checkSelfPermission(
            this@MainActivity,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        // 하나라도 권한이 없으면 시스템 권한 팝업 요청
        if (hasFineLocationPermission != PackageManager.PERMISSION_GRANTED || hasCoarseLocationPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this@MainActivity,
                REQUIRED_PERMISSIONS,
                PERMISSIONS_REQUEST_CODE
            )
        }
    }

    /**
     * 권한 요청 팝업에서 사용자가 허용/거부 선택 후 호출됨
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // 내가 요청한 권한인지 확인
        if (requestCode == PERMISSIONS_REQUEST_CODE && grantResults.size == REQUIRED_PERMISSIONS.size) {
            var checkResult = true

            // 모든 권한이 허용되었는지 검사
            for (result in grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    checkResult = false
                    break
                }
            }

            if (checkResult) {

            } else {
                // 하나라도 거부되면 안내 후 앱 종료
                Toast.makeText(this, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG)
                    .show()
                finish()
            }
        }
    }

    /**
     * GPS가 꺼져 있을 때 설정 화면으로 유도하는 다이얼로그
     */
    private fun showDialogForLocationServiceSetting() {
        // 설정 화면 실행 후 돌아왔을 때 결과 처리
        getGPSPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            // 설정 화면에서 돌아온 후 GPS 상태 다시 확인
            if (isLocationServiceAvailable()) {
                isRunTimePermissionGranted()
            } else {
                Toast.makeText(this, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG)
                    .show()
                finish()
            }

        }

        val builder: AlertDialog.Builder = AlertDialog.Builder(this@MainActivity)
        builder.setTitle("위치 서비스 비활성화")
        builder.setMessage("위치 서비스가 꺼져있습니다. 설정해야 앱을 사용할 수 있습니다.")
        builder.setCancelable(true)
        // 위치 설정 화면으로 이동
        builder.setPositiveButton("설정", DialogInterface.OnClickListener { dialogInterface, i ->
            val callGPSSettingIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            getGPSPermissionLauncher.launch(callGPSSettingIntent)
        })
        builder.setNegativeButton("취소", DialogInterface.OnClickListener { dialogInterface, i ->
            dialogInterface.cancel()
            Toast.makeText(this, "위치 서비스를 사용할 수 없습니다.", Toast.LENGTH_LONG)
                .show()
            finish()
        })
        builder.create().show()
    }
}
package com.rege.airquality

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.app.ActivityCompat

// 위치 정보를 제공하는 클래스
// GPS / 네트워크 중 사용 가능한 위치정보를 저장
class LocationProvider(val context: Context) {

    // 최종적으로 선택된 위치 객체 (nullable)
    private var location: Location? = null

    // 시스템 위치 관리자
    private var locationManager: LocationManager? = null

    /**
     * 클래스 생성 시 자동으로 실행되는 블록
     * (MainActivity에서 LocationProvider(context) 호출하면 바로 실행됨)
     */
    init {
        getLocation()
    }

    private fun getLocation(): Location? {
        try {
            // 시스템에서 LocationManager 가져오기
            locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

            var gpsLocation: Location? = null       // GPS 위치 저장용 변수
            var networkLocation: Location? = null   // 네트워크 위치 저장용 변수

            val isGpsEnabled =
                locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)    // GPS 사용 가능 여부 확인
            val isNetworkEnabled =
                locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)               // 네트워크 위치 사용 가능 여부 확인

            if (!isGpsEnabled && !isNetworkEnabled) {   // GPS도 네트워크도 다 꺼져 있으면 위치 못 얻음
                return null
            } else {
                // 위치 권한이 하나라도 없으면 위치 못 얻음
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return null
                }

                if (isNetworkEnabled) { // 네트워크 위치 사용 가능하면 마지막 위치 가져오기
                    networkLocation =
                        locationManager?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                }
                if (isGpsEnabled) { // GPS 사용 가능하면 마지막 위치 가져오기
                    gpsLocation =
                        locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                }

                /**
                 * GPS와 네트워크 위치가 둘 다 있을 경우
                 * → accuracy(정확도) 값을 비교해서 더 정확한 것 선택
                 *
                 * ※ accuracy 값은 "작을수록 정확"
                 */
                if (gpsLocation != null && networkLocation != null) {
                    if (gpsLocation.accuracy > networkLocation.accuracy) {
                        location = gpsLocation
                    } else {
                        location = networkLocation
                    }
                } else {    // 둘 중 하나만 있는 경우
                    if (gpsLocation != null) {
                        location = gpsLocation
                    }
                    if (networkLocation != null) {
                        location = networkLocation
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return location // 최종 선택된 위치 반환
    }

    /**
     * 위도 반환
     */
    fun getLocationLatitude(): Double? {
        return location?.latitude
    }

    /**
     * 경도 반환
     */
    fun getLocationLongitude(): Double? {
        return location?.longitude
    }
}
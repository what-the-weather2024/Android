package com.wtw.whattheweather.ui.upload

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.KakaoMapSdk
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import com.wtw.whattheweather.BuildConfig
import com.wtw.whattheweather.R
import com.wtw.whattheweather.databinding.FragmentUploadBinding


class UploadFragment : Fragment() {

    private lateinit var binding : FragmentUploadBinding
    private lateinit var viewModel : UploadViewModel
    private lateinit var mContext : Context
    private lateinit var mapView : MapView
    private lateinit var kakaoMap : KakaoMap

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private fun getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(
                mContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                mContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
            fusedLocationProviderClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                null
            ).addOnSuccessListener { location: Location? ->

                location?.let {
                    kakaoMap.moveCamera(CameraUpdateFactory.newCenterPosition(LatLng.from(it.latitude, it.longitude)))
                    viewModel.selectedLocation.value = getAddressFromLocation(it.latitude, it.longitude)

                    kakaoMap.labelManager?.layer?.removeAll()

                    val styles = kakaoMap.labelManager?.addLabelStyles(LabelStyles.from(LabelStyle.from(R.drawable.map_label_img)))
                    val options = LabelOptions.from(LatLng.from(it.latitude,it.longitude)).setStyles(styles)
                    val layer = kakaoMap.labelManager?.layer
                    layer?.addLabel(options)
                }

            }
    }

    private fun getAddressFromLocation(latitude: Double, longitude: Double): String? {
        val geocoder = Geocoder(mContext)
        return try {
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0].getAddressLine(0)
                address
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun initMap() {
        KakaoMapSdk.init(mContext, BuildConfig.KAKAO_MAP_KEY)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        mapView = binding.fragmentUploadMapFrame
        mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {

            }

            override fun onMapError(p0: Exception?) {

            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(p0: KakaoMap) {

                kakaoMap = p0
                getCurrentLocation()

                kakaoMap.setOnMapClickListener { kakaoMap, latLng, pointF, poi ->

                    kakaoMap.labelManager?.layer?.removeAll()

                    kakaoMap.moveCamera(CameraUpdateFactory.newCenterPosition(latLng))
                    viewModel.selectedLocation.value = getAddressFromLocation(latLng.latitude, latLng.longitude)

                    val styles = kakaoMap.labelManager?.addLabelStyles(LabelStyles.from(LabelStyle.from(R.drawable.map_label_img)))
                    val options = LabelOptions.from(latLng).setStyles(styles)
                    val layer = kakaoMap.labelManager?.layer
                    layer?.addLabel(options)
                }
            }
        })
    }

    private val locationPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {

        initMap()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_upload, container, false)
        viewModel = ViewModelProvider(this)[UploadViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner


        if(ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {

            locationPermissionLauncher.launch(arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ))
        }
        else {
            initMap()

        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fragmentUploadReSearchLocationBtn.setOnClickListener {

            getCurrentLocation()


        }

        binding.fragmentUploadLocationConfirmBtn.setOnClickListener {

            findNavController().navigate(UploadFragmentDirections.actionNavigationUploadToNavigationImageUpload(viewModel.selectedLocation.value.toString()))
//            findNavController().navigate(R.id.action_navigation_upload_to_navigation_image_upload)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mContext = context
    }

    override fun onResume() {
        super.onResume()

        mapView.resume()
    }

    override fun onPause() {
        super.onPause()

        mapView.pause()
    }
}
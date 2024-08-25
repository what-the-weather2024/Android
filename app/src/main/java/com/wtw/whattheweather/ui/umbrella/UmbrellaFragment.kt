package com.wtw.whattheweather.ui.umbrella

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.KakaoMapSdk
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.Label
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import com.kakao.vectormap.label.LabelTextBuilder
import com.wtw.whattheweather.BuildConfig
import com.wtw.whattheweather.R
import com.wtw.whattheweather.databinding.FragmentUmbrellaBinding
import com.wtw.whattheweather.databinding.MapLabelInfoLayoutBinding
import com.wtw.whattheweather.model.UmbrellaItemData
import com.wtw.whattheweather.network.RetrofitBuilder.networkService
import kotlinx.coroutines.launch
import kotlin.Exception

class UmbrellaFragment : Fragment() {

    private lateinit var binding : FragmentUmbrellaBinding
    private lateinit var viewModel : UmbrellaViewModel
    private lateinit var mContext : Context

    private lateinit var mapView : MapView
    private lateinit var kakaoMap : KakaoMap

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private var umbrellaItemList : List<UmbrellaItemData>? = null


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

                Log.e("location", location.toString())

                location?.let {
                    kakaoMap.moveCamera(CameraUpdateFactory.newCenterPosition(LatLng.from(it.latitude, it.longitude)))

//                    val styles = kakaoMap.labelManager?.addLabelStyles(LabelStyles.from(LabelStyle.from(R.drawable.map_label_icon)))
//                    val options = LabelOptions.from(LatLng.from(it.latitude,it.longitude)).setStyles(styles)
//                    val layer = kakaoMap.labelManager?.layer
//                    layer?.addLabel(options)
                }

            }
    }

    private fun initMap() {
        KakaoMapSdk.init(mContext, BuildConfig.KAKAO_MAP_KEY)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        mapView = binding.umbrellaMapView
        mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {

            }

            override fun onMapError(p0: Exception?) {

            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(p0: KakaoMap) {

                kakaoMap = p0
                getCurrentLocation()

                getUmbrellaList()

            }
        })
    }

    private val locationPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {

        initMap()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_umbrella, container, false)
        viewModel = ViewModelProvider(this)[UmbrellaViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

    }

    private fun getUmbrellaList() = lifecycleScope.launch {

        val list = networkService.getUmbrellaAddressList()

        try {
            if(list.isSuccessful) {
                list.body()?.let {
                    umbrellaItemList = it

                    umbrellaItemList?.forEachIndexed { index, umbrellaItem ->
                        val styles = kakaoMap.labelManager?.addLabelStyles(LabelStyles.from(LabelStyle.from(R.drawable.map_label_img)))

                        Log.e("$index umbrella", umbrellaItem.toString())
                        val options = LabelOptions.from(LatLng.from(umbrellaItem.latitude,umbrellaItem.longitude)).setStyles(styles)
//                            .setTexts(LabelTextBuilder().setTexts(umbrellaItem.name, umbrellaItem.phoneNumber))

                        val optionsLabelTextBuilder = options.labelTextBuilder.setTexts(umbrellaItem.name, umbrellaItem.phoneNumber)
                        options.setTexts(optionsLabelTextBuilder)

                        optionsLabelTextBuilder.addTextLine(umbrellaItem.name,0)
                        optionsLabelTextBuilder.addTextLine(umbrellaItem.phoneNumber,1)

                        val layer = kakaoMap.labelManager?.layer
                        layer?.addLabel(options)
                    }

                    kakaoMap.setOnLabelClickListener { kakaoMap, labelLayer, label ->

                        Log.e("label", label.texts[0])

                        val bottomSheet = MapLabelInfoLayoutBinding.inflate(layoutInflater,null,false)
                        val bottomSheetDialog = BottomSheetDialog(mContext)
                        bottomSheetDialog.setContentView(bottomSheet.root)
                        bottomSheet.data = umbrellaItemList?.find { it.name == label.texts[0] }

                        bottomSheetDialog.show()

                        true
                    }


                }


            }
            else {


            }

        }
        catch (e: Exception) {
            Log.e("getUmbrellaError",e.message.toString())

        }


    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mContext = context
    }

}
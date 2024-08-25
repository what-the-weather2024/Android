package com.wtw.whattheweather.ui.upload

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.wtw.whattheweather.R
import com.wtw.whattheweather.databinding.FragmentPictureUploadBinding
import com.wtw.whattheweather.network.RetrofitBuilder.networkService
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import java.util.Date

class PictureUploadFragment : Fragment() {

    private lateinit var binding : FragmentPictureUploadBinding
    private lateinit var mContext : Context
    private val args : PictureUploadFragmentArgs by navArgs()

    lateinit var filePath: String
    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            val option = BitmapFactory.Options()
            option.inSampleSize = 10
            val bitmap = BitmapFactory.decodeFile(filePath, option)
            val file = File(filePath)



            binding.imageUploadCameraImg.setImageURI(file.toUri())
            binding.pictureUploadBtn.visibility = View.VISIBLE
            binding.imageUploadText.visibility = View.INVISIBLE

        }
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_picture_upload, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.pictureUploadBackBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.imageUploadFrame.setOnClickListener {

            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA), 1)
            } else {

                val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                val storageDir: File? = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

                val file = File.createTempFile(
                    "JPEG_${timeStamp}_",
                    ".jpg",
                    storageDir
                )

                filePath = file.absolutePath

                val photoURI: Uri = FileProvider.getUriForFile(
                    mContext,
                    "com.wtw.whattheweather.FileProvider",
                    file)

                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                resultLauncher.launch(intent)
            }

        }

        binding.pictureUploadBtn.setOnClickListener {

            binding.pictureUploadBtn.isEnabled = false

            lifecycleScope.launch {
                binding.uploadLoadingLottie.visibility = View.VISIBLE
                binding.uploadLoadingLottie.playAnimation()

                uploadImage(filePath, "test")
            }


        }
    }

    private fun compressImageFile(imageFile: File) : File {
        // 이미지 파일 압축 로직 구현
        // 파일 크기를 줄이기 위해 이미지 품질 조정 (예: JPEG 압축)

        val compressedFile = File(imageFile.parent, "compressed_" + imageFile.name)

        // Bitmap으로 이미지 파일을 로드하고, 압축
        val bitmap = BitmapFactory.decodeFile(imageFile.path)
        FileOutputStream(compressedFile).use { outStream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outStream)
        }

        return compressedFile

    }

    suspend fun uploadImage(filePath: String, description: String) {
        val file = File(filePath)

//        val compressImageFile = compressImageFile(file)
        val requestFile = RequestBody.create("image/jpeg".toMediaTypeOrNull(), file)

        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
        val address = args.pictureUploadArgument

        val addressList = address.split(" ")
        val city = RequestBody.create("text/plain".toMediaTypeOrNull(),addressList[1])
        val district = RequestBody.create("text/plain".toMediaTypeOrNull(),addressList[2])

//        val city = RequestBody.create("text/plain".toMediaTypeOrNull(), "서울시")
//        val district = RequestBody.create("text/plain".toMediaTypeOrNull(), "관악구")
        val neighborhood = RequestBody.create("text/plain".toMediaTypeOrNull(), "봉천동")
        val memberId = RequestBody.create("text/plain".toMediaTypeOrNull(), "1")

        try {
            val call = networkService.imageUpload(body, city,district,null)

            if(call.isSuccessful) {
                Log.e("success",call.body()?.weatherStatus.toString())
                Log.e("body",call.body().toString())
                binding.uploadLoadingLottie.cancelAnimation()
                binding.uploadLoadingLottie.visibility = View.INVISIBLE

                if(call.body()?.weatherStatus == "기타") {
                    Toast.makeText(mContext,"사진을 다시 찍어주세요",Toast.LENGTH_SHORT).show()
                    binding.uploadLoadingLottie.cancelAnimation()
                    binding.uploadLoadingLottie.visibility = View.INVISIBLE
                    binding.pictureUploadBtn.isEnabled = true
                }
                else {
                    call.body()?.let {
                        this@PictureUploadFragment.findNavController().navigate(
                            PictureUploadFragmentDirections.actionNavigationImageUploadToNavigationUploadResult(it)
                        )
                    }
                }


            }
            else {
                Log.e("failBody",call.body().toString())
                Log.e("failErrorBody",call.errorBody().toString())
                Toast.makeText(mContext,"사진을 다시 찍어주세요",Toast.LENGTH_SHORT).show()
                binding.uploadLoadingLottie.cancelAnimation()
                binding.uploadLoadingLottie.visibility = View.INVISIBLE
                binding.pictureUploadBtn.isEnabled = true
            }

        }
        catch (e: Exception) {
            Log.e("error",e.message.toString())
            binding.uploadLoadingLottie.cancelAnimation()
            binding.uploadLoadingLottie.visibility = View.INVISIBLE
            binding.pictureUploadBtn.isEnabled = true
        }


    }

    private val imageUploadActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {




    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mContext = context
    }
}
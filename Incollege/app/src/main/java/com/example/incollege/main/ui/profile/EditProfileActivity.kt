package com.example.incollege.main.ui.profile

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import com.example.incollege.R
import com.example.incollege.main.model.userdetail.UserDetailData
import com.example.incollege.main.model.userphoto.EditPhotoResponse
import com.example.incollege.main.network.api.ApiClient
import com.example.incollege.main.network.api.ApiInterface
import com.example.incollege.main.sharedpreferences.SessionManager
import com.example.incollege.main.ui.profile.achievement.AchievementActivity
import com.example.incollege.main.ui.profile.activities.ActivitiesActivity
import com.example.incollege.main.ui.profile.committee.CommitteeActivity
import com.example.incollege.main.ui.profile.description.DescriptionActivity
import com.example.incollege.main.ui.profile.documentation.DocumentationActivity
import com.example.incollege.main.ui.profile.member.MemberActivity
import com.example.incollege.main.ui.profile.socialmedia.SocialMediaActivity
import com.example.incollege.main.ui.profile.visimisi.VisionMissionActivity
import com.example.incollege.main.utils.FileUtil
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_edit_profile.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.*


class EditProfileActivity : AppCompatActivity(), View.OnClickListener {

    private var userDetailData: UserDetailData? = null
    private var loginData: HashMap<String, String>? = null
    private val mainViewModel: ProfileViewModel by viewModels()
    private var actualImage: File? = null
    private var compressedImage: File? = null

    private var userId: String? = null
    private var description: String? = null
    private var vission: String? = null
    private var mission: String? = null

    companion object {
        private const val IMAGE_PICK_CODE = 1;
        private const val PERMISSION_CODE = 1001;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        initiateData()
    }

    private fun initiateUI() {
        btn_desc_edit.setOnClickListener(this)
        btn_visi_edit.setOnClickListener(this)
        btn_pengurus_edit.setOnClickListener(this)
        btn_anggota_edit.setOnClickListener(this)
        btn_kegiatan_edit.setOnClickListener(this)
        btn_prestasi_edit.setOnClickListener(this)
        btn_dokumentasi_edit.setOnClickListener(this)
        btn_medsos_edit.setOnClickListener(this)
        btn_back_edit.setOnClickListener(this)
        btn_user_edit_photo.setOnClickListener(this)

        Picasso.with(this)
            .load(userDetailData?.photo)
            .fit()
            .centerCrop()
            .into(civ_user_photo_edit)
    }

    private fun initiateData() {
        loginData = SessionManager(this).getUserData()
        getUserDetail(loginData?.get("userid"))
    }

    private fun getUserDetail(userId: String?) {

        if (userId != null) {
            mainViewModel.setUserDetailData(userId)
        }

        mainViewModel.getUserDetailData().observe(this) {
            userDetailData = it
            this@EditProfileActivity.userId = userDetailData?.userId
            description = userDetailData?.deskripsi
            vission = userDetailData?.visi
            mission = userDetailData?.misi
            initiateUI()
        }

        mainViewModel.getErrorUserDetail().observe(this) {
            Toast.makeText(
                this,
                it,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_back_edit -> onBackPressed()
            R.id.btn_desc_edit -> startActivity(Intent(this, DescriptionActivity::class.java))
            R.id.btn_visi_edit -> startActivity(Intent(this, VisionMissionActivity::class.java))
            R.id.btn_pengurus_edit -> startActivity(Intent(this, CommitteeActivity::class.java))
            R.id.btn_anggota_edit -> startActivity(Intent(this, MemberActivity::class.java))
            R.id.btn_kegiatan_edit -> startActivity(Intent(this, ActivitiesActivity::class.java))
            R.id.btn_prestasi_edit -> startActivity(Intent(this, AchievementActivity::class.java))
            R.id.btn_dokumentasi_edit -> startActivity(
                Intent(
                    this,
                    DocumentationActivity::class.java
                )
            )
            R.id.btn_medsos_edit -> startActivity(Intent(this, SocialMediaActivity::class.java))
            R.id.btn_user_edit_photo -> {
                updatePhoto()
            }
        }
    }

    private fun updatePhoto() {
        checkPermissionReadExternal()
    }

    private fun checkPermissionReadExternal() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_DENIED
            ) {
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                requestPermissions(permissions, PERMISSION_CODE)
            } else {
                pickImageFromGallery()
            }
        } else {
            pickImageFromGallery()
        }
    }


    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGallery()
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun getRealPathFromURI(uri: Uri?): String? {
        val cursor: Cursor? = contentResolver.query(uri!!, null, null, null, null)
        cursor?.moveToFirst()
        val idx: Int? = cursor?.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
        return cursor?.getString(idx!!)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
//            val path: String? = data?.data?.path
            val path = getRealPathFromURI(data?.data)
            val file = File(path ?: "")
            val fileName = FileUtil.getFileName(this, data?.data)

            val image: MultipartBody.Part? = getMultiPart("photo", fileName, file)
            updateUserPhoto(image)


//            if (data == null) {
//                showError("Gagal mengambil gambar")
//                return
//            }
//            try {
//                actualImage = FileUtil.from(this, data.data)?.also {
//                    val path = it.path.toString()
//                    val image: MultipartBody.Part = path.getMultiPart("photo")
//                    updateUserPhoto(image)
//                }
//            } catch (e: IOException) {
//                showError("Gagal membaca gambar")
//                e.printStackTrace()
//            }
        }
    }

    private fun updateUserPhoto(image: MultipartBody.Part?) {
        val apiInterface: ApiInterface = ApiClient.instance
        val editPhotoCall = apiInterface.updateUserPhoto(
            userId,
            image,
            description,
            vission,
            mission
        )
        editPhotoCall.enqueue(object : Callback<EditPhotoResponse> {
            override fun onResponse(
                call: Call<EditPhotoResponse>,
                response: Response<EditPhotoResponse>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@EditProfileActivity,
                        response.body()?.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    response.body()
                } else {
                    response.errorBody()
                    Toast.makeText(
                        this@EditProfileActivity,
                        "Gagal",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<EditPhotoResponse>, t: Throwable) {
                Toast.makeText(this@EditProfileActivity, t.localizedMessage, Toast.LENGTH_SHORT)
                    .show()
            }

        })
    }

    private fun String.getMultiPart(desc: String): MultipartBody.Part {
        val file = File(this)
        val requestFile = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(desc, file.name, requestFile)
    }

    private fun getMultiPart(desc: String, fileName: String, file: File): MultipartBody.Part {
        val requestFile = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(desc, fileName, requestFile)
    }

//    private fun compressImage() {
//        actualImage?.let { imageFile ->
//            lifecycleScope.launch {
//                // Default compression
//                compressedImage = Compressor.compress(this@EditProfileActivity, imageFile)
//            }
//        } ?: showError("Please choose an image!")
//    }

//    private fun customCompressImage() {
//        actualImage?.let { imageFile ->
//            lifecycleScope.launch {
//                // Full custom
//                compressedImage = Compressor.compress(this@EditProfileActivity, imageFile) {
//                    resolution(1280, 720)
//                    quality(80)
//                    format(Bitmap.CompressFormat.WEBP)
//                    size(2_097_152) // 2 MB
//                }
//            }
//        } ?: showError("Please choose an image!")
//    }

    private fun showError(errorMessage: String) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }

}
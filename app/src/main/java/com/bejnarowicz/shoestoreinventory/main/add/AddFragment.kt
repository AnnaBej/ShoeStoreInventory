package com.bejnarowicz.shoestoreinventory.main.add

import android.app.Activity
import android.app.AlertDialog
import android.content.*
import android.content.Intent.ACTION_PICK
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bejnarowicz.shoestoreinventory.R
import com.bejnarowicz.shoestoreinventory.databinding.FragmentAddBinding
import com.bejnarowicz.shoestoreinventory.database.model.Shoe
import com.bejnarowicz.shoestoreinventory.viewmodel.ShoeViewModel
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*

class AddFragment : Fragment() {

    private lateinit var viewModel: ShoeViewModel

    private lateinit var binding: FragmentAddBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add, container, false)

        (requireActivity() as AppCompatActivity).supportActionBar?.show()


        viewModel = ViewModelProvider(this).get(ShoeViewModel::class.java)

        binding.apply {
            addFragment = this@AddFragment
            lifecycleOwner = viewLifecycleOwner
        }

        setSeekBar()

        return binding.root
    }


    fun insertDataToDatabase() {
        val name = binding.etName.text.toString()
        val brand = binding.etBrand.text.toString()
        val stock = binding.numberSeekBar.text.toString()
        val image = binding.ivPhoto.toString()
        val comment = binding.etComments.text.toString()

        if (inputCheck(name, brand, stock)) {
            //create shoe object
            val shoe = Shoe(0, name, brand, stock, image, comment)
            //add data
            viewModel.addShoe(shoe)
            Toast.makeText(requireContext(), "Successfully added", Toast.LENGTH_LONG).show()
            //navigate back
            findNavController().navigate(R.id.action_addFragment_to_mainViewFragment)
        } else {
            Toast.makeText(
                requireContext(),
                "Please fill out the required fields",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun inputCheck(name: String, brand: String, stock: String): Boolean {
        return !(TextUtils.isEmpty(name) && TextUtils.isEmpty(brand) && TextUtils.isEmpty(stock))
    }


    private fun setSeekBar() {
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.numberSeekBar.text = getString(R.string.seek_bar_result, progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
    }


    fun onCancel() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            findNavController().navigate(R.id.action_addFragment_to_mainViewFragment)
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.setTitle("Are you sure you want to cancel?")
        builder.setMessage("Your inputted data will be lost")
        builder.create().show()
    }


    //PHOTOS
    fun setImage() {

        val pictureDialog = AlertDialog.Builder(requireContext())
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf("Select photo from Gallery", "Capture photo from Camera")
        pictureDialog.setItems(pictureDialogItems) { _, which ->
            when (which) {
                0 -> gallery()
                1 -> camera()
            }
        }
        pictureDialog.show()
    }


    private fun gallery() {

        Dexter.withContext(requireContext()).withPermissions(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).withListener(object : MultiplePermissionsListener {

            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                if (report!!.areAllPermissionsGranted()) {

                    val galleryIntent = Intent(
                        ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    )
                    startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE)
                }
            }

            override fun onPermissionRationaleShouldBeShown(
                permissions: MutableList<PermissionRequest>,
                token: PermissionToken
            ) {
                showRationalDialogForPermissions()
            }
        }).onSameThread().check()

    }


    private fun camera() {
        Dexter.withContext(requireContext()).withPermissions(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA
        ).withListener(object : MultiplePermissionsListener {

            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                if (report!!.areAllPermissionsGranted()) {
                    val galleryIntent = Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE
                    )
                    startActivityForResult(galleryIntent, CAMERA_REQUEST_CODE)
                }
            }

            override fun onPermissionRationaleShouldBeShown(
                permissions: MutableList<PermissionRequest>,
                token: PermissionToken
            ) {
                showRationalDialogForPermissions()
            }
        }).onSameThread().check()
    }


    private fun showRationalDialogForPermissions() {
        AlertDialog.Builder(requireContext())
            .setMessage("Please, turn on the permissions in settings to use this functionality")
            .setPositiveButton("Go to settings") { _, _ ->
                try { //trying to send the user to settings when he/she could change the settings
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri: Uri = Uri.fromParts(
                        "package",
                        packageName,//TODO packageName is unknown- doesn't work
                        null
                    )
                    intent.data = uri
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, CAMERA_REQUEST_CODE)
            }
        } else {
            Toast.makeText(
                requireContext(),
                "Oops you have denied the permission for camera. You can change it in settings",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == Activity.RESULT_OK && requestCode == CAMERA_REQUEST_CODE) {
            val bitmap: Bitmap = data?.extras?.get("data") as Bitmap

            saveToInternalStorage(bitmap)
            binding.ivPhoto.setImageBitmap(bitmap)

        } else if (requestCode == Activity.RESULT_OK && requestCode == GALLERY_REQUEST_CODE) {

            if (data != null) {
                val contentUri = data.data
                try {
                    val selectedImageBitmap = MediaStore.Images.Media.getBitmap(
                        requireContext().contentResolver,
                        contentUri
                    )
                    saveToInternalStorage(selectedImageBitmap)
                    binding.ivPhoto.setImageBitmap(selectedImageBitmap)
                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(requireContext(), "Something went wrong :(", Toast.LENGTH_SHORT)
                        .show()
                }
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun saveToInternalStorage(bitmap: Bitmap): Uri {
        val wrapper = ContextWrapper(requireContext())
        var file = wrapper.getDir(IMAGE_DIRECTORY, Context.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jpg")

        try {
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return Uri.parse(file.absolutePath)
    }

    companion object {
        private const val CAMERA_PERMISSION_CODE = 1
        private const val CAMERA_REQUEST_CODE = 2
        private const val GALLERY_REQUEST_CODE = 3
        private const val IMAGE_DIRECTORY = "ShoeInventoryImages"

    }

}
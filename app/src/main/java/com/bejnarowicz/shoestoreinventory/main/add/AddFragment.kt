package com.bejnarowicz.shoestoreinventory.main.add

import android.app.Activity
import android.app.AlertDialog
import android.content.*
import android.content.Intent.ACTION_PICK
import android.os.Bundle
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
import kotlinx.android.synthetic.main.fragment_splash.*
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
            shoe = Shoe(0, "", "", "0/100", "", "")
            lifecycleOwner = viewLifecycleOwner
        }

        setSeekBar()

        return binding.root
    }


    fun insertDataToDatabase() {

        val shoe = binding.shoe!!

        viewModel.addShoe(shoe)
        Toast.makeText(requireContext(), "Successfully added", Toast.LENGTH_LONG).show()
        findNavController().navigate(R.id.action_addFragment_to_mainViewFragment)

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


    fun openGalleryForImage() {
        val intent = Intent(ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == GALLERY_REQUEST_CODE) {
            binding.ivPhoto.setImageURI(data?.data)
            binding.shoe?.photo = data?.data?.toString() ?: ""
        }
    }

    companion object {
        private const val GALLERY_REQUEST_CODE = 3
    }

}


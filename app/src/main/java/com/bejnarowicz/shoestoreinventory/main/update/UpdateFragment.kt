package com.bejnarowicz.shoestoreinventory.main.update

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bejnarowicz.shoestoreinventory.R
import com.bejnarowicz.shoestoreinventory.database.model.Shoe
import com.bejnarowicz.shoestoreinventory.databinding.FragmentUpdateBinding
import com.bejnarowicz.shoestoreinventory.viewmodel.ShoeViewModel

class UpdateFragment : Fragment() {

    private val args by navArgs<UpdateFragmentArgs>()
    private lateinit var viewModel: ShoeViewModel

    private lateinit var binding: FragmentUpdateBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_update, container, false)

        viewModel = ViewModelProvider(this).get(ShoeViewModel::class.java)

        binding.apply {
            updateFragment = this@UpdateFragment
            lifecycleOwner = viewLifecycleOwner
            shoe = Shoe(
                0,
                args.currentShoe.name,
                args.currentShoe.brand,
                args.currentShoe.stock,
                args.currentShoe.photo,
                args.currentShoe.comment
            )
        }

        setSeekBar()

        setHasOptionsMenu(true)

        return binding.root
    }

    fun updateItem() {

        val shoe = binding.shoe!!

        viewModel.updateShoe(shoe)
        Toast.makeText(requireContext(), "Shoe listing updated", Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.action_updateFragment_to_mainViewFragment)

    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_delete) {
            deleteShoe()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteShoe() {

        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            viewModel.deleteShoe(args.currentShoe)
            Toast.makeText(requireContext(), "Successfully deleted", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateFragment_to_mainViewFragment)
        }
        builder.setNegativeButton("No") { _, _ -> }

        builder.setTitle("Delete ${args.currentShoe.name}?")
        builder.setMessage("Are you sure you want to delete ${args.currentShoe.name}?")
        builder.create().show()
    }

    private fun setSeekBar() {
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

                binding.numberUpdateSeekBar.text = getString(R.string.seek_bar_result, progress)

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
            findNavController().navigate(R.id.action_updateFragment_to_mainViewFragment)
        }
        builder.setNegativeButton("No") { _, _ -> }

        builder.setTitle("Are yoy sure you want to cancel?")
        builder.setMessage("Your inputted data will be lost")
        builder.create().show()
    }

    fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == GALLERY_REQUEST_CODE) {

            binding.ivPhoto.setImageURI(data?.data)
            binding.shoe?.photo = data?.data?.toString()
        }
    }

    companion object {
        private const val GALLERY_REQUEST_CODE = 3
    }


}
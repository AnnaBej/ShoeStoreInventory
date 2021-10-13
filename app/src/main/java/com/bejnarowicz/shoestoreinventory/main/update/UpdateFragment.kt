package com.bejnarowicz.shoestoreinventory.main.update

import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
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
        }

        setSeekBar()

        binding.etName.setText(args.currentShoe.name)
        binding.etBrand.setText(args.currentShoe.brand)
        binding.numberUpdateSeekBar.text = args.currentShoe.stock
        binding.ivPhoto.setImageURI(Uri.parse(args.currentShoe.photo))
        binding.etComments.setText(args.currentShoe.comment).toString()


        setHasOptionsMenu(true)

        return binding.root
    }

    fun updateItem() {
        val name = binding.etName.text.toString()
        val brand = binding.etBrand.text.toString()
        val stock = binding.numberUpdateSeekBar.text.toString()
        val photo = binding.ivPhoto.toString()
        val comment = binding.etComments.text.toString()

        if (inputCheck(name, brand, stock)) {
            val updatedShoe = Shoe(args.currentShoe.id, name, brand, stock, photo, comment)
            viewModel.updateShoe(updatedShoe)
            Toast.makeText(requireContext(), "Shoe listing updated", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateFragment_to_mainViewFragment)
        } else {
            Toast.makeText(
                requireContext(),
                "Please filled out the required fields",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun inputCheck(name: String, brand: String, stock: String): Boolean {
        return !(TextUtils.isEmpty(name) && TextUtils.isEmpty(brand) && TextUtils.isEmpty(stock))
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
               // binding.seekBar.progress = progress //TODO doesn't work
                //I would like to have the seek bar adjusted to the number

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


}
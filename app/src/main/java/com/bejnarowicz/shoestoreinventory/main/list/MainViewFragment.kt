package com.bejnarowicz.shoestoreinventory.main.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bejnarowicz.shoestoreinventory.R
import com.bejnarowicz.shoestoreinventory.databinding.FragmentMainViewBinding
import com.bejnarowicz.shoestoreinventory.viewmodel.ShoeViewModel

class MainViewFragment : Fragment() {

    private lateinit var mShoeViewModel: ShoeViewModel

    private lateinit var binding: FragmentMainViewBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_view, container, false)

        (requireActivity() as AppCompatActivity).supportActionBar?.show()

        val adapter = MainViewAdapter()
        val recyclerView = binding.recyclerViewList
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        mShoeViewModel = ViewModelProvider(this).get(ShoeViewModel::class.java)
        mShoeViewModel.getAllShoes.observe(viewLifecycleOwner, { shoe ->
            adapter.submitList(shoe)

            if (shoe.isEmpty()) {
                binding.recyclerViewList.visibility = View.GONE
                binding.tvNoRecords.visibility = View.VISIBLE
            } else {
                binding.recyclerViewList.visibility = View.VISIBLE
                binding.tvNoRecords.visibility = View.GONE
            }
        })

        binding.fabAdd.setOnClickListener {
            findNavController().navigate(R.id.action_mainViewFragment_to_addFragment)
        }

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.log_out, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.delete_all) {
            deleteAll()
        }
        if (item.itemId == R.id.log_out) {
            findNavController().navigate(R.id.action_mainViewFragment_to_loginFragment)
        }

        return super.onOptionsItemSelected(item)
    }

    private fun deleteAll() {

        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            mShoeViewModel.deleteAll()
            Toast.makeText(requireContext(), "Successfully deleted", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("No") { _, _ -> }

        builder.setTitle("Delete all inventory?")
        builder.setMessage("Are you sure you want to delete everything?")
        builder.create().show()
    }

}
package com.yannis.mayalisten.fragment

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.yannis.mayalisten.adapter.SingleAlbumItemAdapter
import com.yannis.mayalisten.databinding.SingleAlbumContentFragmentBinding
import com.yannis.mayalisten.view_mode.SingleAlbumContentViewModel

class SingleAlbumContentFragment : Fragment() {

    private lateinit var binding: SingleAlbumContentFragmentBinding
    private var albumId: Int = 0
    private var trackId: Int = 0
    private var asc: Boolean = false
    private lateinit var viewModel: SingleAlbumContentViewModel
    private lateinit var singleAlbumItemAdapter: SingleAlbumItemAdapter

    companion object {
        fun newInstance(albumId: Int, asc: Boolean, trackId: Int): SingleAlbumContentFragment {
            val args = Bundle()
            args.putInt("albumId", albumId)
            args.putBoolean("asc", asc)
            args.putInt("trackId", trackId)
            val fragment = SingleAlbumContentFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SingleAlbumContentFragmentBinding.inflate(inflater, container, false)
        Log.e(TAG, "onCreateView: ")
        arguments?.let {
            albumId = it.getInt("albumId")
            trackId = it.getInt("trackId")
            asc = it.getBoolean("asc")
        }
        binding.apply {
            singleAlbumItemAdapter = SingleAlbumItemAdapter(null)
            recyclerView.adapter = singleAlbumItemAdapter
            recyclerView.layoutManager =
                LinearLayoutManager(this@SingleAlbumContentFragment.context)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e(TAG, "onViewCreated: ")
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.e(TAG, "onActivityCreated: ")
        viewModel = ViewModelProvider(this)[SingleAlbumContentViewModel::class.java]
        viewModel.getSingleAlbumContent(albumId, true, trackId)
            .observe(viewLifecycleOwner, Observer {
                binding.tvCountTotal.text = "共${it.album.tracks.toString()}集"
                singleAlbumItemAdapter.setNewData(it.tracks.list)
            })
    }

}
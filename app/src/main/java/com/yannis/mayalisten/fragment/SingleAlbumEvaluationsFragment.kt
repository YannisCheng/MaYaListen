package com.yannis.mayalisten.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.yannis.mayalisten.adapter.SingleAlbumEvaluationAdapter
import com.yannis.mayalisten.databinding.FragmentSingleAlbumEvaluationsBinding
import com.yannis.mayalisten.view_mode.SingleAlbumEvaluationsMV


private const val ALBUM_ID = "albumId"

class SingleAlbumEvaluationsFragment : Fragment() {

    private var param1: Int? = 0
    private lateinit var evaluationsMV: SingleAlbumEvaluationsMV
    private lateinit var evaluationAdapter: SingleAlbumEvaluationAdapter

    companion object {
        @JvmStatic
        fun newInstance(albumId: Int) =
            SingleAlbumEvaluationsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ALBUM_ID, albumId)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getInt(ALBUM_ID, 0)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentSingleAlbumEvaluationsBinding.inflate(inflater, container, false)
        evaluationsMV = ViewModelProvider(this)[SingleAlbumEvaluationsMV::class.java]
        evaluationAdapter = SingleAlbumEvaluationAdapter(null)
        binding.apply {
            recyclerView.layoutManager =
                LinearLayoutManager(this@SingleAlbumEvaluationsFragment.context)
            recyclerView.adapter = evaluationAdapter
        }

        param1?.let {
            evaluationsMV.getSingleAlbumEvaluations(it).observe(viewLifecycleOwner, Observer {
                evaluationAdapter.setNewData(it.comments.list)
            })
        }
        return binding.root
    }


}
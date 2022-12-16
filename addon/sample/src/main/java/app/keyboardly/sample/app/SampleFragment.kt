package app.keyboardly.sample.app

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.keyboardly.sample.databinding.FragmentSampleBinding

class SampleFragment : Fragment() {

    private lateinit var binding: FragmentSampleBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSampleBinding.inflate(inflater)
        return binding.root
    }
}
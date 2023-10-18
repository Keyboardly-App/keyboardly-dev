package app.keyboardly.addon.sample.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.keyboardly.addon.sample.databinding.SampleFragmentHomeBinding


class HomeSampleFragment : Fragment() {

    private var binding: SampleFragmentHomeBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SampleFragmentHomeBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding!!.root
    }
}
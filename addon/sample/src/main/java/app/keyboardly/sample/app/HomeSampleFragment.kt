package app.keyboardly.sample.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.keyboardly.sample.databinding.FragmentHomeSampleBinding


class HomeSampleFragment : Fragment() {

    private var binding: FragmentHomeSampleBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeSampleBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding!!.root
    }
}
package app.keyboardly.addon.sample.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import app.keyboardly.addon.sample.databinding.SampleFragmentSampleBinding

class SampleFragment : Fragment() {

    private lateinit var binding: SampleFragmentSampleBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = SampleFragmentSampleBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            listMenu.apply {
                layoutManager = LinearLayoutManager(requireActivity())
                val listSubmenu = mutableListOf<SubMenuModel>()
                listSubmenu.add(SubMenuModel("Home"))
                listSubmenu.add(SubMenuModel("Dashboard"))
                listSubmenu.add(SubMenuModel("Shop"))
                listSubmenu.add(SubMenuModel("Profile"))
                adapter = SubMenuAdapter(listSubmenu){
                    if (it==0){
                        findNavController().navigate(
                            SampleFragmentDirections.defaultToHome()
                        )
                    }
                }
            }
        }
    }
}
package com.example.mvvmdemo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.mvvmdemo.databinding.FragmentFirstBinding
import com.example.mvvmdemo.expand.bindView
import com.example.mvvmdemo.socket.SocketClientActivity
import com.example.mvvmdemo.socket.SocketServerActivity
import com.example.mvvmdemo.utils.startActivity

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    // private val binding by bindView<FragmentFirstBinding>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding?.run {
            buttonFirst.setOnClickListener {
                findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
            }
            btnClient.setOnClickListener {
                startActivity<SocketClientActivity>()
            }
            btnServer.setOnClickListener {
                startActivity<SocketServerActivity>()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
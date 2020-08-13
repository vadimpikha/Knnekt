package knnekt.presentation.ui.main.chats.messages

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import knnekt.R
import knnekt.databinding.FragmentChatBinding
import knnekt.presentation.util.viewBinding

class ChatFragment : Fragment(R.layout.fragment_chat) {

    private val args: ChatFragmentArgs by navArgs()
    private val binding by viewBinding(FragmentChatBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            lifecycleOwner = viewLifecycleOwner
        }
        binding.text.text = args.id
    }

}

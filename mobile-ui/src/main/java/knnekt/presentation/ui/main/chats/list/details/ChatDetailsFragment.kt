package knnekt.presentation.ui.main.chats.list.details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import knnekt.R
import knnekt.databinding.FragmentChatDetailsBinding
import knnekt.presentation.chats.details.ChatDetailsViewModel
import knnekt.presentation.messages.ChatDetailsViewModelFactory
import knnekt.presentation.util.viewBinding
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.x.closestDI
import org.kodein.di.direct

class ChatDetailsFragment : Fragment(R.layout.fragment_chat_details), DIAware {

    override val di: DI by closestDI()
    private val args: ChatDetailsFragmentArgs by navArgs()
    private val chatDetailsViewModel: ChatDetailsViewModel by viewModels {
        ChatDetailsViewModelFactory(args.chatId, di.direct)
    }
    private val binding by viewBinding(FragmentChatDetailsBinding::bind)

    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        with(binding) {
            lifecycleOwner = viewLifecycleOwner
            viewModel = chatDetailsViewModel
        }
        binding.toolbar.setupWithNavController(navController)
    }

}
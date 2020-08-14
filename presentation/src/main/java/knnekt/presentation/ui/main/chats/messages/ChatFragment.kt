package knnekt.presentation.ui.main.chats.messages

import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import androidx.transition.TransitionInflater
import knnekt.R
import knnekt.databinding.FragmentChatBinding
import knnekt.presentation.di.viewModelInstance
import knnekt.presentation.lifecycle.observeEvent
import knnekt.presentation.util.setActionBar
import knnekt.presentation.util.toast
import knnekt.presentation.util.viewBinding
import kotlinx.android.synthetic.main.fragment_chats_list.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein

class ChatFragment : Fragment(R.layout.fragment_chat), KodeinAware {

    override val kodein by closestKodein()
    private val viewModel: ChatViewModel by viewModelInstance()
    private val args: ChatFragmentArgs by navArgs()
    private val binding by viewBinding(FragmentChatBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null)
            viewModel.initChat(args.id)

        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setActionBar(toolbar)
        toolbar.setupWithNavController(findNavController())
        with(binding) {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@ChatFragment.viewModel
        }
        initViews()
        bindLiveData()
    }

    private fun initViews() {
        ViewCompat.setTransitionName(binding.chatPhoto, "avatar_${args.id}")
    }

    private fun bindLiveData() {
        viewModel.toast.observeEvent(viewLifecycleOwner) {
            toast(it)
        }
    }

}

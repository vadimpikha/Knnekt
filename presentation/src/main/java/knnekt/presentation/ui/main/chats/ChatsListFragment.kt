package knnekt.presentation.ui.main.chats

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import knnekt.R
import knnekt.databinding.FragmentChatsListBinding
import knnekt.presentation.di.viewModelInstance
import knnekt.presentation.lifecycle.observeEvent
import knnekt.presentation.util.toast
import knnekt.presentation.util.viewBinding
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein

class ChatsListFragment : Fragment(R.layout.fragment_chats_list), KodeinAware {

    override val kodein by closestKodein()

    private val viewModel: ChatsListViewModel by viewModelInstance()
    private val binding by viewBinding(FragmentChatsListBinding::bind)
    private val chatsAdapter by lazy { ChatsAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            lifecycleOwner = viewLifecycleOwner
        }
        binding.chatsRecycler.adapter = chatsAdapter
        onBindLiveData()
    }

    private fun onBindLiveData() {
        viewModel.chats.observe(viewLifecycleOwner) { chats ->
            chatsAdapter.chats = chats
        }
        viewModel.toastEvent.observeEvent(viewLifecycleOwner) { text ->
            toast(text)
        }
    }


}
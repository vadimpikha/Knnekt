package blasty.presentation.main.chats

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import blasty.R
import blasty.databinding.FragmentChatsListBinding
import blasty.di.viewModelInstance
import blasty.lifecycle.observeEvent
import blasty.utils.toast
import blasty.utils.viewBinding
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
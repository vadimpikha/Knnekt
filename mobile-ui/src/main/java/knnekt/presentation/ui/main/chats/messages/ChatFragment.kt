package knnekt.presentation.ui.main.chats.messages

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import knnekt.R
import knnekt.databinding.FragmentChatBinding
import knnekt.domain.entity.Message
import knnekt.presentation.messages.ChatMessagesViewModel
import knnekt.presentation.ui.HoldListener
import knnekt.presentation.ui.widget.JumpSmoothScroller
import knnekt.presentation.ui.widget.MarginItemDecoration
import knnekt.presentation.messages.ChatMessagesViewModelFactory
import knnekt.presentation.messages.MessageSenderViewModel
import knnekt.presentation.messages.MessageSenderViewModelFactory
import knnekt.presentation.ui.main.chats.list.details.ChatDetailsFragment
import knnekt.presentation.ui.setOnHoldListener
import knnekt.presentation.ui.widget.HolderPrefetcher
import knnekt.presentation.ui.widget.PrefetchRecycledViewPool
import knnekt.presentation.util.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import org.kodein.di.DIAware
import org.kodein.di.android.x.closestDI
import org.kodein.di.direct

@ExperimentalCoroutinesApi
class ChatFragment : Fragment(R.layout.fragment_chat), DIAware {

    override val di by closestDI()
    private val args: ChatFragmentArgs by navArgs()
    private val chatViewModel: ChatMessagesViewModel by viewModels {
        ChatMessagesViewModelFactory(args.chatId, di.direct)
    }
    private val senderViewModel: MessageSenderViewModel by viewModels {
        MessageSenderViewModelFactory(args.chatId, di.direct)
    }
    private val binding by viewBinding(FragmentChatBinding::bind)

    private val prefetchScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    private lateinit var messagesAdapter: ChatMessagesAdapter
    private lateinit var navController: NavController
    private lateinit var chatRecyclerLayoutManager: LinearLayoutManager
    private lateinit var viewPool: PrefetchRecycledViewPool


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initChatAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            lifecycleOwner = viewLifecycleOwner
            viewModel = chatViewModel
            with(messagePad) {
                viewModel = senderViewModel
                lifecycleOwner = viewLifecycleOwner
            }
        }

        viewPool = PrefetchRecycledViewPool(
            view.context,
            prefetchScope
        ).apply {
            prepare()
        }

        navController = findNavController()
        initViews()
        bindData()
    }

    override fun onStop() {
        super.onStop()
        if (isRemoving)
            hideKeyboard(binding.messagePad.messageInput)
    }

    private fun initChatAdapter() {
        messagesAdapter = ChatMessagesAdapter()
        messagesAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                if (positionStart == 0 && isAutoScrollNeeded()) {
                    scrollTo(0)
                }
            }
        })
    }

    private fun isAutoScrollNeeded(): Boolean {
        return senderViewModel.messageJustSent || (chatViewModel.messageJustReceived && isStartOfList())
    }

    private fun isStartOfList(): Boolean {
        return chatRecyclerLayoutManager.findFirstVisibleItemPosition() == 0
    }

    private fun scrollTo(position: Int) {
        val scroller = JumpSmoothScroller(requireContext()).apply {
            targetPosition = position
        }
        chatRecyclerLayoutManager.startSmoothScroll(scroller)
    }

    private fun initViews() {
        chatRecyclerLayoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            true
        )

        binding.messagesRecycler.apply {
            layoutManager = chatRecyclerLayoutManager
            adapter = messagesAdapter
            addItemDecoration(MarginItemDecoration(requireContext(), 8, true))
            setRecycledViewPool(viewPool)
            prefetchItems(viewPool)
        }

        with(binding.toolbar) {
            setNavigationOnClickListener {
                navController.navigateUp()
            }
            setOnClickListener {
                val action =
                    ChatFragmentDirections.actionChatFragmentToChatDetailsFragment(args.chatId)
                navController.navigate(action)
            }
        }

        with(binding.messagePad) {
            btnRecordVideoMsg.apply {
                setOnClickListener { swapSecondaryInputButtons() }
                setOnHoldListener(object : HoldListener {
                    override fun onHold(view: View) {
                        toast("btn_record_video_msg hold")
                    }

                    override fun onReleased(view: View) {
                        toast("btn_record_video_msg release")
                    }
                })
            }


            btnRecordVoiceMsg.apply {
                setOnClickListener { swapSecondaryInputButtons() }
                setOnHoldListener(object : HoldListener {
                    override fun onHold(view: View) {
                        toast("btn_record_voice_msg hold")
                    }

                    override fun onReleased(view: View) {
                        toast("btn_record_voice_msg release")
                    }
                })
            }
        }
    }

    private fun prefetchItems(holderPrefetcher: HolderPrefetcher) {
        val count = 20
        holderPrefetcher.setViewsCount(
            R.layout.item_message_simple_incoming,
            count
        ) { fakeParent, viewType ->
            ChatMessagesAdapter.ChatMessageViewHolder.Factory(fakeParent, viewType)
        }
        holderPrefetcher.setViewsCount(
            R.layout.item_message_simple_ougoing,
            count
        ) { fakeParent, viewType ->
            ChatMessagesAdapter.ChatMessageViewHolder.Factory(fakeParent, viewType)
        }
    }

    private fun swapSecondaryInputButtons() {
        with(binding.messagePad) {
            val translationXTmp = btnRecordVideoMsg.translationX

            btnRecordVideoMsg.animate()
                .translationX(btnRecordVoiceMsg.translationX)
                .disableWhileAnimation(btnRecordVideoMsg)


            btnRecordVoiceMsg.animate()
                .translationX(translationXTmp)
                .disableWhileAnimation(btnRecordVoiceMsg)
        }
    }

    private fun bindData() {
        chatViewModel.scrollToEvent.observeEvent(viewLifecycleOwner, this::scrollTo)

        chatViewModel.toast.observeEvent(viewLifecycleOwner) {
            toast(it)
        }
        viewLifecycleOwner.lifecycleScope.launch {
            chatViewModel.messagesPagingData.collectLatest { data ->
                messagesAdapter.submitData(data)
            }
        }
    }

    override fun onDestroyView() {
        viewPool.clear()
        super.onDestroyView()
    }

}

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
import knnekt.presentation.lifecycle.observeEvent
import knnekt.presentation.ui.HoldListener
import knnekt.presentation.ui.setOnHoldListener
import knnekt.presentation.ui.widget.JumpSmoothScroller
import knnekt.presentation.ui.widget.MarginItemDecorator
import knnekt.presentation.util.disableWhileAnimation
import knnekt.presentation.util.hideKeyboard
import knnekt.presentation.util.toast
import knnekt.presentation.util.viewBinding
import knnekt.presentation.viewmodelfactory.ChatViewModelFactory
import knnekt.presentation.viewmodelfactory.MessageSenderViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.direct

class ChatFragment : Fragment(R.layout.fragment_chat), KodeinAware {

    override val kodein by closestKodein()
    private val args: ChatFragmentArgs by navArgs()
    private val chatViewModel: ChatViewModel by viewModels {
        ChatViewModelFactory(args.chat, kodein.direct)
    }
    private val senderViewModel: MessageSenderViewModel by viewModels {
        MessageSenderViewModelFactory(args.chat, kodein.direct)
    }
    private val binding by viewBinding(FragmentChatBinding::bind)
    private lateinit var messagesAdapter: ChatMessagesAdapter
    private lateinit var navController: NavController
    private lateinit var scroller: JumpSmoothScroller
    private lateinit var chatRecyclerLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scroller = JumpSmoothScroller(requireContext())
        initChatAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            lifecycleOwner = viewLifecycleOwner
            viewModel = chatViewModel
            chat = args.chat
            messagePad.viewModel = senderViewModel
            messagePad.lifecycleOwner = viewLifecycleOwner
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
        chatRecyclerLayoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            true
        )

        messagesAdapter = ChatMessagesAdapter()
        messagesAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                if (positionStart == 0 && senderViewModel.scrollNeeded) {
                    scrollTo(0)
                }
            }
        })
    }

    private fun scrollTo(position: Int) {
        scroller.targetPosition = position
        chatRecyclerLayoutManager.startSmoothScroll(scroller)
    }

    private fun initViews() {
        binding.messagesRecycler.apply {
            layoutManager = chatRecyclerLayoutManager
            adapter = messagesAdapter
            addItemDecoration(MarginItemDecorator(requireContext(), 8, true))
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val totalItemCount = chatRecyclerLayoutManager.itemCount
                    val firstVisible = chatRecyclerLayoutManager.findFirstVisibleItemPosition()

                    var shouldShow = firstVisible >= 1
                    if (dy < 0) {
//                    onScrolled Upwards
                    } else if (dy > 0) {
//                    onScrolled Downwards
                        shouldShow = false
                    }

                    if (totalItemCount > 0 && shouldShow) {
                        if (!binding.scrollDownFb.isShown) {
                            binding.scrollDownFb.show()
                        }
                    } else {
                        if (binding.scrollDownFb.isShown) binding.scrollDownFb.hide()
                    }
                }
            })
        }

        binding.toolbar.setNavigationOnClickListener {
            navController.navigateUp()
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
        lifecycleScope.launch {
            chatViewModel.messagesPagingData.collectLatest { data ->
                messagesAdapter.submitData(data)
            }
        }
    }

}

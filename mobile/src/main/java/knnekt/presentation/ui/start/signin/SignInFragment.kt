package knnekt.presentation.ui.start.signin

import android.os.Bundle
import android.text.style.UnderlineSpan
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import knnekt.R
import knnekt.databinding.FragmentSignInBinding
import knnekt.presentation.di.activityViewModelInstance
import knnekt.presentation.lifecycle.observeEvent
import knnekt.presentation.ui.main.MainActivity
import knnekt.presentation.util.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein

class SignInFragment : Fragment(R.layout.fragment_sign_in), KodeinAware {

    override val kodein by closestKodein()
    private val viewModel: SignInViewModel by activityViewModelInstance()
    private val binding by viewBinding(FragmentSignInBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            viewModel = this@SignInFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        initViews()
        onBindLiveData()
    }

    private fun initViews() {
        binding.password.onActionDone {
            viewModel.signIn()
        }
        binding.forgotPasswordLink.setOnClickListener {
            toast("This feature not implemented yet")
        }
        binding.signUpLink.onClick(true) {
            findNavController().navigate(R.id.action_signIn_to_registrationFragment)
        }
        binding.forgotPasswordLink.applySpan(UnderlineSpan())
        binding.signUpLink.applySpan(UnderlineSpan())
    }

    private fun onBindLiveData() {
        viewModel.userSignedInEvent.observeEvent(viewLifecycleOwner) {
            startActivity(MainActivity.intent(requireContext()))
            activity?.finish()
        }
        viewModel.toastEvent.observeEvent(viewLifecycleOwner) { text ->
            toast(text)
        }
    }

}
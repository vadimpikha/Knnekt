package knnekt.presentation.ui.start

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import knnekt.R
import knnekt.databinding.FragmentSignInBinding
import knnekt.presentation.di.activityViewModelInstance
import knnekt.presentation.lifecycle.observeEvent
import knnekt.presentation.ui.MainActivity
import knnekt.presentation.util.toast
import knnekt.presentation.util.viewBinding
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein

class SignInFragment : Fragment(R.layout.fragment_sign_in), KodeinAware {

    override val kodein by closestKodein()

    private val viewModel: AuthViewModel by activityViewModelInstance()
    private val binding by viewBinding(FragmentSignInBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            viewModel = this@SignInFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        onBindLiveData()
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
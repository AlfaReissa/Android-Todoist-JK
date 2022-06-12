package com.readthym.doesapp.ui.auth

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.henrylabs.qumparan.data.remote.QumparanResource
import com.readthym.doesapp.utils.base.BaseFragment
import com.readthym.doesapp.R
import com.readthym.doesapp.data.local.MyPreference
import com.readthym.doesapp.data.remote.reqres.ReadthymLoginResponse
import com.readthym.doesapp.databinding.FragmentReadtyhmLoginBinding
import com.readthym.doesapp.ui.home.HomeActivity
import org.koin.android.viewmodel.ext.android.viewModel


class ReadthymLoginFragment : BaseFragment() {

    private val binding get() = _binding!!
    private var _binding: FragmentReadtyhmLoginBinding? = null

    val viewModel: AuthViewModel by viewModel()

    override fun initUI() {
        setupFormListener()

        if (MyPreference(requireContext()).getUserID().isNullOrEmpty().not()) {
            proceedToNextActivity()
            showToast(getString(R.string.u_login_with) + MyPreference(requireContext()).getUserID())
        }
    }

    private fun setupFormListener() {
        binding.textFieldEmail.editText?.apply {
            doAfterTextChanged {
                if (text.toString().isNotEmpty()) {
                    binding.textFieldEmail.error = null
                }
            }
        }

        binding.textFieldPassword.editText?.apply {
            doAfterTextChanged {
                if (text.toString().isNotEmpty()) {
                    binding.textFieldPassword.error = null
                }
            }
        }
    }

    fun validateInput() {
        val email = binding.textFieldEmail.editText?.text
        val password = binding.textFieldPassword.editText?.text
        var isError = false

        if (isValidEmail(email).not()) {
            isError = true
            binding.textFieldEmail.error = getString(R.string.email_format_error)
        }

        if (password != null) {
            if (password.length < 8) {
                isError = true
                binding.textFieldPassword.error = getString(R.string.minimal_8_karakter)
            }
        }

        if (password.isNullOrEmpty()) {
            isError = true
            binding.textFieldPassword.error = getString(R.string.column_required)
        }

        if (!isError) {
            proceedLogin(
                email = email.toString(),
                password = password.toString()
            )
        }
    }

    fun isValidEmail(target: CharSequence?): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    override fun initObserver() {
        viewModel.loginLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is QumparanResource.Default -> {
                    showLoading(false)
                }
                is QumparanResource.Error -> {
                    showLoading(false)
                    showToast(it.message.toString())
                }
                is QumparanResource.Loading -> {
                    showLoading(true)
                }
                is QumparanResource.Success -> {
                    showLoading(false)
                    showToast(it.message.toString())
                    it.data?.let {
                        proceedLoginSuccess(it)
                    }
                }
            }
        })
    }

    private fun proceedLoginSuccess(it: ReadthymLoginResponse) {
        saveLoginData(it)
        proceedToNextActivity()
    }

    private fun proceedToNextActivity() {
        startActivity(Intent(requireActivity(), HomeActivity::class.java))
    }

    private fun saveLoginData(it: ReadthymLoginResponse) {
        MyPreference(requireContext()).apply {
            val user = it.resData
            saveUserID(user.id.toString())
            saveUserEmail(user.email)
            saveUserName(user.name)
        }
    }

    private fun showLoading(b: Boolean) {
        if (b) {
            binding.btnLogin.makeGone()
            binding.loadingLogin.makeVisible()
        } else {
            binding.btnLogin.makeVisible()
            binding.loadingLogin.makeGone()
        }
    }

    override fun initAction() {
        binding.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.registerFragment)
        }

        binding.btnLogin.setOnClickListener {
            validateInput()
        }
    }

    private fun proceedLogin(email: String, password: String) {
        viewModel.login(email, password)
    }

    override fun initData() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentReadtyhmLoginBinding.inflate(inflater)
        return binding.root
    }

}
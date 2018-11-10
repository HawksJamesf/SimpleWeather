package com.hawksjamesf.spacecraft.ui.login

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import com.hawksjamesf.spacecraft.R
import com.hawksjamesf.spacecraft.data.bean.login.ClientException
import com.hawksjamesf.spacecraft.data.bean.login.ClientState
import com.hawksjamesf.spacecraft.data.bean.login.SignInReq
import com.hawksjamesf.spacecraft.ui.BaseActivity
import com.hawksjamesf.spacecraft.ui.home.HomeActivity
import com.hawksjamesf.spacecraft.ui.mvp.AutoDisposable
import com.hawksjamesf.spacecraft.util.TextUtil
import com.hawksjamesf.spacecraft.util.hideSoftInput
import com.hawksjamesf.spacecraft.util.openActivity
import com.hawksjamesf.spacecraft.util.subscribeBy
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.view.visibility
import com.jakewharton.rxbinding2.widget.editorActions
import com.jakewharton.rxbinding2.widget.text
import com.jakewharton.rxbinding2.widget.textChanges
import com.orhanobut.logger.Logger
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.Observables
import kotlinx.android.synthetic.main.activity_signin.*

/**
 * Copyright ® $ 2017
 * All right reserved.
 *
 * @author: hawks.jamesf
 * @since: Oct/23/2018  Tue
 */
class SignInActivity : BaseActivity() {
    private val TAG = "SignInActivity"

    override fun initComponent(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_signin)
    }

    override fun handleCallback(autoDisposable: AutoDisposable) {
        tv_signup.clicks()
                .doOnNext { tv_signup.isPressed = true }
                .subscribe({
                    openActivity<SignUpActivity>(false)
                }, { }, {}).autoDisposable()

        Observables.combineLatest(atv_mobile.textChanges(), et_password.textChanges())
                .map { !TextUtil.isEmpty(it.first, it.second) }
                .subscribe { bt_sign_in.isEnabled = it }
                .autoDisposable()

        et_password.editorActions()
                .filter { it == EditorInfo.IME_ACTION_DONE }
                .map { Unit }
                .mergeWith(bt_sign_in.clicks())
                .publish().apply {
                    subscribe { hideSoftInput() }
                    filter { !TextUtil.isEmpty(atv_mobile.text.toString(), et_password.text.toString()) }
                            .subscribe { signin(SignInReq(atv_mobile.text.toString(), et_password.text.toString())) }

                }.connect(autoDisposable)


        onBackPress.map { client.state == ClientState.SIGNING_IN }
                .publish().apply {
                    filter { it }.subscribe { client.signOut() }.autoDisposable()
                    filter { !it }.subscribe { navigateBack() }.autoDisposable()

                }.connect(autoDisposable)

    }

//    private fun isValidate(mobile: CharSequence, password: CharSequence) = mobile.isNotBlank() && password.isNotBlank()

    fun signin(signInReq: SignInReq) {
        client.signIn(signInReq)
                .subscribeBy(
                        onSubscribe = { client.stateData = StateData(signinginDisposable = it) },
                        onSuccess = {
                            client.stateData = StateData(profile = it)
                            Logger.t(TAG).d("sign in--->resp:$it and state:${client.stateData}")
                            //todo:将新的Profile存入到数据库
                        },
                        onError = {
                            client.stateData = StateData()
                            client.emitSignInFailedEvent(signInReq.mobile, signInReq.password, it as ClientException)
                        }

                )

    }

    override fun loadData(autoDisposable: AutoDisposable) {
    }

    override fun onResume(autoDisposable: AutoDisposable) {
        super.onResume(autoDisposable)
        client.stateObservable.publish().apply {
            map { it == ClientState.SIGNING_IN }.observeOn(AndroidSchedulers.mainThread()).subscribe {
                pb_signin_progress.visibility = if (it) View.VISIBLE else View.GONE
            }.autoDisposable()
            map { it == ClientState.SIGNED_OUT }.subscribe { ll_signin.visibility() }.autoDisposable()
            filter { it == ClientState.SIGNED_IN }.subscribe { openActivity<HomeActivity>() }.autoDisposable()
            filter { it == ClientState.SIGNING_UP }.subscribe { openActivity<SignUpActivity>(false) }.autoDisposable()

        }.connect(autoDisposable)

        client.signInFailedEventObservable.publish().apply {
            map { it.mobile }.subscribe { atv_mobile.text() }.autoDisposable()
            map { it.password }.subscribe { et_password.text() }.autoDisposable()
            map { it.excep }.observeOn(AndroidSchedulers.mainThread()).subscribe { Toast.makeText(this@SignInActivity, "$it", Toast.LENGTH_SHORT).show() }
        }.connect(autoDisposable)
    }

}
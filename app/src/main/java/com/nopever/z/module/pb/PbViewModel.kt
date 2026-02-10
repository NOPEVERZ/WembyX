package com.nopever.z.module.pb

import android.app.Application
import com.nopever.z.module.base.MyBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PbViewModel @Inject constructor(val app: Application) : MyBaseViewModel(app) {

}
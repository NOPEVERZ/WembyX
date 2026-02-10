package com.nopever.z.module.dialog

import android.app.Application
import com.nopever.z.module.base.MyBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DialogTestViewModel @Inject constructor(val app: Application) : MyBaseViewModel(app) {

}
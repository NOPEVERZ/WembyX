package com.nopever.viewx.dialog

import android.util.SparseArray
import android.view.View
import android.widget.TextView
import androidx.annotation.IdRes

class DialogViewHolder(val convertView: View) {
    private val views = SparseArray<View>()

    @Suppress("UNCHECKED_CAST")
    fun <T : View> getView(@IdRes viewId: Int): T {
        var view = views.get(viewId)
        if (view == null) {
            view = convertView.findViewById(viewId)
            views.put(viewId, view)
        }
        return view as T
    }

    fun setText(@IdRes viewId: Int, text: CharSequence?): DialogViewHolder {
        getView<TextView>(viewId).text = text
        return this
    }

    fun setOnClickListener(@IdRes viewId: Int, listener: (View) -> Unit): DialogViewHolder {
        getView<View>(viewId).setOnClickListener(listener)
        return this
    }
}
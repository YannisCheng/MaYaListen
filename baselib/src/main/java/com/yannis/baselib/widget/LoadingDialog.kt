package com.yannis.baselib.widget

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View.GONE
import android.widget.ProgressBar
import android.widget.TextView
import com.yannis.baselib.R
import com.yannis.baselib.databinding.WidgetLoadingDialogLayoutBinding

/**
 * LoadingDialog 加载loading
 *
 * @author  yannischeng  cwj1714@163.com
 * @date    2020/6/9 - 14:20
 */
class LoadingDialog(context: Context) : Dialog(context) {

    class Builder(private var mContext: Context) {

        private lateinit var msg: String
        private var mIsCancelable: Boolean = false
        private var mIsCancelOutSide: Boolean = false


        /**
         * 设置提示信息
         * @param message
         * @return
         */
        fun setMsg(message: String): Builder {
            this.msg = message
            return this
        }


        /**
         * 设置是否可以按返回键取消
         * @param isCancelable
         * @return
         */
        fun isCancelable(isCancelable: Boolean): Builder {
            this.mIsCancelable = isCancelable
            return this
        }

        /**
         * 设置是否可以取消
         * @param isCancelOutSide
         * @return
         */
        fun isCancelOutSide(isCancelOutSide: Boolean): Builder {
            this.mIsCancelOutSide = isCancelOutSide
            return this
        }

        @SuppressLint("InflateParams")
        fun create(): LoadingDialog {
            val loadingDialog = LoadingDialog(mContext)
            // 去掉Dialog弹窗出现时，一层黑色半透明的背景遮罩
            loadingDialog.window?.setDimAmount(0f);
            // 去掉Dialog自身的白色背景色
            loadingDialog.window?.decorView?.background = null;
            val inflate = WidgetLoadingDialogLayoutBinding.inflate(LayoutInflater.from(mContext))
            val rootView = inflate.root
            val progress = rootView.findViewById<ProgressBar>(R.id.progress) as ProgressBar
            val msgTv = rootView.findViewById<TextView>(R.id.tv_msg) as TextView

            msg.takeIf {
                it != ""
            }?.let {
                msgTv.text = msg
            } ?: run { msgTv.visibility = GONE }

            loadingDialog.apply {
                setContentView(rootView)
                setCancelable(mIsCancelable)
                setCanceledOnTouchOutside(mIsCancelOutSide)
            }

            return loadingDialog
        }

    }
}
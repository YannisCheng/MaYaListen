package com.yannis.baselib.widget

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.yannis.baselib.databinding.ActivityWebViewBinding

/**
 * WebViewActivity
 *
 * @author  yannischeng  cwj1714@163.com
 * @date    2020/7/2 - 16:53
 */
class WebViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWebViewBinding

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater);
        setContentView(binding.root)

        binding.webView.settings.apply {
            javaScriptEnabled = true
            //支持内容重新布局
            layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
            //设置可以访问文件
            allowFileAccess = true
            //支持自动加载图片
            loadsImagesAutomatically = true
            //设置自适应屏幕，两者合用
            //将图片调整到适合webview的大小
            setUseWideViewPort(true)
            // 缩放至屏幕的大小
            setLoadWithOverviewMode(true)
            setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW)
            //缩放操作
            //支持缩放，默认为true。是下面那个的前提。
            setSupportZoom(true);
            //设置内置的缩放控件。若为false，则该WebView不可缩放
            setBuiltInZoomControls(true);
            //隐藏原生的缩放控件
            setDisplayZoomControls(false);
            //其他细节操作
            //关闭webview中缓存
            setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK)
            //支持通过JS打开新窗口
            setJavaScriptCanOpenWindowsAutomatically(true)
            //设置编码格式
            setDefaultTextEncodingName("utf-8")

        }

        binding.webView.apply {

            //当应用程序(存在webview)被切换到后台时，这个方法不仅仅针对当前的webview而是全局的全应用程序的webview
            //它会暂停所有webview的布局显示、解析、延时，从而降低CPU功耗
            //pauseTimers()

            //恢复pauseTimers状态
            //resumeTimers()

            //是否可以前进
            canGoForward()

            //前进网页
            goForward()

            webViewClient = object : WebViewClient() {
                //重写shouldOverrideUrlLoading()方法，使得打开网页时不调用系统浏览器， 而是在本WebView中显示
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    view?.loadUrl(url);
                    return true
                }

                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    binding.progressBar.visibility = View.VISIBLE
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    binding.progressBar.visibility = View.GONE
                }

                override fun onLoadResource(view: WebView?, url: String?) {
                    super.onLoadResource(view, url)
                }
            }

            // 辅助 WebView 处理 Javascript 的对话框,网站图标,网站标题等等。
            webChromeClient = object : WebChromeClient() {
                // 获得网页的加载进度并显示
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    super.onProgressChanged(view, newProgress)
                    if (newProgress < 100) {
                        binding.progressBar.progress = newProgress
                    } else {
                        binding.llWebView.visibility = View.VISIBLE
                    }
                }

                //  每个网页的页面都有一个标题
                override fun onReceivedTitle(view: WebView?, title: String?) {
                    super.onReceivedTitle(view, title)
                    binding.tvWebTitle.text = title
                }
            }
            // https://blog.csdn.net/weixin_40438421/article/details/85700109
            // file:///android_asset/html/html_use_camera.html
            loadUrl("https://blog.csdn.net/weixin_40438421/article/details/85700109")
        }
    }

    override fun onResume() {
        super.onResume()
        //激活WebView为活跃状态，能正常执行网页的响应
        binding.webView.onResume()
    }

    override fun onPause() {
        super.onPause()
        //当页面被失去焦点被切换到后台不可见状态，需要执行onPause()
        //通过onPause()动作通知内核暂停所有的动作，比如DOM的解析、JavaScript执行等
        binding.webView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        // 销毁
        binding.webView.let {
            //清除网页访问留下的缓存
            //由于内核缓存是全局的因此这个方法不仅仅针对webview而是针对整个应用程序.
            it.clearCache(true)

            //清除当前webview访问的历史记录
            //只会webview访问历史记录里的所有记录除了当前访问记录
            it.clearHistory()

            //这个api仅仅清除自动完成填充的表单数据，并不会清除WebView存储到本地的数据
            it.clearFormData()
            //销毁Webview
            //在关闭了Activity时，如果Webview的音乐或视频，还在播放，就必须销毁Webview。
            //但是注意：webview调用destory时,webview仍绑定在Activity上
            //这是由于自定义webview构建时传入了该Activity的context对象
            //因此需要先从父容器中移除webview,然后再销毁webview
            binding.root.removeView(binding.webView)
            it.destroy()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (binding.webView.canGoBack()) {
                binding.webView.goBack()
                return true
            } else {
                backClickListener()
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun backClickListener() {
        finish()
    }


}
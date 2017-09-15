package com.tribe.explorer.view.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.tribe.explorer.R;
import com.tribe.explorer.model.Config;
import com.tribe.explorer.model.Utils;

public class CouponFragment extends Fragment {

    WebView webBlog;
    Dialog dialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_coupon, container, false);
        initViews(view);
        return view;
    }

    public void initViews(View view) {
        dialog = Utils.showDialog(getActivity());
        dialog.show();
        webBlog = view.findViewById(R.id.webBlog);

        initWebView();
        webBlog.loadUrl(Config.SHOP_URL);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {
        webBlog.setWebChromeClient(new MyWebChromeClient(getActivity()));
        webBlog.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            /*@Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webBlog.loadUrl(url);
                return true;
            }*/

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                dialog.dismiss();
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                dialog.dismiss();
                Toast.makeText(getActivity(), R.string.failed_to_load_data, Toast.LENGTH_SHORT).show();
            }
        });
        webBlog.clearCache(true);
        webBlog.clearHistory();
        webBlog.getSettings().setJavaScriptEnabled(true);
        webBlog.setHorizontalScrollBarEnabled(false);
    }

    private class MyWebChromeClient extends WebChromeClient {
        Context context;

        private MyWebChromeClient(Context context) {
            super();
            this.context = context;
        }
    }

}

/* 
 * Copyright (C) 2014 Peter Cai
 *
 * This file is part of BlackLight
 *
 * BlackLight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BlackLight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with BlackLight.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.dudutech.biu.ui.login;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.dudutech.biu.R;
import com.dudutech.biu.Utils.Utility;
import com.dudutech.biu.api.BaseApi;
import com.dudutech.biu.api.LoginApi;
import com.dudutech.biu.dao.HttpClientUtils;
import com.dudutech.biu.dao.login.LoginDao;
import com.dudutech.biu.ui.common.BaseActivity;
import com.dudutech.biu.ui.main.MainActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.dudutech.biu.BuildConfig.DEBUG;


/* BlackMagic Login Activity */
public class WebLoginActivity extends BaseActivity {
	private static final String TAG = WebLoginActivity.class.getSimpleName();

    @InjectView(R.id.wb_login)
	WebView webView;

	@InjectView(R.id.toolbar)
	Toolbar toolbar;
	
	private MenuItem mMenuItem;
	

	private String mAppId;
	private String mAppSecret;


	private boolean isDoingLogin=false;

	private LoginDao mLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
//		mLayout = R.layout.login;
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_login);
        ButterKnife.inject(this);

		// Create login instance
		mLogin = LoginDao.getInstance(this);

		setSupportActionBar(toolbar);

		// Login page
		WebSettings settings = webView.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setSaveFormData(false);
		settings.setSavePassword(false);
		settings.setCacheMode(WebSettings.LOAD_NO_CACHE);

		webView.setWebViewClient(new MyWebViewClient());

		webView.loadUrl(LoginApi.getOauthLoginPage());




	}



	private class MyWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {

			if (LoginApi.isUrlRedirected(url)) {
				view.stopLoading();
				Log.d(TAG, "shouldOverrideUrlLoading...");
				handleRedirectedUrl(url);
			} else {
				view.loadUrl(url);
			}
			return true;
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			if (!url.equals("about:blank") && LoginApi.isUrlRedirected(url)) {
				view.stopLoading();
				Log.d(TAG, "onPageStarted...");
				handleRedirectedUrl(url);
				return;
			}
			super.onPageStarted(view, url, favicon);
		}
	}






	private void handleRedirectedUrl(String url) {
		Log.d(TAG, "handleRedirectedUrl...");

		if (!url.contains("error")) {
			int tokenIndex = url.indexOf("access_token=");
			int expiresIndex = url.indexOf("expires_in=");
			String token = url.substring(tokenIndex + 13, url.indexOf("&", tokenIndex));
			String expiresIn = url.substring(expiresIndex + 11, url.indexOf("&", expiresIndex));

			if (DEBUG) {
				Log.d(TAG, "url = " + url);
				Log.d(TAG, "token = " + token);
				Log.d(TAG, "expires_in = " + expiresIn);
			}

			if (!isDoingLogin)
				new LoginTask().execute(token, expiresIn);


		} else {
			showLoginFail();
		}
	}


	private class LoginTask extends AsyncTask<String, Void, Long>
	{
		private ProgressDialog progDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			isDoingLogin=true;
			progDialog = new ProgressDialog(WebLoginActivity.this);
			progDialog.setMessage(getResources().getString(R.string.loadding_user_data));
			progDialog.setCancelable(false);
			progDialog.show();
		}

		@Override
		protected Long doInBackground(String... params) {
			if (DEBUG) {
				Log.d(TAG, "doInBackground...");
			}
				mLogin.login(params[0], params[1]);
				return mLogin.getExpireDate();

		}

		@Override
		protected void onPostExecute(Long result) {
			super.onPostExecute(result);
			progDialog.dismiss();
			isDoingLogin=false;

			if ( mLogin.getAccessToken() != null) {
				if (DEBUG) {
					Log.d(TAG, "Access Token:" + mLogin.getAccessToken());
					Log.d(TAG, "Expires in:" + mLogin.getExpireDate());
				}
				mLogin.cache();

			} else if (mLogin.getAccessToken() == null) {
				showLoginFail();
				return;
			}
			String msg = String.format(getResources().getString(R.string.expires_in), Utility.expireTimeInDays(result));
			// Expire date
			Toast.makeText(WebLoginActivity.this, msg, Toast.LENGTH_LONG).show();
			Intent i = new Intent();
			i.setAction(Intent.ACTION_MAIN);
			i.setClass(WebLoginActivity.this, MainActivity.class);
			startActivity(i);
			finish();
		}

	}

	private void showLoginFail() {
		// Wrong username or password
		new AlertDialog.Builder(WebLoginActivity.this)
				.setMessage(R.string.login_fail)
				.setCancelable(true)
				.create()
				.show();
	}

}

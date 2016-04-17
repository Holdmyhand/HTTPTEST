package com.httpget.test;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends Activity {
	private static final int SHOW_RESPONSE = 0;
	Button ok;
	TextView xianshi;
	EditText xuehao,mima;

	private Handler hander = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case SHOW_RESPONSE:
					String response = (String) msg.obj;
					xianshi.setText(response);
			}
		}
	};


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lay_main);
		ok = (Button) findViewById(R.id.btn_1);
		xianshi = (TextView) findViewById(R.id.tv_1);
		xuehao = (EditText) findViewById(R.id.xuehao0);
		mima = (EditText) findViewById(R.id.mima0);



		ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (v.getId() == R.id.btn_1) {
					kaishi();
				}
			}
		});
	}
	private void kaishi() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				HttpURLConnection connection = null;
				try {
					URL url = new URL("http://onminda.aidchou.com/course/"+xuehao.getText().toString()+"/"+mima.getText().toString()+"/2015-2016-2");
					connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(8000);
					connection.setReadTimeout(8000);
					InputStream in = connection.getInputStream();
					BufferedReader reader = new BufferedReader(new InputStreamReader(in));
					StringBuilder response = new StringBuilder();
					String line;
					while ((line = reader.readLine()) != null) {
						response.append(line);
					}
					Message message = new Message();
					message.what = SHOW_RESPONSE;
					message.obj = response.toString();
					hander.sendMessage(message);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (connection != null) {
						connection.disconnect();
					}
				}
			}
		}).start();
	}
}

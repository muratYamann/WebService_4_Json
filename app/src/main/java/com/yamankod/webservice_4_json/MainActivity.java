package com.yamankod.webservice_4_json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
	String URL = "http://30.10.24.106/webservice2/index.php?isim=";
	public static final int connectTimeout = 10 * 1000; // 10 seconds..
	private String name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button ok = (Button) findViewById(R.id.button1);
		final EditText edtext = (EditText) findViewById(R.id.editText1);
		ok.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				name = edtext.getText().toString();
				// Arkaplanda calis 
				MyAsyncTask task= new MyAsyncTask();	
				task.execute(URL+name);					 
			}
		});
	}
	public static String getUrlString(String url) throws MalformedURLException,
			SocketTimeoutException, IOException {
		
		InputStream in = null;
		BufferedReader reader = null;
		URLConnection feedUrl;
		String ret = "";
		feedUrl = new URL((url.toString())).openConnection();

	try{
		feedUrl.setConnectTimeout(connectTimeout);
		feedUrl.setReadTimeout(connectTimeout);
		in = feedUrl.getInputStream();
		reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			sb.append(line + "\n");
		}
		ret = sb.toString();
		in.close();
	}catch(Exception e)
	{
		
		System.out.println("sunucu hatası");
	}
		return ret;
	}
	
	private class MyAsyncTask extends AsyncTask<String, Void, JSONObject> {

		private ProgressDialog mProgressDialog;
		private TextView tv=(TextView)findViewById(R.id.textView1);
		private EditText edtext = (EditText) findViewById(R.id.editText1);

		@Override
		protected void onPostExecute(JSONObject result) {
			mProgressDialog.dismiss();
			try {
				if(result==null)
				{
					tv.setText("Sunucu bağlantı hatası");
				}
				else{
					if(result.getBoolean("deger"))
					{
						tv.setText("");
						tv.setBackgroundResource(R.drawable.valid_icon);
						
					}
					else{
						
						tv.setText("Geçersiz lütfen tekrar deneyin");
						tv.setTextColor(Color.RED);
						edtext.setText("");
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		protected void onPreExecute() {
			mProgressDialog = ProgressDialog.show(MainActivity.this,
					"Loading...", "Data is Loading...");
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			
			JSONObject tmp=new JSONObject();
			// your network operation
			try {
				String ret = getUrlString(params[0]);
				ret = ret.trim();
				 tmp = new JSONObject(ret);
				System.out.println(tmp);
			} catch (SocketTimeoutException e) {
				e.printStackTrace();
				System.out.println("sunucu hatası");
				//
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			catch (Exception e) {
				// TODO: handle exception
				System.out.println("sunucu hatası"+e.toString());
			}
			return tmp;
		}
	}
}

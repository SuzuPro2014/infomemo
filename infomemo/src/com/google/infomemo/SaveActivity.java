package com.google.infomemo;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SaveActivity extends Activity implements LocationListener {

	String wifiData = "◆Wi-Fi情報";
	String gpsData = "◆位置情報";
	String date = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_save);

		TextView day = (TextView)findViewById(R.id.tv_day);

		Time time = new Time("Asia/Tokyo");
		time.setToNow();
		date = time.year + "年" + (time.month + 1) + "月" + time.monthDay + "日" + time.hour + "時" + time.minute + "分";
		day.setText(date);


		getWiFiInfo();
		getGPSInfo();


	}


	// データを保存
	public void dataSave (View view) {

		EditText filename = (EditText)findViewById(R.id.et_filename);
		EditText etc = (EditText)findViewById(R.id.et_etc);

		String message = "";

		String file = filename.getText().toString();

		if (file.isEmpty()) {
			message = "ファイル名を入力して下さい";
			Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
		} else {
			String file2 = file + ".txt";
			String etc2 = etc.getText().toString();

			String inputData = "◆記録日時\r\n"+ date + "\r\n\r\n\r\n" + wifiData + gpsData + "◆備考\r\n" + etc2;

			String[] fileList = this.fileList();

			boolean flg = false;

			for (int i = 0; i < fileList.length; i++) {
				if (file2.equals(fileList[i])) {
					flg = true;
				}
			}
			if (flg == false) {
				try {
				      FileOutputStream outStream = openFileOutput(file2, MODE_PRIVATE);
				      OutputStreamWriter writer = new OutputStreamWriter(outStream);
				      writer.write(inputData);
				      writer.flush();
				      writer.close();

				      message = "保存しました"
				      		+ "";
				    } catch (FileNotFoundException e) {
				      message = e.getMessage();
				      e.printStackTrace();
				    } catch (IOException e) {
				      message = e.getMessage();
				      e.printStackTrace();
				    }

				Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();

				Intent i = new Intent();
				setResult(RESULT_OK, i);
			    finish();

			} else {
				message = "そのファイル名は既に使われています";
				Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
			}
		}

	}

	// 周囲のWi-Fi情報取得とリスト表示
	public void getWiFiInfo () {

		// Wi-Fiマネージャ取得
		WifiManager manager = (WifiManager)getSystemService(WIFI_SERVICE);

		// 現在周知しているWi-Fiスポット検索開始
		manager.startScan();

		List<ScanResult> scanResults = manager.getScanResults();
		int size = scanResults.size();

		ArrayList<Map<String, String>> wifiInfo = new ArrayList<Map<String, String>>();

		for (int i = 0; i <size; i++) {

			String ssid = scanResults.get(i).SSID;
			String bssid = scanResults.get(i).BSSID;

			if (ssid != "") {
				HashMap<String, String> data = new HashMap<String, String>();

				String d = "SSID：" + ssid + "\r\nBSSID：" + bssid + "\r\n";

				wifiData = wifiData + "\r\n" + d;

				data.put("SSID", ssid);
				data.put("BSSID", bssid);
				wifiInfo.add(data);
			}
		}

		wifiData = wifiData + "\r\n\r\n";

        SimpleAdapter adapter = new SimpleAdapter(this, wifiInfo, android.R.layout.simple_list_item_2, new String[] {"SSID", "BSSID"}, new int[] {android.R.id.text1, android.R.id.text2});

        Spinner s = (Spinner)this.findViewById(R.id.test);
        s.setAdapter(adapter);

	}

	// 位置情報取得準備
	public void getGPSInfo () {

        LocationManager mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setPowerRequirement(Criteria.POWER_MEDIUM);

        String provider = mLocationManager.getBestProvider(criteria, true);
        mLocationManager.requestLocationUpdates(provider, 0, 0, this);


	}

	// 位置情報取得とリスト表示
	@Override
	public void onLocationChanged(Location location) {
		// TODO 自動生成されたメソッド・スタブ

		String [] gps = new String [2];

		gps[0] = "Lat：" + location.getLatitude();
		gps[1] = "Ing：" + location.getLongitude();

		for (int i = 0; i < gps.length; i++) {
			gpsData = gpsData + "\r\n" + gps[i];
		}

		gpsData = gpsData + "\r\n\r\n\r\n";

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, gps);

		ListView gpsList = (ListView) findViewById(R.id.gpsList);
		gpsList.setAdapter(adapter);

	}


	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO 自動生成されたメソッド・スタブ

	}


	@Override
	public void onProviderEnabled(String provider) {
		// TODO 自動生成されたメソッド・スタブ

	}


	@Override
	public void onProviderDisabled(String provider) {
		// TODO 自動生成されたメソッド・スタブ

	}



}

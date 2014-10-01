package com.google.infomemo;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		FileList_View();

	}

	public void FileList_View () {

		String[] fileList = this.fileList();

        final ArrayAdapter <String> adapter = new ArrayAdapter <String> (this, android.R.layout.simple_expandable_list_item_1, fileList);

        ListView fList = (ListView) findViewById(R.id.infolist);
        fList.setAdapter(adapter);

        fList.setOnItemClickListener(
        		new AdapterView.OnItemClickListener() {
				public void onItemClick(AdapterView<?> av, View view, int position, long id) {
					String filename = adapter.getItem(position);
					showContent(filename);
				}
        		});

	}

	public void showContent (final String filename) {

		String str = "", tmp;

		try {
			 FileInputStream in = openFileInput(filename);
			 BufferedReader reader = new BufferedReader( new InputStreamReader( in , "UTF-8") );


			 while ((tmp = reader.readLine()) != null) {
				 str = str + tmp + "\n";

			}
			reader.close();
		} catch (FileNotFoundException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		AlertDialog.Builder b = new AlertDialog.Builder(this);

		b.setTitle(filename).setMessage(str)
		.setPositiveButton("Delete",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO 自動生成されたメソッド・スタブ
						check(filename);
					}
				})
		.setNeutralButton("Close",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO 自動生成されたメソッド・スタブ

					}
				}).show();

	}

	public void check (final String filename) {

		AlertDialog.Builder b = new AlertDialog.Builder(this);
		b.setTitle("Warning!").setMessage("本当に\n" + filename + "\nを削除しますか？")
		.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO 自動生成されたメソッド・スタブ
						deleteFile(filename);
						FileList_View();

					}
				})
		.setNeutralButton("Cancel",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO 自動生成されたメソッド・スタブ

					}
				})
		.show();

	}

	public void newData (View view) {

		Intent i = new Intent(this, com.google.infomemo.SaveActivity.class);
		this.startActivityForResult(i, 1);

	}

	@Override
	protected void onActivityResult (int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 1 && resultCode == RESULT_OK) {
			FileList_View();
		}


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}

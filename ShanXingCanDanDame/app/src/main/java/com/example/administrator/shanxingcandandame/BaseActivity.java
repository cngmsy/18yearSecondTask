package com.example.administrator.shanxingcandandame;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface.OnClickListener;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class BaseActivity extends Activity {



	private ProgressDialog dialog;
	public Button getButton(int id) {
		return (Button) findViewById(id);
	}

	public EditText getEditText(int id) {
		return (EditText) findViewById(id);
	}

	public TextView getTextView(int id) {
		return (TextView) findViewById(id);
	}

	public ImageView getImageView(int id) {
		return (ImageView) findViewById(id);
	}

	public ListView getListView(int id) {
		return (ListView) findViewById(id);
	}

	public void toasts(String text) {
		Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT)
				.show();
	}

	public void toastl(String text) {
		Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
	}

	public void syso(String text) {
		System.out.println(text);

	}

	public void i(String text) {
		Log.i("show", text);
	}
	/**
	 * 
	 * @param title	   	����
	 * @param message	����
	 * @param yesListener	ȷ����ť����
	 * @param noListener	ȡ����ť����  Ϊ��������ȡ����ť
	 */	
	public void dialog(String title, String message, OnClickListener yesListener,OnClickListener noListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(title)
		.setMessage(message)
		.setCancelable(false);
		builder.setNeutralButton("ȷ��", yesListener);
		if (noListener!=null) {
			builder.setNegativeButton("ȡ��", noListener);
		}

		builder.show();
	}
	/**
	 *  �������̵߳���dialog�ᱨ��
	 * @param message
	 */
	public void showDialog(String message) {
		try {
			if (dialog == null) {
				dialog = new ProgressDialog(this);
				dialog.setCancelable(true);
			}
			dialog.setMessage(message);
			dialog.show();
		} catch (Exception e) {
			
		}
	}
	void hideDialog() {
		if (dialog != null && dialog.isShowing())
			try {
				dialog.dismiss();
			} catch (Exception e) {
			}
	}
	
}

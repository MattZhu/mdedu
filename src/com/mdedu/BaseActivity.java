package com.mdedu;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.provider.Settings;

import com.mdedu.utils.AsyncTaskEx;

public class BaseActivity  extends Activity implements OnCancelListener {
	
	protected final static int SELECT_CAR_TYPE_DIALOG_ID = 1000;
	
	protected final static int PROGRESS_DIALOG=1001;
	protected final static int NO_NETWORK=1002;
	protected static final int EXIT_CONFIRM_DIALOG = 1002;


	protected boolean dialogCanceled =false;
	
	protected AsyncTaskEx task;
	
	@SuppressWarnings("deprecation")
	public void showProgressDialog(int id){
		this.dialogCanceled=false;
		this.showDialog(id);
	}

	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case PROGRESS_DIALOG:
			return createProgressDialog();
		case NO_NETWORK:
			return createNoNetworkDialog();
		}
		return super.onCreateDialog(id);
	}
	private Dialog createNoNetworkDialog() {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("无网络连接")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setMessage("设置网络连接？")
				.setCancelable(false)
				.setPositiveButton("设置",
						new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int which) {
						enableNetworkSettings();
					}
				}).setNegativeButton(android.R.string.cancel, null);
		return builder.create();
	}
	
	protected String getProgressMessage(){
		return "";
	}
	
	private Dialog createProgressDialog() {
		ProgressDialog d= ProgressDialog.show(this, "",getProgressMessage(), true);
		d.setCancelable(true);
		d.setContentView(R.layout.progress_dialog);
		d.setOnCancelListener(this);		
		return d;
	}
	private void enableNetworkSettings() {
		Intent settingsIntent = new Intent(
				Settings.ACTION_WIRELESS_SETTINGS);
		startActivityForResult(settingsIntent, 1);
		startActivity(settingsIntent);
	}
	
	
	public void onCancel(DialogInterface dialog) {
		dialogCanceled=true;	
		if(task!=null)
		{
			task.cancel(true);
		}
	}

	public void setExcuteTask(AsyncTaskEx task){
		this.task=task;
	}
	
	public AsyncTaskEx getExcuteTask(){
		return task;
	}
}

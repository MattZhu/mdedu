package com.mdedu.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.mdedu.R;


public class LoadingDialogFragment extends DialogFragment {

	public LoadingDialogFragment() {
	}

	public Dialog onCreateDialog(Bundle savedInstanceState) {
		ProgressDialog dialog=ProgressDialog.show(this.getActivity(), "", null);
		
		dialog.setContentView(R.layout.progress_dialog);
		dialog.setCanceledOnTouchOutside(false);
		return dialog;
	}

}

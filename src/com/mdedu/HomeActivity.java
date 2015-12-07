package com.mdedu;

import java.util.Arrays;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mdedu.domainobject.Charpater;
import com.mdedu.domainobject.Course;
import com.mdedu.domainobject.Video;
import com.mdedu.serverapi.JsonApi;
import com.mdedu.serverapi.message.CharpaterResponse;
import com.mdedu.serverapi.message.CourseResponse;
import com.mdedu.utils.AsyncTaskEx;
import com.mdedu.utils.ListAdapter;
import com.mdedu.utils.ListItemViewCreator;
import com.mdedu.utils.LvHeightUtil;

public class HomeActivity extends BaseActivity implements
		ListItemViewCreator<Course> {
	protected static final int PLAY_VEDIO_DIALOG_ID = 1100;
	
	private JsonApi<CharpaterResponse> chApi;

	private ListAdapter<Course> courseAdapter;

	private ListAdapter<Charpater> charpAdapter;

	private JsonApi<CourseResponse> courseApi;
	
	private Video currentVideo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		chApi = new JsonApi<CharpaterResponse>(CharpaterResponse.class);
		courseApi = new JsonApi<CourseResponse>(CourseResponse.class);

		ListView list = (ListView) findViewById(R.id.home_grades);
		courseAdapter = new ListAdapter<Course>(this, this);
		list.setAdapter(courseAdapter);
		list.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Course c=(Course)	arg0.getAdapter().getItem(arg2);
//				loadCh(c.getId());
			}
			
		});
		ListView chlist = (ListView) findViewById(R.id.home_chs);
		charpAdapter = new ListAdapter<Charpater>(this,
				new ChViewCreator());
		chlist.setAdapter(charpAdapter);;
		loadData();

	}
	
	private class ChViewCreator implements ListItemViewCreator<Charpater> {

		

		@Override
		public View createOrUpdateView(Charpater item,
				View convertView, int position, ViewGroup parent) {
			LinearLayout result = null;
			if (convertView != null) {
				result = (LinearLayout) convertView;
			} else {
				LayoutInflater inflater = (LayoutInflater) HomeActivity.this
						.getSystemService(LAYOUT_INFLATER_SERVICE);
				result = (LinearLayout) inflater.inflate(
						R.layout.chitem, null);
			}
			TextView title = (TextView) result
					.findViewById(R.id.ch_title);
//			title.setText(item.getName());
			ListView childrenlist = (ListView) result.findViewById(R.id.sub_chs);
			ListAdapter<Charpater> adapter=new ListAdapter<Charpater>(HomeActivity.this,
					new ChViewCreator());
			childrenlist.setAdapter(adapter);
			childrenlist.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//					currentVideo=((Charpater)arg0.getAdapter().getItem(arg2)).getVideo();
//					currentVideo=new Video();
//					currentVideo.setId(1);
//					currentVideo.setUrl("http://192.168.1.108:8080/mdbe/download?id=1");
//					currentVideo.setName(((Charpater)arg0.getAdapter().getItem(arg2)).getName());
					if(currentVideo!=null){
						HomeActivity.this.showDialog(PLAY_VEDIO_DIALOG_ID);
					}
				}

			}
			);
//			if (item.getChildren() != null
//					&& item.getChildren().length != 0) {
//			
//				
//				adapter.setData(item.getChildren());
//				
//			}
//			else{
//				adapter.setData(new Charpater[0]);
//			}
			LvHeightUtil.setListViewHeightBasedOnChildren(childrenlist);
			return result;
		}

	};

	private void updateUI(List<Charpater> chs, List<Course> courses) {
		
		updateCourse(courses);
		updateCharpater(chs);
	}

	private void updateCourse(List<Course> courses) {

		courseAdapter.setData(courses);
	}

	private void updateCharpater(List<Charpater> chs) {
		charpAdapter.setData(chs);
	}

	private void loadCh(final Integer cId)
	{
		showProgressDialog(PROGRESS_DIALOG);
		task = new AsyncTaskEx<String, Void, Void>() {
			Charpater[] chs;


			@Override
			protected Void doInBackground(String... params) {
				
					chApi.setBaseUrl(chApi.getHost() + "/chapters/clients");
					CharpaterResponse chResponse = chApi.get("cId",
							cId+ "", null);
					if (chResponse != null) {
//						chs = chResponse.getCharpaters();
					}
					return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				HomeActivity.this.dismissDialog(PROGRESS_DIALOG);

				updateCharpater(Arrays.asList(chs));
			}

		}.execute();
	}
	
	private void loadData() {
		showProgressDialog(PROGRESS_DIALOG);
		task = new AsyncTaskEx<String, Void, Void>() {
			Charpater[] chs;

			Course[] courses;

			@Override
			protected Void doInBackground(String... params) {
				courseApi.setBaseUrl(courseApi.getHost()
						+ "/courses/10/clients");
				CourseResponse r = courseApi.get("bac", "10", null);
				if (r != null) {
//					courses = r.getCourses();
				}
				if (courses != null&&courses.length>0) {
					chApi.setBaseUrl(chApi.getHost() + "/chapters/clients");
					CharpaterResponse chResponse = chApi.get("cId",
							courses[0].getId() + "", null);
					if (chResponse != null) {
//						chs = chResponse.getCharpaters();
					}
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				HomeActivity.this.dismissDialog(PROGRESS_DIALOG);

				updateUI(Arrays.asList(chs), Arrays.asList(courses));
			}

		}.execute();
	}


	@Override
	public View createOrUpdateView(Course item, View convertView, int position,
			ViewGroup parent) {
		TextView result = null;
		if (convertView != null) {
			result = (TextView) convertView;
		} else {
			LayoutInflater inflater = (LayoutInflater) HomeActivity.this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			result = (TextView) inflater.inflate(
					R.layout.gradeitem, null);
		}
		
//		result.setText(item.getGrade().getName());
		return result;
	}

	@Override
	protected Dialog onCreateDialog(int id, Bundle args) {
		if(id==PLAY_VEDIO_DIALOG_ID){
			return createPlayVedioDialog(args);
		}
		else
		{
			return super.onCreateDialog(id);
		}
	}
	
	
	private Dialog createPlayVedioDialog(Bundle args) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(this.currentVideo.getName())
				.setIcon(android.R.drawable.ic_dialog_info)
				.setCancelable(false)
				.setPositiveButton("播放",
						new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int which) {
						Intent i=new Intent(HomeActivity.this,VedioWatchActivity.class);
//						i.putExtra("uri", currentVideo.getUrl());						
						startActivity(i);
					}
				}).setNegativeButton("缓存", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int which) {
						
					}}
				);
		return builder.create();
	}
	
	

}

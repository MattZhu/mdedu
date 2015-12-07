package com.mdedu.fragment;

import java.util.List;

import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mdedu.R;
import com.mdedu.VedioWatchActivity;
import com.mdedu.domainobject.DaoSession;
import com.mdedu.domainobject.Question;
import com.mdedu.domainobject.QuestionAttempt;
import com.mdedu.domainobject.Video;
import com.mdedu.serverapi.Constants;
import com.mdedu.utils.DatabaseHelper;
import com.mdedu.utils.ListAdapter;
import com.mdedu.utils.ListItemViewCreator;
import com.mdedu.utils.LvHeightUtil;

public class ChapterDetailFragement extends Fragment implements
		OnClickListener, OnItemClickListener, ListItemViewCreator<Video> {

	private DaoSession session;

	private ListAdapter<Video> adapter;

	private ListAdapter<QuestionAttempt> questionAttemptAdapter;

	private Long chId;

	private String title;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		this.getActivity().getActionBar().show();
		session = DatabaseHelper.getDaoSession(this.getActivity());
		adapter = new ListAdapter<Video>(this.getActivity(), this);
		setHasOptionsMenu(true);
		((ActionBarActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		questionAttemptAdapter = new ListAdapter<QuestionAttempt>(
				this.getActivity(), new ListItemViewCreator<QuestionAttempt>() {

					@Override
					public View createOrUpdateView(QuestionAttempt item,
							View convertView, int position, ViewGroup parent) {
						LayoutInflater inflater = (LayoutInflater) getActivity()
								.getSystemService(
										Context.LAYOUT_INFLATER_SERVICE);
						View result = inflater.inflate(
								R.layout.question_attempt_item, parent, false);
						TextView text = (TextView) result
								.findViewById(R.id.time_used);
						text.setText(getTimeString(item.getTimeUsed()));
						text = (TextView) result.findViewById(R.id.submit_date);
						text.setText(item.getSubmitDate().toLocaleString());
						text = (TextView) result
								.findViewById(R.id.total_correct);
						text.setText("5/" + item.getCorrectCount());
						return result;
					}

				});
	}

	private String getTimeString(Integer timeUsed) {
		int sec = timeUsed % 60;
		int min = (timeUsed / 60) % 60;
		int hour = timeUsed / 3600;
		return hour > 0 ? hour + " ±" + min + "∑÷" + sec + "√Î" : min > 0 ? min
				+ "∑÷" + sec + "√Î" : sec + "√Î";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
	 * android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View result = inflater.inflate(R.layout.fragment_chapter_detail,
				container, false);
		initViews(result);
		return result;
	}

	private void initViews(View result) {
//		TextView view = (TextView) result.findViewById(R.id.title_name);
		title = getArguments().getString("title");
//		view.setText(title);

		this.getActivity().setTitle(title);
		//result.findViewById(R.id.back_btn).setOnClickListener(this);
		ListView contentList = (ListView) result
				.findViewById(R.id.content_list);
		contentList.setAdapter(adapter);
		contentList.setOnItemClickListener(this);
		chId = this.getArguments().getLong("chId");
		refreshUI();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
//		case R.id.refresh:
//			refreshUI();
//			break;
		}

	}

	private void refreshUI() {
		List<Video> data = session.getVideoDao()._queryCharpater_Videos(chId);
		List<Question> questions = session.getQuestionDao()
				._queryCharpater_Questions(chId);
		if (questions.size() > 0) {
			Video q = new Video();
			q.setId(-1l);
			q.setName("¡∑œ∞Ã‚");
			q.setLink("¡∑œ∞Ã‚");
			q.setCharpaterId(this.chId);
			data.add(q);
		}
		adapter.setData(data);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		Video v = adapter.getItem(position);
		if (v.getId() > 0) {
			Intent i = new Intent(this.getActivity(), VedioWatchActivity.class);
			Log.d("Video","Download Id "+v.getDownloadId());
			if(v.getDownloadId()!=null){
				String url=getLocalURi(v.getDownloadId());
				if(url!=null){
					i.putExtra("uri", url);
				}else{
					//show a dialog
				}
			}else{
				i.putExtra("uri", Constants.DOWNLOAD_URL+v.getId());
			}
			startActivity(i);
		} else {
			Fragment fragment = new QuestionFragment();
			FragmentTransaction transaction = getFragmentManager()
					.beginTransaction();
			Bundle args = new Bundle();
			args.putString("title", title + "-¡∑œ∞Ã‚");
			args.putLong("chId", chId);
			fragment.setArguments(args);
			transaction.replace(R.id.fragment_container, fragment);
			transaction.addToBackStack(null);
			transaction.commit();

		}
		// Uri uri =
		// Uri.parse("http://192.168.1.109:8080/mdbe/download?id="+v.getId());
		// Intent intent = new Intent(Intent.ACTION_VIEW);
		// Log.v("URI:::::::::", uri.toString());
		// intent.setDataAndType(uri, "video/mp4");
		// startActivity(intent);
	}

	private String getLocalURi(Long downloadId) {
		DownloadManager dm = (DownloadManager) this.getActivity()
				.getSystemService(Context.DOWNLOAD_SERVICE);
		Query query=new Query();
		query.setFilterById(downloadId);
		Cursor c=dm.query(query);
		if(c.moveToNext()){
			int statusInd = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
			int uriInd = c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);
			int status=c.getInt(statusInd);
			if(status==DownloadManager.STATUS_SUCCESSFUL){
				return c.getString(uriInd);
			}
		}
		return null;
	}

	@Override
	public View createOrUpdateView(final Video item, View convertView, int position,
			ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) this.getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View result ;
		if (item.getId() < 0) {
			result = inflater.inflate(R.layout.question_item, parent, false);
			ImageView image = (ImageView) result.findViewById(R.id.video_image);
			image.setImageResource(R.drawable.compose);
			
			List<QuestionAttempt> qas=session.getQuestionAttemptDao()
			._queryCharpater_QuestionAttempts(chId);

			ImageView arrow=(ImageView)result.findViewById(R.id.download);
			if(qas.size()==0){
				arrow.setVisibility(View.INVISIBLE);
			}else{
				arrow.setVisibility(View.VISIBLE);
			}
			final ListView attemptList = (ListView) result
					.findViewById(R.id.attemptList);
			arrow.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					ImageView image =(ImageView)v;
					if(attemptList.getVisibility()==View.GONE){
						attemptList.setVisibility(View.VISIBLE);
						image.setImageResource(R.drawable.arrow_down);
//						result.measure(0, 0);
//						LayoutParams lp=(LayoutParams) result.getLayoutParams();
//						lp.height=result.getMeasuredHeight();
//						result.setLayoutParams(lp);
//						Log.d("QuestionView","Quetsion item VISIBLE height:"+result.getMeasuredHeight());
					}else{
						attemptList.setVisibility(View.GONE);
						image.setImageResource(R.drawable.arrow_up);
//						result.measure(0, 0);
//						LayoutParams lp=(LayoutParams) result.getLayoutParams();
//						lp.height=result.getMeasuredHeight();
//						result.setLayoutParams(lp);
//						Log.d("QuestionView","Quetsion item INVISIBLE height :"+result.getMeasuredHeight());
					}
				}
				
			});

			result.findViewById(R.id.question_head).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							Fragment fragment = new QuestionFragment();
							FragmentTransaction transaction = getFragmentManager()
									.beginTransaction();
							Bundle args = new Bundle();
							args.putString("title", title + "-¡∑œ∞Ã‚");
							args.putLong("chId", chId);
							fragment.setArguments(args);
							transaction.replace(R.id.fragment_container,
									fragment);
							transaction.addToBackStack(null);
							transaction.commit();
						}

					});

			
			attemptList.setAdapter(questionAttemptAdapter);
			questionAttemptAdapter.setData(qas);
			attemptList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Fragment fragment = new QuestionFragment();
					FragmentTransaction transaction = getFragmentManager()
							.beginTransaction();

					Bundle args = new Bundle();
					args.putString("title", title + "-¡∑œ∞Ã‚");
					args.putLong("chId", chId);
					args.putLong("attempId",
							questionAttemptAdapter.getItem(position).getId());
					fragment.setArguments(args);
					transaction.replace(R.id.fragment_container, fragment);
					transaction.addToBackStack(null);
					transaction.commit();
				}
			});
			LvHeightUtil.setListViewHeightBasedOnChildren(attemptList);
		} else {
			result = inflater.inflate(R.layout.video_item, parent, false);
			ImageView downloadView=(ImageView)result.findViewById(R.id.download);
			if(item.getDownloadId()!=null){
				downloadView.setVisibility(View.INVISIBLE);
			}
			else{
				downloadView.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						ImageView view=(ImageView)v;
						view.setClickable(false);
						download(item);
					}});
				}
		}
		TextView t = (TextView) result.findViewById(R.id.video_name);
		t.setText(adapter.getItem(position).getName());
		return result;
	}
	
	private void download(Video v){
		DownloadManager dm = (DownloadManager) this.getActivity()
				.getSystemService(Context.DOWNLOAD_SERVICE);
		Request request = new Request(Uri.parse( Constants.DOWNLOAD_URL
				+ v.getId()));
		request.setDestinationInExternalFilesDir(
				this.getActivity(), Environment.DIRECTORY_MOVIES,
				v.getId()+".mp4");
		request.setDescription(v.getLink());
		request.setTitle(v.getName());
		
		request.setVisibleInDownloadsUi(false);
		Log.d("Download","URL:"+Constants.DOWNLOAD_URL
				+ v.getId()+" Request:"+request.toString());
		Long downloadId=dm.enqueue(request);
		v.setDownloadId(downloadId);
		session.getVideoDao().update(v);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateOptionsMenu(android.view.Menu, android.view.MenuInflater)
	 */
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.clear();
		super.onCreateOptionsMenu(menu, inflater);
		
		inflater.inflate(R.menu.main, menu);
		
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.d("ChapterFragment","upand back clicked:"+item.getItemId());
	    switch (item.getItemId()) {
	    // Respond to the action bar's Up/Home button
	    case android.R.id.home:	    
	    	getFragmentManager().popBackStackImmediate();
	        return true;
	   
	    case R.id.action_download_all:
	    	
	    	return true;
	    case R.id.action_delete_all:
	    	return true;
	    }
	    return super.onOptionsItemSelected(item);
	}
}

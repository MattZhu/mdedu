package com.mdedu.fragment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
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
import android.widget.ListView;
import android.widget.TextView;

import com.mdedu.R;
import com.mdedu.domainobject.Charpater;
import com.mdedu.domainobject.CharpaterDao;
import com.mdedu.domainobject.DaoSession;
import com.mdedu.domainobject.Question;
import com.mdedu.domainobject.Video;
import com.mdedu.serverapi.JsonApi;
import com.mdedu.serverapi.message.Chapter;
import com.mdedu.serverapi.message.CharpaterResponse;
import com.mdedu.utils.AsyncTaskEx;
import com.mdedu.utils.DatabaseHelper;
import com.mdedu.utils.ListAdapter;
import com.mdedu.utils.ListItemViewCreator;

public class ChapterFragment extends Fragment implements OnClickListener,
		ListItemViewCreator<Charpater>, OnItemClickListener {

	private JsonApi<CharpaterResponse> chApi;

	private Long courseId;

	private CharpaterDao dao;

	private DaoSession session;

	private ListAdapter<Charpater> adapter;

	private Long parentId;
	
	private boolean needRefresh=false;

	private int refreshMenu;

	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		session = DatabaseHelper.getDaoSession(this.getActivity());
		dao = session.getCharpaterDao();
		chApi = new JsonApi<CharpaterResponse>(CharpaterResponse.class);
		adapter = new ListAdapter<Charpater>(this.getActivity(), this);

		setHasOptionsMenu(true);
		((ActionBarActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	
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

		View result = inflater.inflate(R.layout.fragment_chapter, container,
				false);
		//TextView view = (TextView) result.findViewById(R.id.title_name);
		String title = getArguments().getString("title");
		//view.setText(title);
		this.getActivity().setTitle(title);
		courseId = getArguments().getLong("couseId", -1);

		parentId = getArguments().getLong("parentId", -1);
//		View v = result.findViewById(R.id.back_btn);
//		v.setOnClickListener(this);
//		v = result.findViewById(R.id.refresh);
//		v.setOnClickListener(this);

		ListView contentList = (ListView) result
				.findViewById(R.id.content_list);
		contentList.setAdapter(adapter);
		contentList.setOnItemClickListener(this);
		if(courseId!=-1){
			needRefresh=true;
		}else{
	//		v.setVisibility(View.GONE);
			needRefresh=false;
		}
		refreshCUI();
		return result;

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
//		case R.id.back_btn:
//			getFragmentManager().popBackStackImmediate();
//			break;
//		case R.id.refresh:
//			needRefresh=true;
//			refreshCUI();
//			break;
		}

	}
	
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.clear();
		super.onCreateOptionsMenu(menu, inflater);
		MenuItem item=menu.add(1, 3, 100, "fresh");
		item.setIcon(R.drawable.refresh);
		item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		refreshMenu=item.getItemId();
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		Log.d("ChapterFragment","upand back clicked:"+item.getItemId());
	    switch (item.getItemId()) {
	    // Respond to the action bar's Up/Home button
	    case android.R.id.home:	    
	    	getFragmentManager().popBackStackImmediate();
	        return true;
	    }
	    if(refreshMenu==item.getItemId()){
	    	needRefresh=true;
			refreshCUI();
	    	return true;
	    }
	    return super.onOptionsItemSelected(item);
	}
	
	

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onPrepareOptionsMenu(android.view.Menu)
	 */
	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onPrepareOptionsMenu(menu);
		
		MenuItem item=menu.getItem(0);
		item.setVisible(courseId!=-1);
	}

	private void refreshCUI() {
		final LoadingDialogFragment fragment = new LoadingDialogFragment();
		fragment.show(this.getActivity().getSupportFragmentManager(), "loading");

		new AsyncTaskEx<String, Void, List<Charpater>>() {

			@Override
			protected List<Charpater> doInBackground(String... params) {
				List<Charpater> result = new ArrayList<Charpater>();
				List<Video> videos=new ArrayList<Video>();
				List<Question> question =new ArrayList<Question>();
				Log.d("ChapterFragment", "get charpter List for course:"
						+ courseId + "  parentId:" + parentId);
				if (courseId != -1) {
					
					CharpaterResponse chResponse=null;
					if(needRefresh){
						chApi.setBaseUrl(chApi.getHost() + "/chapters/clients");
						chResponse = chApi.get("cId", courseId
							+ "", null);
						needRefresh=false;
					}
					if (chResponse != null) {
						setChildrens(result,videos,question, null, chResponse.getCharpaters());
						dao.insertOrReplaceInTx(result);
					
						session.getVideoDao().insertOrReplaceInTx(videos);
						session.getQuestionDao().insertOrReplaceInTx(question);
						getResult(result);
					} else {
						result = dao
								.queryBuilder()
								.where(dao.queryBuilder()
										.and(CharpaterDao.Properties.CourseId
												.eq(courseId),
												CharpaterDao.Properties.Parent
														.isNull())).list();
					}

				} else if (parentId != -1) {
					Charpater pCh = dao.load(parentId);
					result = pCh.getChidrenCharpeter();
				}

				return result;
			}

			private List<Charpater> getResult(List<Charpater> result) {
				if (courseId != null) {
					Iterator<Charpater> it = result.iterator();
					while (it.hasNext()) {
						Charpater ch = it.next();
						if (ch.getParent() != null) {
							it.remove();
						}
					}
				}

				return result;
			}

			private void setChildrens(List<Charpater> result,List<Video> videos, List<Question>questions,Charpater parent,
					Chapter[] children) {
				if (children != null) {
					for (Chapter c : children) {
						Charpater ch = new Charpater();
						ch.setName(c.getName());
						ch.setId(Long.valueOf(c.getId()));
						ch.setCourseId(courseId);
						if (parent != null)
							ch.setParent(parent.getId());
						result.add(ch);
						if(c.getVideos()!=null){
							for(com.mdedu.serverapi.message.Video v:c.getVideos()){
								videos.add(convertVideo(v));
							}
						}
						if(c.getQuestions()!=null){
							for(com.mdedu.serverapi.message.Question q:c.getQuestions()){
								questions.add(convertQuestion(q));
							}
						}
						setChildrens(result,videos,questions, ch, c.getChildren());
					}
				}
			}

			private Question convertQuestion(
					com.mdedu.serverapi.message.Question q) {
				Question result=new Question();
				result.setCharpaterId(Long.valueOf(q.getChId()));
				result.setCorrectAnswer(q.getCorrectAnswer());
				result.setId(Long.valueOf(q.getId()));
				result.setImagePath(q.getImagePath());
				result.setOptions(q.getOptions());
				result.setTitle(q.getTitle());
				result.setType(q.getType());
				result.setExplaination(q.getExplaination());
				return result;
			}

			private Video convertVideo(com.mdedu.serverapi.message.Video v) {
				Video video= new Video();
				Video pVideo=session.getVideoDao().load(Long.valueOf(v.getId()));
				video.setId(Long.valueOf(v.getId()));
				video.setCharpaterId(Long.valueOf(v.getChId()));
				video.setName(v.getSavedName());
				video.setLink(v.getName());
				if(pVideo!=null&&pVideo.getDownloadId()!=null){
					video.setDownloadId(pVideo.getDownloadId());
				}
				return video;
			}

			@Override
			protected void onPostExecute(List<Charpater> result) {
				fragment.dismiss();
				adapter.setData(result);
			}

		}.execute();
	}

	@Override
	public View createOrUpdateView(Charpater item, View convertView,
			int position, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) this.getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View result = inflater.inflate(R.layout.chapter_item, parent, false);
		TextView t = (TextView) result.findViewById(R.id.chapter_name);
		t.setText(adapter.getItem(position).getName());
		return result;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Charpater c = adapter.getItem(position);
		if (c.getChidrenCharpeter().size() > 0) {
			ChapterFragment fragment = new ChapterFragment();
			FragmentTransaction transaction = getFragmentManager()
					.beginTransaction();
			Bundle args = new Bundle();
			args.putString("title", c.getName());
			args.putLong("parentId", c.getId());
			fragment.setArguments(args);
			transaction.replace(R.id.fragment_container, fragment);
			transaction.addToBackStack(null);
			transaction.commit();
		} else {
			ChapterDetailFragement fragment = new ChapterDetailFragement();
			FragmentTransaction transaction = getFragmentManager()
					.beginTransaction();
			Bundle args = new Bundle();
			args.putString("title", c.getName());
			args.putLong("chId", c.getId());
			fragment.setArguments(args);
			transaction.replace(R.id.fragment_container, fragment);
			transaction.addToBackStack(null);
			transaction.commit();
		}
	}
}

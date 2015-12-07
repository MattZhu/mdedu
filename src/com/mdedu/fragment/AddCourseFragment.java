package com.mdedu.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mdedu.R;
import com.mdedu.domainobject.Course;
import com.mdedu.domainobject.CourseDao;
import com.mdedu.domainobject.DaoSession;
import com.mdedu.serverapi.Constants;
import com.mdedu.serverapi.JsonApi;
import com.mdedu.serverapi.message.CourseResponse;
import com.mdedu.serverapi.message.Grade;
import com.mdedu.serverapi.message.Subject;
import com.mdedu.utils.AsyncTaskEx;
import com.mdedu.utils.DatabaseHelper;
import com.mdedu.utils.ImageLoadTask;
import com.mdedu.utils.ListAdapter;
import com.mdedu.utils.ListItemViewCreator;

public class AddCourseFragment extends Fragment implements
		ListItemViewCreator<Course>, OnClickListener {

	private static final String TAG="AddCourseFragment";
	
	private DaoSession session;

	private CourseDao dao;

	private JsonApi<CourseResponse> courseApi;

	private ListAdapter<Course> adapter;
	
	private int refreshMenu;

	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		session = DatabaseHelper.getDaoSession(this.getActivity());
		dao = session.getCourseDao();
		courseApi = new JsonApi<CourseResponse>(CourseResponse.class);
		setHasOptionsMenu(true);
		((ActionBarActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		adapter = new ListAdapter<Course>(this.getActivity(), this);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {		
		this.getActivity().setTitle(R.string.add_course);
		View result = inflater.inflate(R.layout.fragment_add_course, container,false);		
		initViews(result);
		return result;
	}

	private void initViews(View view) {


		ListView list = (ListView) view.findViewById(R.id.content_list);
		list.setAdapter(adapter);
		refreshUI();
//		View v=view.findViewById(R.id.back_btn);
//		v.setOnClickListener(this);
//		v=view.findViewById(R.id.refresh);
//		v.setOnClickListener(this);
		
	}
	

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateOptionsMenu(android.view.Menu, android.view.MenuInflater)
	 */
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.clear();
		super.onCreateOptionsMenu(menu, inflater);
		MenuItem item=menu.add(1, 1, 100, "fresh");
	
		item.setIcon(R.drawable.refresh);
		item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		refreshMenu=item.getItemId();
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		Log.d("AddCourseFragment","upand back clicked:"+item.getItemId());
	    switch (item.getItemId()) {
	    // Respond to the action bar's Up/Home button
	    case android.R.id.home:	    
	    	getFragmentManager().popBackStackImmediate();
	        return true;
	    }
	    if(refreshMenu==item.getItemId()){
			refreshUI();
	    	return true;
	    }
	    return super.onOptionsItemSelected(item);
	}
	@Override
	public View createOrUpdateView(final Course item, View convertView, int position,
			ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) this.getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View result = inflater.inflate(R.layout.course_item, parent,false);
		TextView name = (TextView) result.findViewById(R.id.course_name);
		name.setText(item.getSubject());
		TextView grade = (TextView) result.findViewById(R.id.course_grade);
		grade.setText(item.getGrade());
		TextView publisher=(TextView)result.findViewById(R.id.coures_publisher);
		publisher.setText(item.getPublisher());
		if (item.getImage() != null) {
			ImageView courseImage = (ImageView) result
					.findViewById(R.id.course_image);

			ImageLoadTask task = new ImageLoadTask(courseImage,
					this.getActivity());

			task.execute(Constants.IMAGE_URL + item.getImage(), "courseImage",
					item.getImage());
		}
		ImageView checkStatus = (ImageView) result
				.findViewById(R.id.checked_status);
		if(item.getSaved()!=null){
			checkStatus
				.setImageResource(item.getSaved() ? R.drawable.checkmark_checked
						: R.drawable.checkmark_unchecked);
		}
		checkStatus.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				if(item.getSaved()!=null){
					item.setSaved(!item.getSaved());
				}else{
					item.setSaved(true);
				}
				dao.insertOrReplace(item);
				ImageView checked=(ImageView)v;
				checked.setImageResource(item.getSaved()?R.drawable.checkmark_checked
						: R.drawable.checkmark_unchecked);
			}});
		return result;
	}
	
	private void refreshUI(){
		final LoadingDialogFragment fragment = new LoadingDialogFragment();
		fragment.show(this.getActivity().getSupportFragmentManager(), "loading");
		new AsyncTaskEx<Void, Void, List<Course>>() {

			@Override
			protected List<Course> doInBackground(Void... params) {
				
				List<Course> result = new ArrayList<Course>();
				try{
				courseApi.setBaseUrl(courseApi.getHost() + "/courses/clients");
				CourseResponse response = courseApi.get("bac", "aa", null);
				Log.d("AddCourseFragment",
						"response size:" + response.getCourses().length);
				SparseArray<Grade> gradeSA = new SparseArray<Grade>();
				SparseArray<Subject> subjectSA = new SparseArray<Subject>();

				for (Grade g : response.getGrades()) {
					gradeSA.put(g.getId(), g);
				}

				for (Subject s : response.getSubjects()) {
					subjectSA.put(s.getId(), s);
				}
				SparseArray<Course> courseSA=new SparseArray<Course>();
				for(Course c:dao.loadAll()){
					courseSA.put(c.getId().intValue(), c);
				}
				Course savedCourse;
				for (com.mdedu.serverapi.message.Course c : response
						.getCourses()) {
					Course c1 = new Course();
					c1.setGrade(gradeSA.get(c.getGradeId().intValue())
							.getName());
					c1.setSubject(subjectSA.get(c.getSubjectId().intValue())
							.getName());
					c1.setId(c.getId());
					c1.setImage(c.getImagePath());
					c1.setPublisher(c.getPublisher());
					savedCourse=courseSA.get(c.getId().intValue());
					if(savedCourse!=null){
						c1.setSaved(savedCourse.getSaved());
					}
					c1.setName(c.getName());
					result.add(c1);
				}
				}catch(Exception e){
					Log.e(TAG,"error go the result",e);
				}
				return result;
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see com.mdedu.utils.AsyncTaskEx#onPostExecute(java.lang.Object)
			 */
			@Override
			protected void onPostExecute(List<Course> result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				fragment.dismiss();
				adapter.setData(result);
			}

		}.execute();
	}
	
	
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		}
		
	}
}

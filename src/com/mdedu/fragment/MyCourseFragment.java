package com.mdedu.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.mdedu.R;
import com.mdedu.custom.SwipeListView;
import com.mdedu.domainobject.Course;
import com.mdedu.domainobject.CourseDao;
import com.mdedu.domainobject.DaoSession;
import com.mdedu.serverapi.Constants;
import com.mdedu.utils.DatabaseHelper;
import com.mdedu.utils.ImageLoadTask;
import com.mdedu.utils.ListAdapter;
import com.mdedu.utils.ListItemViewCreator;

public class MyCourseFragment extends Fragment implements OnClickListener,
		ListItemViewCreator<Course>, OnItemClickListener {

	private static final String TAG = "MyCourseFragment";

	private DaoSession session;

	private CourseDao dao;

	private ListAdapter<Course> adapter;

	private SwipeListView listview;
	
	private int addCourseMenu;
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate()");
		session = DatabaseHelper.getDaoSession(this.getActivity());
		dao = session.getCourseDao();
		adapter = new ListAdapter<Course>(this.getActivity(), this);

		setHasOptionsMenu(true);
		getFragmentManager().addOnBackStackChangedListener(
				new FragmentManager.OnBackStackChangedListener() {
					public void onBackStackChanged() {
						// Update your UI here.
						if(getFragmentManager().getBackStackEntryCount()==0){
							((ActionBarActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
							updateUI();
						}
					}
				});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		this.getActivity().setTitle(R.string.my_courses);
		View result = inflater
				.inflate(R.layout.fragment_list, container, false);
		initViews(result);

		return result;
	}

	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.clear();
		super.onCreateOptionsMenu(menu, inflater);
		MenuItem item=menu.add(1, 2, 100, "add");
	
		item.setIcon(R.drawable.plus);
		item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		addCourseMenu=item.getItemId();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		Log.d("MyCourseFragment","onResume().");
		updateUI();
	}
	
	

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onHiddenChanged(boolean)
	 */
	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if(!hidden){
			this.getActivity().setTitle(R.string.my_courses);
		}
	}

	private void initViews(View view) {
//		View v = view.findViewById(R.id.plus);
//		v.setOnClickListener(this);
		this.getActivity().setTitle(R.string.my_courses);
		listview = (SwipeListView) view.findViewById(R.id.content_list);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(this);
	}

	private void updateUI() {
		//
		this.getActivity().setTitle(R.string.my_courses);
		adapter.setData(dao.queryBuilder()
				.where(CourseDao.Properties.Saved.eq(true)).list());
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onViewStateRestored(android.os.Bundle)
	 */
	@Override
	public void onViewStateRestored(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewStateRestored(savedInstanceState);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		Log.d("MyCourseFragment","upand back clicked:"+item.getItemId());
	    switch (item.getItemId()) {
	    
	    }
	    if(item.getItemId()==addCourseMenu){
			AddCourseFragment fragment = new AddCourseFragment();
			FragmentTransaction transaction = getFragmentManager()
					.beginTransaction();
			transaction.addToBackStack(null);
			transaction.replace(R.id.fragment_container, fragment);			
			
			transaction.commit();
			return true;
	    }
	    return super.onOptionsItemSelected(item);
	}
	

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onPause()
	 */
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.d("MyCourseFragment","onPause().");
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		}

	}

	@Override
	public View createOrUpdateView(final Course item, View convertView,
			int position, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) this.getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View result = inflater.inflate(R.layout.my_coures_item, parent, false);
		TextView name = (TextView) result.findViewById(R.id.course_name);
		name.setText(item.getSubject());
		TextView grade = (TextView) result.findViewById(R.id.course_grade);
		grade.setText(item.getGrade());
		if (item.getImage() != null) {
			ImageView courseImage = (ImageView) result
					.findViewById(R.id.course_image);

			ImageLoadTask task = new ImageLoadTask(courseImage,
					this.getActivity());

			task.execute(Constants.IMAGE_URL + item.getImage(), "courseImage",
					item.getImage());
		}
		TextView publisher = (TextView) result
				.findViewById(R.id.coures_publisher);
		publisher.setText(item.getPublisher());
		result.findViewById(R.id.checked_status).setVisibility(View.GONE);
		View rightLayout = result.findViewById(R.id.layout2);
		View v = result.findViewById(R.id.delete_btn);
		v.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				item.setSaved(false);
				dao.update(item);
				updateUI();
			}
		});
		LinearLayout.LayoutParams lp2 = new LayoutParams(
				listview.getRightViewWidth(), LayoutParams.MATCH_PARENT);
		rightLayout.setLayoutParams(lp2);
		return result;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		ChapterFragment fragment = new ChapterFragment();
		FragmentTransaction transaction = getFragmentManager()
				.beginTransaction();
		Bundle args = new Bundle();
		Course c = adapter.getItem(position);
		args.putString("title", c.getSubject() + "-" + c.getGrade());
		args.putLong("couseId", c.getId());
		fragment.setArguments(args);
		transaction.addToBackStack(null);
		transaction.replace(R.id.fragment_container, fragment);

		transaction.commit();
	}
}

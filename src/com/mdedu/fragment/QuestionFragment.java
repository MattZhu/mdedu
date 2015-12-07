package com.mdedu.fragment;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.mdedu.R;
import com.mdedu.domainobject.DaoSession;
import com.mdedu.domainobject.QuesitonAnswer;
import com.mdedu.domainobject.QuesitonAnswerDao;
import com.mdedu.domainobject.Question;
import com.mdedu.domainobject.QuestionAttempt;
import com.mdedu.utils.DatabaseHelper;

public class QuestionFragment extends Fragment implements OnClickListener {
	private DaoSession session;

	private Long chId;

	private List<Question> questions;

	private WebView webview;

	private long start;
	
	private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		session = DatabaseHelper.getDaoSession(this.getActivity());
		setHasOptionsMenu(true);
		((ActionBarActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		chId = this.getArguments().getLong("chId");
		start=System.currentTimeMillis();
		Log.d("QuestionFragment", "Chapter Id:" + chId);
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
		View result = inflater.inflate(R.layout.fragment_question, container,
				false);
		initViews(result);
		return result;
	}

	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.clear();
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.d("ChapterFragment","upand back clicked:"+item.getItemId());
	    switch (item.getItemId()) {
	    // Respond to the action bar's Up/Home button
	    case android.R.id.home:	    
	    	getFragmentManager().popBackStackImmediate();
	        return true;
	    }
	    return super.onOptionsItemSelected(item);
	}
	
	@SuppressLint({ "SetJavaScriptEnabled", "JavascriptInterface" })
	private void initViews(View result) {
		//TextView view = (TextView) result.findViewById(R.id.title_name);
		String title = getArguments().getString("title");
		this.getActivity().setTitle(title);

		//result.findViewById(R.id.back_btn).setOnClickListener(this);
		webview = (WebView) result.findViewById(R.id.webview);
		webview.getSettings().setJavaScriptEnabled(true);

		questions = session.getQuestionDao()._queryCharpater_Questions(chId);
		JavascriptInterfaceImpl javasriptInterface = new JavascriptInterfaceImpl(
				this.getActivity());

		
		webview.addJavascriptInterface(javasriptInterface, "Android");
		
		Long attempId = this.getArguments().getLong("attempId",-1l);
		if(attempId!=-1l){
		webview.loadDataWithBaseURL("file:///android_asset/index.html",
				getQuestionsWithAnswer(attempId), "text/html", "UTF-8", "");
		}else{
			webview.loadDataWithBaseURL("file:///android_asset/index.html",
					getQuestionsText(), "text/html", "UTF-8", "");
			
		}
		// webview.setWebChromeClient(new WebChromeClient(){
		//
		// /* (non-Javadoc)
		// * @see
		// android.webkit.WebChromeClient#onJsAlert(android.webkit.WebView,
		// java.lang.String, java.lang.String, android.webkit.JsResult)
		// */
		// @Override
		// public boolean onJsAlert(WebView view, String url, String message,
		// JsResult result) {
		// Log.d("Alert",message);
		// return false;
		// // return super.onJsAlert(view, url, message, result);
		// }
		//
		//
		// });
	}
	private String getQuestionsWithAnswer(Long attemptId) {
		StringBuilder sb = new StringBuilder();
		getHeader(sb);
		QuestionAttempt qAttempt=session.getQuestionAttemptDao().load(attemptId);
//		Log.d("QuestionFragmetn","User Answer "+answer);
		int i = 0;	
		
		sb.append("<p class='course-quiz-feedback'>您于<strong>").
		append(format(qAttempt.getSubmitDate())).append("</strong>提交的答案，用时<strong>")
		.append(getTimeString(qAttempt.getTimeUsed())).append("</strong>。总共<strong>").
		append(questions.size()).
		append("</strong>道题，答对<strong>").append(qAttempt.getCorrectCount())
		.append("</strong>道题。</p>");
		List<QuesitonAnswer> userAnswer=qAttempt.getAnswers();
		HashMap<Long,Set<String>>answerMap=new HashMap<Long,Set<String>>();
		for(QuesitonAnswer questionAnswer:userAnswer){
			HashSet<String> sets=new HashSet<String>();
			sets.addAll(Arrays.asList(questionAnswer.getAnswer().split(":=;=:")));
			answerMap.put(questionAnswer.getQuestionId(), sets);
		}
		
		for (Question q : questions) {
			i++;
			boolean correct=true;
			//question begin;
			sb.append("<div class='course-quiz-question-body' id='q").append(i)
					.append("'>");
			//question header
			sb.append("<h3 class='course-quiz-question-number'>问题 ").append(i)
					.append("</h3>");
			//question description
			sb.append("<div class='course-quiz-question-text' id='qt")
					.append(i).append("'>").append(q.getTitle())
					.append("</div>");
			sb.append("<table class='table'><tbody><tr><th>你的回答</th><th></th></tr>");

			String op = q.getOptions();
			if (op != null) {

				String type = q.getType().equals("2") ? "checkbox" : "radio";
				Set<String> answerSet=answerMap.get(q.getId());
				String[] options = op.split(":=;=:");
				int oId = 0;
				String checked="checked";
				Set<String>correctAnsers=new HashSet<String>();
				correctAnsers.addAll(Arrays.asList(q.getCorrectAnswer().split(":=;=:")));
				for(String option:options){
					oId++;
					String oIdStr=oId+"";
					Log.d("QuestionFragment", "AnswerSet:"+answerSet);
					if(answerSet!=null&&answerSet.contains(oIdStr)){
						checked="checked";
					}else{
						checked="";
					}
					sb.append("<tr>");
					sb.append("<td class='course-quiz-student-answer' >")
					.append("<input class='course-quiz-input' type='").append(type).
					append("' disabled='' ")
					.append(checked).append(" >").append(option)
					.append("</td>");
					if(q.getType().equals("2")){
						if(answerSet!=null){
							if(answerSet.contains(oIdStr)){
								if(correctAnsers.contains(oIdStr)){
									sb.append("<td><span class='course-quiz-answer-correct' title='Correct' alt='Correct'><span class='icon-ok' alt='Correct'></span></span></td>");
								}else{
									sb.append("<td><span class='course-quiz-answer-incorrect' title='Incorrect' alt='Incorrect'><span class='icon-remove' alt='Incorrect'></span></span></td>");
									correct=false;
								}
							}else{
								if(!correctAnsers.contains(oIdStr)){
									sb.append("<td><span class='course-quiz-answer-correct' title='Correct' alt='Correct'><span class='icon-ok' alt='Correct'></span></span></td>");
								}else{
									correct=false;
									sb.append("<td><span class='course-quiz-answer-incorrect' title='Incorrect' alt='Incorrect'><span class='icon-remove' alt='Incorrect'></span></span></td>");
								}
							}
						}else{
							correct=false;
							sb.append("<td></td>");							
						}
					}
					else{
						if(answerSet!=null&&answerSet.contains(oIdStr)){
							if(correctAnsers.contains(oIdStr)){
								sb.append("<td><span class='course-quiz-answer-correct' title='Correct' alt='Correct'><span class='icon-ok' alt='Correct'></span></span></td>");
							}else{
								correct=false;
								sb.append("<td><span class='course-quiz-answer-incorrect' title='Incorrect' alt='Incorrect'><span class='icon-remove' alt='Incorrect'></span></span></td>");
							}
						}else{
							if(answerSet==null){
								correct=false;
							}
							sb.append("<td></td>");
						}
						
					}
					sb.append("</tr>");
				}
			}
			sb.append("</tbody></table>");
			if(!correct){
				sb.append("<div><p>解答提示:</p><p>").append(q.getExplaination()).append("</p></div>");
			}
			sb.append("</div>");// question end

		}
		getFooter(sb);
		return sb.toString();

	}

	private Object format(Date submitDate) {
		return sdf.format(submitDate);
	}

	private Object getTimeString(Integer timeUsed) {
				int sec=timeUsed%60;
				int min=(timeUsed/60)%60;
				int hour=timeUsed/3600;
		return hour>0?hour+"时"+min+"分"+sec+"秒":min>0?min+"分"+sec+"秒":sec+"秒";
	}

	private int getCorrectCount(Map<Long, Set<String>> answerMap) {
		int result=0;
		for(Question q:questions){
			if(answerMap.get(q.getId())!=null){
				Set<String> correctSet=new HashSet<String>();
				Set<String> answerSet=answerMap.get(q.getId());
				correctSet.addAll(Arrays.asList(q.getCorrectAnswer().split(":=;=:")));
				
				if(correctSet.size()==answerSet.size()){
					boolean correct=true;
					for(String s:answerSet){
						if(!correctSet.contains(s)){
							correct=false;
							break;
						}
					}
					if(correct){
						result++;
					}
				}
			}
		}
		return result;
	}

	private void getHeader(StringBuilder sb) {
		sb.append("<html><head><link rel='stylesheet' href='jqmath-0.4.0.css'>")
				.append("<link rel='stylesheet' href='question.css'>")
				.append("<script src='jquery-1.4.3.min.js'></script>")
				.append("<script src='jqmath-etc-0.4.2.min.js'></script>")
				.append("<script src='jscurry-0.3.0.min.js'></script>")
				.append("</head><body>");
	}

	private String getQuestionsText() {
		StringBuilder sb = new StringBuilder();
		getHeader(sb);
		sb.append("	<form method=\"post\" id=\"quiz_form\" accept-charset=\"UTF-8\">");
		int i = 0;
		for (Question q : questions) {
			i++;
			sb.append("<div class='course-quiz-question-body' id='q").append(i)
					.append("'>");
			sb.append("<h3 class='course-quiz-question-number'>问题 ").append(i)
					.append("</h3>");
			sb.append("<div class='course-quiz-question-text' id='qt")
					.append(i).append("'>").append(q.getTitle())
					.append("</div>");
			sb.append("<div class='course-quiz-options'>");
			String op = q.getOptions();
			String type = q.getType().equals("2") ? "checkbox" : "radio";
			if (op != null) {
				String[] options = op.split(":=;=:");
				int oId = 0;
				for (String option : options) {
					oId++;
					String oIdStr = q.getId() + "_" + oId;
					sb.append("<div class='course-quiz-option'>");
					sb.append("<input class='course-quiz-input' name='qanswer_")
							.append(q.getId()).append("' id='").append(oIdStr)
							.append("' type='").append(type)
							.append("' value='").append(oId).append("'/>");
					sb.append("<label for='").append(oIdStr).append("'>");
					sb.append(option).append("</label>");
					sb.append("</div>");
				}
			}
			sb.append("</div>");// question option end
			sb.append("</div>");// question end

		}
		sb.append("<div class=\"course-quiz-submit-button-container\">")
				.append("<input class=\"btn btn-success\" type=\"submit\" name=\"submit_answers\" value=\"提交\">")
				.append("</div>");
		// sb.append("<textarea></textarea>");
		sb.append("</form>").append("<script src='app.js'></script>");
		getFooter(sb);
		return sb.toString();
	}

	private void getFooter(StringBuilder sb) {
		sb.append("</body>").append("</html>");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		}

	}
	public Long saveAnswer(String answer) {
		
		String answers[]=answer.replaceAll("qanswer_", "").split("&");
		Map<Long,Set<String>> answerMap=new HashMap<Long,Set<String>>();
		for(String a:answers){
			if(!a.isEmpty()){
				Log.d("QuestionFragmetn","Answer "+a);
				String []an=a.split("=");
				Set<String> answerSet =answerMap.get(Long.valueOf(an[0]));
				if(answerSet==null){
					answerSet=new HashSet<String>();
					answerMap.put(Long.valueOf(an[0]), answerSet);
				}
				answerSet.add(an[1]);
			}
		}
		
		QuestionAttempt attempt=new QuestionAttempt();
		attempt.setCharpaterId(chId);
		attempt.setSubmitDate(new Date());
		attempt.setTimeUsed((int)(System.currentTimeMillis()-start)/1000);
		attempt.setCorrectCount(getCorrectCount(answerMap));
		session.getQuestionAttemptDao().insert(attempt);
		QuesitonAnswerDao qaDao=session.getQuesitonAnswerDao();
		for(Question q:questions){
			if(answerMap.get(q.getId())!=null){
				QuesitonAnswer qa=	new QuesitonAnswer();
				qa.setAttemptId(attempt.getId());
				Set<String> correctSet=new HashSet<String>();
				Set<String> answerSet=answerMap.get(q.getId());
				correctSet.addAll(Arrays.asList(q.getCorrectAnswer().split(":=;=:")));
				boolean correct=true;
				if(answerSet.size()==correctSet.size()){
					for(String an:answerSet){
						if(!correctSet.contains(an)){
							correct=false;
							break;
						}
					}
				}else{
					correct=false;
				}
				String answerStr="";
				for(String s:answerSet){
					answerStr+=":=;=:"+s;
				}
				qa.setAnswer(answerStr.substring(5));
				qa.setQuestionId(q.getId());
				qa.setCorrect(correct);
				qaDao.insert(qa);
			}
		}
		return attempt.getId();
	}

	class JavascriptInterfaceImpl {
		Context mContext;

		JavascriptInterfaceImpl(Context c) {
			mContext = c;
		}

		@JavascriptInterface
		public boolean getAnswer(final String answer) {
			Log.d("JSCallback", answer);
			final Long attemptId=saveAnswer(answer);
			webview.post(new Runnable() {
				public void run() {
					webview.loadDataWithBaseURL(
							"file:///android_asset/index.html",
							getQuestionsWithAnswer(attemptId), "text/html",
							"UTF-8", "");
				}
			});
			return true;
		}
	}
	
	
}

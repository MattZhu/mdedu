package com.mdedu.domainobject;

import java.util.List;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table CHARPATER.
 */
public class Charpater {

    private Long id;
    private Long courseId;
    private Long parent;
    private String name;
    private Integer seq;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient CharpaterDao myDao;

    private Course course;
    private Long course__resolvedKey;

    private Charpater charpater;
    private Long charpater__resolvedKey;

    private List<Charpater> chidrenCharpeter;
    private List<Video> videos;
    private List<Question> questions;
    private List<QuestionAttempt> questionAttempts;

    public Charpater() {
    }

    public Charpater(Long id) {
        this.id = id;
    }

    public Charpater(Long id, Long courseId, Long parent, String name, Integer seq) {
        this.id = id;
        this.courseId = courseId;
        this.parent = parent;
        this.name = name;
        this.seq = seq;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getCharpaterDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Long getParent() {
        return parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    /** To-one relationship, resolved on first access. */
    public Course getCourse() {
        Long __key = this.courseId;
        if (course__resolvedKey == null || !course__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            CourseDao targetDao = daoSession.getCourseDao();
            Course courseNew = targetDao.load(__key);
            synchronized (this) {
                course = courseNew;
            	course__resolvedKey = __key;
            }
        }
        return course;
    }

    public void setCourse(Course course) {
        synchronized (this) {
            this.course = course;
            courseId = course == null ? null : course.getId();
            course__resolvedKey = courseId;
        }
    }

    /** To-one relationship, resolved on first access. */
    public Charpater getCharpater() {
        Long __key = this.parent;
        if (charpater__resolvedKey == null || !charpater__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            CharpaterDao targetDao = daoSession.getCharpaterDao();
            Charpater charpaterNew = targetDao.load(__key);
            synchronized (this) {
                charpater = charpaterNew;
            	charpater__resolvedKey = __key;
            }
        }
        return charpater;
    }

    public void setCharpater(Charpater charpater) {
        synchronized (this) {
            this.charpater = charpater;
            parent = charpater == null ? null : charpater.getId();
            charpater__resolvedKey = parent;
        }
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<Charpater> getChidrenCharpeter() {
        if (chidrenCharpeter == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            CharpaterDao targetDao = daoSession.getCharpaterDao();
            List<Charpater> chidrenCharpeterNew = targetDao._queryCharpater_ChidrenCharpeter(id);
            synchronized (this) {
                if(chidrenCharpeter == null) {
                    chidrenCharpeter = chidrenCharpeterNew;
                }
            }
        }
        return chidrenCharpeter;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetChidrenCharpeter() {
        chidrenCharpeter = null;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<Video> getVideos() {
        if (videos == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            VideoDao targetDao = daoSession.getVideoDao();
            List<Video> videosNew = targetDao._queryCharpater_Videos(id);
            synchronized (this) {
                if(videos == null) {
                    videos = videosNew;
                }
            }
        }
        return videos;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetVideos() {
        videos = null;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<Question> getQuestions() {
        if (questions == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            QuestionDao targetDao = daoSession.getQuestionDao();
            List<Question> questionsNew = targetDao._queryCharpater_Questions(id);
            synchronized (this) {
                if(questions == null) {
                    questions = questionsNew;
                }
            }
        }
        return questions;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetQuestions() {
        questions = null;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<QuestionAttempt> getQuestionAttempts() {
        if (questionAttempts == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            QuestionAttemptDao targetDao = daoSession.getQuestionAttemptDao();
            List<QuestionAttempt> questionAttemptsNew = targetDao._queryCharpater_QuestionAttempts(id);
            synchronized (this) {
                if(questionAttempts == null) {
                    questionAttempts = questionAttemptsNew;
                }
            }
        }
        return questionAttempts;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetQuestionAttempts() {
        questionAttempts = null;
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

}

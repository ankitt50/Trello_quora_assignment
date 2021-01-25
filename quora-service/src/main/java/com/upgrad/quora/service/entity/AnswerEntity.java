package com.upgrad.quora.service.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "answer")
@NamedQueries({@NamedQuery(name = "answerById",query = "SELECT a FROM AnswerEntity a WHERE a.uuid =:uuid")})
public class AnswerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "uuid")
    @Size(max = 200)
    private String uuid;

    @Column(name = "ans")
    @Size(max = 255)
    private String ans;

    @Column(name = "date")
    private LocalDateTime date;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "question_id")
    private QuestionEntity question;

    public AnswerEntity() {

    }

    public AnswerEntity(@Size(max = 200) String uuid, @Size(max = 255) String ans, LocalDateTime date, UserEntity user, QuestionEntity question) {
        this.uuid = uuid;
        this.ans = ans;
        this.date = date;
        this.user = user;
        this.question = question;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getAns() {
        return ans;
    }

    public void setAns(String ans) {
        this.ans = ans;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public QuestionEntity getQuestion() {
        return question;
    }

    public void setQuestion(QuestionEntity question) {
        this.question = question;
    }

    @Override
    public String toString() {
        return "AnswerEntity{" +
            "id=" + id +
            ", uuid='" + uuid + '\'' +
            ", ans='" + ans + '\'' +
            ", date=" + date +
            ", user=" + user +
            ", question=" + question +
            '}';
    }

}
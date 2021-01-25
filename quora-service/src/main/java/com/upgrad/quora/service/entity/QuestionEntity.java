package com.upgrad.quora.service.entity;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "question")
@NamedQueries({@NamedQuery(name = "getAllQuestions", query = "select q from QuestionEntity q"),
    @NamedQuery(name = "getQuestionByUuidAndUserId",query = "SELECT q FROM QuestionEntity q WHERE q.UUID = :uuid AND q.user = :user"),
    @NamedQuery(name = "getQuestionByUuid", query = "SELECT q FROM QuestionEntity q WHERE q.UUID = :uuid"),
    @NamedQuery(name = "getAllQuestionsByUser", query = " SELECT q FROM QuestionEntity q WHERE q.user = :user")})
public class QuestionEntity {

  // primary key
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "uuid")
  @Size(max = 200)
  private String UUID;

  @Column(name = "content")
  @NotNull
  @Size(max = 500)
  private String content;

  @Column(name = "date")
  private LocalDateTime date;

  // a user can post many questions
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id")
  private UserEntity user;

  // a question can have many answers
  @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
  private List<AnswerEntity> answers;

  public List<AnswerEntity> getAnswers() {
    return answers;
  }

  public void setAnswers(List<AnswerEntity> answers) {
    this.answers = answers;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getUUID() {
    return UUID;
  }

  public void setUUID(String UUID) {
    this.UUID = UUID;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
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
}

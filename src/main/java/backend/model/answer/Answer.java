package backend.model.answer;

import backend.model.question.Question;
import backend.model.questionnaire.Questionnaire;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;

@Entity
@Inheritance(strategy= InheritanceType.JOINED)
public abstract class Answer {

    private Long id;
    private Question question;
    private Questionnaire questionnaire;

    @Id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne
    @LazyCollection(LazyCollectionOption.FALSE)
    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    @ManyToOne
    @LazyCollection(LazyCollectionOption.FALSE)
    public Questionnaire getQuestionnaire() { return questionnaire; }

    public void setQuestionnaire(Questionnaire questionnaire) { this.questionnaire = questionnaire; }
}

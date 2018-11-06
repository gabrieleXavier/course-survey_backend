package backend.model.answer;

import backend.model.measurment.Choice;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.Entity;
import javax.persistence.*;

@Entity
public class MultipleAnswer extends Answer {

    private Choice choice;

    public MultipleAnswer(){}

    public MultipleAnswer(Long id){ setId(id); }

    @ManyToOne
    @LazyCollection(LazyCollectionOption.FALSE)
    public Choice getChoice() {
        return choice;
    }

    public void setChoice(Choice choice) {
        this.choice = choice;
    }

}

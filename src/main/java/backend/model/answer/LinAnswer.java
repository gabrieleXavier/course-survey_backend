package backend.model.answer;

import backend.model.measurment.Value;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.Entity;
import javax.persistence.*;

@Entity
public class LinAnswer extends Answer {

    private Value choice;

    public LinAnswer(){}

    public LinAnswer(Long id){ setId(id); }

    @ManyToOne
    @LazyCollection(LazyCollectionOption.FALSE)
    public Value getChoice() {
        return choice;
    }

    public void setChoice(Value choice) {
        this.choice = choice;
    }
}

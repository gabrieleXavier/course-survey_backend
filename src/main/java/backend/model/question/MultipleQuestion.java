package backend.model.question;

import backend.model.measurment.Choice;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.*;
import java.util.List;

@Entity
public class MultipleQuestion extends Question {

    private List<Choice> choices;

    public MultipleQuestion(){}

    public MultipleQuestion(Long id){
        super.setId(id);
    }

    @ManyToMany(cascade={CascadeType.PERSIST, CascadeType.MERGE})
    @LazyCollection(LazyCollectionOption.FALSE)
    public List<Choice> getChoices() {
        return choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }
}

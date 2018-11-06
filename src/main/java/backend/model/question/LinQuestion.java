package backend.model.question;

import backend.model.measurment.Value;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.*;
import java.util.List;

@Entity
public class LinQuestion extends Question{

    private List<Value> choices;

    public LinQuestion(){}

    public LinQuestion(Long id){
        super.setId(id);
    }

    @ManyToMany (cascade={CascadeType.PERSIST, CascadeType.MERGE})
    @LazyCollection(LazyCollectionOption.FALSE)
    public List<Value> getChoices() {
        return choices;
    }

    public void setChoices(List<Value> choices) {
        this.choices = choices;
    }

}

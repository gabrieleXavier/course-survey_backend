package backend.model.answer;

import javax.persistence.Entity;

@Entity
public class EssayAnswer extends Answer {

    private String answer;

    public EssayAnswer(){}

    public EssayAnswer(Long id){ setId(id); }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}

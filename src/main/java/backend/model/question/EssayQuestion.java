package backend.model.question;

import com.google.gson.annotations.SerializedName;

import javax.persistence.Entity;

@Entity
public class EssayQuestion extends Question{

    @SerializedName("max_len")
    private Integer maxLenght;

    public EssayQuestion(){}

    public EssayQuestion(Long id){
        super.setId(id);
    }

    public Integer getMaxLenght() {
        return maxLenght;
    }

    public void setMaxLenght(Integer maxLenght) {
        this.maxLenght = maxLenght;
    }
}

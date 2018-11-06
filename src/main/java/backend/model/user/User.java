package backend.model.user;

import backend.model.course.Course;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;

@Entity
public class User {

    private Long id;
    private String matriculation;
    private String password;
    private String name;
    private List<Course> courses;
    private Role role;

    public User(){}

    public User(Long id){
        setId(id);
    }

    @Id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMatriculation() {
        return matriculation;
    }

    public void setMatriculation(String matriculation) {
        this.matriculation = matriculation;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToMany (cascade = {CascadeType.PERSIST})
    @LazyCollection(LazyCollectionOption.FALSE)
    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    @Enumerated(EnumType.STRING)
    public Role getRole() { return role; }

    public void setRole(Role role) { this.role = role; }
}

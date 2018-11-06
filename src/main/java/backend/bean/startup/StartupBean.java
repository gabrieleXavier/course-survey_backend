package backend.bean.startup;

import backend.dao.AnswerDao;
import backend.dao.UserDao;
import backend.model.answer.EssayAnswer;
import backend.model.answer.LinAnswer;
import backend.model.answer.MultipleAnswer;
import backend.model.course.Course;
import backend.model.measurment.Choice;
import backend.model.measurment.Value;
import backend.model.question.EssayQuestion;
import backend.model.question.LinQuestion;
import backend.model.question.MultipleQuestion;
import backend.model.question.Question;
import backend.model.questionnaire.Questionnaire;
import backend.model.user.Role;
import backend.model.user.User;

import javax.annotation.PostConstruct;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.ejb.Singleton;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

//@Singleton
//@Startup
public class StartupBean {

    @Inject
    private UserDao userDao;
    @Inject
    private AnswerDao answerDao;

    @PostConstruct
    @Transactional
    public void init() {
        EssayQuestion q1 = essayQuestion("Report here suggestions about this course", 10, 1L);

        Choice choice1 = choice(1L, "English");
        Choice choice2 = choice(2L, "Italian");
        List<Choice> choices = new ArrayList<>();
        choices.add(choice1);
        choices.add(choice2);
        MultipleQuestion q2 = multipleQuestion("Which language do you prefer?", choices, 2L);

        Value v1 = value(1L, 1);
        Value v2 = value(2L, 2);
        Value v3 = value(3L, 3);
        Value v4 = value(4L, 4);
        Value v5 = value(5L, 5);
        Value v6 = value(6L, 6);
        Value v7 = value(7L, 7);
        List<Value> values = new ArrayList<>();
        values.add(v1);
        values.add(v2);
        values.add(v3);
        values.add(v4);
        values.add(v5);
        values.add(v6);
        values.add(v7);
        LinQuestion q3 = linQuestion("How do you judge the laboratory lessons?", values, 3L);

        List<Question> questions1 = new ArrayList<>();
        questions1.add(q1);
        questions1.add(q2);
        questions1.add(q3);

        Questionnaire quest1 = questionnaire("course survey", 5L, "false",
                "05/07/2018 - 10:00", "05/06/2018 - 10:30", "12",
                "Mario Rossi", questions1);

        List<Question> questions2 = new ArrayList<>();
        questions2.add(q3);
        Questionnaire quest2 = questionnaire("course evaluation", 6L, "false",
                "05/07/2018 - 10:00", "05/06/2018 - 10:30", "13",
                "Mario Rossi", questions2);

        MultipleAnswer a1 = multipleAnswer(1L, q2, choice2, quest1);
        EssayAnswer a2 = essayAnswer(2L, q1, "I liked it very much", quest1);
        MultipleAnswer a3 = multipleAnswer(3L, q2, choice2, quest1);
        LinAnswer a4 = linAnswer(4L, q3, v1, quest1);
        LinAnswer a5 = linAnswer(5L, q3, v5, quest1);

        LinAnswer a6 = linAnswer(6L, q3, v3, quest2);


        List<Questionnaire> questionnaires1 = new ArrayList<>();
        questionnaires1.add(quest1);
        Course course1 = course(12L, "Deep Networks", 9, "2017/2018", "Master",
                "true", questionnaires1);

        List<Questionnaire> questionnaires2 = new ArrayList<>();
        questionnaires2.add(quest2);
        Course course2 = course(13L, "SWAP", 9, "2017/2018", "Master",
                "true", questionnaires2);

        //Create an instance of Professor
        List<Course> courses1 = new ArrayList<>();
        courses1.add(course1);
        User professor = user(14L, "PROF000", "Mario Rossi", "000",
                Role.PROFESSOR, courses1);

        //Create an instance of Student
        List<Course> courses2 = new ArrayList<>();
        courses2.add(course1);
        courses2.add(course2);
        User student = user(15L, "STUD000", "Gabriele Giannini", "000",
                Role.STUDENT, courses2);

        userDao.save(student);
        userDao.save(professor);
        answerDao.save(a1);
        answerDao.save(a2);
        answerDao.save(a3);
        answerDao.save(a4);
        answerDao.save(a5);
        answerDao.save(a6);
    }

    private EssayQuestion essayQuestion(String questionText, Integer maxLenght, Long id) {
        EssayQuestion essayQuestion = new EssayQuestion();
        essayQuestion.setQuestionText(questionText);
        essayQuestion.setMaxLenght(maxLenght);
        essayQuestion.setId(id);

        return essayQuestion;
    }

    private EssayAnswer essayAnswer(Long id, EssayQuestion question, String answer, Questionnaire questionnaire) {
        EssayAnswer essayAnswer = new EssayAnswer();

        essayAnswer.setId(id);
        essayAnswer.setQuestion(question);
        essayAnswer.setAnswer(answer);
        essayAnswer.setQuestionnaire(questionnaire);

        return essayAnswer;
    }

    private MultipleQuestion multipleQuestion(String questionText, List<Choice> choices, Long id) {
        MultipleQuestion multipleQuestion = new MultipleQuestion();
        multipleQuestion.setQuestionText(questionText);
        multipleQuestion.setChoices(choices);
        multipleQuestion.setId(id);

        return multipleQuestion;
    }

    private MultipleAnswer multipleAnswer(Long id, Question question, Choice choice, Questionnaire questionnaire) {
        MultipleAnswer multipleAnswer = new MultipleAnswer();
        multipleAnswer.setId(id);
        multipleAnswer.setQuestion(question);
        multipleAnswer.setChoice(choice);
        multipleAnswer.setQuestionnaire(questionnaire);

        return multipleAnswer;
    }

    private LinQuestion linQuestion(String questionText, List<Value> values, Long id) {
        LinQuestion linQuestion = new LinQuestion();
        linQuestion.setQuestionText(questionText);
        linQuestion.setChoices(values);
        linQuestion.setId(id);

        return linQuestion;
    }

    private LinAnswer linAnswer(Long id, Question question, Value value, Questionnaire questionnaire) {
        LinAnswer linAnswer = new LinAnswer();
        linAnswer.setId(id);
        linAnswer.setQuestion(question);
        linAnswer.setChoice(value);
        linAnswer.setQuestionnaire(questionnaire);

        return linAnswer;
    }

    private Choice choice(Long id, String choiceText) {
        Choice choice = new Choice();
        choice.setId(id);
        choice.setText(choiceText);

        return choice;
    }

    private Value value(Long id, Integer value) {
        Value v = new Value();
        v.setId(id);
        v.setValue(value);

        return v;
    }

    private Questionnaire questionnaire(String title, Long id, String gps, String deadline, String activation,
                                        String courseId, String professor, List<Question> questions) {
        Questionnaire questionnaire = new Questionnaire();

        questionnaire.setTitle(title);
        questionnaire.setId(id);
        questionnaire.setGps(gps);
        questionnaire.setDeadline(deadline);
        questionnaire.setActivation(activation);
        questionnaire.setCourseId(courseId);
        questionnaire.setProfessor(professor);
        questionnaire.setQuestions(questions);
        questionnaire.setIsTemplate("false");

        return questionnaire;
    }

    private Questionnaire template(String title, Long id, List<Question> questions, String professor) {
        Questionnaire template = new Questionnaire();

        template.setTitle(title);
        template.setId(id);
        template.setQuestions(questions);
        template.setProfessor(professor);
        template.setIsTemplate("true");
        template.setCreator("ADMIN");

        return template;
    }

    private Course course(Long id, String name, Integer cfu, String year, String degree, String active,
                          List<Questionnaire> questionnaires) {
        Course course = new Course();
        course.setId(id);
        course.setName(name);
        course.setCfu(cfu);
        course.setYear(year);
        course.setDegree(degree);
        course.setActive(active);
        course.setQuestionnaires(questionnaires);

        return course;
    }

    private User user(Long id, String matriculation, String name, String password, Role role, List<Course> courses) {
        User user = new User();
        user.setId(id);
        user.setMatriculation(matriculation);
        user.setName(name);
        user.setPassword(password);
        user.setRole(role);
        user.setCourses(courses);

        return user;
    }
}
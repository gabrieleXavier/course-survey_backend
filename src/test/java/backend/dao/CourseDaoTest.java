package backend.dao;


import backend.model.course.Course;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Test;
import org.junit.runners.model.InitializationError;

import static junit.framework.TestCase.assertEquals;

public class CourseDaoTest extends DaoTest {
    private CourseDao courseDao;
    private Course course, anotherCourse;

    @Override
    protected void init() throws InitializationError {
        course = new Course(new Long(100));
        course.setName("TDC");

        anotherCourse = new Course(new Long(101));
        anotherCourse.setName("Analisi I");

        courseDao = new CourseDao();

        try {
            FieldUtils.writeField(courseDao, "entityManager", entityManager, true);
        } catch (IllegalAccessException e) {
            throw new InitializationError(e);
        }
    }

    @Test
    public void testSave() {
        courseDao.save(anotherCourse);

        assertEquals(anotherCourse.getName(), entityManager
                .createQuery("from Course where id =:ID", Course.class)
                .setParameter("ID", anotherCourse.getId())
                .getSingleResult().getName());

    }

    @Test
    public void testSaveUpdate() {
        course = entityManager.find(Course.class, anotherCourse.getId());
        String changedCourse = "Fisica I";
        course.setName(changedCourse);

        courseDao.save(course);

        assertEquals(changedCourse, entityManager
                .createQuery("from Course where id =:ID", Course.class)
                .setParameter("ID", course.getId())
                .getSingleResult()
                .getName());

    }

    @Test
    public void testFindById() {
        courseDao.save(course);

        Course result = courseDao.findById(course.getId());

        assertEquals(course.getId(), result.getId());
        assertEquals(course.getName(), result.getName());
    }
}

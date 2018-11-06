package backend.service;

import backend.dao.CourseDao;
import backend.model.course.Course;
import com.google.gson.JsonElement;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

@Path("/course")
@Produces({"application/json"})
public class CourseResource {

    @Inject
    private CourseDao courseDao;

    @GET
    public String getCourse(@QueryParam("code") Long code) {
        Course course = courseDao.findById(code);
        JsonElement courseJson = courseDao.courseToJson(course);

        return courseJson.toString();
    }
}

package backend.service;

import backend.dao.UserDao;
import backend.model.user.User;
import com.google.gson.JsonElement;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

@Path("/authentication")
@Produces({"application/json"})
public class AuthenticationResource {

    @Inject
    private UserDao userDao;

    @GET
    public String getAuthentication(@QueryParam("matriculation") String matriculation) {
        User user = userDao.findByMatriculation(matriculation);
        JsonElement authentication = userDao.authenticationToJson(user);

        return authentication.toString();
    }
}

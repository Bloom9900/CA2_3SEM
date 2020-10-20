package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.PersonDTO;
import utils.EMF_Creator;
import facades.PersonFacade;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

//Todo Remove or change relevant parts before ACTUAL use
@Path("person")
public class PersonResource {
            
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
       
    private static final PersonFacade facade =  PersonFacade.getFacadeExample(EMF);
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String demo() {
        return "{\"msg\":\"Hello World\"}";
    }
    
    @Path("all")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllPersons() {
        return gson.toJson(facade.getAllPersons());
    }
    
    @Path("{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getPersonByID(@PathParam("id") long id) {
      /*  return gson.toJson(facade.getPersonByID(id));*/
        return "{\"msg\":\"Hello World\"}";
    }
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String addPerson(String person) {
       /* PersonDTO pDTO = gson.fromJson(person, PersonDTO.class);
        PersonDTO pAdded = facade.addPerson(/* getters *//*);
        return gson.toJson(pAdded);*/
        return "{\"msg\":\"Hello World\"}";
    } 
    
    @Path("{hobby}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getPersonByHobby(@PathParam("hobby") String hobby) {
       // return gson.toJson(facade.getPersonByHobby(hobby));
        return "{\"msg\":\"Hello World\"}";
    }
    
    @Path("{city}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getPhoneNumberByCity(@PathParam("city") String city) {
     //   return gson.toJson(facade.getPhoneNumberByCity(city));
     return "{\"msg\":\"Hello World\"}";
    }
    
    @PUT
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public String updatePerson(@PathParam("id") long id,  String person) {
        /*PersonDTO pDTO = GSON.fromJson(person, PersonDTO.class);
        Person p = new Person(pDTO.getfName(), pDTO.getlName(), pDTO.getPhone());
        p.setId(id);
        Person pNew = FACADE.editPerson(p);
        return GSON.toJson(new PersonDTO(pNew));
        */
        return "{\"msg\":\"Hello World\"}";
    }
    
    @Path("../zipcodes")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getZipCodes() {
     return "{\"msg\":\"Hello World\"}";
    }
}

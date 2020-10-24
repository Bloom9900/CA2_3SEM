package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.AddressDTO;
import dto.HobbyDTO;
import dto.PersonDTO;
import dto.PhoneDTO;
import exceptions.DublicateException;
import exceptions.MissingInputException;
import exceptions.ObjectNotFoundException;
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
    @Produces(MediaType.APPLICATION_JSON)
    public String demo() {
        return "{\"msg\":\"Hello World\"}";
    }
    
    
    @Path("all")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllPersons() throws ObjectNotFoundException {
        return gson.toJson(facade.getAllPersons());
    }
    
    @Path("all/zipcodes")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getZipCodes() throws ObjectNotFoundException {
        return gson.toJson(facade.getAllCityInfos());
    }

    @Path("{id}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getPerson(@PathParam("id") long id ) throws ObjectNotFoundException {       
        return gson.toJson(facade.getPerson(id));
    }
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String addPerson(String info) throws MissingInputException, ObjectNotFoundException, DublicateException {
       PersonDTO pDTO = gson.fromJson(info, PersonDTO.class);
       AddressDTO aDTO = gson.fromJson(info, AddressDTO.class);
       PhoneDTO phDTO = gson.fromJson(info, PhoneDTO.class);
       HobbyDTO hDTO = gson.fromJson(info, HobbyDTO.class);
       
       return gson.toJson(facade.addPerson(pDTO.getEmail(), pDTO.getfName(), pDTO.getlName(), phDTO.getpNumbers(), phDTO.getpDescription(), aDTO.getStreet(), aDTO.getAdditionalInfo(), aDTO.getZipCode(), aDTO.getCity(), hDTO.gethNames(), hDTO.gethDescription()));
    } 
    
    
    @Path("hobby/{hobby}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getPersonByHobby(@PathParam("hobby") String hobby) throws ObjectNotFoundException {
        return gson.toJson(facade.getPersonsByHobby(hobby));
    }
    
    @Path("city/{city}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getPhoneNumberByCity(@PathParam("city") String city) throws ObjectNotFoundException {
        return gson.toJson(facade.getPersonsByCity(city));
    }
    
    @PUT
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public String updatePerson(@PathParam("id") long id,  String person) throws MissingInputException, ObjectNotFoundException {
        PersonDTO pDTO = gson.fromJson(person, PersonDTO.class);
        pDTO.setId(id);
        PersonDTO pNew = facade.editPerson(pDTO);
        return gson.toJson(pNew);
    }
    
}

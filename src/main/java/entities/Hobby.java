
package entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

@Entity
public class Hobby implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String hobby;
   
    @ManyToMany(mappedBy ="hobbies", cascade = { CascadeType.PERSIST })
    private List<Person> persons;

    public Hobby(String hobby) {
        this.hobby = hobby;
    }

   

    public Hobby() {
    }

    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }

    
    public void addPerson(Person person) {
        if(person != null) {
            persons.add(person);
        }
    }
    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
    
    
    
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    
}

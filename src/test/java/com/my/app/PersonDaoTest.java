package com.my.app;

import com.my.app.dao.ContactDao;
import com.my.app.dao.PersonDao;
import com.my.app.model.Contact;
import com.my.app.model.Person;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import static org.hamcrest.CoreMatchers.*;

/**
 * Created by mgiec on 9/5/2016.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PersonDaoTest {

    @Autowired
    private PersonDao personDao;

    @Autowired
    private ContactDao contactDao;

    @Test
    public void testSavePerson() {
        String personFirstname = UUID.randomUUID().toString();
        String personLastname = UUID.randomUUID().toString();
        Person person = new Person(personFirstname, personLastname);
        Person personFromDb = personDao.savePerson(person);
        Assert.assertEquals(person, personFromDb);
        System.out.println("Person: " + personFromDb.getFirstName() + personFromDb.getLastname());
    }

    @Test
    public void testSavePersonWithContacts(){
        String personFirstname = UUID.randomUUID().toString();
        String personLastname = UUID.randomUUID().toString();
        Person person = new Person(personFirstname, personLastname);
        List<Contact> contacts = new ArrayList<>();
        for(int i=0; i<4;i++){
            contacts.add(new Contact(UUID.randomUUID().toString(), UUID.randomUUID().toString()));
        }
        Person personFromDb = personDao.savePersonWithContacts(person, contacts);

        List<Contact> contactsFromDb = contactDao.findContactByPerson(personFromDb);
        System.out.println("\nPerson from db - id:" + personFromDb.getId() + " name: " + person.getFirstName() + " lastname: " + personFromDb.getLastname());
        System.out.println("Contacts from db for person: ");

        for(Contact c : contactsFromDb){
            System.out.println(c.getId() + ". " + c.getType() + "| " + c.getValue() + "| " + c.getIdPerson());
        }
        Assert.assertEquals(person, personFromDb);
        Assert.assertThat(contacts, is(contactsFromDb));
    }
}

package com.my.app.dao;

import com.my.app.model.Contact;
import com.my.app.model.Person;

import java.util.List;

/**
 * Created by mgiec on 9/5/2016.
 */

public interface PersonDao {
    Person savePerson(Person person);
    Person findPersonById(int id);
    Person savePersonWithContacts(Person person, List<Contact> newContacts);
}

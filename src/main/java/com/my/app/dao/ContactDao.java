package com.my.app.dao;

import com.my.app.model.Contact;
import com.my.app.model.Person;

import java.util.List;

/**
 * Created by mgiec on 9/5/2016.
 */
public interface ContactDao {
    Contact saveContact(Contact contact);
    Contact findContactById(int id);
    List<Contact> findContactByPerson(Person person);
}

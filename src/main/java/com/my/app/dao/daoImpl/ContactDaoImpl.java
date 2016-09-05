package com.my.app.dao.daoImpl;

import com.my.app.dao.ContactDao;
import com.my.app.model.Contact;
import com.my.app.model.ContactRowMapper;
import com.my.app.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by mgiec on 9/5/2016.
 */

@Repository
public class ContactDaoImpl implements ContactDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Contact saveContact(Contact contact) {
        String query = "INSERT INTO phonebook.contacts(_type, _value, id_person) Values(?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                        PreparedStatement ps =
                                connection.prepareStatement(query, new String[] {"_id"});
                        ps.setString(1, contact.getType());
                        ps.setString(2, contact.getValue());
                        ps.setInt(3, contact.getIdPerson());
                        return ps;
                    }
                },
                keyHolder);
        Contact contactFromDb = null;
        contactFromDb = findContactById(Integer.parseInt(keyHolder.getKey().toString()));
        return  contactFromDb;
    }

    @Override
    public Contact findContactById(int id) {
        String query = "SELECT * FROM phonebook.contacts WHERE _id=?";
        Contact contact = (Contact) jdbcTemplate.queryForObject(
                query, new Object[] {id}, new ContactRowMapper());
        return  contact;

    }

    @Override
    public List<Contact> findContactByPerson(Person person) {
        String query = "SELECT * FROM phonebook.contacts WHERE id_person=?";
        List contacts = jdbcTemplate.query(query, new ContactRowMapper(), new Object[]{person.getId()});
        return contacts;
    }
}

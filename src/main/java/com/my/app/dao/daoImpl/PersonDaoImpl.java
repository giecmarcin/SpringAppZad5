package com.my.app.dao.daoImpl;

import com.my.app.dao.ContactDao;
import com.my.app.dao.PersonDao;
import com.my.app.model.Contact;
import com.my.app.model.Person;
import com.my.app.model.PersonRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by mgiec on 9/5/2016.
 */


@Repository
public class PersonDaoImpl implements PersonDao {

//    @Autowired
//    DataSource dataSource;
//
//    @PostConstruct
//    private void initialize(){
//        setDataSource(dataSource);
//    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ContactDao contactDao;

    @Override
    public Person savePerson(Person person) {
        String query = "INSERT INTO phonebook.people(firstname, lastname) Values(?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(
                new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                        PreparedStatement ps =
                                connection.prepareStatement(query, new String[] {"id_person"});
                        ps.setString(1, person.getFirstName());
                        ps.setString(2, person.getLastname());
                        return ps;
                    }
                },
                keyHolder);
        Person personFromDb = null;
        personFromDb = findPersonById(Integer.parseInt(keyHolder.getKey().toString()));
        return personFromDb;
    }

    @Override
    public Person findPersonById(int id) {
        String query = "SELECT * FROM phonebook.people WHERE id_person=?";
        Person person = (Person) jdbcTemplate.queryForObject(
                query, new Object[] {id}, new PersonRowMapper());
        return  person;
    }

    @Transactional
    @Override
    public Person savePersonWithContacts(Person person, List<Contact> newContacts) {
        Person personFromDb = savePerson(person);
        for(Contact c: newContacts){
            c.setIdPerson(personFromDb.getId());
            contactDao.saveContact(c);
        }
        return personFromDb;
    }
}

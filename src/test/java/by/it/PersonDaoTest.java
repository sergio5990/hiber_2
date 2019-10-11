package by.it;

import by.it.entity.Person;
import by.it.dao.PersonDao;
import by.it.util.EMUtil;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;

public class PersonDaoTest {
    @Before
    public void init() {
        EntityManager entityManager = EMUtil.getEntityManager("by.it.test");
    }

    @Test
    public void testSave() {
        Person test = new Person(null, 30, "Test", "");
        PersonDao personDao = new PersonDao();
        personDao.save(test);
    }
}

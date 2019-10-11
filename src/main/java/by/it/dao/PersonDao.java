package by.it.dao;

import by.it.entity.Person;
import by.it.util.EMUtil;

import javax.persistence.EntityManager;

public class PersonDao {

    public void save(Person person) {
        EntityManager entityManager = EMUtil.getEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(person);
        entityManager.getTransaction().commit();
    }
}

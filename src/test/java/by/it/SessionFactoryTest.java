package by.it;

import by.it.util.SFUtil;
import org.hibernate.Session;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import by.it.entity.Person;

public class SessionFactoryTest {

    @Test
    public void save() {
        Session session = SFUtil.getSession();
        Person person = new Person(null, 50, "Test", "Test");
        session.beginTransaction();
        session.save(person);
        System.out.println("after save");
        session.getTransaction().commit();
        session.close();
    }

    @Test
    public void saveTest() {
        Session session = SFUtil.getSession();
        Person person = new Person(null, 25, "Yuli", "Slabko");
        session.beginTransaction();
        session.save(person);
        session.getTransaction().commit();
        session.evict(person);

        Person personFromDb = session.get(Person.class, person.getId());
        Assert.assertEquals(person, personFromDb);

        session.close();
    }

    @Test
    public void loadTest() {
        Session session = SFUtil.getSession();
        Person person = new Person(null, 50, "Test", "Test");
        session.beginTransaction();
        session.saveOrUpdate(person);
        session.getTransaction().commit();
        session.close();

        session = SFUtil.getSession();
        Person loadedPerson = session.load(Person.class, person.getId());
        System.out.println("after load");
        loadedPerson.getAge();
        session.close();
    }

    @Test
    public void lazyLoadTest() {
        Session session = SFUtil.getSession();
        Person person = new Person(null, 50, "Test", "Test");
        session.beginTransaction();
        session.saveOrUpdate(person);
        session.getTransaction().commit();
        session.close();

        session = SFUtil.getSession();
        Person loadedPerson = session.load(Person.class, person.getId());
        System.out.println(loadedPerson.getClass());
        session.clear();
        loadedPerson.getAge();
        session.close();
    }

    @Test
    public void getTest() {
        Session session = SFUtil.getSession();
        Person person = new Person(null, 50, "Test", "Test");
        session.beginTransaction();
        session.saveOrUpdate(person);
        session.getTransaction().commit();
        session.close();

        session = SFUtil.getSession();
        Person loadedPerson = session.get(Person.class, person.getId());
        System.out.println(loadedPerson.getClass());
        session.close();
    }

    @Test
    public void updateTest() {
        Session session = SFUtil.getSession();
        Person person = new Person(null, 50, "Test", "Test");
        session.beginTransaction();
        session.saveOrUpdate(person);
        session.getTransaction().commit();
        session.close();

        session = SFUtil.getSession();
        session.beginTransaction();
        Person loadedPerson = session.load(Person.class, person.getId());
        loadedPerson.setAge(42);
        session.getTransaction().commit();
        session.close();
    }

    @Test
    public void updateFlushTest() {
        Session session = SFUtil.getSession();
        Person person = new Person(null, 50, "Test", "Test");
        session.beginTransaction();
        session.saveOrUpdate(person);
        session.getTransaction().commit();
        session.close();

        session = SFUtil.getSession();
        Person loadedPerson = session.get(Person.class, person.getId());
        loadedPerson.setAge(42);
        session.refresh(loadedPerson);
        session.close();
    }

    @Test
    public void isDirtyTest() {
        Session session = SFUtil.getSession();
        Person person = new Person(null, 50, "Test", "Test");
        session.beginTransaction();
        session.save(person);
        session.getTransaction().commit();
        session.close();

        session = SFUtil.getSession();
        Person loadedPerson = session.get(Person.class, person.getId());
        loadedPerson.setAge(555);
        System.out.println(session.isDirty());
        session.close();
    }

    @Test
    public void deleteTest() {
        Session session = SFUtil.getSession();
        Person personForDelete = new Person(null, 100, "Deleted", "Man");
        System.out.println("Count before save " + session.createQuery("Select count(*) from Person").getSingleResult());
        session.beginTransaction();
        session.save(personForDelete);
        System.out.println("Count after save " + session.createQuery("Select count(*) from Person").getSingleResult());
        session.delete(personForDelete);
        session.getTransaction().commit();
        System.out.println("Count after delete " + session.createQuery("Select count(*) from Person").getSingleResult());

        session.close();
    }

    @Test
    public void deleteExistedTest() {
        Session session = SFUtil.getSession();
        Person person = new Person(null, 50, "Test", "Test");
        session.beginTransaction();
        session.save(person);
        session.getTransaction().commit();
        session.close();

        session = SFUtil.getSession();
        session.beginTransaction();
        Person personForDelete = session.get(Person.class, person.getId());
        session.delete(personForDelete);
        session.getTransaction().commit();

        session.close();
    }

    @Test
    public void flushCommitTest() {
        Session session = SFUtil.getSession();
        Person person = new Person(null, 25, "Yuli", "Slabko");
        session.beginTransaction();
        session.save(person);
        System.out.println("Person is persisted before commit.");
        session.getTransaction().commit();
        session.close();
    }
}

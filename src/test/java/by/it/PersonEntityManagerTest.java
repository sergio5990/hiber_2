package by.it;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;

import by.it.util.SFUtil;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;

import by.it.entity.Person;
import by.it.util.EMUtil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PersonEntityManagerTest {
    @Test
    public void saveTest() {
        Person person = new Person(null, 25, "Yuli", "Slabko");
        EntityManager em = EMUtil.getEntityManager();
        em.getTransaction().begin();
        em.persist(person);
        System.out.println("after persist");
        em.getTransaction().commit();
        em.clear();

        em.getTransaction().begin();
        Person personFromDb = em.find(Person.class, person.getId());
        Assert.assertEquals(person, personFromDb);
        em.getTransaction().commit();
    }
    @Test
    public void flushAutoJPQLTest() {
        EntityManager entityManager = EMUtil.getEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(new Person(null, 25, "Yuli", "Slabko"));
        entityManager.createQuery( "select d from Department d" ).getResultList();
        entityManager.createQuery( "select p from Person p" ).getResultList();
        entityManager.getTransaction().commit();
        entityManager.close();
    }
    @Test
    public void flushAutoNativeSqlTest() {
        EntityManager entityManager = EMUtil.getEntityManager();
        entityManager.getTransaction().begin();
        System.out.println(entityManager.createNativeQuery( "select count(*) from Person" ).getSingleResult());
        entityManager.persist(new Person(null, 25, "Yuli", "Slabko"));
        System.out.println(entityManager.createNativeQuery( "select count(*) from Person" ).getSingleResult());
        entityManager.getTransaction().commit();
        entityManager.close();
    }
    @Test
    public void flushCommitJPQLTest() {
        EntityManager entityManager = EMUtil.getEntityManager();
        entityManager.getTransaction().begin();
        Person person = new Person(null, 25, "Yuli", "Slabko");
        entityManager.persist(person);
        person.setAge(300);
        entityManager.merge(person);
        entityManager.createQuery( "select d from Department d" )
                .setFlushMode(FlushModeType.COMMIT)
                .getResultList();
        System.out.println(entityManager.createQuery( "select p from Person p" )
                .setFlushMode(FlushModeType.COMMIT)
                .getResultList().size());
        System.out.println(entityManager.createQuery( "select p from Person p" )
                .getResultList().size());
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Test
    public void flushCommitNativeSQLTest() {
        EntityManager entityManager = EMUtil.getEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(new Person(null, 25, "Yuli", "Slabko"));
        System.out.println(entityManager.createNativeQuery( "select * from Person" )
                .setFlushMode(FlushModeType.COMMIT)
                .getResultList().size());

        System.out.println(entityManager.createNativeQuery( "select * from Person" )
                .getResultList().size());
        entityManager.getTransaction().commit();
        entityManager.close();
    }
    @Test
    public void flushManualTest() {
        Session session = SFUtil.getSession();
        session.setHibernateFlushMode(FlushMode.MANUAL);
        session.getTransaction().begin();
        session.persist(new Person(null, 25, "Yuli", "Slabko"));
        assertEquals(1, ((Number) session
                .createNativeQuery("select count(*) from Person")
                .uniqueResult()).intValue());
//        entityManager.flush();
        session.getTransaction().commit();
        session.close();
    }
    @Test
    public void flushOperationOrderTest() {
        EntityManager entityManager = EMUtil.getEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(new Person(null, 25, "Yuli", "Slabko"));
        entityManager.getTransaction().commit();
        entityManager.getTransaction().begin();
        Person person = entityManager.find( Person.class, 1);
        System.out.println(person.getClass());
        entityManager.remove(person);

        entityManager.persist( new Person(null, 22, "Yulij", "Slabko") );
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @AfterClass
    public static void cleanUp() {
        EMUtil.closeEMFactory();
    }
}

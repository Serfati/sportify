package BL.Server.utils;

import BL.Server.ServerSystem;
import DL.Team.Assets.Stadium;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * @author Serfati
 * Description: This class contains the methods for connecting to the database, getting data,
 * updating the database, preparing statements, executing prepared statements,
 * starting transactions, committing transactions, and rolling back transactions
 * @version Id: 1.0
 **/
public class DB implements Serializable {

    @PersistenceUnit
    protected static EntityManagerFactory emf;
    private static final long serialVersionUID = 1L;
    public final static Logger logger = Logger.getLogger(DB.class);
    private static DB instance;

    /**
     * @param _emf- Entity Manager Factory object
     * @return the instance of this facade.
     */
    public static DB getDataBaseInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new DB();
            logger.log(Level.INFO, "DBFacade launched");
        }
        return instance;
    }

    /*
     --------------------------------------------------------------------

                        persistence GENERAL methods

      --------------------------------------------------------------------
    */

    /**
     * Persists the object provided as a parameter to the database.
     *
     * @param entity the entity to persists.
     */
    public static void persist(Object entity) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            em.persist(entity);
            em.getTransaction().commit();
        } catch(Exception e) {
            em.getTransaction().rollback();
            throw e;
        }
    }

    /**
     * Persists the list of objects in the parameter to the database.
     *
     * @param entities the list tof entities to persist.
     */
    public static void persistAll(List<?> entities) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            entities.forEach(em::persist);
            em.getTransaction().commit();
        } catch(Exception e) {
            em.getTransaction().rollback();
            throw e;
        }
    }

    /**
     * Remove the object passes as parameter from the database.
     *
     * @param entity the entity to remove.
     */
    public static void remove(Object entity) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            Object mergedEntity = em.merge(entity);
            em.remove(mergedEntity);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * Remove the list of objects from the database.
     *
     * @param entities the list of objects to remove from the database.
     */
    public static void removeAll(List<?> entities) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            for (Object entity : entities) {
                em.remove(entity);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }


    /**
     * Updates the object passes as parameter from the database.
     *
     * @param entity the entity to update.
     */
    public static Object merge(Object entity) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            entity = em.merge(entity);
            em.getTransaction().commit();
        } catch(Exception e) {
            em.getTransaction().rollback();
            return null;
        } finally {
            em.close();
        }
        return entity;
    }

    public static Object update(String queryName, Object data) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Map<String, Object> map = (Map<String, Object>) data;
        Query fixed = em.createNamedQuery(queryName);
        try {
//            for (Map.Entry<String, Object> entry : map.entrySet()) {
//                String k = entry.getKey();
//                Object v = entry.getValue();
//                fixed = em.createNamedQuery(queryName).setParameter(k,v);
//            }
            map.forEach(fixed::setParameter);
            fixed.executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            return null;
        } finally {
            em.close();
        }
        return data;
    }


    public static List query(String queryName, Object data) {
        EntityManager em = emf.createEntityManager();
        List resultList = null;
        em.getTransaction().begin();
        Map<String, Object> map = (Map<String, Object>) data;
        Query fixed = em.createNamedQuery(queryName);
        try {
            map.forEach(fixed::setParameter);
            System.out.println("\n\nrunning query: " + fixed);
            resultList = fixed.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
        return resultList;
    }

    // Query methods -------------------------------------------------------------------------------

    /**
     * Builds a fixed query from variables
     *
     * @param ql   - sql query with parameters fields
     * @param args - the parameters
     * @return fixed query
     */
    public static Query newQuery(String ql, Object... args) {
        Query query = emf.createEntityManager().createQuery(ql);
        IntStream.range(0, args.length).forEach(i -> query.setParameter(i, args[i]));
        return query;
    }


    /* Simple manual tests ------------------------------------------------------------------------------- */
    // tested - CREATE TRUNCATE INSERT SELECT DROP CONNECTION; left - UPDATE namedQuery Parameters
    public static void main(String[] args) {
        emf = ServerSystem
            .createEntityManagerFactory(ServerSystem.DbSelector.TEST, ServerSystem.Strategy.NONE);
        EntityTransaction txn;
        EntityManager em = emf.createEntityManager();
        txn = em.getTransaction();
        txn.begin();
//        em.createNativeQuery("CREATE TABLE IF NOT EXISTS Standings (id INTEGER PRIMARY KEY, name VARCHAR(50) NOT NULL)").executeUpdate();
//        em.createNativeQuery("TRUNCATE TABLE Standings").executeUpdate(); // clears the table content
//        em.createNativeQuery("INSERT INTO Standings VALUES (1,'one'),(2,'two'),(3,'three'),(4,'four')").executeUpdate(); // insert values
//        em.createNativeQuery("UPDATE Standings SET name='one updated' WHERE id = 1").executeUpdate();
//        Query q = em.createNativeQuery("SELECT a.id, a.name FROM Standings a");
//        List<Object[]> ids = q.getResultList();
//        for(Object[] a : ids) System.out.println("Standings ~ "+a[0]+":"+a[1]);// Show Result
//        em.createNativeQuery("DROP TABLE IF EXISTS Standings").executeUpdate(); // drops the table

        Stadium anfld = new Stadium("Anfield", 80);
        Stadium ot = new Stadium("old traford", 80);
        DB.persist(anfld);
        DB.persist(ot);
        Query q = em.createNativeQuery("SELECT s.name, s.capacity FROM Stadium s");
        List<Object[]> ids = q.getResultList();
        for (Object[] s : ids) {
            System.out.println("Stadium ~ " + s[0] + ":" + s[1]);// Show Result
        }

//        HashMap<String,Object> map = new HashMap<>() ;
//        map.put("name", "anfield");
//        query("stadiumByName", map);

        txn.commit();

        em.close();
        emf.close();
    }
}
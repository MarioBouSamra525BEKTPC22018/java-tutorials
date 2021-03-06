package ljug.control;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import ljug.provide.ServiceFactory;
import ljug.provide.Services;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import static org.junit.Assert.*;

/**
 *
 * @author pfares
 */
public class ServiceControleTest {
   
    public ServiceControleTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getEntityManager method, of class ServiceControle.
     */
    

    /**
     * Test of addUser method, of class ServiceControle.
     */
    @org.junit.Test
    public void testAny() {
        EntityManagerFactory emf
                = Persistence.createEntityManagerFactory("REALMPU");
        
        Services service = ServiceFactory.ServicesBuilder(emf);
        
        service.addUser("pascal", "pascal", null);
        assertNotNull(service.getUser("pascal"));
        service.rmUser("pascal");
        assertNull(service.getUser("pascal"));
        service.addRole("admin");
        assertNotNull(service.getRole("admin"));
        service.rmtRole("admin");
        assertNull(service.getRole("admin"));
        /*
        service.associate("pascal", "admin");
        service.associate("pascal", "prof");
        service.associate("pascal1", "prof");
        
        service.dessociate("pascal1", "prof");
        
        service.rmUser("pascal");
        */
        
    }

   
    
}

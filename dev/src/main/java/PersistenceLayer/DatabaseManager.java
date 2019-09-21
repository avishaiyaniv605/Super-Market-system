package PersistenceLayer;

import PersistenceLayer.Inventory.StockProductDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;

import javax.persistence.criteria.CriteriaBuilder;
import java.sql.*;
import java.util.Properties;

public class DatabaseManager {

    private String connectionString;
    private final SessionFactory sessionFactory = buildSessionFactory();
    private Session session ;
    private CriteriaBuilder cb;
    private static DatabaseManager instance = new DatabaseManager();

    public static DatabaseManager getInstance() {
        return instance;
    }

    private DatabaseManager() {
        this.connectionString = "jdbc:sqlite::resource:DataBase.db";
        // Configure the session factory
        try {
            session = sessionFactory.openSession();
            cb= session.getCriteriaBuilder();
        }
        catch (Exception e){
            System.out.println("Unable to open session");
        }
    }

    //</editor-fold>
    private SessionFactory buildSessionFactory() {
        try {
            // Create the ServiceRegistry from hibernate.cfg.xml
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()//
                    .configure("hibernate.cfg.xml").build();

            // Create a metadata sources using the specified service registry.
            Metadata metadata = new MetadataSources(serviceRegistry).getMetadataBuilder().build();

            return metadata.getSessionFactoryBuilder().build();
        } catch (Throwable ex) {

            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
    public void commitAndFLush(){
        Transaction tx =  session.beginTransaction();
        tx.commit();
    }
    public Session getSession() {
        return session;
    }
    //<editor-fold desc="Getters">
    public String getConnectionString() {
        return connectionString;
    }



    public CriteriaBuilder getCb() {
        return cb;
    }

    public void closeSession() {
        if(session != null) {
            session.close();
        }
    }
}

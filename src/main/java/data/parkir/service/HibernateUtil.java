package data.parkir.service;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtil {

    private static SessionFactory sessionFactory;

    public static synchronized SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                StandardServiceRegistry registry;
                try {
                    registry = new StandardServiceRegistryBuilder().configure("data/parkir/hibernate.cfg.xml").build();
                } catch (Exception e1) {
                    registry = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
                }
                Metadata metadata = new MetadataSources(registry)
                    .getMetadataBuilder()
                    .build();
                sessionFactory = metadata.getSessionFactoryBuilder().build();
            } catch (Exception e) {
                e.printStackTrace();
                throw new ExceptionInInitializerError("Inisialisasi SessionFactory gagal: " + e.getMessage());
            }
        }
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }
}

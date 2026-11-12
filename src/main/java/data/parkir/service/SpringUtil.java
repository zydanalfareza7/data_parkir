package data.parkir.service;

import data.parkir.dao.ParkirDAO;
import data.parkir.dao.ParkirDAOImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringUtil {

    private static ApplicationContext context;
    private static ParkirDAO parkirDAO;

    public static synchronized ApplicationContext getContext() {
        if (context == null) {
            try {
                try {
                    context = new ClassPathXmlApplicationContext("data/parkir/applicationContext.xml");
                } catch (Exception e1) {
                    context = new ClassPathXmlApplicationContext("applicationContext.xml");
                }
                System.out.println("[SPRING] ApplicationContext berhasil di-load!");
            } catch (Exception e) {
                System.err.println("[SPRING NOTICE] Load applicationContext.xml: " + e.getMessage());
            }
        }
        return context;
    }

    public static synchronized ParkirDAO getParkirDAO() {
        if (parkirDAO != null) {
            return parkirDAO;
        }

        try {
            ApplicationContext ctx = getContext();
            if (ctx != null && ctx.containsBean("parkirDAO")) {
                parkirDAO = (ParkirDAO) ctx.getBean("parkirDAO");
                System.out.println("[SPRING] Bean 'parkirDAO' berhasil diinjeksi dari Spring Container.");
                return parkirDAO;
            }
        } catch (Exception ignored) {
        }

        ParkirDAOImpl impl = new ParkirDAOImpl();
        impl.setSessionFactory(HibernateUtil.getSessionFactory());
        parkirDAO = impl;
        return parkirDAO;
    }
}

package data.parkir.dao;

import data.parkir.model.Parkir;
import data.parkir.service.HibernateUtil;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class ParkirDAOImpl implements ParkirDAO {

    private SessionFactory sessionFactory;

    public ParkirDAOImpl() {
    }

    public ParkirDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getSession() {
        if (sessionFactory != null) {
            try {
                return sessionFactory.openSession();
            } catch (Exception ignored) {
            }
        }
        return HibernateUtil.getSessionFactory().openSession();
    }

    @Override
    public boolean simpan(Parkir p) {
        Transaction tx = null;
        try (Session session = getSession()) {
            tx = session.beginTransaction();
            session.save(p);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Parkir p) {
        Transaction tx = null;
        try (Session session = getSession()) {
            tx = session.beginTransaction();
            session.update(p);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean hapus(int id) {
        Transaction tx = null;
        try (Session session = getSession()) {
            tx = session.beginTransaction();
            Parkir p = session.get(Parkir.class, id);
            if (p != null) {
                session.delete(p);
                tx.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Parkir getById(int id) {
        try (Session session = getSession()) {
            return session.get(Parkir.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Parkir getByPlatAktif(String platNomor) {
        try (Session session = getSession()) {
            Query<Parkir> query = session.createQuery(
                "FROM Parkir WHERE LOWER(platNomor) = LOWER(:p) AND status = 'Parkir'", Parkir.class
            );
            query.setParameter("p", platNomor.trim());
            query.setMaxResults(1);
            List<Parkir> list = query.list();
            return list.isEmpty() ? null : list.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Parkir> getAll() {
        try (Session session = getSession()) {
            return session.createQuery("FROM Parkir ORDER BY id DESC", Parkir.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public List<Parkir> getAktif() {
        try (Session session = getSession()) {
            return session.createQuery("FROM Parkir WHERE status = 'Parkir' ORDER BY jamMasuk DESC", Parkir.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public long hitungAktifByJenis(String jenis) {
        try (Session session = getSession()) {
            Query<Long> query = session.createQuery(
                "SELECT COUNT(p) FROM Parkir p WHERE p.status = 'Parkir' AND LOWER(p.jenis) = LOWER(:j)", Long.class
            );
            query.setParameter("j", jenis);
            Long count = query.uniqueResult();
            return count != null ? count : 0L;
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    @Override
    public List<Parkir> getByRentangTanggal(LocalDateTime awal, LocalDateTime akhir) {
        try (Session session = getSession()) {
            Query<Parkir> query = session.createQuery(
                "FROM Parkir WHERE jamMasuk >= :awal AND jamMasuk <= :akhir ORDER BY jamMasuk DESC", Parkir.class
            );
            query.setParameter("awal", awal);
            query.setParameter("akhir", akhir);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public int hitungTotalPendapatan() {
        try (Session session = getSession()) {
            Query<Long> query = session.createQuery(
                "SELECT COALESCE(SUM(totalBayar), 0) FROM Parkir WHERE status = 'Selesai'", Long.class
            );
            Long sum = query.uniqueResult();
            return sum != null ? sum.intValue() : 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}

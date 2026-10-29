package mate.academy.dao.impl;

import jakarta.persistence.EntityManager;
import mate.academy.dao.TicketDao;
import mate.academy.model.Ticket;
import mate.academy.util.HibernateUtil;

public class TicketDaoImpl implements TicketDao {

    private EntityManager getEntityManager() {
        return HibernateUtil.getEntityManager();
    }

    @Override
    public Ticket add(Ticket ticket) {
        try (EntityManager em = HibernateUtil.getEntityManager()) {
            em.getTransaction().begin();
            em.persist(ticket);
            em.getTransaction().commit();
            return ticket;
        }
    }
}

package mate.academy.service.impl;

import java.util.List;
import mate.academy.dao.ShoppingCartDao;
import mate.academy.dao.TicketDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.lib.Inject;
import mate.academy.lib.Service;
import mate.academy.model.MovieSession;
import mate.academy.model.ShoppingCart;
import mate.academy.model.Ticket;
import mate.academy.model.User;
import mate.academy.service.ShoppingCartService;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Inject
    private ShoppingCartDao shoppingCartDao;
    @Inject
    private TicketDao ticketDao;

    @Override
    public void addSession(MovieSession movieSession, User user) {
        if (shoppingCartDao.getByUser(user).isPresent()) {
            Ticket ticket = new Ticket();
            ticket.setMovieSession(movieSession);
            ticket.setUser(user);
            Ticket ticketFromDb = ticketDao.add(ticket);

            ShoppingCart shoppingCart = getByUser(user);
            shoppingCart.getTickets().add(ticketFromDb);
            shoppingCartDao.update(shoppingCart);
        } else {
            throw new DataProcessingException("User " + user + " hasn't shopping cart");
        }
    }

    @Override
    public ShoppingCart getByUser(User user) {
        return shoppingCartDao.getByUser(user)
                .orElseThrow(() -> new DataProcessingException("Can't get shopping cart "
                        + "by user " + user));
    }

    @Override
    public void registerNewShoppingCart(User user) {
        if (shoppingCartDao.getByUser(user).isEmpty()) {
            ShoppingCart shoppingCart = new ShoppingCart();
            shoppingCart.setUser(user);
            shoppingCartDao.add(shoppingCart);
        } else {
            throw new DataProcessingException("User " + user + " has shopping cart yet.");
        }
    }

    @Override
    public void clear(ShoppingCart shoppingCart) {
        if (shoppingCartDao.getByUser(
                shoppingCart.getUser()).isPresent()) {
            shoppingCart.setTickets(List.of());
            shoppingCartDao.update(shoppingCart);
        } else {
            throw new DataProcessingException("Shopping cart " + shoppingCart
                    + " is absent in DB");
        }
    }
}

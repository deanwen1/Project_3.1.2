package springboot.web.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import springboot.web.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    public void addUser(User user) {
        entityManager.persist(user);
    }

    @Override
    public void updateUser(User user) {
        entityManager.merge(user);
    }

    @Override
    public void deleteUser(long id) {
        getUserById(id).getEmail();
        User deletedUser = getUserById(id);
        entityManager.remove(deletedUser);
    }

    @Override
    public User getUserById(long id) {
        return entityManager.find(User.class, id);
    }

    @Override
    @Transactional(readOnly = true)
    public long getUserIdByLogin(String email) {
        long result;
        EntityManager em = getEntityManager();
        List<?> tempResult =
                em.createQuery("SELECT user from User user where user.email = ?1")
                        .setParameter(1, email)
                        .getResultList();
        if (tempResult.size() != 0) {
            result = ((User) tempResult.get(0)).getId();
        } else {
            result = -1;
        }
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> listUsers() {
        return entityManager.createQuery("select user from User user").getResultList();
    }
}
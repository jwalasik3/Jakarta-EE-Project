package com.example.jee_project.user.repository.persistence;

import com.example.jee_project.user.entity.User;
import com.example.jee_project.user.repository.api.UserRepository;
import jakarta.enterprise.context.Dependent;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Dependent
public class UserPersistenceRepository implements UserRepository {

    private EntityManager em;

    @PersistenceContext
    public void setEm(EntityManager em) {

        this.em = em;
    }

    @Override
    public Optional<User> find(UUID id) {

        return em.createQuery("SELECT u FROM User u WHERE u.id = :id", User.class)
                .setParameter("id", id)
                .getResultStream()
                .findFirst();
    }

    @Override
    public List<User> findAll() {

        return em.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    @Override
    public void create(User entity) {

        em.persist(entity);
    }

    @Override
    public void delete(UUID id) {

        em.remove(em.find(User.class, id));
    }

    @Override
    public void update(User entity) {

        em.merge(entity);
    }

    @Override
    public Optional<User> findByLogin(String login) {

        return em.createQuery("SELECT u FROM User u WHERE u.login = :login", User.class)
                .setParameter("login", login)
                .getResultStream()
                .findFirst();
    }
}

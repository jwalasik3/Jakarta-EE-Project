package com.example.jee_project.user.repository.persistence;

import com.example.jee_project.user.entity.User;
import com.example.jee_project.user.entity.User_;
import com.example.jee_project.user.repository.api.UserRepository;
import jakarta.enterprise.context.Dependent;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

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

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> root = cq.from(User.class);
        cq.select(root).where(cb.equal(root.get(User_.id), id));
        return em.createQuery(cq).getResultStream().findFirst();
    }

    @Override
    public List<User> findAll() {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> root = cq.from(User.class);
        cq.select(root);
        return em.createQuery(cq).getResultList();
    }

    @Override
    public void create(User entity) {

        em.persist(entity);
    }

    @Override
    public void delete(UUID id) {

        if (!em.isJoinedToTransaction()) {
            em.joinTransaction();
        }
        em.remove(em.find(User.class, id));
    }

    @Override
    public void update(User entity) {

        if (!em.isJoinedToTransaction()) {
            em.joinTransaction();
        }
        em.merge(entity);
    }

    @Override
    public void detach(User entity) {

        em.detach(entity);
    }

    @Override
    public Optional<User> findByLogin(String login) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> root = cq.from(User.class);
        cq.select(root).where(cb.equal(root.get(User_.login), login));
        return em.createQuery(cq).getResultStream().findFirst();
    }
}

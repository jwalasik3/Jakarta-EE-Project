package com.example.jee_project.note.repository.persistance;

import com.example.jee_project.note.entity.Note;
import com.example.jee_project.note.entity.NoteThread_;
import com.example.jee_project.note.entity.Note_;
import com.example.jee_project.note.repository.api.NoteRepository;
import com.example.jee_project.user.entity.User_;
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
public class NotePersistenceRepository implements NoteRepository {

    private EntityManager em;

    @PersistenceContext
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<Note> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Note> query = cb.createQuery(Note.class);
        Root<Note> root = query.from(Note.class);
        query.select(root);
        return em.createQuery(query).getResultList();
    }

    @Override
    public List<Note> findAll(UUID userId) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Note> query = cb.createQuery(Note.class);
        Root<Note> root = query.from(Note.class);
        query.select(root).where(cb.equal(root.get(Note_.user).get(User_.id), userId));
        return em.createQuery(query).getResultList();
    }

    @Override
    public List<Note> findAllByThread(UUID threadId) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Note> query = cb.createQuery(Note.class);
        Root<Note> root = query.from(Note.class);
        query.select(root).where(cb.equal(root.get(Note_.noteThread).get(NoteThread_.id), threadId));
        return em.createQuery(query).getResultList();
    }

    @Override
    public List<Note> findAllByUsername(String username) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Note> query = cb.createQuery(Note.class);
        Root<Note> root = query.from(Note.class);
        query.select(root).where(cb.equal(root.get(Note_.user).get(User_.login), username));
        return em.createQuery(query).getResultList();
    }

    @Override
    public Optional<Note> find(UUID id) {

        return Optional.ofNullable(em.find(Note.class, id));
    }

    @Override
    public void create(Note entity) {

        if (!em.isJoinedToTransaction()) {
            em.joinTransaction();
        }
        em.persist(entity);
    }

    @Override
    public void delete(UUID id) {

        if (!em.isJoinedToTransaction()) {
            em.joinTransaction();
        }
        Note toRemove = em.find(Note.class, id);
        if (toRemove != null) {
            em.remove(toRemove);
        }
    }

    @Override
    public void detach(Note entity) {
        em.detach(entity);
    }


    @Override
    public void update(Note entity) {

        if (!em.isJoinedToTransaction()) {
            em.joinTransaction();
        }
        em.merge(entity);
    }
}

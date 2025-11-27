package com.example.jee_project.note.repository.persistance;

import com.example.jee_project.note.entity.Note;
import com.example.jee_project.note.entity.NoteThread;
import com.example.jee_project.note.entity.NoteThread_;
import com.example.jee_project.note.entity.Note_;
import com.example.jee_project.note.repository.api.NoteThreadRepository;
import jakarta.enterprise.context.Dependent;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Dependent
public class NoteThreadPersistenceRepository implements NoteThreadRepository {

    private EntityManager em;

    @PersistenceContext
    public void setEm(EntityManager em) {

        this.em = em;
    }

    @Override
    public Optional<NoteThread> find(UUID id) {

        return Optional.ofNullable(em.find(NoteThread.class, id));
    }

    @Override
    public List<NoteThread> findAll() {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<NoteThread> query = cb.createQuery(NoteThread.class);
        Root<NoteThread> root = query.from(NoteThread.class);
        query.select(root);
        return em.createQuery(query).getResultList();
    }

    @Override
    public void create(NoteThread entity) {

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

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaDelete<Note> noteDelete = cb.createCriteriaDelete(Note.class);
        Root<Note> noteRoot = noteDelete.from(Note.class);
        noteDelete.where(cb.equal(noteRoot.get(Note_.noteThread).get(NoteThread_.id), id));

        CriteriaDelete<NoteThread> threadDelete = cb.createCriteriaDelete(NoteThread.class);
        Root<NoteThread> threadRoot = threadDelete.from(NoteThread.class);
        threadDelete.where(cb.equal(threadRoot.get(NoteThread_.id), id));
    }

    @Override
    public void update(NoteThread entity) {

        if (!em.isJoinedToTransaction()) {
            em.joinTransaction();
        }
        em.merge(entity);
    }

    @Override
    public void detach(NoteThread entity) {

        em.detach(entity);
    }
}

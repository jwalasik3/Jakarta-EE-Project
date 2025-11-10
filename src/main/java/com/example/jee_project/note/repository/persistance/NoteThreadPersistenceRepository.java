package com.example.jee_project.note.repository.persistance;

import com.example.jee_project.note.entity.NoteThread;
import com.example.jee_project.note.repository.api.NoteThreadRepository;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequestScoped
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

        return em.createQuery("SELECT t FROM NoteThread t", NoteThread.class).getResultList();
    }

    @Override
    public void create(NoteThread entity) {

        em.persist(entity);
    }

    @Override
    public void delete(UUID id) {
        em.createQuery("DELETE FROM Note n WHERE n.noteThread.id = :id")
                .setParameter("id", id)
                .executeUpdate();

        NoteThread thread = em.find(NoteThread.class, id);
        if (thread != null) {
            em.remove(thread);
        }
    }

    @Override
    public void update(NoteThread entity) {

        em.merge(entity);
    }
}

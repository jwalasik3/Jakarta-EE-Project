package com.example.jee_project.note.repository.persistance;

import com.example.jee_project.note.entity.Note;
import com.example.jee_project.note.repository.api.NoteRepository;
import jakarta.enterprise.context.Dependent;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

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

        return em.createQuery("select n from Note n", Note.class).getResultList();
    }

    @Override
    public List<Note> findAll(UUID userId) {

        return em.createQuery("SELECT n FROM Note n WHERE n.user.id = :userId", Note.class)
                .setParameter("userId", userId).getResultList();
    }

    @Override
    public List<Note> findAllByThread(UUID threadId) {

        return em.createQuery("SELECT n FROM Note n WHERE n.noteThread.id = :threadId", Note.class)
                .setParameter("threadId", threadId)
                .getResultList();
    }

    @Override
    public List<Note> findAllByUsername(String username) {
        return em.createQuery("SELECT n FROM Note n WHERE n.user.login = :username", Note.class)
                .setParameter("username", username)
                .getResultList();
    }

    @Override
    public Optional<Note> find(UUID id) {

        return Optional.ofNullable(em.find(Note.class, id));
    }

    @Override
    public void create(Note entity) {
        em.persist(entity);
        em.flush();
        System.out.println("[NotePersistenceRepository.create] persisted note id=" + entity.getId() + ", threadId now=" + (entity.getNoteThread() == null ? null : entity.getNoteThread().getId()));
    }

    @Override
    public void delete(UUID id) {
        em.remove(em.find(Note.class, id));
    }

    @Override
    public void update(Note entity) {

        em.merge(entity);
    }
}

package com.example.jee_project.note.view;

import com.example.jee_project.component.ModelFunctionFactory;
import com.example.jee_project.note.model.NoteCreateModel;
import com.example.jee_project.note.model.ThreadModel;
import com.example.jee_project.note.service.NoteService;
import com.example.jee_project.note.service.NoteThreadService;
import jakarta.enterprise.context.Conversation;
import jakarta.enterprise.context.ConversationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * View bean for rendering single character create form. Creating a character is divided into number of steps where each
 * step is separate JSF view. In order to use single bean, conversation scope is used.
 */
@ConversationScoped
@Named
@Log
@NoArgsConstructor(force = true)
public class NoteCreate implements Serializable {

    private final NoteService noteService;
    private final NoteThreadService noteThreadService;
    private final ModelFunctionFactory factory;

    @Getter
    private NoteCreateModel note;
    @Getter
    private List<ThreadModel> threads;

    private final Conversation conversation;

    @Inject
    public NoteCreate(
            NoteService noteService,
            NoteThreadService noteThreadService,
            ModelFunctionFactory factory,
            Conversation conversation
    ) {

        this.noteService = noteService;
        this.factory = factory;
        this.noteThreadService = noteThreadService;
        this.conversation = conversation;
    }

    public void init() {

        if (conversation.isTransient()) {
            threads = noteThreadService.getNoteThreads().stream()
                    .map(factory.threadToModel())
                    .collect(Collectors.toList());
            note = NoteCreateModel.builder()
                    .id(UUID.randomUUID())
                    .build();
            conversation.begin();
        }
    }

    public String goToThreadAction() {

        return "/notes/note_create__thread.xhtml?faces-redirect=true";
    }

    public String goToImportanceAction() {

        return "/notes/character_create__importance.xhtml?faces-redirect=true";
    }

    public Object goToBasicAction() {

        return "/notes/note_create__basic.xhtml?faces-redirect=true";
    }

    public String cancelAction() {

        conversation.end();
        return "/threads/note_thread_list.xhtml?faces-redirect=true";
    }

    public String goToConfirmAction() {

        return "/notes/note_create__confirm.xhtml?faces-redirect=true";
    }

    public String saveAction() {

        noteService.createNote(factory.modelToNote().apply(note));
        conversation.end();
        return "/threads/note_thread_list.xhtml?faces-redirect=true";
    }
}


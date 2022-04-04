package com.example.demo;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

	@Autowired
	private NoteRepository repository;
	
	@GetMapping
	public ResponseEntity<List<Note>> getNotes() {
		List<Note> notes = (List<Note>) repository.findAll();
		return !notes.isEmpty()
				? ResponseEntity.ok(notes)
				: ResponseEntity.noContent().build();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Note> getNote(@PathVariable long id) {
		Optional<Note> note = repository.findById(Long.valueOf(id));
		return note.isPresent()
				? (ResponseEntity<Note>) ResponseEntity.ok(note.get())
				: ResponseEntity.notFound().build();
	}

	@PostMapping
	public ResponseEntity<Note> addNote(@RequestBody Note note) {
		try {
			note = repository.save(note);
			URI uri = linkTo(NoteController.class).slash(note.getId()).toUri();
			return ResponseEntity.created(uri).build();			
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Void> changeNote(@PathVariable long id, @RequestBody Note note) {
		Optional<Note> existing = repository.findById(Long.valueOf(id));
		if (existing.isPresent()) {
			try {
				existing.get().setTitle(note.getTitle());
				existing.get().setContent(note.getContent());
				repository.save(existing.get());
				return ResponseEntity.noContent().build();
			} catch (Exception e) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			}
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> removeNote(@PathVariable long id) {
		if (repository.existsById(Long.valueOf(id))) {
			try {
				repository.deleteById(Long.valueOf(id));
				return ResponseEntity.noContent().build();
			} catch (Exception e) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			}
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}

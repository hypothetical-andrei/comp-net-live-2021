package ro.ase.rest;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

	@Autowired
	private NoteRepository repository;
	
	@GetMapping
	private ResponseEntity<List<Note>> getNotes() {
		List<Note> notes = (List<Note>) repository.findAll();
		return notes.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(notes);
	}
	
	@GetMapping("/{id}")
	private ResponseEntity<Note> getNote(@PathVariable long id) {
		Optional<Note> note = repository.findById(Long.valueOf(id));
		return note.isPresent() ? ResponseEntity.ok(note.get()) : ResponseEntity.notFound().build();
	}
}

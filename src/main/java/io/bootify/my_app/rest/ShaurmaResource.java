package io.bootify.my_app.rest;

import io.bootify.my_app.model.ShaurmaDTO;
import io.bootify.my_app.service.ShaurmaService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/shaurmas", produces = MediaType.APPLICATION_JSON_VALUE)
public class ShaurmaResource {

    private final ShaurmaService shaurmaService;

    public ShaurmaResource(final ShaurmaService shaurmaService) {
        this.shaurmaService = shaurmaService;
    }

    @GetMapping
    public ResponseEntity<List<ShaurmaDTO>> getAllShaurmas() {
        return ResponseEntity.ok(shaurmaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShaurmaDTO> getShaurma(@PathVariable final Long id) {
        return ResponseEntity.ok(shaurmaService.get(id));
    }

    @PostMapping
    public ResponseEntity<Long> createShaurma(@RequestBody @Valid final ShaurmaDTO shaurmaDTO) {
        final Long createdId = shaurmaService.create(shaurmaDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateShaurma(@PathVariable final Long id,
            @RequestBody @Valid final ShaurmaDTO shaurmaDTO) {
        shaurmaService.update(id, shaurmaDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShaurma(@PathVariable final Long id) {
        shaurmaService.delete(id);
        return ResponseEntity.noContent().build();
    }

}

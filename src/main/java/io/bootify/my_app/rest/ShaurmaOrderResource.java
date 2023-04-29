package io.bootify.my_app.rest;

import io.bootify.my_app.model.ShaurmaOrderDTO;
import io.bootify.my_app.service.ShaurmaOrderService;
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
@RequestMapping(value = "/api/shaurmaOrders", produces = MediaType.APPLICATION_JSON_VALUE)
public class ShaurmaOrderResource {

    private final ShaurmaOrderService shaurmaOrderService;

    public ShaurmaOrderResource(final ShaurmaOrderService shaurmaOrderService) {
        this.shaurmaOrderService = shaurmaOrderService;
    }

    @GetMapping
    public ResponseEntity<List<ShaurmaOrderDTO>> getAllShaurmaOrders() {
        return ResponseEntity.ok(shaurmaOrderService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShaurmaOrderDTO> getShaurmaOrder(@PathVariable final Long id) {
        return ResponseEntity.ok(shaurmaOrderService.get(id));
    }

    @PostMapping
    public ResponseEntity<Long> createShaurmaOrder(
            @RequestBody @Valid final ShaurmaOrderDTO shaurmaOrderDTO) {
        final Long createdId = shaurmaOrderService.create(shaurmaOrderDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateShaurmaOrder(@PathVariable final Long id,
            @RequestBody @Valid final ShaurmaOrderDTO shaurmaOrderDTO) {
        shaurmaOrderService.update(id, shaurmaOrderDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShaurmaOrder(@PathVariable final Long id) {
        shaurmaOrderService.delete(id);
        return ResponseEntity.noContent().build();
    }

}

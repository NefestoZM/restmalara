package br.edu.uniara.lpi2.rest.controller;

import br.edu.uniara.lpi2.rest.model.Employee;
import br.edu.uniara.lpi2.rest.model.EmployeeRepository;
import java.util.List;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie.ResponseCookieBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    EmployeeRepository repository;

    @GetMapping("/{id}")
    public Employee one(@PathVariable Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new RuntimeException(
                "Erro pesquisando id: " +id));
    }

    @GetMapping
    public ResponseEntity<?> all(
        @RequestParam int page,
        @RequestParam int size
    ) {
        if (page < 1) {
            return ResponseEntity.badRequest().body(
                "page deve ser > 0");
        }
        if (size < 1 || size > 500) {
            return ResponseEntity.badRequest().body(
                "size deve ser entre 1 e 500");

        }
        final List<Employee> listEmployee = repository.findAll();
        return ResponseEntity.ok(listEmployee);
    }

    @PostMapping
    public ResponseEntity<Employee> insert(
        @RequestBody Employee employee
    ) {
        var employeeSalvo = repository.save(employee);
        return ResponseEntity.ok(employeeSalvo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        boolean employeeFound = repository.existsById(id);
        if (!employeeFound) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.ok(id + "was removed");
    }
}

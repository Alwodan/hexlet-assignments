package exercise.controller;

import exercise.dto.PersonDto;
import exercise.model.Person;
import exercise.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;

@RestController
@RequestMapping("/people")
@RequiredArgsConstructor
public class PeopleController {

    private final PersonRepository personRepository;

    @GetMapping(path = "")
    public Iterable<Person> getPeople() {
        return personRepository.findAll();
    }

    @PostMapping(path = "")
    public void createPerson(@RequestBody PersonDto personDto) {
        var person = new Person();
        person.setEmail(personDto.getEmail());
        person.setFirstName(personDto.getFirstName());
        person.setLastName(personDto.getLastName());
        personRepository.save(person);
    }

    @GetMapping(path = "/{id}")
    public Person getPerson(@PathVariable long id) {
        return personRepository.findById(id);
    }

    @DeleteMapping(path = "/{id}")
    public void deletePerson(@PathVariable long id) {
        personRepository.deleteById(id);
    }

    @PatchMapping(path = "/{id}")
    public void updatePerson(@PathVariable long id, @RequestBody PersonDto personDto) {
        var person = personRepository.findById(id);
        person.setEmail(personDto.getEmail());
        person.setFirstName(personDto.getFirstName());
        person.setLastName(personDto.getLastName());
        personRepository.save(person);
    }
}

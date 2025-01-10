//обработка исключений
//база данных
//классы
//интерфейсы
//лямбда выражения

package com.example.tobo.controllers;

import com.example.tobo.model.TodoItem;
import com.example.tobo.repositories.TodoItemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class TodoController implements CommandLineRunner {

    private final TodoItemRepository todoItemRepository;

    public TodoController(TodoItemRepository todoItemRepository) {
        this.todoItemRepository = todoItemRepository;
    }

    @GetMapping
    public String index(Model model) {
        try {
            List<TodoItem> allTodos = todoItemRepository.findAll();
            model.addAttribute("allTodos", allTodos);
            model.addAttribute("newTodo", new TodoItem());
        } catch (Exception e) {
            model.addAttribute("error", "Error fetching TODO items: " + e.getMessage());
        }
        return "index";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute TodoItem todoItem, Model model) {
        try {
            todoItemRepository.save(todoItem);
        } catch (Exception e) {
            model.addAttribute("error", "Error adding TODO item: " + e.getMessage());
            return "index";
        }
        return "redirect:/";
    }

    @PostMapping("/delete/{id}")
    public String deleteTodoItem(@PathVariable("id") int id, Model model) {
        try {
            todoItemRepository.deleteById(id);
        } catch (Exception e) {
            model.addAttribute("error", "Error deleting TODO item: " + e.getMessage());
            return "index";
        }
        return "redirect:/";
    }

    @PostMapping("/deleteAll")
    public String deleteAll(Model model) {
        try {
            todoItemRepository.deleteAll();
        } catch (Exception e) {
            model.addAttribute("error", "Error deleting all TODO items: " + e.getMessage());
            return "index";
        }
        return "redirect:/";
    }

    @PostMapping("/search")
    public String search(@RequestParam("searchTerm") String searchTerm, Model model) {
        try {
            //лямбда выражения для поиска
            List<TodoItem> searchResults = todoItemRepository.findAll().stream()
                    .filter(item -> item.getTitle() != null &&
                            item.getTitle().toLowerCase().contains(searchTerm.toLowerCase()))
                    .toList();

            model.addAttribute("allTodos", searchResults);
            model.addAttribute("newTodo", new TodoItem());
            model.addAttribute("searchTerm", searchTerm);
        } catch (Exception e) {
            model.addAttribute("error", "Error searching TODO items: " + e.getMessage());
            return "index";
        }
        return "index";
    }

    @Override
    public void run(String... args) {

    }
}

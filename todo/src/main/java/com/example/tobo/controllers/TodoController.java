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
    public String index(Model model){
        List<TodoItem> allTodos =  todoItemRepository.findAll();
        model.addAttribute("allTodos", allTodos);
        model.addAttribute("newTodo", new TodoItem());
        return "index";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute TodoItem todoItem){
        todoItemRepository.save(todoItem);

        return "redirect:/";
    }

    @PostMapping("/delete/{id}")
    public String deleteTodoItem(@PathVariable("id") int id){
        todoItemRepository.deleteById(id);
        return "redirect:/";
    }

    @PostMapping("/deleteAll")
    public String deleteAll(){
        todoItemRepository.deleteAll();
        return "redirect:/";
    }

    @PostMapping("/search")
    public String search(@RequestParam("searchTerm") String searchTerm, Model model){
        List<TodoItem> allItems = todoItemRepository.findAll();
        List<TodoItem> searchResults = new ArrayList<>();

        for (TodoItem item : allItems){
            if (item.getTitle().toLowerCase().contains(searchTerm.toLowerCase())){
                searchResults.add(item);
            }
        }
        model.addAttribute("allTodos", searchResults);
        model.addAttribute("newTodo", new TodoItem());
        model.addAttribute("searchTerm", searchTerm);
        return "index";
    }

    @Override
    public void run(String... args) throws Exception {

    }
}

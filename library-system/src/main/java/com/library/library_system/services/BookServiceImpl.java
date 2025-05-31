package com.library.library_system.services;

import com.library.library_system.model.Author;
import com.library.library_system.model.Book;
import com.library.library_system.model.Category;
import com.library.library_system.model.Publisher;
import com.library.library_system.repository.AuthorRepository;
import com.library.library_system.repository.BookRepository;
import com.library.library_system.repository.CategoryRepository;
import com.library.library_system.repository.PublisherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PublisherRepository publisherRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Book saveBook(Book bookRequest) {
        // Find or create Publisher
//        Publisher publisher = publisherRepository.findById(bookRequest.getPublisher().getId())
//                .orElseThrow(() -> new RuntimeException("Publisher not found"));
//
//
//
//        List<Author> authors = authorRepository.findAllById(bookRequest.getAuthors().stream()
//                .map(Author::getId)
//                .collect(Collectors.toList()));

//
//        List<Category> categories = categoryRepository.findAllById(bookRequest.getCategories().stream()
//                .map(Category::getId)
//                .collect(Collectors.toList()));
//        for (Category category : categories) {
//            if (category.getParent() != null && category.getParent().getId() != null) {
//                Category parent = categoryRepository.findById(category.getParent().getId())
//                        .orElseThrow(() -> new RuntimeException("Parent category not found"));
//                category.setParent(parent);
//            }
//        }

        Publisher publisher = publisherRepository.findByName(bookRequest.getPublisher().getName())
                .orElseGet(() -> publisherRepository.save(bookRequest.getPublisher()));
        bookRequest.setPublisher(publisher);

        // ✅ Handle Authors
        List<Author> resolvedAuthors = new ArrayList<>();
        for (Author a : bookRequest.getAuthors()) {
            Author existing = authorRepository.findByName(a.getName()).orElseGet(() -> authorRepository.save(a));
            resolvedAuthors.add(existing);
        }
        bookRequest.setAuthors(resolvedAuthors);

        // ✅ Handle Categories
        List<Category> resolvedCategories = new ArrayList<>();
        for (Category c : bookRequest.getCategories()) {
            Category existing = categoryRepository.findByName(c.getName()).orElseGet(() -> categoryRepository.save(c));
            resolvedCategories.add(existing);
        }
        bookRequest.setCategories(resolvedCategories);

        // Set resolved entities back
        bookRequest.setPublisher(publisher);
        bookRequest.setAuthors(resolvedAuthors);
        bookRequest.setCategories(resolvedCategories);

        // Save book
        return bookRepository.save(bookRequest);
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with ID: " + id));
    }

    @Override
    public Book updateBook(Long id, Book updatedBook) {
        Book existingBook = getBookById(id);
        existingBook.setTitle(updatedBook.getTitle());
        existingBook.setLanguage(updatedBook.getLanguage());
        existingBook.setIsbn(updatedBook.getIsbn());
        existingBook.setEdition(updatedBook.getEdition());
        existingBook.setPublicationYear(updatedBook.getPublicationYear());
        existingBook.setSummary(updatedBook.getSummary());
        existingBook.setCoverImageUrl(updatedBook.getCoverImageUrl());

        // Optionally update authors, publisher, and categories similarly here

        return bookRepository.save(existingBook);
    }

    @Override
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}
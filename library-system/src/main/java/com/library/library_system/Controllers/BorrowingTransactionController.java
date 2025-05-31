package com.library.library_system.Controllers;

import com.library.library_system.model.Book;
import com.library.library_system.model.BorrowingTransaction;
import com.library.library_system.model.Member;
import com.library.library_system.repository.BookRepository;
import com.library.library_system.repository.BorrowingTransactionRepository;
import com.library.library_system.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import lombok.*;

@Data
@Builder

@RestController
@RequestMapping("/api/borrowings")
public class BorrowingTransactionController {

    @Autowired
    private BorrowingTransactionRepository transactionRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BookRepository bookRepository;

    private static final int MAX_BORROWED_BOOKS = 3;

    @PostMapping
    public String create(@RequestParam Long memberId, @RequestParam Long bookId) {
        Member member = memberRepository.findById(memberId).orElseThrow();
        Book book = bookRepository.findById(bookId).orElseThrow();

        if (transactionRepository.findByMemberAndReturnDateIsNull(member).size() >= MAX_BORROWED_BOOKS) {
            return "Member has reached max limit of borrowed books.";
        }

        if (transactionRepository.existsByBookAndReturnDateIsNull(book)) {
            return "Book is currently borrowed by another member.";
        }

        BorrowingTransaction transaction = BorrowingTransaction.builder()
                .member(member)
                .book(book)
                .borrowDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(14))
                .build();
        transactionRepository.save(transaction);
        return "Borrowing recorded successfully.";
    }

    @PostMapping("/return")
    public String returnBook(@RequestParam Long memberId, @RequestParam Long bookId) {
        Member member = memberRepository.findById(memberId).orElseThrow();
        Book book = bookRepository.findById(bookId).orElseThrow();

        BorrowingTransaction transaction = transactionRepository.findByMemberAndBookAndReturnDateIsNull(member, book);
        if (transaction == null) {
            return "No active borrowing record found for this member and book.";
        }

        transaction.setReturnDate(LocalDate.now());
        transactionRepository.save(transaction);
        return "Book returned successfully.";
    }

    @GetMapping
    public List<BorrowingTransaction> getAll() {
        return transactionRepository.findAll();
    }

    @GetMapping("/{id}")
    public BorrowingTransaction getById(@PathVariable Long id) {
        return transactionRepository.findById(id).orElseThrow();
    }
}

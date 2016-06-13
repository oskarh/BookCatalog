package se.oskarh.bookcatalog.network.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.ArrayList;
import java.util.List;

@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class BookList {

    @JsonField(name = "items")
    List<Book> books = new ArrayList<>();

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> items) {
        this.books = items;
    }

    @Override
    public String toString() {
        return "BookList{" +
                "books=" + books +
                '}';
    }
}

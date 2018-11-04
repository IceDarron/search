package com.icedarron.search;

import com.icedarron.search.model.Book;
import com.icedarron.search.service.BookService;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BookServiceTest {

    @Autowired
    private BookService bookService;

    @Autowired
    private ElasticsearchTemplate esTemplate;

    @Before
    public void before() {
        esTemplate.deleteIndex(Book.class);
        esTemplate.createIndex(Book.class);
        esTemplate.putMapping(Book.class);
        esTemplate.refresh(Book.class);
    }

    @Test
    public void testSave() {

        Book book = new Book("1001", "Elasticsearch Basics", "Rambabu Posa", "23-FEB-2017");
        Book testBook = bookService.save(book);

        assertNotNull(testBook.getId());
        assertEquals(testBook.getTitle(), book.getTitle());
        assertEquals(testBook.getAuthor(), book.getAuthor());
        assertEquals(testBook.getReleaseDate(), book.getReleaseDate());

    }

    @Test
    public void testFindOne() {

        Book book = new Book("1001", "Elasticsearch Basics", "Rambabu Posa", "23-FEB-2017");
        bookService.save(book);

        Book testBook = bookService.findOne(book.getId());

        assertNotNull(testBook.getId());
        assertEquals(testBook.getTitle(), book.getTitle());
        assertEquals(testBook.getAuthor(), book.getAuthor());
        assertEquals(testBook.getReleaseDate(), book.getReleaseDate());

    }

    @Test
    public void testFindByTitle() {

        Book book = new Book("1001", "Elasticsearch Basics", "Rambabu Posa", "23-FEB-2017");
        bookService.save(book);

        List<Book> byTitle = bookService.findByTitle(book.getTitle());
        assertThat(byTitle.size(), is(1));
    }

    @Test
    public void testFindByAuthor() {

        List<Book> bookList = new ArrayList<>();

        bookList.add(new Book("1001", "Elasticsearch Basics", "Rambabu Posa", "23-FEB-2017"));
        bookList.add(new Book("1002", "Apache Lucene Basics", "Rambabu Posa", "13-MAR-2017"));
        bookList.add(new Book("1003", "Apache Solr Basics", "Rambabu Posa", "21-MAR-2017"));
        bookList.add(new Book("1007", "Spring Data + ElasticSearch", "Rambabu Posa", "01-APR-2017"));
        bookList.add(new Book("1008", "Spring Boot + MongoDB", "Mkyong", "25-FEB-2017"));

        for (Book book : bookList) {
            bookService.save(book);
        }

        Page<Book> byAuthor = bookService.findByAuthor("Rambabu Posa", new PageRequest(0, 10));
        assertThat(byAuthor.getTotalElements(), is(4L));

        Page<Book> byAuthor2 = bookService.findByAuthor("Mkyong", new PageRequest(0, 10));
        assertThat(byAuthor2.getTotalElements(), is(1L));

    }

    @Test
    public void testDelete() {

        Book book = new Book("1001", "Elasticsearch Basics", "Rambabu Posa", "23-FEB-2017");
        bookService.save(book);
        bookService.delete(book);
        Book testBook = bookService.findOne(book.getId());
        assertNull(testBook);
    }

    /**************************************************/

    @Autowired
    private ElasticsearchOperations es;

    @Test
    public void testRun(String... args) throws Exception {

        printElasticSearchInfo();

        bookService.save(new Book("1001", "Elasticsearch Basics", "Rambabu Posa", "23-FEB-2017"));
        bookService.save(new Book("1002", "Apache Lucene Basics", "Rambabu Posa", "13-MAR-2017"));
        bookService.save(new Book("1003", "Apache Solr Basics", "Rambabu Posa", "21-MAR-2017"));

        //fuzzey search
        Page<Book> books = bookService.findByAuthor("Rambabu", new PageRequest(0, 10));

        //List<Book> books = bookService.findByTitle("Elasticsearch Basics");

        books.forEach(x -> System.out.println(x));


    }

    //useful for debug, print elastic search details
    private void printElasticSearchInfo() {

        System.out.println("--ElasticSearch--");
        Client client = es.getClient();
        Map<String, Settings> asMap = client.settings().getAsGroups();

        asMap.forEach((k, v) -> {
            System.out.println(k + " = " + v);
        });
        System.out.println("--ElasticSearch--");
    }

}


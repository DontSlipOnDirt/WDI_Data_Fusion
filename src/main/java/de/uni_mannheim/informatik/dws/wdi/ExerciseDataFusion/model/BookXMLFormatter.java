package de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.uni_mannheim.informatik.dws.winter.model.io.XMLFormatter;

import java.time.LocalDateTime;

/**
 * {@link XMLFormatter} for {@link Book}s.
 *
 * @author Your Name (your@email.com)
 *
 */
public class BookXMLFormatter extends XMLFormatter<Book> {


    @Override
    public Element createRootElement(Document doc) {
        return doc.createElement("books");
    }

    @Override
    public Element createElementFromRecord(Book record, Document doc) {
        Element book = doc.createElement("book");
        book.setAttribute("id",record.getIdentifier());
        //book.appendChild(createTextElement("id", record.getIdentifier(), doc));
        book.appendChild(createTextElementWithProvenance("title", record.getTitle(), record.getMergedAttributeProvenance(Book.TITLE), doc));
        book.appendChild(createTextElementWithProvenance("author", record.getAuthor(), record.getMergedAttributeProvenance(Book.AUTHOR), doc));
        book.appendChild(createTextElementWithProvenance("rating", String.valueOf(record.getRating()), record.getMergedAttributeProvenance(Book.RATING), doc));
        book.appendChild(createTextElementWithProvenance("description", record.getDescription(), record.getMergedAttributeProvenance(Book.DESCRIPTION), doc));
        book.appendChild(createTextElementWithProvenance("language", record.getLanguage(), record.getMergedAttributeProvenance(Book.LANGUAGE), doc));
        book.appendChild(createTextElementWithProvenance("isbn", record.getIsbn(), record.getMergedAttributeProvenance(Book.ISBN), doc));
        book.appendChild(createTextElementWithProvenance("genres", String.valueOf(record.getGenres()), record.getMergedAttributeProvenance(Book.GENRES), doc));
        book.appendChild(createTextElementWithProvenance("pages", String.valueOf(record.getPages()), record.getMergedAttributeProvenance(Book.PAGES), doc));
        book.appendChild(createTextElementWithProvenance("publisher", record.getPublisher(), record.getMergedAttributeProvenance(Book.PUBLISHER), doc));

        LocalDateTime dt = record.getPublishDate();
        if(dt != null){
            book.appendChild(createTextElementWithProvenance("publishDate", record.getPublishDate().toString(), record.getMergedAttributeProvenance(Book.PUBLISH_DATE), doc));
        }
        else{
            book.appendChild(createTextElementWithProvenance("publishDate", null, record.getMergedAttributeProvenance(Book.PUBLISH_DATE), doc));
        }

        int d = (int) record.getNumRatings();
        book.appendChild(createTextElementWithProvenance("numRatings", String.valueOf(d), record.getMergedAttributeProvenance(Book.NUM_RATINGS), doc));
        book.appendChild(createTextElementWithProvenance("CoverImage", record.getCoverImg(), record.getMergedAttributeProvenance(Book.COVER_IMG), doc));


        return book;
    }

    protected Element createTextElementWithProvenance(String name, String value, String provenance, Document doc) {
        Element elem = createTextElement(name, value, doc);
        elem.setAttribute("provenance", provenance);
        return elem;
    }


}
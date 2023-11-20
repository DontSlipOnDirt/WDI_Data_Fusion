package de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.uni_mannheim.informatik.dws.winter.model.io.XMLFormatter;

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

        book.appendChild(createTextElement("id", record.getIdentifier(), doc));
        book.appendChild(createTextElementWithProvenance("title", record.getTitle(), record.getMergedAttributeProvenance(Book.TITLE), doc));
        book.appendChild(createTextElementWithProvenance("author", record.getAuthor(), record.getMergedAttributeProvenance(Book.AUTHOR), doc));
        book.appendChild(createTextElementWithProvenance("publishDate", record.getPublishDate().toString(), record.getMergedAttributeProvenance(Book.PUBLISH_DATE), doc));


        return book;
    }

    protected Element createTextElementWithProvenance(String name, String value, String provenance, Document doc) {
        Element elem = createTextElement(name, value, doc);
        elem.setAttribute("provenance", provenance);
        return elem;
    }


}
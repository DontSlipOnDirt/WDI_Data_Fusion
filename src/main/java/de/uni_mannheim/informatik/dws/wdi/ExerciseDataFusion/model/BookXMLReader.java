package de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.*;

import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Node;

import de.uni_mannheim.informatik.dws.winter.model.DataSet;
import de.uni_mannheim.informatik.dws.winter.model.FusibleFactory;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.XMLMatchableReader;

public class BookXMLReader extends XMLMatchableReader<Book, Attribute> implements FusibleFactory<Book, Attribute> {

    @Override
    protected void initialiseDataset(DataSet<Book, Attribute> dataset) {
        super.initialiseDataset(dataset);

        dataset.addAttribute(Book.TITLE);
        dataset.addAttribute(Book.AUTHOR);
        dataset.addAttribute(Book.RATING);
        dataset.addAttribute(Book.DESCRIPTION);
        dataset.addAttribute(Book.LANGUAGE);
        dataset.addAttribute(Book.ISBN);
        dataset.addAttribute(Book.GENRES);
        dataset.addAttribute(Book.PAGES);
        dataset.addAttribute(Book.PUBLISHER);
        dataset.addAttribute(Book.PUBLISH_DATE);
        dataset.addAttribute(Book.NUM_RATINGS);
        dataset.addAttribute(Book.COVER_IMG);
    }

    @Override
    public Book createModelFromElement(Node node, String provenanceInfo) {
        String id = node.getAttributes().getNamedItem("id").getNodeValue();

        // create the object with id and provenance information
        Book book = new Book(id, provenanceInfo);

        // fill the attributes
        book.setTitle(getValueFromChildElement(node, "title"));
        book.setAuthor(getValueFromChildElement(node, "author"));

        // convert the rating string into a Double object
        try {
            String rt = getValueFromChildElement(node, "rating");
            if (rt != null && !rt.isEmpty()) {
                book.setRating(Double.parseDouble(getValueFromChildElement(node, "rating")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        book.setDescription(getValueFromChildElement(node, "description"));
        book.setLanguage(getValueFromChildElement(node, "language"));
        book.setIsbn(getValueFromChildElement(node, "isbn"));
        book.setGenres(getValueFromChildElement(node, "genres"));

        // convert the pages string into a Int object
        try {
            String pg = getValueFromChildElement(node, "pages");
            if (pg != null && !pg.isEmpty()) {
                book.setPages(Integer.parseInt(getValueFromChildElement(node, "pages")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        book.setPublisher(getValueFromChildElement(node, "publisher"));

        // convert the date string into a DateTime object
        try {
            String date = getValueFromChildElement(node, "publishDate");
            if (date != null && !date.isEmpty()) {
                DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                        .appendPattern("yyyy-MM-dd['T'HH:mm:ss.SSS]")
                        .parseDefaulting(ChronoField.CLOCK_HOUR_OF_DAY, 0)
                        .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                        .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                        .optionalStart().appendOffset("+HH:MM", "+00:00").optionalEnd()
                        .toFormatter(Locale.ENGLISH);
                LocalDateTime dt = LocalDateTime.parse(date, formatter);
                book.setPublishDate(dt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // convert the numRating string into a Double object
        try {
            String numR = getValueFromChildElement(node, "numRatings");
            if (numR != null && !numR.isEmpty()) {
                book.setNumRatings(Double.parseDouble(getValueFromChildElement(node, "numRatings")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        book.setCoverImg(getValueFromChildElement(node, "coverImg"));

        return book;
    }

    @Override
    public Book createInstanceForFusion(RecordGroup<Book, Attribute> cluster) {
        List<String> ids = new LinkedList<>();

        for (Book b : cluster.getRecords()) {
            ids.add(b.getIdentifier());
        }

        Collections.sort(ids);

        String mergedId = StringUtils.join(ids, '+');

        return new Book(mergedId, "fused");
    }
}
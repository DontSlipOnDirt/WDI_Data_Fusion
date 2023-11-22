
package de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

import de.uni_mannheim.informatik.dws.winter.model.AbstractRecord;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import org.apache.commons.lang3.StringUtils;

public class Book extends AbstractRecord<Attribute> implements Serializable {
    private static final long serialVersionUID = 1L;

    private String title;
    private String author;
    private double rating;
    private String description;
    private String language;
    private String isbn;
    private List<String> genres;
    private int pages;
    private String publisher;
    private LocalDateTime publishDate;
    private double numRatings;
    private String coverImg;

    public Book(String identifier, String provenance) {
        id = identifier;
    }

    public String getIdentifier() {
        return id;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = Collections.singletonList(genres);
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public LocalDateTime getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(LocalDateTime publishDate) {
        this.publishDate = publishDate;
    }

    public double getNumRatings() {
        return numRatings;
    }

    public void setNumRatings(double numRatings) {
        this.numRatings = numRatings;
    }

    public String getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }

    private Map<Attribute, Collection<String>> provenance = new HashMap<>();
    private Collection<String> recordProvenance;

    public void setRecordProvenance(Collection<String> provenance) {
        recordProvenance = provenance;
    }

    public Collection<String> getRecordProvenance() {
        return recordProvenance;
    }

    public void setAttributeProvenance(Attribute attribute,
                                       Collection<String> provenance) {
        this.provenance.put(attribute, provenance);
    }

    public Collection<String> getAttributeProvenance(String attribute) {
        return provenance.get(attribute);
    }

    public String getMergedAttributeProvenance(Attribute attribute) {
        Collection<String> prov = provenance.get(attribute);

        if (prov != null) {
            return StringUtils.join(prov, "+");
        } else {
            return "";
        }
    }

    public static final Attribute TITLE = new Attribute("Title");
    public static final Attribute AUTHOR = new Attribute("Author");
    public static final Attribute RATING = new Attribute("Rating");
    public static final Attribute DESCRIPTION = new Attribute("Description");
    public static final Attribute LANGUAGE = new Attribute("Language");
    public static final Attribute ISBN = new Attribute("ISBN");
    public static final Attribute GENRES = new Attribute("Genres");
    public static final Attribute PAGES = new Attribute("Pages");
    public static final Attribute PUBLISHER = new Attribute("Publisher");
    public static final Attribute PUBLISH_DATE = new Attribute("PublishDate");
    public static final Attribute NUM_RATINGS = new Attribute("NumRatings");
    public static final Attribute COVER_IMG = new Attribute("CoverImg");

    @Override
    public boolean hasValue(Attribute attribute) {
        if (attribute == TITLE)
            return getTitle() != null && !getTitle().isEmpty();
        else if (attribute == AUTHOR)
            return getAuthor() != null && !getAuthor().isEmpty();
        else if (attribute == RATING)
            return getRating() != 0.0;
        else if (attribute == DESCRIPTION)
            return getDescription() != null && !getDescription().isEmpty();
        else if (attribute == LANGUAGE)
            return getLanguage() != null && !getLanguage().isEmpty();
        else if (attribute == ISBN)
            return getIsbn() != null && !getIsbn().isEmpty();
        else if (attribute == GENRES)
            return getGenres() != null && !getGenres().isEmpty();
        else if (attribute == PAGES)
            return getPages() != 0;
        else if (attribute == PUBLISHER)
            return getPublisher() != null && !getPublisher().isEmpty();
        else if (attribute == PUBLISH_DATE)
            return getPublishDate() != null;
        else if (attribute == NUM_RATINGS)
            return getNumRatings() != 0.0;
        else if (attribute == COVER_IMG)
            return getCoverImg() != null && !getCoverImg().isEmpty();
        else
            return false;
    }

    @Override
    public int getDataSourceIdentifier() {
        return super.getDataSourceIdentifier();
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", rating=" + rating +
                ", description='" + description + '\'' +
                ", language='" + language + '\'' +
                ", isbn='" + isbn + '\'' +
                ", genres=" + genres +
                ", pages=" + pages +
                ", publisher='" + publisher + '\'' +
                ", publishDate=" + publishDate +
                ", numRatings=" + numRatings +
                ", coverImg='" + coverImg + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Book) {
            return this.getIdentifier().equals(((Book) obj).getIdentifier());
        } else
            return false;
    }

    @Override
    public int hashCode() {
        return getIdentifier().hashCode();
    }
}

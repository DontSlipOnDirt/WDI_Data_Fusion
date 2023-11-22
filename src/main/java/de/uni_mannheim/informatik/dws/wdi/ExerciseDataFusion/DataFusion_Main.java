package de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Locale;

import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.evaluation.*;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.fusers.*;
import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.model.*;
import de.uni_mannheim.informatik.dws.winter.datafusion.CorrespondenceSet;
import de.uni_mannheim.informatik.dws.winter.datafusion.DataFusionEngine;
import de.uni_mannheim.informatik.dws.winter.datafusion.DataFusionEvaluator;
import de.uni_mannheim.informatik.dws.winter.datafusion.DataFusionStrategy;
import de.uni_mannheim.informatik.dws.winter.model.DataSet;
import de.uni_mannheim.informatik.dws.winter.model.FusibleDataSet;
import de.uni_mannheim.informatik.dws.winter.model.FusibleHashedDataSet;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroupFactory;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.utils.WinterLogManager;
import org.slf4j.Logger;

public class DataFusion_Main 
{
	/*
	 * Logging Options:
	 * 		default: 	level INFO	- console
	 * 		trace:		level TRACE     - console
	 * 		infoFile:	level INFO	- console/file
	 * 		traceFile:	level TRACE	- console/file
	 *  
	 * To set the log level to trace and write the log to winter.log and console, 
	 * activate the "traceFile" logger as follows:
	 *     private static final Logger logger = WinterLogManager.activateLogger("traceFile");
	 *
	 */

	private static final Logger logger = WinterLogManager.activateLogger("trace");
	
	public static void main( String[] args ) throws Exception
    {
		// Load the Data into FusibleDataSet
		logger.info("*\tLoading datasets\t*");
		FusibleDataSet<Book, Attribute> dataDBPedia = new FusibleHashedDataSet<>();
		new BookXMLReader().loadFromXML(new File("data/input/DBPedia_books_schema.xml"), "/books/book", dataDBPedia);
		dataDBPedia.printDataSetDensityReport();

		FusibleDataSet<Book, Attribute> dataWikipedia = new FusibleHashedDataSet<>();
		new BookXMLReader().loadFromXML(new File("data/input/Wikipedia_books_schema.xml"), "/books/book", dataWikipedia);
		dataWikipedia.printDataSetDensityReport();

		FusibleDataSet<Book, Attribute> dataZenodo = new FusibleHashedDataSet<>();
		new BookXMLReader().loadFromXML(new File("data/input/Zenodo_books_schema.xml"), "/books/book", dataZenodo);
		dataZenodo.printDataSetDensityReport();

		// Maintain Provenance
		dataDBPedia.setScore(2.0);
		dataWikipedia.setScore(1.0);
		dataZenodo.setScore(3.0);

		// Date (e.g. last update)
		DateTimeFormatter formatter = new DateTimeFormatterBuilder()
		        .appendPattern("yyyy-MM-dd")
		        .parseDefaulting(ChronoField.CLOCK_HOUR_OF_DAY, 0)
		        .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
		        .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
		        .toFormatter(Locale.ENGLISH);


		dataDBPedia.setDate(LocalDateTime.parse("2000-01-01", formatter));
		dataWikipedia.setDate(LocalDateTime.parse("2020-01-01", formatter));
		dataZenodo.setDate(LocalDateTime.parse("2023-01-01", formatter));

		// load correspondences
		logger.info("*\tLoading correspondences\t*");
		CorrespondenceSet<Book, Attribute> correspondences = new CorrespondenceSet<>();
		correspondences.loadCorrespondences(new File("data/correspondences/DBPedia_Wikipedia_correspondences.csv"),dataDBPedia, dataWikipedia);
		correspondences.loadCorrespondences(new File("data/correspondences/DBPedia_Zenodo_correspondences.csv"),dataDBPedia, dataZenodo);
		correspondences.loadCorrespondences(new File("data/correspondences/Zenodo_Wikipedia_correspondences.csv"), dataZenodo,dataWikipedia);

		// write group size distribution
		correspondences.printGroupSizeDistribution();

		// load the gold standard
		logger.info("*\tEvaluating results\t*");
		DataSet<Book, Attribute> gs = new FusibleHashedDataSet<>();
		new BookXMLReader().loadFromXML(new File("data/goldstandard/gold.xml"), "/books/book", gs);

		for(Book m : gs.get()) {
			logger.info(String.format("gs: %s", m.getIdentifier()));
		}

		// define the fusion strategy
		DataFusionStrategy<Book, Attribute> strategy = new DataFusionStrategy<>(new BookXMLReader());
		// write debug results to file
		strategy.activateDebugReport("data/output/debugResultsDatafusion.csv", -1, gs);
		
		// add attribute fusers
		strategy.addAttributeFuser(Book.TITLE, new TitleFuserShortestString(),new TitleEvaluationRule());
		strategy.addAttributeFuser(Book.AUTHOR,new AuthorFuserFavourSource(), new AuthorEvaluationRule());
		strategy.addAttributeFuser(Book.RATING, new RatingFuserAverage(),new RatingEvaluationRule());
		strategy.addAttributeFuser(Book.DESCRIPTION, new DescriptionFuserLongestString(),new DescriptionEvaluationRule());
		strategy.addAttributeFuser(Book.LANGUAGE, new LanguageFuserFavourSource(),new LanguageEvaluationRule());
		strategy.addAttributeFuser(Book.ISBN, new IsbnFuserFavourSource(),new IsbnEvaluationRule());
		strategy.addAttributeFuser(Book.GENRES, new GenresFuserUnion(),new GenresEvaluationRule());
		strategy.addAttributeFuser(Book.PAGES, new PagesFuserAverage(),new PagesEvaluationRule());
		strategy.addAttributeFuser(Book.PUBLISHER, new PublisherFuserFavourSource(),new PublisherEvaluationRule());
		strategy.addAttributeFuser(Book.PUBLISH_DATE, new DateFuserMostRecent(),new PublishDateEvaluationRule());
		strategy.addAttributeFuser(Book.NUM_RATINGS, new NumRatingsFuserAverage(),new NumRatingsEvaluationRule());
		strategy.addAttributeFuser(Book.COVER_IMG, new CoverImageFuserFavourSource(),new CoverImageEvaluationRule());

		// create the fusion engine
		DataFusionEngine<Book, Attribute> engine = new DataFusionEngine<>(strategy);

		// print consistency report
		engine.printClusterConsistencyReport(correspondences, null);
		
		// print record groups sorted by consistency
		engine.writeRecordGroupsByConsistency(new File("data/output/recordGroupConsistencies.csv"), correspondences, null);

		// run the fusion
		logger.info("*\tRunning data fusion\t*");
		FusibleDataSet<Book, Attribute> fusedDataSet = engine.run(correspondences,null);

		// write the result
		new BookXMLFormatter().writeXML(new File("data/output/fused.xml"), fusedDataSet);

		// evaluate
		DataFusionEvaluator<Book, Attribute> evaluator = new DataFusionEvaluator<>(strategy, new RecordGroupFactory<Book, Attribute>());
		
		double accuracy = evaluator.evaluate(fusedDataSet, gs, null);

		logger.info(String.format("*\tAccuracy: %.2f", accuracy));
    }
}

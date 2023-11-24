/*
 * Copyright (c) 2017 Data and Web Science Group, University of Mannheim, Germany (http://dws.informatik.uni-mannheim.de/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.fusers;

import de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.model.Book;
import de.uni_mannheim.informatik.dws.winter.datafusion.AttributeValueFuser;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.ClusteredVote;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.FusedValue;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;
import de.uni_mannheim.informatik.dws.winter.similarity.string.TokenizingJaccardSimilarity;

/**
 * {@link AttributeValueFuser} for the titles of {@link Book}s.
 * 
 * @author Oliver Lehmberg (oli@dwslab.de)
 * 
 */
public class TitleFuserClusteredVote extends
		AttributeValueFuser<String, Book, Attribute> {

	public TitleFuserClusteredVote() {
		super(new ClusteredVote<String,Book, Attribute>(new TokenizingJaccardSimilarity(),0.9));
	}

	@Override
	public void fuse(RecordGroup<Book, Attribute> group, Book fusedRecord, Processable<Correspondence<Attribute, Matchable>> schemaCorrespondences, Attribute schemaElement) {

		// get the fused value
		FusedValue<String, Book, Attribute> fused = getFusedValue(group, schemaCorrespondences, schemaElement);

		// set the value for the fused record
		fusedRecord.setTitle(fused.getValue());

		// add provenance info
		fusedRecord.setAttributeProvenance(Book.TITLE, fused.getOriginalIds());
	}

	@Override
	public boolean hasValue(Book record, Correspondence<Attribute, Matchable> correspondence) {
		return record.hasValue(Book.TITLE);
	}

	@Override
	public String getValue(Book record, Correspondence<Attribute, Matchable> correspondence) {
		return record.getTitle();
	}

}
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
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.numeric.Average;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.FusedValue;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;

/**
 * {@link AttributeValueFuser} for the Pages of {@link Book}s.
 *
 * @author Oliver Lehmberg (oli@dwslab.de)
 *
 */
public class PagesFuserAverage extends AttributeValueFuser<Double, Book, Attribute> {

    public PagesFuserAverage() {
        super(new Average<Book, Attribute>());
    }

    @Override
    public boolean hasValue(Book record, Correspondence<Attribute, Matchable> correspondence) {
        return record.hasValue(Book.PAGES);
    }

    @Override
    public Double getValue(Book record, Correspondence<Attribute, Matchable> correspondence) {
        return Double.parseDouble(String.valueOf(record.getPages()));
    }

    @Override
    public void fuse(RecordGroup<Book, Attribute> group, Book fusedRecord, Processable<Correspondence<Attribute, Matchable>> schemaCorrespondences, Attribute schemaElement) {
        FusedValue<Double, Book, Attribute> fused = getFusedValue(group, schemaCorrespondences, schemaElement);
        //String str = fused.getValue().toString();
        if (fused.getValue() == null){
            fusedRecord.setPages(0);
        }
        else {
            double d = Double.parseDouble(fused.getValue().toString());
            fusedRecord.setPages((int) d);
        }
        fusedRecord.setAttributeProvenance(Book.PAGES,
                fused.getOriginalIds());
    }

}

package com.production.game2048.util;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;
import java.util.stream.Collectors;


@Converter
public class IntArrayConverter implements AttributeConverter<int[][], String> {

    private static final String ROW_SEPARATOR = ";";
    private static final String COLUMN_SEPARATOR = ",";


    @Override
    public String convertToDatabaseColumn(int[][] attribute) {
        
        if (attribute == null) {
            return null;
        }

       
        return Arrays.stream(attribute)
                .map(row -> Arrays.stream(row)
                        .mapToObj(String::valueOf)
                        .collect(Collectors.joining(COLUMN_SEPARATOR)))
                .collect(Collectors.joining(ROW_SEPARATOR));
    }

    
    @Override
    public int[][] convertToEntityAttribute(String dbData) {
       
        if (dbData == null || dbData.trim().isEmpty()) {
            return null;
        }

        try {
            return Arrays.stream(dbData.split(ROW_SEPARATOR))
                    .map(row -> Arrays.stream(row.split(COLUMN_SEPARATOR))
                            
                            .mapToInt(Integer::parseInt)
                            .toArray())
                    .toArray(int[][]::new);
        } catch (NumberFormatException e) {
           
            throw new IllegalArgumentException("Failed to convert database data to board. Invalid number format.", e);
        }
    }
}
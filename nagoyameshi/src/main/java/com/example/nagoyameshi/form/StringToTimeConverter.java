package com.example.nagoyameshi.form;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToTimeConverter implements Converter<String, Time> {

    @Override
    public Time convert(String source) {
        if (source == null || source.isEmpty()) {
            return null; // 空の場合はnullを返す
        }
        try {
        	SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            return new Time(timeFormat.parse(source).getTime());
        } catch (ParseException e) {
            throw new IllegalArgumentException("無効な時間形式です。 'HH:mm' 形式で入力してください。");
        }
    }
}

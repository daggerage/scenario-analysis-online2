package org.egc.sao.config;

import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.mapping.model.FieldNamingStrategy;
import org.springframework.data.util.ParsingUtils;

import java.util.Iterator;
import java.util.List;

public class UpperCaseWithUnderscoreFieldNamingStrategy implements FieldNamingStrategy{
    @Override
    public String getFieldName(PersistentProperty<?> property) {
        List<String> parts= ParsingUtils.splitCamelCaseToLower(property.getName());
        StringBuilder sb=new StringBuilder();
        Iterator it=parts.iterator();
        if(it.hasNext()){
            sb.append(it.next().toString().toUpperCase());
            while (it.hasNext()){
                sb.append("_");
                sb.append(it.next().toString().toUpperCase());
            }
        }
        return sb.toString();
    }
}

package com.creatoo.szwhg.core.util;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class GeneralPredicatesBuilder<T> {
    private Class<T> pClass;
    private final List<SearchCriteria> params;

    public GeneralPredicatesBuilder(Class<T> pClass) {
        this.pClass=pClass;
        params = new ArrayList<>();
    }

    public GeneralPredicatesBuilder with(final String key, final String operation, final Object value) {
        params.add(new SearchCriteria(key, operation, value));
        return this;
    }

    public BooleanExpression build(String search) {
        if (search != null) {
            String[] conds=StringUtils.split(search,",");
            for(String cond:conds){
                Pattern pattern = Pattern.compile("(.+?)(:|<|>)(.*)");
                Matcher matcher = pattern.matcher(cond );
                if (matcher.find()) {
                    with(matcher.group(1), matcher.group(2), matcher.group(3));
                }
            }
        }
        if (params.size() == 0) {
            return null;
        }
        final List<BooleanExpression> predicates = new ArrayList<>();
        GeneralPredicate predicate;
        for (final SearchCriteria param : params) {
            predicate = new GeneralPredicate(pClass,param);
            final BooleanExpression exp = predicate.getPredicate();
            if (exp != null) {
                predicates.add(exp);
            }
        }

        BooleanExpression result = predicates.get(0);
        for (int i = 1; i < predicates.size(); i++) {
            result = result.and(predicates.get(i));
        }
        return result;
    }

}

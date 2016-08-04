package com.yuntao.zhushou.common.utils;

import com.yuntao.zhushou.model.enums.LogQueryType;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

/**
 * Created by shan on 2016/5/5.
 */
public class QueryBuilderUtils {

    public  static void buildQuery(BoolQueryBuilder queryBuilder, String queryField, String queryType, Object queryValue){
        if(queryValue == null || queryValue.toString().equals("")){
            return ;
        }
        if(StringUtils.equals(queryType, LogQueryType.MATCH.getCode())){
            queryBuilder.must(QueryBuilders.matchPhraseQuery(queryField, queryValue));
        }else if(StringUtils.equals(queryType, LogQueryType.TERM.getCode())){
            queryBuilder.must(QueryBuilders.commonTermsQuery(queryField, queryValue));
        }else if(StringUtils.equals(queryType, LogQueryType.FUZZY.getCode())){
            queryBuilder.must(QueryBuilders.fuzzyQuery(queryField, queryValue));
        }else if(StringUtils.equals(queryType, LogQueryType.REGEXP.getCode())){
            queryBuilder.must(QueryBuilders.regexpQuery(queryField, queryValue.toString()));
        }else if(StringUtils.equals(queryType, LogQueryType.WILDCARD.getCode())){
            queryBuilder.must(QueryBuilders.wildcardQuery(queryField, queryValue.toString()));
        }else if(StringUtils.equals(queryType, LogQueryType.PREFIX.getCode())){
            queryBuilder.must(QueryBuilders.matchPhrasePrefixQuery(queryField, queryValue.toString()));
        }
    }
}

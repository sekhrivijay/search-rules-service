package com.services.micro.rules.search.bl;

import com.micro.services.search.api.SearchModelWrapper;

public interface RulesExecutionService {
    SearchModelWrapper executePre(SearchModelWrapper searchModelWrapper) throws Exception;
    SearchModelWrapper executePost(SearchModelWrapper searchModelWrapper) throws Exception;
}

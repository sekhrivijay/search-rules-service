package com.ftd.services.rules.search.bl;

import com.ftd.services.search.api.SearchModelWrapper;

public interface RulesExecutionService {
    SearchModelWrapper executePre(SearchModelWrapper searchModelWrapper) throws Exception;
    SearchModelWrapper executePost(SearchModelWrapper searchModelWrapper) throws Exception;
}

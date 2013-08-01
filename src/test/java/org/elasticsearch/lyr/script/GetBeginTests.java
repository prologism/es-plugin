package org.elasticsearch.lyr.script;

import static org.elasticsearch.index.query.FilterBuilders.scriptFilter;
import static org.elasticsearch.index.query.QueryBuilders.filteredQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

import java.io.IOException;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.lyr.AbstractESTest;
import org.elasticsearch.lyr.plugin.CalculateElapsedTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testng.AssertJUnit;

import com.github.tlrx.elasticsearch.test.EsSetup;
import com.github.tlrx.elasticsearch.test.request.CreateIndex;
import com.github.tlrx.elasticsearch.test.support.junit.runners.ElasticsearchRunner;

@RunWith(ElasticsearchRunner.class)
public class GetBeginTests extends AbstractESTest{

	@Before
	public void setUp() {
		super.setUp();
		CreateIndex cIndex = EsSetup.createIndex(indexName)
				.withMapping("line", EsSetup.fromClassPath("typeline.json"))
				.withData(EsSetup.fromClassPath("line-simple.bulk.json"));
		cIndex.execute(client1);
	}

	@Test
	public void testScriptOnSearch() throws IOException {

		SearchRequestBuilder srb1 = client1
				.prepareSearch(indexName)
				.setQuery(
						filteredQuery(matchAllQuery(),
								scriptFilter(CalculateElapsedTime.SCRIPT_NAME).lang("native")
										.addParam("field", "type_evt")));
		SearchResponse sResp = srb1.execute().actionGet();
		AssertJUnit.assertNotNull(sResp);
		System.out.println(srb1.toString());
	}

	
}

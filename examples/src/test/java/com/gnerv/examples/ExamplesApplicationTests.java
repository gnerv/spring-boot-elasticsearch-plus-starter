package com.gnerv.examples;

import com.gnerv.examples.service.BookService;
import com.gnerv.plus.elasticsearch.annotation.ElasticsearchDB;
import com.gnerv.plus.elasticsearch.client.ElasticsearchClient;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@Slf4j
@SpringBootTest
class ExamplesApplicationTests {

	@Resource
	private ElasticsearchClient elasticsearchClient;
	@Resource
	private BookService bookService;

	@SneakyThrows
	@Test
	void contextLoads() {
		bookService.get("123");
		RestHighLevelClient highLevelClient = elasticsearchClient.getRestHighLevelClient("es1");

		String index = "hotel-0301";
		String city = "上海";

		SearchRequest searchRequest = new SearchRequest(index);
		// 创建查询构建器
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		// 构建 查询条件 // 中文会被分词 使用 字段名+ keyword 查询
		TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("city.keyword", city);
		searchSourceBuilder.query(termQueryBuilder);
		// 分页 从下标 5 开始 默认 0
		searchSourceBuilder.from(5);
		// 往后取25条数据 默认 10  默认可输入的最大值 10000
		searchSourceBuilder.size(25);
		// 设定希望返回的字段数组
		searchSourceBuilder.fetchSource(new String[]{"name", "city"}, null);
		searchRequest.source(searchSourceBuilder);
		SearchResponse search = highLevelClient.search(searchRequest, RequestOptions.DEFAULT);
		SearchHits hits = search.getHits();
		hits.forEach(item -> {
			String sourceAsString = item.getSourceAsString();
			log.info(sourceAsString);
		});
		log.info(hits.getHits().length + "");

		System.out.println();

	}


	@ElasticsearchDB("es1")
	public void get(){

	}

}

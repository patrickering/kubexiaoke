package com.xiaoke.model.kube.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoke.common.core.utils.R;
import com.xiaoke.elasticsearch.ElasticsearchConfig;
import com.xiaoke.elasticsearch.ElasticsearchProperties;
import com.xiaoke.entity.kube.dto.AppPodRecordDTO;
import com.xiaoke.entity.kube.entity.AppPodRecord;
import com.xiaoke.model.kube.mapper.AppPodRecordMapper;
import com.xiaoke.model.kube.service.AppPodRecordService;
import com.xiaoke.properties.ColonyProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.HttpAsyncResponseConsumerFactory;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 应用pod记录 Service
 *
 * @author xiaoke
 * @date 2024-08-14 23:23:56
 */
@Service
@AllArgsConstructor
@Slf4j
public class AppPodRecordServiceImpl extends ServiceImpl<AppPodRecordMapper, AppPodRecord> implements AppPodRecordService {
    private final AppPodRecordMapper appPodRecordMapper;
    private final ColonyProperties colonyProperties;

    /**
     * 封装共用查询条件
     *
     * @param appPodRecord
     * @return
     */
    private QueryWrapper<AppPodRecord> baseQuery(AppPodRecord appPodRecord) {
        QueryWrapper<AppPodRecord> query = Wrappers.query();
        query.orderByDesc("id");
        return query;
    }


    /**
     * 获取应用pod记录列表
     *
     * @param appPodRecord
     * @return
     */
    @Override
    public List<AppPodRecord> listAppPodRecord(AppPodRecord appPodRecord) {
        QueryWrapper<AppPodRecord> query = this.baseQuery(appPodRecord);
        if (query == null) {
            return new ArrayList<>();
        }
        return this.list(query);
    }

    /**
     * 添加应用pod记录
     *
     * @param appPodRecord
     * @return
     */
    @Override
    public R<Boolean> saveAppPodRecord(AppPodRecord appPodRecord) {
        log.info("添加应用pod记录！应用pod记录:{}", appPodRecord);
        Boolean saveFlag = this.save(appPodRecord);
        if (saveFlag) {
            return R.ok(Boolean.TRUE, "添加应用pod记录成功！");
        } else {
            return R.ok(Boolean.FALSE, "添加应用pod记录失败！");
        }
    }

    /**
     * pod 日志
     *
     * @param page
     * @param appPodRecord
     * @param namespace
     * @return
     */
    @Override
    public R<IPage> podHistoryLog(Page page, AppPodRecordDTO appPodRecord, String namespace) {


        ElasticsearchProperties elasticsearchProperties = new ElasticsearchProperties();
        List<String> clusterNodes = new ArrayList<>();
        clusterNodes.add(colonyProperties.getLogUrl());
        elasticsearchProperties.setClusterNodes(clusterNodes);
        elasticsearchProperties.setClusterName("elasticsearch-0");

        if (StrUtil.isNotEmpty(colonyProperties.getLogUsername()) && StrUtil.isNotEmpty(colonyProperties.getLogPassword())) {
            ElasticsearchProperties.Account account = new ElasticsearchProperties.Account();
            account.setUsername(colonyProperties.getLogUsername());
            account.setPassword(colonyProperties.getLogPassword());
            elasticsearchProperties.setAccount(account);
        }

        ElasticsearchConfig elasticsearchConfig = new ElasticsearchConfig();
        RestHighLevelClient restHighLevelClient = elasticsearchConfig.restHighLevelClient(elasticsearchProperties);


        SearchRequest searchRequest = new SearchRequest("filebeat-7.3.2-*");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

        boolQueryBuilder.must(QueryBuilders.termQuery("kubernetes.pod.name", appPodRecord.getPodName()));
        boolQueryBuilder.must(QueryBuilders.termQuery("kubernetes.namespace", namespace));

        DateTime startTime = DateUtil.parse(appPodRecord.getStartTime());
        DateTime endTime = DateUtil.parse(appPodRecord.getEndTime());

        boolQueryBuilder.filter(QueryBuilders.rangeQuery("@timestamp").from(startTime.getTime(), true).to(endTime.getTime(), true));

        searchSourceBuilder.query(boolQueryBuilder);
        //分页
        int current = (int) page.getCurrent();
        int size = (int) page.getSize();
        searchSourceBuilder.from((current - 1) * size).size((size));
        ////误拼写时的fuzzy模糊搜索方法 2表示允许的误差字符数
        //QueryBuilders.fuzzyQuery("title", "ceshi").fuzziness(Fuzziness.build("2"));
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = null;
        try {
            RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
            // 默认缓冲限制为100MB，此处修改为30MB。
            builder.setHttpAsyncResponseConsumerFactory(new HttpAsyncResponseConsumerFactory.HeapBufferedResponseConsumerFactory(30 * 1024 * 1024));
            searchResponse = restHighLevelClient.search(searchRequest, builder.build());
        } catch (IOException e) {
            e.printStackTrace();
        }

        SearchHit[] hits = searchResponse.getHits().getHits();
        List<Map> storeList = new ArrayList<>();
        Arrays.stream(hits).forEach(hit -> {
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            storeList.add(sourceAsMap);
        });
        page.setRecords(storeList);
        page.setTotal(searchResponse.getHits().getTotalHits().value);
        return R.ok(page);
    }


}
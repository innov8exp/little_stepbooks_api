package net.stepbooks.domain.xfyun.service.impl;

import cn.xfyun.api.IseClient;
import cn.xfyun.model.response.ise.IseResponseData;
import cn.xfyun.service.ise.AbstractIseWebSocketListener;
import lombok.extern.slf4j.Slf4j;
import net.stepbooks.domain.xfyun.service.XfIseService;
import okhttp3.Response;
import okhttp3.WebSocket;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j

public class XfIseServiceImpl implements XfIseService {
    @Value("${xf-yun.host-url}")
    private String hostUrl;
    @Value("${xf-yun.app-id}")
    private String appId;
    @Value("${xf-yun.api-secret}")
    private String apiSecret;
    @Value("${xf-yun.api-key}")
    private String apiKey;
    @Value("${xf-yun.check-type}")
    private String checkType;
    @Value("${xf-yun.grade}")
    private String grade;
    @Value("${xf-yun.group}")
    private String group;

    @Override
    public Double getIseResult(InputStream inputStream, String text) {
        IseClient client = new IseClient.Builder()
                .signature(appId, apiKey, apiSecret)
                .addSub("ise")
                .addEnt("cn_vip")
                .addCategory("read_sentence")
                .addTte("utf-8")
                .addText('\uFEFF' + text)
                .addRstcd("utf8")
                .addRst("plain")
                .addCheckType(checkType)
                .addGrade(grade)
                .addGroup(group)
                .build();
        CompletableFuture<String> future = new CompletableFuture<>();

        String resultStr = "";
        try {
            client.send(inputStream, new AbstractIseWebSocketListener() {
                @Override
                public void onSuccess(WebSocket webSocket, IseResponseData iseResponseData) {
                    log.info("sid:"
                            + iseResponseData.getSid() + "\n最终识别结果:\n"
                            + new String(Base64.getDecoder().decode(iseResponseData.getData().getData()),
                            StandardCharsets.UTF_8));
                    String result = new String(Base64.getDecoder().decode(iseResponseData.getData().getData()),
                            StandardCharsets.UTF_8);
                    future.complete(result);
                }

                @Override
                public void onFail(WebSocket webSocket, Throwable throwable, Response response) {
                    String result = "";
                    future.complete(result);
                    System.out.println(response);
                }
            });
            resultStr = future.get();
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        double totalScore = 0L;
        if (ObjectUtils.isNotEmpty(resultStr)) {
            try {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(new ByteArrayInputStream(resultStr.getBytes()));

                doc.getDocumentElement().normalize();

                NodeList totalScoreList = doc.getElementsByTagName("total_score");
                if (totalScoreList.getLength() > 0) {
                    Element totalScoreElement = (Element) totalScoreList.item(0);
                    String value = totalScoreElement.getAttribute("value");
                    totalScore = Double.parseDouble(value);
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        return totalScore;
    }
}

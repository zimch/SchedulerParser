package com.zimch.jnews.job;

import com.zimch.jnews.model.News;
import com.zimch.jnews.service.NewsService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ParseTask {

    @Autowired
    NewsService newsService;

    @Scheduled(fixedDelay = 10000)
    public void parseNewNews() {
        String url = "https://spb.hh.ru/search/vacancy?text=java&salary=&area=2&ored_clusters=true&enable_snippets=true";

        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla")
                    .timeout(5000)
                    .referrer("https://google.com")
                    .get();
            Elements news = doc.getElementsByClass("serp-item__title");
            for (Element element : news) {
                String title = element.ownText();
                if (!newsService.isExist(title)) {
                    News obj = new News();
                    obj.setTitle(title);
                    newsService.save(obj);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

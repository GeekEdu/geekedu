package com.zch.demo;

import com.meilisearch.sdk.exceptions.MeilisearchException;
import com.meilisearch.sdk.model.SearchResult;
import org.json.*;
import com.meilisearch.sdk.*;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

/**
 * @author Poison02
 * @date 2024/4/2
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @PostMapping("/add")
    public void add() {
        try {
            insert();
        } catch (Exception e) {
            e.printStackTrace();
            ;
        }
    }

    @GetMapping("/basic")
    public SearchResult basic(@RequestParam("keywords") String keywords) {
        try {
            return basicSearch(keywords);
        } catch (MeilisearchException e) {
            throw new RuntimeException(e);
        }
    }

    public SearchResult basicSearch(String keywords) throws MeilisearchException {
        Client client = new Client(new Config("http://localhost:7700", "vGYHVruzupdS6SBD_Nrv3F8hnp4JETgp824odEZc5vA"));

        // An index is where the documents are stored.
        Index index = client.index("tests");
        return index.search(new SearchRequest(keywords)
                .setShowMatchesPosition(true)
                .setAttributesToHighlight(new String[]{"title"}));
    }

    public void insert() throws MeilisearchException {
        JSONArray array = new JSONArray();
        ArrayList items = new ArrayList() {{
            add(new JSONObject()
                    .put("id", "7")
                    .put("title", "Carghjol")
                    .put("genres", new JSONArray("[\"Romance\",\"Drama\"]"))
            );
            add(new JSONObject()
                    .put("id", "8")
                    .put("title", "Wondegjhjr Woman")
                    .put("genres", new JSONArray("[\"Action\",\"Adventure\"]")));
            add(new JSONObject()
                    .put("id", "9")
                    .put("title", "Life ojhgjf Pi")
                    .put("genres", new JSONArray("[\"Adventure\",\"Drama\"]")));
            add(new JSONObject()
                    .put("id", "10")
                    .put("title", "Mad Mdfghax: Fury Road")
                    .put("genres", new JSONArray("[\"Adventure\",\"Science Fiction\"]")));
            add(new JSONObject()
                    .put("id", "11")
                    .put("title", "Moagsdfghgna")
                    .put("genres", new JSONArray("[\"Fantasy\",\"Action\"]")));
            add(new JSONObject()
                    .put("id", "12")
                    .put("title", "Philasdfsfadelphia")
                    .put("genres", new JSONArray("[\"Drama\"]")));
        }};

        array.put(items);
        String documents = array.getJSONArray(0).toString();
        Client client = new Client(new Config("http://localhost:7700", "vGYHVruzupdS6SBD_Nrv3F8hnp4JETgp824odEZc5vA"));

        // An index is where the documents are stored.
        Index index = client.index("tests");

        // If the index 'movies' does not exist, Meilisearch creates it when you first add the documents.
        index.addDocuments(documents); // => { "taskUid": 0 }
    }

}

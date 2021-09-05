package com.hfad.readapp2.searchview;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;

import com.hfad.readapp2.Manga;

import org.json.JSONArray;
import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;



public class asyn extends AsyncTask<String, Void, ArrayList<Manga>> {

    //Đây là interface khi suggestion được xây dựng xong nó sẽ gọi tới hàm trong MainActivity.java
    MakeSuggestion makeSuggestion ;
    ArrayList<Manga> listArticle = new ArrayList<>();

    public asyn(MakeSuggestion makeSuggestion) {
        this.makeSuggestion = makeSuggestion;
    }

    private List<Suggestion> suggestions = new ArrayList<>();

    @Override
    protected void onPostExecute(ArrayList<Manga> articles) {
        super.onPostExecute(articles);



        //arr_main chính là từ khoá bạn muốn được suggest
        // arr_sub chính là kết quả suggest được trả về từ google

        for (int i = 0; i < articles.size(); i++) {
            Manga manga1 = articles.get(i);
            suggestions.add(new Suggestion(manga1.getTentruyen(),manga1.getLinktruyen(),manga1.getLinkanh())) ;
        }

        //gởi dữ liệu sang MainActivity.java
        makeSuggestion.getSuggestion(suggestions);
    }

    @Override
    protected ArrayList<Manga> doInBackground(String... strings) {
        Document document = null;



        try {
            /*Log.d("hi bye",strings[0]);
            //Thời gian chờ xem thừ người dùng có nhập thêm kí tự nữa hay không.
            Thread.sleep(250);

            Request request = new Request.Builder()
                    .url(strings[0])
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            return response.body().string();*/
            //Thread.sleep(250);
            SystemClock.sleep(2000);
            document = (Document) Jsoup.connect(strings[0]).get();
            if (document != null) {
                Elements sub = document.select("div.thumb-item-flow");
                for (Element element : sub) {
                    Manga article = new Manga();
                    Element titleSubject = element.getElementsByClass("thumb_attr series-title").first();
                    Element imgSubject = element.select("div[data-bg]").first();
                    Element descrip = element.getElementsByClass("thumb_attr chapter-title text-truncate").first();
                    Element linkmanga = element.select("a").first();
                    //Parse to model
                    //Log.d("hi",descrip.text());
                    if (titleSubject != null) {
                        Element titleS = titleSubject.selectFirst("[title]");
                        String title = titleS.attr("title");
                        article.setTentruyen(title);
                    }
                    if (imgSubject != null) {
                        String src = imgSubject.attr("data-bg");

                        article.setLinkanh(src);
                    }
                    if (descrip != null) {
                        String des = descrip.attr("title");
                        article.setChap(des);
                    }
                    if (linkmanga != null) {
                        String linkm = linkmanga.attr("href");
                        article.setLinktruyen(linkm);
                    }
                    listArticle.add(article);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listArticle;
    }
}
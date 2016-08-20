package pe.joedayz.campus.controller;

/**
 * Created by acme on 25/04/2016.
 */
public class PageInfo {
    private String name;
    private String url;

    public PageInfo(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        if (url == null){
            return "URL IS NULL";
        }
        if (url.indexOf(".") < 0){
            return url + ".index";
        }
        return url;
    }
}

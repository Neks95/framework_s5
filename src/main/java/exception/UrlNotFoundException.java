package main.java.exception;

public class UrlNotFoundException extends Exception{
    private String url;

    public UrlNotFoundException(String url){
        super("L'url "+ url + "n'existe pas(404");
        this.url = url;
    }
    public String getUrl(){
        return url;
    }
    
}

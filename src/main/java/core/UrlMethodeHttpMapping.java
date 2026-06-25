package main.java.core;

import java.util.Objects;

public class UrlMethodeHttpMapping {
    private String url;
    private MethodeHttp methode;

    public UrlMethodeHttpMapping(String url, MethodeHttp methode) {
        this.url = url;
        this.methode = methode;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public MethodeHttp getMethode() {
        return methode;
    }
    public void setMethode(MethodeHttp methode) {
        this.methode = methode;
    }
    
    @Override
    public boolean equals(Object obj){
        if(obj == this){
            return true;
        }
        if(!(obj instanceof UrlMethodeHttpMapping)){
            return false;
        }
         UrlMethodeHttpMapping m = (UrlMethodeHttpMapping) obj;
         return (m.getUrl().equals(this.getUrl()) && m.getMethode() == this.getMethode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, methode);
    }
}

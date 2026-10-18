package main.java.core;

public class MapFile {
    private String nom_fichier;
    private String extension;
    private byte[] content;
    
    public MapFile(String nom_fichier, String extension, byte[] content) {
        this.nom_fichier = nom_fichier;
        this.extension = extension;
        this.content = content;
    }

    public String getNom_fichier() {
        return nom_fichier;
    }
    public void setNom_fichier(String nom_fichier) {
        this.nom_fichier = nom_fichier;
    }
    public String getExtension() {
        return extension;
    }
    public void setExtension(String extension) {
        this.extension = extension;
    }
    public byte[] getContent() {
        return content;
    }
    public void setContent(byte[] content) {
        this.content = content;
    }
    
    
}

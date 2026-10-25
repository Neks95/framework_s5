package main.java.core;

import java.io.InputStream;

import jakarta.servlet.http.Part;

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

     public static MapFile toMapFile(Part part) throws Exception {
        String submitted = part.getSubmittedFileName();
        String nom = submitted;
        String ext = "";
        int dot = submitted.lastIndexOf('.');
        if (dot > 0) {
            nom = submitted.substring(0, dot);
            ext = submitted.substring(dot + 1);
        }
        try (InputStream in = part.getInputStream()) {
            return new MapFile(nom, ext, in.readAllBytes());
        }
    }
    
    
}

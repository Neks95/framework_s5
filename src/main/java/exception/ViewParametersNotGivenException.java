package main.java.exception;

public class ViewParametersNotGivenException extends Exception{


    public ViewParametersNotGivenException(){
        super("Vous n'avez preciser le repertoire des vues et l'extension des fichiers , view/ et .jsp vont etre utiliser .");
    }
    
   
    
    
}

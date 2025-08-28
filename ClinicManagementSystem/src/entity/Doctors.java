/*@author ng jim shen*/
package entity;

public class Doctors {
    private String docName;
    private String docSpecialty;
    private String docEmail;
    private String docNumber;
    private String docLanguages;
    private String docID;

    public Doctors(String docName, String docSpecialty, String docEmail, String docNumber, String docLanguages, String docID) {
        this.docName = docName;
        this.docSpecialty = docSpecialty;
        this.docEmail = docEmail;
        this.docNumber = docNumber;
        this.docLanguages = docLanguages;
        this.docID = docID;
    }
    public void setDocID(String docID) {
        this.docID = docID;
    }
    public String getDocID(){
        return docID;
    }
    
     public void setDocName (String docName){
        this.docName= docName;
    }
    public String getDocName(){
        return docName;
    }
    
    public void setDocSpecialty (String docSpecialty){
        this.docSpecialty= docSpecialty;
    }
    public String getDocSpecialty(){
        return docSpecialty;
    }
    
    public void setDocEmail (String docEmail){
        this.docEmail= docEmail;
    }
    public String getDocEmail(){
        return docEmail;
    }
    
    public void setDocNumber (String docNumber){
        this.docNumber= docNumber;
    }
    public String getDocNumber(){
        return docNumber;
    }
    
    public void setDocLanguages (String doclanguages){
        this.docLanguages= doclanguages;
    }
    public String getDocLanguages(){
        return docLanguages;
    }

    public String toString() {
        return "Doctor Name: " + docName + "\n Doctor Specialty : " + docSpecialty + 
                "\n Doctor Email: " + docEmail + "\n Doctor Phone Number: " + docNumber +
                "\n Doctor Spoken Languages: " + docLanguages + "\n Doctor ID: " + docID; 
        
    }
    }

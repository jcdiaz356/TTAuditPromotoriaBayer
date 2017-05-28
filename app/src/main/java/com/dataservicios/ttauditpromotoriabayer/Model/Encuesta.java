package com.dataservicios.ttauditpromotoriabayer.Model;

/**
 * Created by usuario on 07/04/2015.
 */
public class Encuesta {
    private String Question ;
    private int Id, IdAuditoria;

    //private ArrayList<String> genre;

    public Encuesta() {
    }

    public Encuesta(String Question, int Id) {
        this.Question = Question;
        this.Id= Id;
    }
    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public int getIdAuditoria() {
        return IdAuditoria;
    }

    public void setIdAiditoria(int IdAuditoria) {
        this.IdAuditoria = IdAuditoria;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String Question) {
        this.Question = Question;
    }



}

package todolist;

public class Tache{
    private int id;
    private String description;
    private Statut statut;

    public Tache(final String description){
        this.description = description;
        this.statut = Statut.NON_FAITE;
    }

    public int getId(){
        return id;
    }
    public void setId(final int identifiant){
        this.id = identifiant;
    }
    public String getDescription(){
        return description;
    }
    public Statut getStatut(){
        return statut;
    }

    public void changerId(final int id){
        this.id = id;
    }

    public void marquerCommeFaite(){
        this.statut = Statut.FAITE;
    }

    public String toString(){
        return id + " - " + description + " - " + statut;
    }
    public void finalize(){
        System.out.println("Tache supprimée");
    }
}

package todolist;

public class Tache{
    private static int compteur = 1;
    private int id;
    private String description;
    private Statut statut;

    public Tache(final String description){
        this.id = compteur++;
        this.description = description;
        this.statut = Statut.NON_FAITE;
    }

    public int getId(){
        return id;
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
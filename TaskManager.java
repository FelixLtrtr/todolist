package todolist;

import java.io.IOException;
import java.nio.Buffer;
import java.util.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class TaskManager{

    private static Set<Tache> mesTaches = new HashSet<>();
    private static BufferedReader lecture = new BufferedReader(new InputStreamReader(System.in));

    private static void estVide() throws ListeVide{
        if (mesTaches.isEmpty()){
            throw new ListeVide();
        }
    }

    private static void tacheInconnue(final Integer id) throws NumeroIndisponible{
        Set<Integer> listeNumeros = new HashSet<>();
        mesTaches.forEach((maTache) -> {listeNumeros.add(Integer.valueOf(maTache.getId()));});
        if (!listeNumeros.contains(id)){
            throw new NumeroIndisponible();
        }
    }
    
    public static void main(String[] args) throws ListeVide, IOException {
        System.out.println("--- Ma ToDoList ---");
        while (true){
            System.out.println("Que souhaitez-vous faire ?\n1 - Afficher les taches\n2 - Ajouter une tache\n3 - Marquer une tache comme faite\n4 - Filtrer les taches\n5 - Supprimer une tache\n0 - Quitter");
            int choix = Integer.parseInt(lecture.readLine());

            switch(choix){
                case 0 -> System.exit(0);
                case 1 -> afficherTaches();
                case 2 -> ajouterTache();
                case 3 -> marquerCommeFaite();
                case 4 -> filtrerTaches();
                case 5 -> supprimerTache();
                default -> System.out.println("Choix incorrect");
            }
        }
    }

    private static void afficherTaches() throws ListeVide{
        try{
            estVide();
            System.out.println("\n");
            mesTaches.forEach((maTache) -> System.out.println(maTache.toString()));
            System.out.println("\n");
        }catch(ListeVide e){
            System.out.println("Vous n'avez aucune Tache de définie");
        }
    }
    
    private static void ajouterTache() throws IOException {
        System.out.println("Tache à ajouter : ");
        String nouvelleTache = lecture.readLine();
        mesTaches.add(new Tache(nouvelleTache));
    }

    private static void filtrerTaches() throws IOException{
        try {
            estVide();
            System.out.println("Filtrer sur quels critère (FAITE / NON_FAITE) ?");
            Statut s = Statut.valueOf(lecture.readLine().toUpperCase());
            Set<Tache> tachesFiltrees = new HashSet<>();
            mesTaches.forEach((aFiltrer) -> {
                if (aFiltrer.getStatut() == s) {
                    tachesFiltrees.add(aFiltrer);
                }
            });
            tachesFiltrees.forEach(System.out::println);
        }catch(ListeVide e){
            System.out.println("Vous n'avez aucune tache de définie");
        }
    }

    private static void marquerCommeFaite() throws IOException, NumeroIndisponible {
        try {
            System.out.println("ID de la tache à marquer comme faite :");
            String numero = lecture.readLine();
            Integer num = Integer.parseInt(numero);
            int identifiant = num;
            tacheInconnue(num);
            mesTaches.stream()
                    .filter(t -> t.getId() == identifiant)
                    .findFirst()
                    .ifPresent(Tache::marquerCommeFaite);

            System.out.println("La tache " + numero + " a été marquée comme faite.");
        }catch(NumeroIndisponible e){
            System.out.println("Cette tache n'existe pas");
        }
    }

    private static void supprimerTache() throws IOException {
        System.out.println("ID de la tache à supprimer :");
        String numero = lecture.readLine();
        Integer num = Integer.parseInt(numero);
        int identifiant = num;
        tacheInconnue(num);
        mesTaches.stream()
                .filter(t -> t.getId() == identifiant)
                .findFirst()
                .ifPresent(Tache::finalize);

        mesTaches.removeIf(t -> t.getId() == identifiant);

        System.out.println("La tache " + numero + " a été supprimée.");
    }

}

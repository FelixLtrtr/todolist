package todolist;

import java.io.IOException;
import java.nio.Buffer;
import java.util.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.File;
import java.io.FileWriter;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class TaskManager{

    private static Set<Tache> mesTaches = new LinkedHashSet<>();
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

        File fichierTaches = new File("Liste_Taches.txt");
        if (!fichierTaches.createNewFile()){
            Scanner dataExtract = new Scanner(fichierTaches);
            while(dataExtract.hasNextLine()){
                String data = dataExtract.nextLine();
                Pattern pattern = Pattern.compile("\\d - ");
                Matcher match = pattern.matcher(data);
                data = match.replaceFirst("");
                String [] separation = data.split(" - ");
                Tache task = new Tache(separation[0]);
                Statut etat = Statut.valueOf(separation[1].toUpperCase());
                if (etat == Statut.FAITE){
                    task.marquerCommeFaite();
                }
                mesTaches.add(task);
                int id = 0;
                for (Tache tache : mesTaches){
                    id++;
                    tache.setId(id);
                }

            }
        }
        int id = 1;
        System.out.println("--- Ma ToDoList ---");
        while (true){
            System.out.println("Que souhaitez-vous faire ?\n1 - Afficher les taches\n2 - Ajouter une tache\n3 - Marquer une tache comme faite\n4 - Filtrer les taches\n5 - Supprimer une tache\n0 - Quitter");
            int choix = Integer.parseInt(lecture.readLine());

            switch(choix){
                case 0 -> {
                    System.out.print("Sauvegarder les Taches ? (O/N) ");
                    String sauvegarde = lecture.readLine().toUpperCase();
                    if (sauvegarde.equals("O")){
                        sauvegarderTaches(mesTaches);
                        System.exit(0);
                    } else if (sauvegarde.equals("N")) {
                        System.exit(0);
                    }
                }
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
            System.out.println("Vous n'avez aucune tache de définie");
        }
    }
    
    private static void ajouterTache() throws IOException {
        System.out.println("Tache à ajouter : ");
        String nouvelleTache = lecture.readLine();
        mesTaches.add(new Tache(nouvelleTache));
        System.out.println("Tache ajoutée !");
        int id = 0;
        for (Tache tache : mesTaches){
            id++;
            tache.setId(id);
        }

    }

    private static void filtrerTaches() throws ListeVide, IOException, IllegalArgumentException{
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
            System.out.print("\n");
        }catch(ListeVide e){
            System.out.println("Vous n'avez aucune tache de définie");
        }catch(IllegalArgumentException e){
            System.out.println("Filtre inconnu");
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
        try {
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
            int id = 0;
            for (Tache tache : mesTaches){
                id++;
                tache.setId(id);
            }
        }catch(NumeroIndisponible e){
            System.out.println("Cette tache n'existe pas");
        }
    }

    private static void sauvegarderTaches(final Set<Tache> mesTaches) throws IOException, ListeVide{
        try{
            estVide();
            File tachesSauvegardees = new File("Liste_Taches.txt");
            FileWriter ecriture = new FileWriter(tachesSauvegardees.getName());
            mesTaches.forEach((maTache) -> {
                try {
                    ecriture.write(maTache.toString() + "\n");
                } catch(IOException e) {
                    System.out.println("Impossible d'écrire dans le fichier");
                }
            });
            ecriture.close();
        } catch(ListeVide e){
            System.out.println("Vous n'avez aucune tache de définie");
        }
    }
}

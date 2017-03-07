/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datosabiertosapp.DatosAbiertosApp;


import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.RDFNode;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import static java.lang.System.in;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Guille
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); //Ya tenemos el "lector"
        System.out.println("Por favor ingrese una ciudad: ");//Se pide un dato al usuario
        String ciudad = br.readLine(); //Se lee el nombre con readLine() que retorna un String con el dato
        
        ciudad = ciudad.substring(0, 1).toUpperCase() + ciudad.substring(1);
        
        consulta_dbpedia_ByCategoria(ciudad);
        
        
        
        
    }
    
    
    public static void consulta_dbpedia_ByCategoria(String ciudad)
     {
         
          Map<String,String> mapa = new HashMap<>();
         
          String queryAbstract = "PREFIX esdbr: <http://es.dbpedia.org/resource/>\n" +
"PREFIX dbpedia-owl: <http://dbpedia.org/ontology/>\n" +
"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n" +
"SELECT DISTINCT ?nombre ?info ?link  WHERE{ \n" +
"    ?monumento dbpedia-owl:wikiPageWikiLink <http://es.dbpedia.org/resource/Categoría:Monumentos_de_"+ciudad+"> .\n" +
"    ?monumento rdfs:label ?nombre. \n" +
"    ?monumento dbpedia-owl:abstract   ?info. \n" +
"    ?monumento dbpedia-owl:wikiPageExternalLink ?link}";
         
          
        Query query = QueryFactory.create(queryAbstract);
        QueryExecution qe = QueryExecutionFactory.sparqlService("http://es.dbpedia.org/sparql/", query);

        ResultSet results = qe.execSelect();
        
        if(!results.hasNext()){
            System.out.println("No existe en DBPEDIA información sobre monumentos de esta ciudad");
        }    
        
        while (results.hasNext()) {
            QuerySolution qs = (QuerySolution) results.next();
            
              String nombre = qs.get("nombre").toString();
              nombre = nombre.replace("@es", "");
              String info = qs.get("info").toString();
              info = info.replace("@es", "");
              
              if(!mapa.containsKey(nombre)){ //Si no esta
                    mapa.put(nombre, nombre); //Guardamos cada monumeto en un mapa
                    System.out.println("\nNombre: "+nombre);
                    System.out.println("\tInfo: "+info);
              }
              
              
            
            System.out.println("\t\tLink: "+qs.get("link").toString());
        }
        
        
        qe.close();
         
     }
    
    
}

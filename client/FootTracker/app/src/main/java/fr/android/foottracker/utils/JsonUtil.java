package fr.android.foottracker.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import fr.android.foottracker.model.entities.data.common.AbstractIdentifiedData;

// Outil de serialisation/deserialisation JSON.
public final class JsonUtil {

    private final static Gson gson = new GsonBuilder().serializeNulls().create();

    private JsonUtil(){}

    /**
     * Serialise une entite xxxData en chaine de caracteres au format JSON.
     * @param data entite qu'on veut serialiser en JSON.
     * @return La chaine de caracteres contenant l'objet serialise au format JSON.
     */
    public static String toJson(AbstractIdentifiedData data){
        return gson.toJson(data);
    }

    /***
     * Deserialise une chaine de caracteres au format JSON en une entite xxxData.
     * @param json Chaine de caracteres au format JSON contenant l'entite serialisee.
     * @param dataClass Classe de l'objet qu'on obtenir par la deserialisation du JSON.
     * @param <TData> Type de l'objet qu'on obtiendra apres la deserialisation.
     * @return L'objet obtenu apres deserialisation de la chaine de caracteres au format JSON.
     */
    public static <TData extends AbstractIdentifiedData> TData fromJson(String json, Class<TData> dataClass){
        return gson.fromJson(json, dataClass);
    }

}

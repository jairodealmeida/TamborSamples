package com.tambor.samples.placeholder;

import com.tambor.samples.database.models.Quest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class PlaceholderContent {

    /**
     * An array of sample (placeholder) items.
     */
    public static final List<PlaceholderItem> ITEMS = new ArrayList<PlaceholderItem>();

    /**
     * A map of sample (placeholder) items, by ID.
     */
    public static final Map<String, PlaceholderItem> ITEM_MAP = new HashMap<String, PlaceholderItem>();


    private static final List<Quest> list;

    static {
        list = new ArrayList<>();
        list.add(new Quest(1,"Todas as documentações referentes a escavação estão em conformidade (APR, PTE, laudos e projetos)?",false,""));
        list.add(new Quest(2,"Os envolvidos na atividade são devidamente qualificados e habilitados?",false,""));
        list.add(new Quest(3,"Foram levadas possíveis interferências na execução das atividades (tubulações, cabos elétricos, estruturas etc? Estas interferências estão documentadas e anexadas à APR?",false,""));
        list.add(new Quest(4,"As instalações elétricas ..................?",false,""));
        list.add(new Quest(5,"Para escavações ...............?",false,""));
        list.add(new Quest(6,"O escoramento é contínuo? .............?",false,""));
        list.add(new Quest(7,"O acesso dos colaboradores .......................?",false,""));
        list.add(new Quest(8,"O escoramento está construido .............................................?",false,""));
        list.add(new Quest(9,"Foi considerado o desvio ..........................................?",false,""));
        // Add some sample items.
        for (int i = 0; i < list.size(); i++) {
            addItem(createPlaceholderItem(list.get(i)));
        }
    }

    private static void addItem(PlaceholderItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }
    private static PlaceholderItem createPlaceholderItem(Quest quest) {
        return new PlaceholderItem(quest.getId().toString(), quest.getDesc(),quest.getValue(),quest.getObservation());
    }
    /*private static PlaceholderItem createPlaceholderItem(int position) {
        return new PlaceholderItem(
                String.valueOf(position),
                "Item " + position, makeDetails(position),true,"Test");
    }*/

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A placeholder item representing a piece of content.
     */
    public static class PlaceholderItem {
        public final String id;
        public final String details;
        public final Boolean value;
        public final String obs;

        public PlaceholderItem(String id, String details, Boolean value, String obs) {
            this.id = id;
            this.details = details;
            this.value = value;
            this.obs = obs;
        }

        @Override
        public String toString() {
            return details;
        }
    }
}
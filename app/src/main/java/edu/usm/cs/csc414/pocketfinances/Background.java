package edu.usm.cs.csc414.pocketfinances;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for maintaining a list of potential UI backgrounds from which the user can choose.
 */
public class Background {


    // Predefined background objects that can be accessed statically.
    public static final Background SPACE = new Background(R.drawable.background_space, "Space");
    public static final Background DARK_GREY = new Background(R.drawable.background_dark, "Dark Grey");
    public static final Background LIGHT_BLUE = new Background(R.drawable.background_lightblue, "Light Blue");




    // Private member variables
    /**
     * The integer ID of the background as defined in the resources directory of the app.
     */
    private int resourceId;


    /**
     * The display name of the background.
     */
    private String name;






    /**
     * The parameterized constructor for the Background class
     * @param resourceId The integer ID of the background as defined in the resources directory of the app.
     * @param name The display name of the background.
     */
    private Background(int resourceId, String name) {
        this.resourceId = resourceId;
        this.name = name;
    }


    /**
     * Method that gets the list of statically predefined backgrounds.
     *
     * @return A {@link List} of the static objects.
     */
    public static List<Background> getBackgroundList() {
        List<Background> backgrounds = new ArrayList<>();

        backgrounds.add(SPACE);
        backgrounds.add(DARK_GREY);
        backgrounds.add(LIGHT_BLUE);

        return backgrounds;
    }


    /**
     * Method that gets the display name of the background.
     *
     * @return The display name of the background.
     */
    public String getName() {
        return name;
    }


    /**
     * Method that gets the resource ID of the background as defined in the resources directory.
     *
     * @return The integer resource ID of the background.
     */
    public int getResourceId() {
        return resourceId;
    }


    /**
     * Gets the Background object that corresponds to a particular resource ID.
     *
     * @param resourceId The resource ID of the background you would like to retrieve.
     * @return The background object with the specified resource ID, or null if a match cannot be found.
     */
    public static Background getById(int resourceId) {
        for (Background background : getBackgroundList())
            if (resourceId == background.getResourceId())
                return background;
        return null;
    }


    /**
     * Gets the Background object that corresponds to a particular display name.
     *
     * @param name The display name of the background you would like to retrieve.
     * @return The background object with the specified display name, or null if a match cannot be found.
     */
    public static Background getByName(String name) {
        for (Background background : getBackgroundList())
            if (name.equals(background.getName()))
                return background;
        return null;
    }
}

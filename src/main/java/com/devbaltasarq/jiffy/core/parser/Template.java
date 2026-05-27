// Jiffy (c) 2023/25 Baltasar MIT License <baltasarq@gmail.com>


package com.devbaltasarq.jiffy.core.parser;

import java.util.Map;


/** A basic set of templates for the user.
  * @author baltasarq
  */
public class Template {
    public static final String TEMPLATE_INI = """
                                            [[Adventure]]
                                            @author = "author@awesome.com"
                                            @pic = "res/awesome_pic.jpg"
                                            @version = "v1.0"
                                            @start = "House"
                                            
                                            There is a cavern full of treasures close by.
                                            Can you find them all?

                                            [[outside_house | Outside the house]]
                                            An old brick house.
                                              
                                            [Mailbox]
                                            @syn = "box, mail"
                                            A very rusty tin box.

                                            ...""";
    public static final String TEMPLATE_LOC = """
                                              
                                            [[$(LOC_ID) | $(LOC_NAME)]]
                                            @pic = "res/$(LOC_PIC).jpg"
                                            @syn = "word1, word2"

                                            $(LOC_DESC)
                                            """;

    public static final String TEMPLATE_OBJ = """

                                            [$(OBJ_ID) | $(OBJ_NAME)]
                                            @syn = "word1, word2"

                                            $(OBJ_DESC)
                                            """;

    /** A new template.
      * @param template the string with the placeholders.
      */
    public Template(String template)
    {
        this.template = template;
    }
    
    /** Applies all the given substitutions to the given template.
      * @param SUBSTS the dictionary with the substitutions.
      * @return a string with all the substitutions applied.
      */
    public String applySubsts(final Map<String, String> SUBSTS)
    {
        String toret = this.template;

        for(Map.Entry<String, String> entry: SUBSTS.entrySet()) {
            toret = toret.replace( "$(" + entry.getKey() + ")", entry.getValue() );
        }

        return toret;
    }
    
    private String template;
}

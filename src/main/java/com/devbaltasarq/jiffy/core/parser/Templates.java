// Jiffy (c) 2023/25 Baltasar MIT License <baltasarq@gmail.com>


package com.devbaltasarq.jiffy.core.parser;


/** A basic set of templates for the user.
  * @author baltasarq
  */
public class Templates {
    public static final String TEMPLATE_INI = """
                                            [[Adventure]]
                                            @author = "author@awesome.com"
                                            @pic = "res/awesome_pic.jpg"
                                            @version = "v1.0"
                                            @start = "House"
                                            
                                            There is a cavern full of treasures close by.
                                            Can you find them all?

                                              
                                              
                                              
                                            ...""";
    public static final String TEMPLATE_LOC = """
                                            [[$(LOC_NAME]]
                                            @pic = "res/$(LOC_PIC)"

                                            $(LOC_DESC)
                                            """;

    public static final String TEMPLATE_OBJ = """
                                            [$(OBJ_NAME]
                                            $(OBJ_DESC)
                                            """;
}

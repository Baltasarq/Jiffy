// Jiffy (c) 2023 Baltasar MIT License <baltasarq@gmail.com>


package com.devbaltasarq.jiffy.core.emitter.templates.fijs;


import com.devbaltasarq.jiffy.core.ast.Entity;
import com.devbaltasarq.jiffy.core.emitter.templates.Templater;


/** Templates for the companion CSS in fi-js. */
public class Css extends Templater {
    public Css(final Entity ENT)
    {
        super( ENT );
    }

    @Override
    public String subst()
    {
        return CSS_TEMPLATE;
    }

    private static final String CSS_TEMPLATE = """
            /* Default colors and sizes */
            body {
                margin: 5%;
            	background-color:black;
            	color:white;
            	font-familiy:Verdana,Arial,Helvetica;
            	font-size:16pt;
                cursor: crosshair;
            }
                        
            .clsAchieved {
                font-style: italic
            }
                        
            /* Hyperlinks */
            a:link, a:visited {
            	color:#777700;
            	text-decoration: none;
                cursor: crosshair
            }
                        
            a:hover {
            	color:#ffff00;
            	text-decoration: none;
                cursor: crosshair
            }
                        
            /* Hyperlinks: objs */
            a.clsLinkObj:link, a.clsLinkObj:visited {
            	color: lightblue;
            	text-decoration: none;
                cursor: crosshair
            }
                        
            a.clsLinkObj:hover {
            	color: cyan;
            	text-decoration: none;
                cursor: crosshair
            }
                        
            /* Hyperlinks: pnjs */
            a.clsLinkPnj:link, a.clsLinkPnj:visited {
            	color: darkmagenta;
            	text-decoration: none;
                cursor: crosshair
            }
                        
            a.clsLinkPnj:hover {
            	color: magenta;
            	text-decoration: none;
                cursor: crosshair
            }
                        
            /* Intro div, before game */
            #dvIntro {
                width: 70%;
            	text-align:center;
                display: block
            }
                        
            #hrIntroSeparator {
            	color:#000044;
            	background-color: #000044;
            	height:3px;
            	border:0
            }
                        
            #btBoot {
            	color:#770000;
            	font-weight:bold
            }
                        
            /* Game title */
            #dvTitle {
                width:70%;
            	margin:5%;
            	font-size:18pt;
            	color:lightgray
            }
                        
            @media screen and (max-width: 800px) {
                #dvTitle, #dvId {
                    display: none !important;
                }
            }
                        
            /* Game div */
            #dvFi {
                width:80%;
                margin-left: 5%;
                margin-right: 5%;
            }
                        
            #hrFiSeparator {
            	color:#000044;
            	background-color:#000044;
            	height:3px;
            	border:0
            }
                        
            /* Name of the room */
            #dvId {
            	width:100%;
            	color:#77aaff;
            	font-familiy:Verdana,Arial,Helvetica;
            	font-style:italic
            }
                        
            /* Image */
            #dvPic {
            	width:100%;
            	height:40%;
            	text-align:center
            }
                        
            /* Description text for current room */
            #dvDesc  {
            	color: green;
                width: 100%;
                text-align: justify
            }
                        
            /* Game's answer text */
            #dvAnswer {
            	font-familiy: Verdana,Arial,Helvetica;
                height: 150px;
            	font-size:14pt;
                text-align: justify;
                overflow-y: auto
            }
                        
            /* Input section, prompt, icons... */
            #dvInput {
            	color:white;
            	background-color:black;
            	width:100%;
            	font-size:20px;
            }
                        
            #edInput {
            	width:85%;
            	color:white;
            	background-color:black;
            	border:none;
            	font-size:20px;
            	border:1px dotted gray;
            	border-radius: 10px;
            	padding:6px
            }
                        
            #dvObjects {
                font-familiy: monospace;
                font-size: 16pt;
                text-align: center;
                color: white;
            }
                        
            /* Error section, this is normally not seen by players */
            #dvError {
            	color:white;
            	background-color:red;
            	width:100%;
            	font-familiy:Verdana,Arial,Helvetica;
            	font-size:24px;
            }
            """;
}

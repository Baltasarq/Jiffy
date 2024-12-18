// Jiffy (c) 2023 Baltasar MIT License <baltasarq@gmail.com>


package com.baltasarq.jiffy.core.emitter.templates.fijs;


import com.baltasarq.jiffy.core.ast.Entity;
import com.baltasarq.jiffy.core.ast.Story;
import com.baltasarq.jiffy.core.emitter.templates.Templater;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


/** Templates for the companion HTML in fi-js. */
public class Html extends Templater {
    public Html(final String TARGET_FILE_NAME, final Entity ENT)
    {
        super( ENT );
        this.TARGET_FILE_NAME = new File( TARGET_FILE_NAME ).getName();
        this.initDefaultSubsts();
    }

    @Override
    public String subst()
    {
        final Map<String, String> SUBSTS = new HashMap<>();
        final var ENT_STORY = (Story) this.getEntity();

        SUBSTS.put( "$STORY_FILE", this.TARGET_FILE_NAME );
        SUBSTS.put( "$STORY_ID", ENT_STORY.getId().get() );
        SUBSTS.put( "$STORY_UUID", ENT_STORY.getIfId().toString() );

        this.mergeSubstMaps( DEFAULT_SUBSTS, SUBSTS );
        return this.applySubsts( HTML_TEMPLATE, SUBSTS );
    }

    private void initDefaultSubsts()
    {
        final String[] KEYS = {
                "$STORY_FILE",
                "$STORY_ID",
                "$STORY_UUID",
        };
        final String[] VALUES = {
                /* $STORY_FILE      <- */ "error",
                /* $STORY_ID        <- */ "ERROR",
                /* $STORY_UUID      <- */ "123-error-456",
        };

        DEFAULT_SUBSTS.clear();
        for(int i = 0; i < KEYS.length; ++i) {
            DEFAULT_SUBSTS.put( KEYS[ i ], VALUES[ i ] );
        }

        return;
    }

    private final String TARGET_FILE_NAME;
    private static final Map<String, String> DEFAULT_SUBSTS = new HashMap<>();;
    private static final String HTML_TEMPLATE = """
                    <!DOCTYPE HTML>
                      <html>
                      <head>
                          <!-- uuid: $STORY_UUID -->
                      
                          <meta charset="utf-8" />
                          <script type="text/javascript" src="https://baltasarq.github.io/fi-js/lib/fi.js"></script>
                          <script type="text/javascript" src="https://baltasarq.github.io/fi-js/lib/verbs.js"></script>
                          <script type="text/javascript" src="$STORY_FILE.js"></script>
                          <title>$STORY_ID</title>
                          <link href="$STORY_FILE.css" rel="stylesheet" type="text/css" />
                      </head>
                      <body>
                      <div id="dvTop">
                          <table width="100%"><tr>
                              <td align="left"><div id="dvTitle"></div></td>
                              <td align="right"><div id="dvId"></div></td>
                          </tr></table>
                      </div>
                      <div id="dvIntro" style="display:block">
                          <form id="frmIntro" action="submit">
                              <input type="image" id="btBoot" class="btn"
                                     alt="enter" width="32" height="32"
                                     src="res/enter.png"
                                     onClick="return ctrl.boot();">
                          </form>
                      </div>
                      <div id="dvFi" style="display:none">
                          <div id="dvPic">
                          </div>
                          <div id="dvDesc">
                          </div>
                          <p>
                          <div id="dvCmds">
                              <table border="0pt" width="100%">
                                  <tr>
                                      <td style="width: 20%; text-align: center">
                                          <a href="#" onClick="javascript: ctrl.inject('i', true, true)">
                                              <img src="res/inv.png" alt="Inventario" title="Inventario"
                                                   style="max-width: 32px"/>
                                          </a>
                                      </td>
                                      <td style="width: 20%; text-align: center">
                                          <a href="#" onClick="javascript: ctrl.inject('m', true, true)">
                                              <img src="res/look.png" alt="Mirar alrededor"
                                                   title="Mirar alrededor"
                                                   style="max-width: 32px"/>
                                          </a>
                                      </td>
                                      <td style="width: 20%; text-align: center">
                                          <a href="#" onClick="javascript: ctrl.inject('save', true, true)">
                                              <img src="res/save.png" alt="Guarda" title="Guarda"
                                                   style="max-width: 32px"/>
                                          </a>
                                      </td>
                                      <td style="width: 20%; text-align: center">
                                          <a href="#" onClick="javascript: if ( window.confirm( '¿Seguro? Perderás todo lo que no esté guardado...' ) ) { ctrl.inject('load', true, true); }">
                                              <img src="res/load.png" alt="Recupera" title="Recupera"
                                                   style="max-width: 32px"/>
                                          </a>
                                      </td>
                              </table>
                          </div>
                          <p>
                          <div id="dvAnswer">
                          </div>
                          <p>
                          <div id="dvInput" style="width:100%;display:block">
                              <table width="100%"><tr>
                                  <form id="frmInput" action="submit" onsubmit="return parser.doParse();">
                                      <td align="left">
                                          &gt; <input type="text" class="txt" size="40"
                                                      maxLength="80" id="edInput"
                                                      placeholder="Tu orden..."
                                                      style="width:85%;border:none">
                                      </td>
                                      <td align="right"><table><tr>
                                          <td>
                                              <img id="btSend" alt="enter" width="32" height="32"
                                                   style="vertical-align:middle;"
                                                   src="res/enter.png"
                                                   onClick="return parser.doParse();">
                                          </td>
                                      </tr></table></td>
                                  </form>
                              </tr></table>
                              <table id="tbl-objects-actions" width="100%">
                                  <tr>
                                      <td>
                                          <div id="dvActions">
                                              <table id="subtbl-actions" align="center"></table>
                                          </div>
                                      </td>
                                      <td>
                                          <div id="dvObjects">
                                          </div>
                                      </td>
                                  </tr></table>
                          </div>
                          <div id="dvError">
                          </div>
                      </div>
                      </body>
                      </html>
            """;
}

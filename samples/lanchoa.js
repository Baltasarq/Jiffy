// lanchoa
/*
    ¡Cómete una anchoa!

    @ 2023-09-25 15:19
    IfId 09668b3e-62d0-4c8c-abd6-59580e53bd37
*/


// -------------------------------------------------- dormitorio ---
const LOC_DORMITORIO = ctrl.locs.crea(
    "dormitorio",
    [  ],
    "Solo hay una ${cama, ex cama} y una desvencijada mesilla. Una puerta \
     permite volver al ${salón, o}." );

LOC_DORMITORIO.ini = function() {
    this.pic = "";
};

const OBJ_CAMA = ctrl.creaObj(
    "cama",
    [  ],
    "Empujas el colchón hacia abajo y una nubecilla de polvo se eleva sobre \
     tu mano...",
    LOC_DORMITORIO,
    Ent.Scenery
);


// -------------------------------------------------- calle ---
const LOC_CALLE = ctrl.locs.crea(
    "calle",
    [  ],
    "" );

LOC_CALLE.ini = function() {
    this.pic = "";
};


// -------------------------------------------------- salón ---
const LOC_SALON = ctrl.locs.crea(
    "salón",
    [  ],
    "El salón es... antiguo. Parece como conservado en el tiempo por algún \
     misterioso mecanismo. El ${tresillo, ex tresillo} se sitúa frente a \
     un ${televisor, ex televisor} y una vieja ${radio, ex radio}, con una \
     ${mesa, ex mesa} en el medio, mientras tres puertas en los extremos \
     permiten dirigirse a la ${cocina, o}, al ${dormitorio, e} y a la \
     ${calle, s}." );

LOC_SALON.ini = function() {
    this.pic = "";
};

const OBJ_MESA = ctrl.creaObj(
    "mesa",
    [  ],
    "La típica mesa de centro.",
    LOC_SALON,
    Ent.Scenery
);

const OBJ_TELEVISOR = ctrl.creaObj(
    "televisor",
    [  ],
    "Si no fuera por el tamaño de la parte trasera (es de tubo), no tendría \
     nada de especial.",
    LOC_SALON,
    Ent.Scenery
);

const OBJ_TRESILLO = ctrl.creaObj(
    "tresillo",
    [  ],
    "Un sofá y dos sillones.",
    LOC_SALON,
    Ent.Scenery
);

const OBJ_RADIO = ctrl.creaObj(
    "radio",
    [  ],
    "La radio <b>no</b> funciona.",
    LOC_SALON,
    Ent.Scenery
);


// -------------------------------------------------- cocina ---
const LOC_COCINA = ctrl.locs.crea(
    "cocina",
    [  ],
    "La cocina es tremendamente austera, con solo una puerta entre el \
     ${salón, e} y ella. Un ${frigorífico, ex frigorifico} se arrincona \
     contra la esquina, mientras el ${fregadero, ex fregadero} se sitúa al \
     final de una ${mesado, ex mesado} entre ellos." );

LOC_COCINA.ini = function() {
    this.pic = "";
};

const OBJ_MESADO = ctrl.creaObj(
    "mesado",
    [  ],
    "Una larga plancha de piedra que... <b>no</b> es mármol de carrara.",
    LOC_COCINA,
    Ent.Scenery
);

const OBJ_FRIGORIFICO = ctrl.creaObj(
    "frigorífico",
    [  ],
    "Pues sí, una nevera blanca y alta, de las de siempre.",
    LOC_COCINA,
    Ent.Scenery
);

const OBJ_FREGADERO = ctrl.creaObj(
    "fregadero",
    [  ],
    "De acero inoxidable.",
    LOC_COCINA,
    Ent.Scenery
);


// ------------------------------------------------------- Player ---
const PLAYER = ctrl.personas.creaPersona(
    "Jugador",
    ["jugador", "jugadora" ],
    "El bravo PC.",
    LOC_DORMITORIO
);


// ---------------------------------------------------------- Ini ---
ctrl.ini = function() {
    this.setTitle( "lanchoa" );
    this.setIntro( "¡Cómete una anchoa!" );
    // this.setPic( "res/portada.jpg" );
    this.setAuthor( "Increíble Autor" );
    this.setVersion( "20230925" );
    this.personas.changePlayer( PLAYER );
    this.locs.setStart( LOC_DORMITORIO );
};

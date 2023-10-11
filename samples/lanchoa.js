// lanchoa
/*
    ¡Cómete una anchoa! Tendrás que comerte una anchoa, pero... ¡no vale \
     hacerlo de cualquier manera!

    @ 2023-10-11 16:44
    IfId 8cb9fdad-b054-4fc1-b06e-2570b7e0d45b
*/


// -------------------------------------------------- dormitorio ---
const LOC_DORMITORIO = ctrl.locs.crea(
    "Dormitorio",
    [ "dormitorio", "dormitorio" ],
    "Solo hay una ${cama, ex cama} y una desvencijada mesilla. Una puerta \
     permite volver al ${salón, $DIR}." );

LOC_DORMITORIO.ini = function() {
    this.pic = "res/dormitorio.jpg";
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
    "Calle",
    [ "calle", "calle" ],
    "" );

LOC_CALLE.ini = function() {
    this.pic = "res/calle.jpg";
};


// -------------------------------------------------- cocina ---
const LOC_COCINA = ctrl.locs.crea(
    "Cocina",
    [ "cocina", "cocina" ],
    "La cocina es tremendamente austera, con solo una puerta entre el \
     ${salón, $DIR} y ella. Un ${frigorífico, ex frigorifico} se arrincona \
     contra la esquina, mientras el ${fregadero, ex fregadero} se sitúa al \
     final de una ${mesado, ex mesado} entre ellos." );

LOC_COCINA.ini = function() {
    this.pic = "res/cocina.jpg";
};

const OBJ_MESADO = ctrl.creaObj(
    "mesado",
    [  ],
    "Una larga plancha de piedra que... <b>no</b> es mármol de carrara.",
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

const OBJ_FRIGORIFICO = ctrl.creaObj(
    "frigorifico",
    [  ],
    "Pues sí, una nevera blanca y alta, de las de siempre.",
    LOC_COCINA,
    Ent.Scenery
);


// -------------------------------------------------- salon ---
const LOC_SALON = ctrl.locs.crea(
    "Salón",
    [ "salon", "salón" ],
    "El salón es... antiguo. Parece como conservado en el tiempo por algún \
     misterioso mecanismo. El ${tresillo, ex tresillo} se sitúa frente a \
     un ${televisor, ex televisor} y una vieja ${radio, ex radio}, con una \
     ${mesa, ex mesa} en el medio, mientras tres puertas en los extremos \
     permiten dirigirse a la ${cocina, $DIR}, al ${dormitorio, $DIR} y a \
     la ${calle, $DIR}." );

LOC_SALON.ini = function() {
    this.pic = "res/salon.jpg";
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


// ------------------------------------------------------- Player ---
const PLAYER = ctrl.personas.creaPersona(
    "Jugador",
    ["jugador", "jugadora" ],
    "El bravo PC.",
    salon
);


// ---------------------------------------------------------- Ini ---
ctrl.ini = function() {
    this.setTitle( "lanchoa" );
    this.setIntro( "¡Cómete una anchoa! Tendrás que comerte una anchoa, pero... ¡no vale \
     hacerlo de cualquier manera!" );
    this.setPic( "res/salon.jpg" );
    this.setAuthor( "baltasarq@gmail.com" );
    this.setVersion( "v1.0 20231011" );
    this.personas.changePlayer( PLAYER );
    this.locs.setStart( salon );
};

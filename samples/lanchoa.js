// lanchoa
/*
    ¡Cómete una anchoa!

    @ 2023-09-18 21:29
    IfId 5bef445d-78bc-4889-b136-d3028dfcee6d
*/


// -------------------------------------------------- dormitorio ---
const LOC_DORMITORIO = ctrl.locs.crea(
    "dormitorio",
    [  ],
    "Solo hay una [cama] y una desvencijada mesilla." );

LOC_DORMITORIO.ini = function() {
    this.pic = "";
};

const OBJ_CAMA = ctrl.creaObj(
    "cama",
    [  ],
    "Empujas el colchón hacia abajo y una nubecilla de polvo se eleva sobre tu mano...",
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
    "El salón es... antiguo. Parece como conservado en el tiempo por algún misterioso mecanismo. El [tresillo] se sitúa frente a un [televisor] y una [vieja radio](radio), con una [mesa] en el medio, mientras mientras tres puertas en los extremos permiten dirigirse a la [[cocina]], el [[dormitorio]] y a la [[calle]]." );

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
    "Si no fuera por el tamaño de la parte trasera (es de tubo), no tendría nada de especial.",
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
    "La radio **no** funciona.",
    LOC_SALON,
    Ent.Scenery
);


// -------------------------------------------------- cocina ---
const LOC_COCINA = ctrl.locs.crea(
    "cocina",
    [  ],
    "La cocina es tremendamente austera, con solo una puerta entre el [[e, salón]] y ella. Un [frigorífico] se arrincona contra la esquina, mientras el [fregadero] se sitúa al final de una [mesado] entre ellos." );

LOC_COCINA.ini = function() {
    this.pic = "";
};

const OBJ_MESADO = ctrl.creaObj(
    "mesado",
    [  ],
    "Una larga plancha de piedra que... **no** es mármol de carrara.",
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
    this.setVersion( "20230918" );
    this.personas.changePlayer( PLAYER );
    this.locs.setStart( LOC_DORMITORIO );
};

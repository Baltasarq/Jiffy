// lanchoa
/*
    ¡Cómete una anchoa! Tendrás que comerte una anchoa, pero... ¡no vale \
     hacerlo de cualquier manera!

    @ 2025-11-23 21:15
    IfId b5911505-f853-4925-9370-fa134b76d96e
*/


// -------------------------------------------------- dormitorio ---
const LOC_DORMITORIO = ctrl.locs.crea(
    "Dormitorio",
    [ "" ],
    "Solo hay una ${cama, ex cama} y una desvencijada mesilla. Una puerta \
     permite volver al ${salón, o}.",
    function() {
        this.pic = "res/dormitorio.jpg";
        this.setExit( "west", LOC_SALON );
    }
);

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
    [ "" ],
    "Aquí no hay nada que hacer. La puerta de la casa permite volver al \
     ${interior, n}.",
    function() {
        this.pic = "res/calle.jpg";
        this.setExit( "north", LOC_SALON );
    }
);


// -------------------------------------------------- cocina ---
const LOC_COCINA = ctrl.locs.crea(
    "Cocina",
    [ "" ],
    "La cocina es tremendamente austera, con solo una puerta entre el \
     ${salón, e} y ella. Un ${frigorífico, ex frigorifico} se arrincona \
     contra la esquina, mientras el ${fregadero, ex fregadero} se sitúa al \
     final de una ${mesado, ex mesado} entre ellos.",
    function() {
        this.pic = "res/cocina.jpg";
        this.setExit( "east", LOC_SALON );
    }
);

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
    "Salon",
    [ "" ],
    "El salón es... antiguo. Parece como conservado en el tiempo por algún \
     misterioso e invisible mecanismo. El ${tresillo, ex tresillo} se \
     sitúa frente a un ${televisor, ex televisor} y una vieja ${radio, ex \
     radio}, con una ${mesa, ex mesa} en el medio, mientras tres puertas \
     en los extremos permiten dirigirse a la ${cocina, o}, al \
     ${dormitorio, e} y a la ${calle, s}.",
    function() {
        this.pic = "res/salon.jpg";
        this.setExit( "west", LOC_COCINA );
        this.setExit( "east", LOC_DORMITORIO );
        this.setExit( "south", LOC_CALLE );
    }
);

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
    LOC_SALON
);


// ---------------------------------------------------------- Ini ---
ctrl.ini = function() {
    this.setTitle( "lanchoa" );
    this.setIntro( "¡Cómete una anchoa! Tendrás que comerte una anchoa, pero... ¡no vale \
     hacerlo de cualquier manera!" );
    this.setPic( "res/salon.jpg" );
    this.setAuthor( "baltasarq@gmail.com" );
    this.setVersion( "v1.0 20251123" );
    this.personas.changePlayer( PLAYER );
    this.locs.setStart( LOC_SALON );
};

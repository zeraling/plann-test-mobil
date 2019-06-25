package co.saping.testplannen.Utils;

public class Constantes {

    //Constantes campos tabla mascota

    public static final String TABLA_PROSPECTOS="prospectos";
    public static final String PROSPECTO_CUSTOMER_ID="customerId";
    public static final String PROSPECTO_NOMBRE="nombre";
    public static final String PROSPECTO_APELLIDO="apellido";
    public static final String PROSPECTO_DIRECCION="direccion";
    public static final String PROSPECTO_TELEFONO="telefono";
    public static final String PROSPECTO_ESTADO="estado";
    public static final String PROSPECTO_USER_ID="userId";

    public static final String CREAR_TABLA_PROSPECTOS="CREATE TABLE "+TABLA_PROSPECTOS+" ("+
            PROSPECTO_CUSTOMER_ID +" VARCHAR(100) PRIMARY KEY, " +
            PROSPECTO_NOMBRE+" VARCHAR(100), "+
            PROSPECTO_APELLIDO+" VARCHAR(100),"+
            PROSPECTO_DIRECCION+" VARCHAR(100),"+
            PROSPECTO_TELEFONO+" VARCHAR(100),"+
            PROSPECTO_ESTADO+" VARCHAR(100),"+
            PROSPECTO_USER_ID+" VARCHAR(100))";

}

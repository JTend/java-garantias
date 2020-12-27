package servidigital;

/**
 *
 * @author José Tendero
 */
public abstract class Cadenas {

    public static boolean correoValido(String Correo) {
        boolean A = false; boolean P = false;
        int L = Correo.length(); int Cont = 0;
        for(int i = 0; i < L; i++) {
            if(A) {
                if((""+Correo.charAt(i)).equals(".") && i < (L - 2))
                    P = true;
                if((""+Correo.charAt(i)).equals("@"))
                    Cont++;
            }
            else
                if((""+Correo.charAt(i)).equals("@") && i < (L - 5) && i > 0) {
                    Cont++;
                    A = true;
                }
        }
        if(A && P && (Cont == 1)) {
            return true;
        }
        else
            return false;
    }

    public static String sinEspacios(int maxL, String Cadena) {
        String C = Cadena;
        int L = C.length();
        String F = "";
        for(int i = 0; i < L; i++) {
            if(!(""+C.charAt(i)).equals(" ") && F.length() < maxL)
                F = F + C.charAt(i);
        }
        return F;
    }

    public static String MiTrim(int maxL, String Cadena) {
        String C = Cadena.trim();
        int L = C.length();
        String F = "";
        for(int i = 0; i < L; i++) {
            if(i != 0) {
                if(!((""+C.charAt(i)).equals(" ") && (""+C.charAt(i-1)).equals(" ")) && F.length() < maxL)
                    F = F + C.charAt(i);
            }
            else F = F + C.charAt(i);
        }
        return F;
    }

    public static String Numeros(int maxL, String Cadena) {//No se ve
        int L = Cadena.length();
        String F = "";
        for(int i = 0; i < L; i++) {
            if((F.length() < 10) && (Cadena.codePointAt(i) >= 48) && (Cadena.codePointAt(i) <= 57) && F.length() < maxL)
                F = F + Cadena.charAt(i);
        }
        return F;
    }

    public static String Nombre(int maxL, String Cadena) {
        String C = MiTrim(maxL, Cadena);
        int L = C.length();
        String F = "";
        for(int i = 0; i < L; i++) {
            if(F.length() < 100) {
                if(i == 0 || ("" + C.charAt(i - 1)).equals(" ")) {
                    F = F + ("" + C.charAt(i)).toUpperCase();
                }
                else
                    F = F + ("" + C.charAt(i)).toLowerCase();
            }
        }
        return F;
    }
}

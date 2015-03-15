package oskaro.synesthesia.oskar.leonad.franssom.synesthesiaconverter;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to create a color schema that will be used when converting an html.
 * There are 2 precoded schemas, josh's and magnetos. Later i will add the functionality
 * to create custom schemas.
 *
 * Online Epub converters dont really like "'<span style =\"color:'+colorsArray[i]+'\">'+alpha[i]+'</span>');}"
 * so i cannot use shading ( text-shadow: #000 0.03em 0.03em 0.03em) on certain letter as i would like to
 *
 */
public class ColorizeSchemeClassObject {

    private String A;
    private String B;
    private String C;
    private String D;
    private String E;
    private String F;
    private String G;
    private String H;
    private String I;
    private String J;
    private String K;
    private String L;
    private String M;

    private String N;
    private String O;
    private String P ;
    private String Q;
    private String R ;
    private String S;
    private String T;

    private String U;
    private String V;
    private String W;
    private String X ;
    private String Y;
    private String Z;

    private String COLOR_0;
    private String COLOR_1;
    private String COLOR_2;
    private String COLOR_3;
    private String COLOR_4;
    private String COLOR_5;
    private String COLOR_6;
    private String COLOR_7;
    private String COLOR_8;
    private String COLOR_9;

    private List<String> theColors;


    public ColorizeSchemeClassObject(int colorScheme){
        theColors = new ArrayList<String>();

        if(colorScheme == 0){
            //Josh color scheme
            JoshColorScheme();
        }
        else if(colorScheme == 1){
            //Magnet Scheme
            MagnetScheme();
        }else if(colorScheme == 2){
            OskarScheme();
        }else if(colorScheme == 3){
            RainBow();
        }else{
            Rain();
        }
        populateTheColorsList();
    }

    private void Rain() {
        A = "#7E6C09";
        B = "#0002FF";
        C = "#00b3b3";
        D = "#3595FF";
        E = "#000074";
        F = "#00FF29";
        G = "#00FFB4";
        H = "#720B1C";
        I = "#000000";
        J = "#F7F700";
        K = "#00FFFC";
        //   F60F88
        L = "#FFA4E2";
        M = "#03B32E";

        N = "#ff0000";
        O = "#043A6C";
        P = "#FF00E9";
        Q = "#340034";
        R = "#FFA100";
        S = "#A6B7FF";
        T = "#6900FF";

        U = "#0A2A0A";
        //724976
        V = "#00FF99";
        W = "#C200FF";
        X = "#000000";
        //077bbd
        Y = "#FF797D";
        Z = "#A25000";

        COLOR_0 = "#EEEEEE";
        COLOR_1 = "#FFFF00";
        COLOR_2 = "#FF0000";
        COLOR_3 = "#00FF00";

        COLOR_4 = "#0072DD";
        COLOR_5 = "#C85500";
        COLOR_6 = "#AF0078";
        COLOR_7 = "#FF6633";
        COLOR_8 = "#660099";
        COLOR_9 = "#FF0033";
    }



    private void populateTheColorsList(){
        String []tepColorVar = new String[]{A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z,
                COLOR_0, COLOR_1, COLOR_2, COLOR_3, COLOR_4, COLOR_5, COLOR_6, COLOR_7, COLOR_8, COLOR_9};
        for (String hex : tepColorVar){
            theColors.add(hex);
        }
    }

    private void MagnetScheme() {

        A = "#EF123B";
        B = "#F85935";
        C = "#DEDA77";
        D = "#00B342";
        E = "#0356C3";
        F = "#621887";
        G = "#8F0B1F";
        H = "#642015";
        I = "#FFFF00";
        J = "#01D156";
        K = "#00FFFF";
        L = "#623B66";
        M = "#E3576C";

        N = "#FFC300";
        O = "#FFEF99";
        P = "#99FF00";
        Q = "#340034";
        R = "#800080";
        S = "#FF007F";
        T = "#D2B48C";

        U = "#EDFF4F";
        V = "#B2FF4F";
        W = "#043A6C";
        X = "#000000";
        Y = "#F0276B";
        Z = "#A25000";

        COLOR_0 = "#EEEEEE";
        COLOR_1 = "#FFFF00";
        COLOR_2 = "#FF0000";
        COLOR_3 = "#00FF00";

        COLOR_4 = "#0072DD";
        COLOR_5 = "#C85500";
        COLOR_6 = "#AF0078";
        COLOR_7 = "#FF6633";
        COLOR_8 = "#660099";
        COLOR_9 = "#FF0033";
    }

    private void OskarScheme() {

        A = "#7E6C09";
        B = "#0006FB";
        C = "#00b3b3";
        D = "#00bfff";
        E = "#000074";
        F = "#AFDA00";
        G = "#7fff00";
        H = "#720B1C";
        I = "#000000";
        J = "#FFF73B; text-shadow: #000 0.02em 0.02em 0.02em;";
        K = "#00FFFF";
        //   F60F88
        L = "#CB0048";
        //03B32E
        M = "#03AE2C";

        N = "#ff0000";
        O = "#043A6C";
        P = "#ff00ff";
        Q = "#340034";
        R = "#FFA100";
        S = "#A6B7FF";
        T = "#5D3ED4";

        U = "#0A2A0A";
        //724976
        V = "#00FF99";
        W = "#9100EB";
        X = "#000000";
        //077bbd
        Y = "#FFC7D8";
        Z = "#A25000";


        COLOR_0 = "#A4A2A0";
        COLOR_1 = "#FFA100";
        COLOR_2 = "#0AA300";
        COLOR_3 = "#C80900";

        COLOR_4 = "#1100FF";
        COLOR_5 = "#D100FF";
        COLOR_6 = "#2A8EFF";
        COLOR_7 = "#FF45ED";
        COLOR_8 = "#8BFF00";
        COLOR_9 = "#00FCFF";
    }

    private void RainBow() {

        A = "#7E6C09";
        B = "#66FFFF";
        C = "#00b3b3";
        D = "#00FF99";
        E = "#000074";
        F = "#66FF33";
        G = "#FF0000";
        H = "#642015";
        I = "#000000";
        J = "#FF0066";
        K = "#FF00FF";
        L = "#9966FF";
        M = "#3366FF";

        N = "#9999FF";
        O = "#043A6C";
        P = "#FF99FF";
        Q = "#340034";
        R = "#FF9999";
        S = "#33CC33";
        T = "#66CCFF";

        U = "#720B1C";
        V = "#CCCCFF";
        W = "#9100EB";
        X = "#000000";
        Y = "#077bbd";
        Z = "#A25000";

        COLOR_1 = "#EEEEEE";
        COLOR_1 = "#00028A";
        COLOR_2 = "#4F4FFF";
        COLOR_3 = "#00A8A9";

        COLOR_4 = "#00FEFF";
        COLOR_5 = "#1D9200";
        COLOR_6 = "#32FF00";
        COLOR_7 = "#8B0E00";
        COLOR_8 = "#FF0600";
        COLOR_9 = "#FF55FD";
    }

    private void JoshColorScheme() {

        A = "#ffcd03";
        B = "#0046c6";
        C = "#66029f";
        D = "#d00016";
        E = "#e7a800";
        F = "#5f4978";
        G = "#037621";
        H = "#65340b";
        I = "#f88734";
        J = "#1ab153";
        K = "#9e0da7";
        L = "#77132f";
        M = "#723305";

        N = "#5c4b78";
        O = "#cd641f";
        P = "#4362a7";
        Q = "#ae0253";
        R = "#943b3d";
        S = "#074664";
        T = "#f10017";

        U = "#AD7460";
        V = "#1f4979";
        W = "#745724";
        X = "#070c7c";
        Y = "#077bbd";
        Z = "#d4308d";

//        n0, #n0 { color:#ffffff;text-shadow: #666 0.03em 0.03em 0.09em; }
//        .n1, #n1 { color:#ffea00;text-shadow: #ff6633 0.03em 0.03em 0.09em; }
//        .n2, #n2 { color: #0066ff; }
//        .n3, #n3 { color: #009900; }
//        .n4, #n4 { color: #000000; }
//        .n5, #n5 { color: #804000; }
//        .n6, #n6 { color: #008080; }
//        .n7, #n7 { color: #ff6633; }
//        .n8, #n8 { color: #660099; }
//        .n9, #n9 { color: #ff0033; }

        COLOR_0 = "#EEEEEE";
        COLOR_1 = "#f10017";
        COLOR_2 = "#0066ff";
        COLOR_3 = "#009900";

        COLOR_4 = "#000000";
        COLOR_5 = "#804000";
        COLOR_6 = "#008080";
        COLOR_7 = "#ff6633";
        COLOR_8 = "#660099";
        COLOR_9 = "#ff0033";



        /*Use HEX
        A = Color.rgb(255, 203, 3);
        B = Color.rgb(0, 70, 198);
        C = Color.rgb(102, 2, 159);
        D = Color.rgb(208, 0, 22);
        E = Color.rgb(231, 168, 0);
        F = Color.rgb(95, 73, 120);
        G = Color.rgb(3, 118, 33);
        H = Color.rgb(101, 52, 11);
        I = Color.rgb(248, 135, 52);
        J = Color.rgb(26, 177, 83);
        K = Color.rgb(158, 13, 167);
        L = Color.rgb(119, 19, 47);
        M = Color.rgb(114, 51, 5);

        N = Color.rgb(92, 75, 120);
        O = Color.rgb(205, 100, 31);
        P = Color.rgb(67, 98, 167);
        Q = Color.rgb(174, 2, 83);
        R = Color.rgb(148, 59, 61);
        S = Color.rgb(7, 70, 100);
        T = Color.rgb(241, 0, 23);

        U = new Integer(Color.rgb(173,116,96));
        V = new Integer(Color.rgb(31,73,121));
        W = new Integer(Color.rgb(116,87,36));
        X = new Integer(Color.rgb(7,12,124));
        Y = new Integer(Color.rgb(7,123,189));
        Z = new Integer(Color.rgb(212,48,141));

        COLOR_0 = new Integer(Color.rgb(255,235,205));
        COLOR_1 = new Integer(Color.rgb(255,234,0));
        COLOR_2 = new Integer(Color.rgb(0,102,255));
        COLOR_3 = new Integer(Color.rgb(0,153,0));
        COLOR_4 = new Integer(Color.rgb(0,0,0));
        COLOR_5 = new Integer(Color.rgb(128,64,0));
        COLOR_6 = new Integer(Color.rgb(0,128,128));
        COLOR_7 = new Integer(Color.rgb(255,102,51));
        COLOR_8 = new Integer(Color.rgb(102,0,153));
        COLOR_9 = new Integer(Color.rgb(255,0,51));
        */
    }

    public List<String> getColorArray(){

        return theColors;
    }

    // Gives back the representing color for a letter.
    public String ReturnCharColor(char letter){
        if (letter == 'a' || letter == 'A'){
            return A;
        }
        else if(letter == 'b' || letter == 'B'){
            return B;
        }
        else if(letter == 'c' || letter == 'C'){
            return C;
        }
        else if(letter == 'd' || letter == 'D'){
            return D;
        }
        else if(letter == 'e' || letter == 'E'){
            return E;
        }
        else if(letter == 'f' || letter == 'F'){
            return F;
        }

        else if(letter == 'g' || letter == 'G'){
            return G;
        }
        else if(letter == 'h' || letter == 'H'){
            return H;
        }
        else if(letter == 'i' || letter == 'I'){
            return I;
        }
        else if(letter == 'j' || letter == 'J'){
            return J;
        }
        else if(letter == 'k' || letter == 'K'){
            return K;
        }
        else if(letter == 'l' || letter == 'L'){
            return L;
        }
        else if(letter == 'm' || letter == 'M'){
            return M;
        }

        else if(letter == 'n' || letter == 'N'){
            return N;
        }
        else if(letter == 'o' || letter == 'O'){
            return O;
        }
        else if(letter == 'p' || letter == 'P'){
            return P;
        }
        else if(letter == 'q' || letter == 'Q'){
            return Q;
        }
        else if(letter == 'r' || letter == 'R'){
            return R;
        }
        else if(letter == 's' || letter == 'S'){
            return S;
        }
        else if(letter == 't' || letter == 'T'){
            return T;
        }
        else if(letter == 'u' || letter == 'U'){
            return U;
        }
        else if(letter == 'v' || letter == 'V'){
            return V;
        }

        else if(letter == 'w' || letter == 'W'){
            return W;
        }
        else if(letter == 'x' || letter == 'X'){
            return X;
        }
        else if(letter == 'y' || letter == 'Y'){
            return Y;
        }
        else if(letter == 'z' || letter == 'Z'){
            return Z;
        }
        else if(letter == '0'){
            return COLOR_0;
        }
        else if(letter == '1'){
            return COLOR_1;
        }
        else if(letter == '2'){
            return COLOR_2;
        }
        else if(letter == '3'){
            return COLOR_3;
        }
        else if(letter == '4'){
            return COLOR_4;
        }
        else if(letter == '5'){
            return COLOR_5;
        }
        else if(letter == '6'){
            return COLOR_6;
        }
        else if(letter == '7'){
            return COLOR_7;
        }
        else if(letter == '8'){
            return COLOR_8;
        }
        else if(letter == '9'){
            return COLOR_9;
        }

        return A;
    }

}

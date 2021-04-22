import Mysql.MySqlConnection;
import com.digitalpersona.uareu.*;
import tesseract.ApiMethods;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import javax.swing.text.TabExpander;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.Base64;
import java.util.Scanner;
import java.util.zip.DeflaterOutputStream;

public class Comparation {

    BufferedImage imagen;
    ByteArrayOutputStream bos;

    String ALGO = "AES";
    byte[] keyValue = "Ad0#2s!3oGyRq!5F".getBytes();
    Key key = null;

    String pulgar = "Rk1SACAyMAABuAAz/v8AAAFlAYgAxQDFAQAAAFZEQEIAZ3BggIEBK5RgQH4ANA1eQEoBV4lcgOsApaxbgFYBMIVbgOsAR1xaQPMAV69ZQLYAyq5ZQDMApW5YQQQAv6NYQCkBUX9XQHABMI1XgKEA5gRWQKsAbWdVgCEA23JVgN0A36BVQKIAoAhVgLgAWwJUQGkAoRZUQQsA+JtUQCMBJHpUQGwAfBFTgDMAghZTQMQA0VpTQI4AjQtTgDkBPIJTQPcBQIlSQGcASBBSQN0BD41RQMwAPQJRgHAAlm5QgL4BJYlQgGcBRo9QgOkBP4dLgQUA4Z5KgMMA/55JQMMBV3VJgDUAShRIQI4BOTFIgJcBLSBHQRoA355EgL4BUndEgLYBCpRCgKEBNnhCgIQBUrBBgRgA7kQ/gREA9pg/QNsBQII+gKsBOng+QFoBcDE9QQ8BRo08QM4BRIE6QKgBGYE5gJYBbbM5QDwBNX04gRgA/EQ3gQoBYyk3QMcBQnk1AKUBDHgzARcA8kYzAQQA/JYxAOMBeSUxAMoBeyMxASUA0aQwALcBQiguAJABRjkuAIsBcgQtAAA=";
    String indice = "Rk1SACAyMAABuAAz/v8AAAFlAYgAxQDFAQAAAFZEgFoAQBhfgNIAlI1egGEAWXVcgGcAgoFcQFAAvoZagL8Au4FaQLQAK6dXQHAAtYtXQO4AbZpSgIIAu5tSQNsAc5hRgIIAwptQQNsAIaVQgFsAI3BQgJcAtS1OgJEAWatOQJYAjYVJQG4ANG5HgKcAhItHgLoAjolHQM8Ao4tHgPEAr4lAQHEAEQ0/QPUAV6A+QOoA6Sk8QJMAxzo7gPcAhJQ6QGcA2JI6gPMAej86gPUAU6E5QPEAjT04gL8A8ng3QNcAzy83gPkApY82gPwAy481AN4AwTE1AN0A2IM1AO4AyC80ANsA0iUzAOQA3S8zAO4A4owzAPcA1jEyAP4AYUExAJUAvDExAF0A1o8xAOMA6S8xAPAAnToxAN8AzCsxAQ8AgzYwAQsAmjEwAPwAoDEwAQYAvzEwAMQA83owANgA3YEvAQYAujEtAOQAvC8tAMoA8CItANAA93stARIAkCksAP4AgzosAJYA20AsAPwAbUMrAPMApTorAO8Au4grAPEAy4srAOQAq4spAPMA1YspANQA5ScpAAA=";
    String medio = "Rk1SACAyMAABuAAz/v8AAAFlAYgAxQDFAQAAAFZEgNkAr4dkgEoASHphgJEAtJddgIIATnpcgF0AmoNcgMcAjYFbQIQAGwxYgHEAy49YgK0AiS5XgC8AIxlVgJgAcYNVQKsBL0lUgIoBJERUgEgAlH9QgKkANKxQgKsAeohOgGkBAptOQJwAoZtOQMIAFFpNgHwARxlNgOQAu4xMQF8AlYVLgKMA7KNLgN0A7DFLQMMAsINKQPUAbI9JgOgA9jJGgLcAb4xFQJUA45lEgOMAaIxEgNAAtYVDgF0BGJtDgKgArj5DQFoBHZpDQL0BEKxBQKsAyKhAgJABJT09QKUBK0Y8QJMA7pg7gK4A0qs6gNcA91Y6gKUAepA5QQYAlpM5QK0AkzM4gK4AmCc3gPwAzJE2gOQBDVQ2QFoBLTc1AQgAgo01AF0BKDYzALcBRFEzAM8A9lQyAKkAbIkxAQsAfJMxAK0A8a4wAKMAbIUvAKkApTovAL4AvicvAPUA3JEvAMIA3UgvAJgBM6svAKEAY3ouAK4AwqUuAMgAyCUuALEApT0tAQYAupIsAK8ArzgsAKcAmDMrAAA=";
    String anular = "Rk1SACAyMAABuAAz/v8AAAFlAYgAxQDFAQAAAFZEgLgA/YVkgJAAaVVeQHEAYq5eQIAAVl9cgG8BQHNcgFkBMHFbQIQAd6RZgF8AyhlXgIcAv4lVQH4BBH5VgGkBEHdUgMMAc0NTgEUA7IdTgMgAiZRSgHcAk2dSQF8A/XZRgMIBUH9RgNUBEI1OQJYA3YVNgGwA8H5MQMIAtI9MQGcBAyVMQI0BGIFLgE8AjQ9LgG4BDHpKgE8BKBpKQEoBYBdKgFsBYW5JgFMBCi9JgK0AtYxHQHAAv39GQFUA+ChFgEoBFS1FgGYAwX9DgDwBKy9BgDoBVhRBgFABYBZAgEkBXBc/QPAAtzc+gOsAuzc8QGcAung7QOQBHpI7gOEAyzU7gDIBOCs6gOEAmz02ADMBY2U1AJEBeR40AN0AjZozACkAsHozAIcBeB4zANcAfpwxACcAqncxAKUBex8xAOUAtjUwAOoA5ZIwAOEAk5guAOsAq5IuAOgBBJAuAF8BdxsuAP4AtpQtAEgBIBwtAC4BTGotAOsA3ZIsAHsAq48rAHABdxsrAOoAoJEqAFYA/iUpAOsAwZEoAAA=";
    String meñique = "Rk1SACAyMAABuAAz/v8AAAFlAYgAxQDFAQAAAFZEgMIBJHpkQLgA439bQHYBGhtYgMoAfFRYgHUBAiVYQNIBI3pWgFQA4n9TgJgATgRSgFMAd3xRQGEALBJQQK8BaWxQQHABUhJPgK0ApX9PgK4AsINPQGYBDCVNgMIAoItNQN0ASEtMQOUAy4dMQLQATKdMQPYAtIxJQLcAKwVJgKkAUmNIQKkAfqtGgI0BVhNGgLYBXRJGQL8A+YJFgEABDyVFQE4AdHxEQEUBGiJEQC4Aln5DQNQASFJDQPwApTVCgN4AUE9BgEkAdnxBQHAARxdAgHAAPHRAQDoBDytAgEoBVxRAgO4A34s/QMQBMSI/QOQBCIE+gREAbZM+QL8BYG4+QQAAqS09QQgAwTE9QGMBXQs9gPwAbj88gNIARVI8gCgAnns7QE8BWBQ7QKIBcm07gO4AvzQ6gEAA2yM6gIABVhQ6QK0AKWQ6gL8BCiU5QEIBPhs5gKgBSXM5gMgAKVM4QO8Aljo4gD8AuiU4QGYAJxQ4QOoAPKM4QC4Au3s4gK4BSWw4gHsBVxQ4QMoBWG44gKgBXBk4AAA=";

    String dedos[] = {
            pulgar,indice,medio,anular,meñique
    };

    public enum Evento{
        DETECTAR,
        FINALIZADO,
        ERROR
    }

    public Reader reader;
    Engine engine;
    byte[] data;

    /*Interfaz que recibe eventos de enrolamiento*/

    public  interface EnrolamientoListener{
        //El sensonser es activado cuando se genera esta interfaz
        void eventoEnrolamiento(Evento e,String msg);
    }

    public void cancelarDeteccion(){
        try{
            reader.CancelCapture();
            reader.Close();
        }catch(Exception ex){

        }
    }

    //Inicializa el lector y el motor de identificacion
    public Comparation(){
        try{
            ReaderCollection lectores = UareUGlobal.GetReaderCollection();
            int i  = 0;
            while(lectores.size() == 0 && i++<10){
                lectores.GetReaders();
            }
            reader = lectores.get(0);
            engine = UareUGlobal.GetEngine();
        }catch(UareUException | ArrayIndexOutOfBoundsException ex){
            ex.printStackTrace();
        }
    }

    //Enrola huella y envia eventos DETECTAR, FINALIZADO Y ERROR mediante el listener

    public void enrolar(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    reader.Open(Reader.Priority.EXCLUSIVE);
                    //final  Fmd huellas[] = new Fmd[5];

                    //Propuesto por Retiz
                    final Fmd huellas[] = new Fmd[1];

                    Fmd externa = null;
                    for (int i = 0; i < huellas.length; i++) {
                        System.out.println("DETECTAR");
                        huellas[i] = engine.CreateFmd(reader.Capture(Fid.Format.ANSI_381_2004, Reader.ImageProcessing.IMG_PROC_DEFAULT, 500, -1).image, Fmd.Format.ANSI_378_2004);
                        //externa = engine.
                    }

                    Fmd fmd = engine.CreateEnrollmentFmd(Fmd.Format.ANSI_378_2004, new Engine.EnrollmentCallback() {
                        int i = 0;

                        @Override
                        public Engine.PreEnrollmentFmd GetFmd(Fmd.Format format) {
                            Engine.PreEnrollmentFmd result = new Engine.PreEnrollmentFmd();
                            try {
                                //Propiesto por Retiz
                               /* for(int i=0 ;i < huellas.length; i++){
                                    result.fmd = huellas[i];
                                }*/
                                result.fmd = huellas[i];
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                return null;
                            }
                            return result;
                        }
                    });
                    data = fmd.getData();
                    //Propuesto por Retiz

                    String mano = Base64.getEncoder().encodeToString(fmd.getData());
                    System.out.println(mano);


                    // String huella = "Rk1SACAyMAABuAAz/v8AAAFlAYgAxQDFAQAAAFZEQE4AiXFjQJMBGIxggQsAiKxfgI0AeXVbgKMBF5VbQF0ASHFZQIYAhBlZQIQAKQ9YQMIAgwhYgPEBOIJYQQ8ANqtXQIgAXBFWQK0AbA1WQMkATWhVQOUAtlhTgNUAOgRSgPYBYX9SQQAAv6BRgHYBHYBRQQgAJVxQgE8AYxdOgMIAywROgOQA5Z9OQOgAHgRNQQAA9o1LgIoBM4tLQLEBIzFKQNgAq6tJgLoBEyBJQOUBRoBJQFQA6XxHQJgAExBGgOEBColGQJ4BP6RGgMoBJXdFQDoAnRdFQM8BK3NFgC4AtiVCgOQBVnpCQLEAQmpAgP4BNYNAQE0A4x4/gSgAU1U/gD8AwRs/QNkA7J4/QKMBOqw/QQoBTyg/QMwA/n8+gPkBUig+QD0Ay349gSsAW1Y9QQABLX89gNgBS3E9gL0BYGA9gCcA13c8QN0BMCc8gDkApRc7QL8BGnw7gC8AriA7QDIAwiI5gSYATak3QRABKoM3gK8BF4w3QQUBKIk3gSAAPVQ2QSgAR6s2AR0AMF41ASkAoKU1AAA=";

                    /*System.out.println("FINALIZADO");
                    System.out.println("Width " + fmd.getWidth());
                    System.out.println("Height " + fmd.getHeight() );
                    System.out.println("Resolution " + fmd.getResolution());
                    System.out.println("CbeffId " + fmd.getCbeffId());
                    System.out.println("format " + fmd.getFormat());
                    System.out.println("View Count  " + fmd.getViewCnt() + " " + fmd.getViews());
                    System.out.println(data);*/

                    //creacion
                    ApiMethods methods = new ApiMethods();
                    Fmd archivo = null;
                    imagen = ImageIO.read(new File("C:\\Users\\Tesseract\\Documents\\IBVSDigital\\resources\\dedo.png"));
                    bos = new ByteArrayOutputStream();
                    ImageIO.write(imagen, "png", bos);

                    


                    



                    //Comparacion
                   // archivo = engine.CreateFmd(datas,357,392,197,1,3407615,Fmd.Format.ANSI_378_2004);

                    //Propuesto por retiz
                   /* byte[] manoE  = Base64.getDecoder().decode("Rk1SACAyMAABuAAz/v8AAAFlAYgAxQDFAQAAAFZEgMwAblJjgNAAk0xcgJAAjQRTQLgA3YxTgKUA8YNSgK0AiKZPQJ0AimVPgEIAq3xNQFkAaBJMgKMA5oFMQKEAaGRLgLgBOIFLQKEAvKlKgGwBPydKgPEApKBJQGYBPyhFgDQA5iRDgK8BJH9BQLQBPIFBgKMAjmVAgO8AfEQ+QOsA6DU+QOgA/DM+gO8Ab0U9gOkA8o09QPYAlZ85QPYAsJk5QDQA8CQ5gDgBA404QPYAoKE4gPMA0JE3gIQBT3o3gOsA0Iw3QOkAZqA2QPkAfkQ2gCIAq3g2gHcBS3o2QCEAv3w1AP4A0ZU0AOMA/jI0APcAjUEzAQQAlDQzACgA0iIzAOMBFY0zAE4AsBkyANIBLYkyADwBDYEyANsBIy0yAP4A4z8xAC8Az4IwAP4AoEgwAKIA0XcwAQIAiZgvAQYAsZYvAO4BA40vAM4BMYsvAPUA+Y8uAO4AYkEtAPkBApItAH4BT3ctAPsAsUYsAPMAtz0sAQgAdDosAQQAgzUsAP8AmEYsAQoApZAsAB4Ay30sAB8Ann8rAAA=");
                    Fmd manoFmd = UareUGlobal.GetImporter().ImportFmd(manoE, Fmd.Format.ANSI_378_2004, Fmd.Format.ANSI_378_2004);

                    for(int i=0; i< huellas.length; i++){
                        if(engine.Compare(manoFmd,0,huellas[i],0)<2000){
                            System.out.println("Se han encontrado coincidencias");
                        }else{
                            System.out.println("No hay coincidencias");
                        }
                    }*/


                    archivo = engine.CreateFmd(toBytes(imagen),imagen.getWidth(),imagen.getHeight(),500,0,3407615,Fmd.Format.ISO_19794_2_2005);


                    //Ulix methos
                    Fmd dedosM[] = new Fmd[5];
                    for(int i= 0 ; i<dedosM.length; i++){
                        dedosM[i] = UareUGlobal.GetImporter().ImportFmd(
                                Base64.getDecoder().decode(dedos[i]), Fmd.Format.ANSI_378_2004, Fmd.Format.ANSI_378_2004
                        );
                    }
                    for(int i=0; i<dedosM.length; i++){
                        if(engine.Compare(dedosM[i],0,huellas[0],0)< 2000){
                            System.out.println("Se ahn encontrado coincidencias");
                        }else{
                            System.out.println("No hay coincidencias");
                        }
                    }


                    /*if(engine.Compare(archivo,0,fmd,0) < 2000){
                        System.out.println("Persona autenticada, las huellas coinciden");
                        System.out.println("Resultado obtenido: " + engine.Compare(archivo,0,fmd,0));
                    }else{
                        System.out.println("El resultado no coincide");
                        System.out.println("Resultado obtenido: " + engine.Compare(archivo,0,fmd,0));
                    }*/
                    //System.out.println(engine.Compare(archivo,0,fmd,0));
                    reader.Close();
                }catch(UareUException ex){
                    cancelarDeteccion();
                    String r = ex.toString();
                    if(ex.toString().contains("FMD")){
                        System.out.println("LAS HUELLAS NO COINCIDEN");
                    }else{
                        System.out.println("LECTOR NO DISPONIBLE");
                    }
                    ex.printStackTrace();
                }catch (Exception ex){
                    cancelarDeteccion();
                    System.out.println("Error detectando, intenta de nuevo");
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    public Fmd capturar(){
        Fmd fmd = null;
        try{
            reader.Close();
        }catch(UareUException ex){
            ex.printStackTrace();
        }
        try{
            reader.Open(Reader.Priority.EXCLUSIVE);
            Fid imagen = null;
            while(imagen == null){
                imagen = reader.Capture(Fid.Format.ANSI_381_2004,Reader.ImageProcessing.IMG_PROC_DEFAULT,500,-1).image;

                System.out.println("imagen = " + imagen);
            }
            fmd = engine.CreateFmd(imagen,Fmd.Format.ANSI_378_2004);
            reader.Close();
        }catch(UareUException ex){
            ex.printStackTrace();
        }
        return  fmd;
    }

    public byte[] ctoBA(){
        byte [] datas = null;
        try {
            BufferedImage imagen = ImageIO.read(new File("C:\\Users\\Tesseract\\Documents\\IBVSDigital\\resources\\dedo.png"));
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(imagen, "png", bos);
            datas = bos.toByteArray();
        }catch(IOException ex){
            ex.printStackTrace();
        }
        return  datas;
    }

    public Fmd fromImage(){
        byte [] datas = null;
        Fmd archivo = null;
        try {
            BufferedImage imagen = ImageIO.read(new File("C:\\Users\\Tesseract\\Documents\\IBVSDigital\\resources\\papa.png"));
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(imagen, "png", bos);
            bos.flush();
            datas = bos.toByteArray();

            // archivo = engine.CreateFmd(datas,357,392,197,0,3407615,Fmd.Format.ISO_19794_2_2005);
            archivo = engine.CreateFmd(toBytes(imagen),imagen.getWidth(),imagen.getHeight(),500,0,3407615,Fmd.Format.ISO_19794_2_2005);
            System.out.println(archivo.getData());

        }catch(IOException | UareUException ex){
            ex.printStackTrace();
        }

        return archivo;
    }

    public void cifrarHuella(byte[] huella){
        ApiMethods api = new ApiMethods();
        api.encryptAES(ApiMethods.EncryptionMode.ENCRYPT,huella);
    }

    //metodo que works
    public byte[] toBytes(BufferedImage image){
        WritableRaster raster = image.getRaster();
        DataBufferByte data= (DataBufferByte) raster.getDataBuffer();
        return (data.getData());
    }

    //cifrar byteArray
    public byte[] encrypt(byte[] Data) throws  Exception{
         key = generateKey();
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.ENCRYPT_MODE,key);
        byte[] encVal = c.doFinal(Data);
        return encVal;
    }

    public byte[] decrypt(byte[] Data) throws  Exception{
        key = generateKey();
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.DECRYPT_MODE,key);
        byte[] decValue = c.doFinal(Data);
        return decValue;
    }

    private  Key generateKey() throws  Exception{
        if(key == null){
            key = new SecretKeySpec(keyValue,ALGO);
        }
        return  key;
    }

    public static void main(String[]  args){


        Scanner scan = new Scanner(System.in);
        Comparation comparation = new Comparation();
        int opc = 0;
        do{
            System.out.println("--------------------------------------------------------------------------------------");
            System.out.println("Ingrese la opcion deseada");
            System.out.println("1- Cifrar huella");
            System.out.println("2- Descifrar huella");
            System.out.println("3- Validar huella");
            System.out.println("4- Salir");
            System.out.println("--------------------------------------------------------------------------------------");
            opc = scan.nextInt();
            switch (opc){
                case 1:
                    //comparation.cifrarHuella();
                    break;
                case 3:
                    comparation.enrolar();
                    break;
                case 4:
                    System.out.println("Byes");
                    break;
                default:
                    System.out.println("Opcion invalida");
            }
        }while(opc < 4);
       /* try{
            Comparation comparation = new Comparation();
            //comparation.enrolar();
            comparation.TestEncript();

        }catch (ArrayIndexOutOfBoundsException ex){
            ex.printStackTrace();
        }*/
    }

}
